package com.example.jwtv1.controller;

import com.example.jwtv1.auth.AuthenticationService;
import com.example.jwtv1.auth.LoginDTO;
import com.example.jwtv1.auth.RegisterDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO body){
        return authenticationService.register(body);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO body){
        return authenticationService.login(body);
    }
}
