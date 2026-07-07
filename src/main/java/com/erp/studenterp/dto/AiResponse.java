package com.erp.studenterp.dto;

/**
 * DTO returned from AI endpoints containing the generated text.
 */
public class AiResponse {

    private String response;

    public AiResponse() {
    }

    public AiResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
