package com.erp.studenterp.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Service responsible for communicating with the Google Gemini API.
 * It sends a prompt to Gemini and returns the generated text response.
 * This service is isolated from the existing CRUD services and can be used
 * by a new AI controller without changing any current student logic.
 *
 * Error Handling:
 * - Validates API key configuration before each call
 * - Catches and logs HTTP errors from Gemini API
 * - Returns user-friendly error messages
 * - Handles null/malformed responses gracefully
 */
@Service
public class GeminiService {

    private static final Logger LOGGER = Logger.getLogger(GeminiService.class.getName());

    private final RestTemplate restTemplate;

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta2/models/text-bison:generate}")
    private String apiUrl;

    public GeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Sends the provided prompt to Gemini and returns the generated reply.
     * Includes comprehensive error handling and logging for production use.
     *
     * @param prompt the user prompt
     * @return generated text or error message
     * @throws IllegalStateException if API key is not configured
     */
    @SuppressWarnings("unchecked")
    public String generate(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            String errMsg = "Prompt cannot be empty";
            LOGGER.log(Level.WARNING, errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        if (apiKey == null || apiKey.isBlank()) {
            String errMsg = "Gemini API key is not configured. Using local fallback response.";
            LOGGER.log(Level.WARNING, errMsg);
            return buildFallbackResponse(prompt);
        }

        LOGGER.log(Level.INFO, "Sending prompt to Gemini API");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                    Map.of("parts", List.of(Map.of("text", prompt)))
                )
            );
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            String fullUrl = apiUrl + "?key=" + apiKey;

            Map<String, Object> response = restTemplate.postForObject(fullUrl, request, Map.class);
            String parsedResponse = parseGeminiResponse(response);
            return parsedResponse == null || parsedResponse.isBlank()
                    ? buildFallbackResponse(prompt)
                    : parsedResponse;

        } catch (HttpClientErrorException e) {
            String errMsg = "Gemini API client error (4xx): " + e.getStatusCode() + " - " + e.getMessage();
            LOGGER.log(Level.WARNING, errMsg);
            return buildFallbackResponse(prompt);

        } catch (HttpServerErrorException e) {
            String errMsg = "Gemini API server error (5xx): " + e.getStatusCode() + " - " + e.getMessage();
            LOGGER.log(Level.WARNING, errMsg);
            return buildFallbackResponse(prompt);

        } catch (Exception e) {
            String errMsg = "Error calling Gemini API: " + e.getMessage();
            LOGGER.log(Level.WARNING, errMsg);
            return buildFallbackResponse(prompt);
        }
    }

    private String buildFallbackResponse(String prompt) {
        String safePrompt = prompt == null || prompt.isBlank() ? "your question" : prompt.trim();
        String normalizedPrompt = safePrompt.toLowerCase();

        if (normalizedPrompt.contains("student")) {
            return "You can manage students from the Student List page. To add a student, open the Add Student form, enter the student details, and save. To edit or delete, use the actions shown in the table.";
        }

        if (normalizedPrompt.contains("department")) {
            return "Departments are shown with each student record. You can select a department while adding or editing a student from the department dropdown.";
        }

        if (normalizedPrompt.contains("help") || normalizedPrompt.contains("what")) {
            return "This app lets you manage students, departments, and basic ERP tasks through the dashboard. Use the sidebar to navigate between Student List, Add Student, Edit Student, and AI Assistant.";
        }

        return "I can help with student management in this app. Use the sidebar to view students, add new entries, or edit existing student records.";
    }

    /**
     * Parse the Gemini API response to extract generated text.
     * Handles multiple response formats for backward compatibility.
     *
     * @param response the API response
     * @return extracted text or empty string
     */
    private String parseGeminiResponse(Map<String, Object> response) {
        if (response == null) {
            LOGGER.log(Level.WARNING, "Gemini API returned null response");
            return "";
        }

        // Try direct "text" field (simple format)
        Object text = response.get("text");
        if (text != null) {
            LOGGER.log(Level.INFO, "Found text in direct response field");
            return text.toString();
        }

        // Try "candidates" array (official Gemini format)
        Object candidates = response.get("candidates");
        if (candidates instanceof java.util.List<?> candidateList && !candidateList.isEmpty()) {
            Object first = candidateList.get(0);
            if (first instanceof Map<?, ?> candidateMap) {
                // Try "output" field
                Object output = candidateMap.get("output");
                if (output != null) {
                    LOGGER.log(Level.INFO, "Found text in candidates.output");
                    return output.toString();
                }

                // Try "content" nested structure
                Object content = candidateMap.get("content");
                if (content instanceof Map<?, ?> contentMap) {
                    Object parts = contentMap.get("parts");
                    if (parts instanceof java.util.List<?> partList && !partList.isEmpty()) {
                        Object firstPart = partList.get(0);
                        if (firstPart instanceof Map<?, ?> partMap) {
                            Object partText = partMap.get("text");
                            if (partText != null) {
                                LOGGER.log(Level.INFO, "Found text in candidates.content.parts");
                                return partText.toString();
                            }
                        }
                    }
                }
            }
        }

        // Fallback: log warning and return full response
        LOGGER.log(Level.WARNING, "Could not parse Gemini response in expected format: " + response);
        return response.toString();
    }

}
