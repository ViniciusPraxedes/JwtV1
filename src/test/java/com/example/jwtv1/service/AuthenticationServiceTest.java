package com.example.jwtv1.service;

import com.example.jwtv1.auth.LoginDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.jwtv1.auth.RegisterDTO;
import com.example.jwtv1.logoutToken.Token;
import com.example.jwtv1.logoutToken.TokenRepository;
import com.example.jwtv1.logoutToken.TokenType;
import com.example.jwtv1.user.Role;
import com.example.jwtv1.user.User;
import com.example.jwtv1.user.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthenticationService authService;
    @Test
    public void Register_RegisterUser_ReturnResponseEntityOKAndJWT() {
        //Given
        RegisterDTO registerDTO = new RegisterDTO();
        User user = new User("John", "Doe", "john@example.com", "encodedPassword", Role.USER);

        //When
        when(userRepository.findByEmail(registerDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(user);
        when(jwtService.generateTokenWithoutExtraClaims(any())).thenReturn("generatedJwt");
        when(tokenRepository.save(any())).thenReturn(new Token("generatedJwt", TokenType.BEARER, false, false, user));
        ResponseEntity<?> responseEntity = authService.register(registerDTO);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("generatedJwt", responseEntity.getBody());
    }
    @Test
    public void Register_RegisterUser_EmailTaken() {
        //Given
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("john@example.com");
        User existingUser = new User("John", "Doe", "john@example.com", "encodedPassword", Role.USER);


        //When
        when(userRepository.findByEmail(registerDTO.getEmail())).thenReturn(Optional.of(existingUser));


        //Then
        assertThrows(IllegalStateException.class, () -> {
            authService.register(registerDTO);
        });
    }

    @Test
    public void Login_LoginUser_ReturnResponseEntityOkAndJWT(){
        //Given
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("test");
        User user = new User("John", "Doe", "test@example.com", "test", Role.USER);

        //When
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(loginDTO.getPassword(),user.getPassword())).thenReturn(true);

        when(jwtService.generateTokenWithoutExtraClaims(user)).thenReturn("generatedJwt");

        when(tokenRepository.save(any())).thenReturn(new Token("generatedJwt", TokenType.BEARER, false, false, user));

        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        //Then
        ResponseEntity<String> responseEntity = authService.login(loginDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("generatedJwt", responseEntity.getBody());
    }
    @Test
    public void Login_LoginUser_UserNotFound(){
        //Given
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("test");

        //When
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.empty());

        //Then
        assertThrows(UsernameNotFoundException.class, () -> {
            authService.login(loginDTO);
        });

    }

    @Test
    public void Login_LoginUser_InvalidPassword() {
        //Given
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("test");
        User user = new User("John", "Doe", "test@example.com", "test", Role.USER);

        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.login(loginDTO);
        });
    }

}
