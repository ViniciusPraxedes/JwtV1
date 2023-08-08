package com.example.jwtv1.controller;

import com.example.jwtv1.service.AuthenticationService;
import com.example.jwtv1.auth.LoginDTO;
import com.example.jwtv1.auth.RegisterDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@CrossOrigin("*")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO body){
        return authenticationService.register(body);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO body){
        return authenticationService.login(body);
    }
}
