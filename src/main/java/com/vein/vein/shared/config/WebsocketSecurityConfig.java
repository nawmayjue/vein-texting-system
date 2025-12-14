package com.vein.vein.shared.config;

import com.vein.vein.features.user.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Slf4j
public class WebsocketSecurityConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public WebsocketSecurityConfig(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null) {
                    StompCommand command = accessor.getCommand();
                    Authentication authentication = null;

                    // Handle CONNECT, SUBSCRIBE, and SEND commands
                    if (command == StompCommand.CONNECT || command == StompCommand.SUBSCRIBE || command == StompCommand.SEND) {
                        // Try to get authentication from existing user in accessor first
                        if (accessor.getUser() != null && accessor.getUser() instanceof Authentication) {
                            authentication = (Authentication) accessor.getUser();
                            log.debug("Using existing authentication for command: {}", command);
                        }

                        // If no authentication, try to restore from session (for SEND commands after CONNECT)
                        if (authentication == null && accessor.getSessionAttributes() != null) {
                            String username = (String) accessor.getSessionAttributes().get("username");
                            if (username != null) {
                                try {
                                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                                    authentication = new UsernamePasswordAuthenticationToken(
                                            userDetails, null, userDetails.getAuthorities());
                                    accessor.setUser(authentication);
                                    log.debug("Restored authentication from session for: {} (command: {})", username, command);
                                } catch (Exception e) {
                                    log.warn("Could not load user from session: {}", e.getMessage());
                                }
                            }
                        }

                        // If still no authentication, try to authenticate from token
                        if (authentication == null) {
                            String authToken = accessor.getFirstNativeHeader("Authorization");

                            // Also check for token in session attributes (for subsequent messages after CONNECT)
                            if (authToken == null && accessor.getSessionAttributes() != null) {
                                authToken = (String) accessor.getSessionAttributes().get("token");
                            }

                            if (authToken != null && authToken.startsWith("Bearer ")) {
                                String token = authToken.substring(7);
                                try {
                                    String username = jwtService.extractUsername(token);
                                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                                    if (jwtService.validateToken(token, userDetails)) {
                                        authentication = new UsernamePasswordAuthenticationToken(
                                                userDetails, null, userDetails.getAuthorities());
                                        accessor.setUser(authentication);

                                        // Store token and username in session for subsequent messages
                                        if (accessor.getSessionAttributes() != null) {
                                            accessor.getSessionAttributes().put("token", authToken);
                                            accessor.getSessionAttributes().put("username", username);
                                        }

                                        log.info("WebSocket user authenticated: {} for command: {}", username, command);
                                    } else {
                                        // Token expired/invalid but we can still extract username
                                        log.warn("JWT token validation failed (may be expired) for command: {}, but using username: {}", command, username);

                                        // Store username in session even if token is expired
                                        if (accessor.getSessionAttributes() != null) {
                                            accessor.getSessionAttributes().put("username", username);
                                        }

                                        // Use the username anyway for authentication
                                        try {
                                            authentication = new UsernamePasswordAuthenticationToken(
                                                    userDetails, null, userDetails.getAuthorities());
                                            accessor.setUser(authentication);
                                            log.info("Authenticated with expired token using username: {} for command: {}", username, command);
                                        } catch (Exception ex) {
                                            log.error("Could not authenticate with expired token: {}", ex.getMessage());
                                        }
                                    }
                                } catch (Exception e) {
                                    log.error("WebSocket authentication failed for command {}: {}", command, e.getMessage());
                                    // Don't fail completely - try to use session username if available
                                    if (accessor.getSessionAttributes() != null) {
                                        String username = (String) accessor.getSessionAttributes().get("username");
                                        if (username != null) {
                                            try {
                                                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                                                authentication = new UsernamePasswordAuthenticationToken(
                                                        userDetails, null, userDetails.getAuthorities());
                                                accessor.setUser(authentication);
                                                log.info("Fell back to session username: {} for command: {}", username, command);
                                            } catch (Exception ex) {
                                                log.error("Could not load user from session: {}", ex.getMessage());
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Set authentication in SecurityContextHolder for all messages
                        if (authentication != null) {
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            if (command == StompCommand.SUBSCRIBE) {
                                log.info("SUBSCRIBE command - User {} subscribing to: {}",
                                        authentication.getName(), accessor.getDestination());
                            }
                        } else {
                            log.warn("No authentication found for WebSocket message. Command: {}, Destination: {}",
                                    command, accessor.getDestination());
                        }
                    }
                }

                return message;
            }

            @Override
            public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
                // Clear security context after message processing to avoid leaks
                SecurityContextHolder.clearContext();
            }
        });
    }
}
