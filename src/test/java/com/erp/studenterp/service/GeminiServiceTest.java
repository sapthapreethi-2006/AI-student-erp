package com.erp.studenterp.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class GeminiServiceTest {

    @Test
    void shouldReturnHelpfulLocalResponseForStudentQuestions() {
        GeminiService geminiService = new GeminiService(new RestTemplate());

        String response = geminiService.generate("How do I add a student?");

        assertTrue(response.toLowerCase().contains("student"));
        assertTrue(response.toLowerCase().contains("manage") || response.toLowerCase().contains("add"));
    }
}
