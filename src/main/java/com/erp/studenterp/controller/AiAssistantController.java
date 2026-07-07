package com.erp.studenterp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.studenterp.dto.AiRequest;
import com.erp.studenterp.dto.AiResponse;
import com.erp.studenterp.service.GeminiService;

/**
 * Controller exposing AI endpoints. Keeps AI routes separate from existing
 * CRUD controllers to avoid any interference with student management.
 */
@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:3000")
public class AiAssistantController {

    private static final Logger log = LoggerFactory.getLogger(AiAssistantController.class);

    private final GeminiService geminiService;

    public AiAssistantController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    /**
     * Generate text using Gemini for a given prompt.
     *
     * @param request containing the prompt
     * @return generated text
     */
    @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AiResponse> generate(@RequestBody AiRequest request) {
        log.info("Received AI generate request");
        try {
            String result = geminiService.generate(request.getPrompt());
            return ResponseEntity.ok(new AiResponse(result));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new AiResponse(e.getMessage()));
        } catch (IllegalStateException e) {
            log.error("Configuration error: {}", e.getMessage());
            return ResponseEntity.status(503).body(new AiResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error while generating AI response", e);
            return ResponseEntity.status(502).body(new AiResponse("AI service unavailable"));
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AiResponse> handleExceptions(Exception e) {
        log.error("Unhandled exception in AI controller", e);
        return ResponseEntity.status(500).body(new AiResponse("Internal server error"));
    }

}
