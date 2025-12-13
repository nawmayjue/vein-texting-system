package com.vein.vein.shared.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Incoming Request: [Method: GET, URI: /api/v1/vein/users]
        log.info("Incoming Request: [Method: {}, URI: {}]", request.getMethod(), request.getRequestURI());

        request.setAttribute("startTime", System.currentTimeMillis());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        long startTime = (long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        if (ex != null) {
            log.error(
                    "Request completed with Error: [URI: {}], [Status: {}] [Time: {} ms] [Exception: {}]",
                    request.getRequestURI(), response.getStatus(), duration,
                    ex.getMessage()
            );
        } else {
            log.info(
                    "Request completed successfully: [URI: {}], [Status: {}] [Time: {} ms]",
                    request.getRequestURI(), response.getStatus(), duration
            );
        }
    }
}
