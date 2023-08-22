package com.example.jwtv1.controller;


import com.example.jwtv1.auth.RegisterDTO;
import com.example.jwtv1.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
class AuthenticationControllerTest {
    @InjectMocks
    private AuthenticationController authenticationController;
    @Mock
    private AuthenticationService authenticationService;

    @Test
    public void testRegister_Success() {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO(); // Create a valid DTO object
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("JWT Token", HttpStatus.OK);

        when(authenticationService.register(registerDTO)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<?> response = authenticationController.register(registerDTO);

        // Assert
        verify(authenticationService, times(1)).register(registerDTO); // Verify that the service method was called
        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
    }
}