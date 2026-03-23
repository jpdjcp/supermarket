package com.supermarket.supermarket_api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.supermarket_api.dto.auth.AuthResponse;
import com.supermarket.supermarket_api.dto.auth.LoginRequest;
import com.supermarket.supermarket_api.dto.auth.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractLocalIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String obtainAccessToken(String username, String password) throws Exception {

        SignupRequest signupRequest = new SignupRequest(username, password);

        mockMvc.perform(post("/api/v1/auth/signup")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = new LoginRequest(username, password);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        AuthResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AuthResponse.class
        );

        return response.token();
    }
}
