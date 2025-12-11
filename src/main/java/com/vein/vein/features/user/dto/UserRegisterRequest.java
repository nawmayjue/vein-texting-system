package com.vein.vein.features.user.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserRegisterRequest {
    @NotBlank
    @NotNull
//    @Max(value = 15, message = "display name cannot exceed 15")
    private String displayName;

    @NotBlank
    @NotNull
//    @Min(value = 5, message = "username must be at least 5")
//    @Max(value = 15, message = "username cannot exceed 15")
    private String username;

    @NotBlank
    @NotNull
//    @Min(value = 8, message = "password must be at least 5")
//    @Max(value = 15, message = "password cannot exceed 15")
    private String password;

//    @Email(message = "Invalid Email format")
    @NotBlank
    @NotNull
    private String email;

    public String getDisplayName() {
        return displayName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
