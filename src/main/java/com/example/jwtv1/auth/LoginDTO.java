package com.example.jwtv1.auth;

import lombok.Data;

@Data
public class LoginDTO {
    private String email;
    private String password;
}
