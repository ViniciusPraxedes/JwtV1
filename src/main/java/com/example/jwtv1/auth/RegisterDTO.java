package com.example.jwtv1.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank(message = "First name is mandatory")
    private String firstname;
    @NotBlank(message = "Last name is mandatory")
    private String lastname;
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "Password is mandatory")
    private String password;
}
