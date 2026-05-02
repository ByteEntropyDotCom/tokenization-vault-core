package com.byteentropy.tokenization_vault_core.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VaultControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnTokenForValidPan() throws Exception {
        String jsonRequest = "{\"pan\":\"4111222233334444\", \"requestContext\":\"mobile-app\"}";

        mockMvc.perform(post("/api/v1/vault/tokenize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isString());
    }
}