package com.example.jwtv1.auth;

import lombok.Data;

@Data
public class RegisterDTO {
    private String email;
    private String firstname;
    private String lastname;
    private String password;
}
