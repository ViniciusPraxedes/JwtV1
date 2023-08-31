package com.example.jwtv1.controller;

import com.example.jwtv1.logoutToken.TokenRepository;
import com.example.jwtv1.service.AuthenticationService;
import com.example.jwtv1.service.JwtService;
import com.example.jwtv1.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
@WebMvcTest(controllers = DemoController.class)
@AutoConfigureMockMvc(addFilters = false)
class DemoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private TokenRepository tokenRepository;
    @Mock
    private UserRepository userRepository;
    @MockBean
    private AuthenticationService authenticationService;
    @Test
    public void testSuccess() {
        DemoController demoController = new DemoController();
        String response = demoController.test();
        assertEquals("test", response);
    }
}