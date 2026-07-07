package com.erp.studenterp.dto;

/**
 * DTO representing a request from the frontend to the AI assistant.
 * Contains the user's prompt or instruction to send to Gemini.
 */
public class AiRequest {

    private String prompt;

    public AiRequest() {
    }

    public AiRequest(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

}
