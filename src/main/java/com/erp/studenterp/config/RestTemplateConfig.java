package com.erp.studenterp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class that provides shared HTTP client beans.
 *
 * We define a single RestTemplate bean so components like GeminiService
 * can perform outbound HTTP calls. This keeps HTTP client configuration
 * centralized and avoids creating RestTemplate instances everywhere.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Create a singleton RestTemplate used for synchronous HTTP calls.
     *
     * @return a RestTemplate instance managed by Spring
     */
    @Bean
    public RestTemplate restTemplate() {
        // Configure reasonable timeouts to avoid long hangs when calling Gemini
        org.springframework.http.client.SimpleClientHttpRequestFactory factory = new org.springframework.http.client.SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(20000);
        return new RestTemplate(factory);
    }

}
