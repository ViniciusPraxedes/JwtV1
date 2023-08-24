package com.example.jwtv1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.doThrow;
@WebMvcTest(controllers = DemoController.class)
@AutoConfigureMockMvc(addFilters = false)
class DemoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void AuthenticationController_Success_Success() throws Exception {
        // When
        ResultActions response = mockMvc.perform(get("/api/v1/auth/test"));

        // Then
        response
                .andExpect(status().isOk()) // Expect a 200 OK response
                .andExpect(content().string("test")); // Expect the response content to be "test"
    }
}