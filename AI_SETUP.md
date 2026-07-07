# AI Assistant Setup Guide

This document explains how to set up and use the Gemini AI integration in the Student ERP system.

## Overview

The Student ERP now includes an AI-powered assistant using the Google Generative Language (Gemini) API. This allows you to:

- Generate insights from student data
- Create AI-powered recommendations
- Summarize reports and information
- Test AI prompts through the web interface

## Prerequisites

1. **Google Cloud Account** — You need a valid Google Cloud account
2. **Gemini API Access** — Enable the Generative Language API in your Google Cloud project
3. **API Key** — Generate an API key from Google Cloud Console

### Getting a Gemini API Key

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable the **Generative Language API**
4. Go to **APIs & Services > Credentials**
5. Click **Create Credentials > API Key**
6. Copy the generated API key (keep it secure!)

## Configuration

### Environment Variable Setup

The AI service reads the API key from the `GEMINI_API_KEY` environment variable. This ensures the key is never hardcoded in the source code.

#### Windows (PowerShell)

```powershell
# Set the environment variable
$env:GEMINI_API_KEY = "your_api_key_here"

# Start the backend
cd student-erp
.\mvnw.cmd spring-boot:run
```

#### Linux/Mac (Bash/Zsh)

```bash
# Set the environment variable
export GEMINI_API_KEY="your_api_key_here"

# Start the backend
cd student-erp
mvn spring-boot:run
```

#### Docker (see docker-compose.yml)

```yaml
environment:
  - GEMINI_API_KEY=your_api_key_here
```

### Application Properties

The following properties are configured in `application.properties`:

```properties
# Gemini API key (read from environment variable)
gemini.api.key=${GEMINI_API_KEY:}

# Gemini API URL (official Google endpoint)
gemini.api.url=${GEMINI_API_URL:https://generativelanguage.googleapis.com/v1beta2/models/text-bison:generate}
```

If you need to use a different Gemini model, update the `GEMINI_API_URL` environment variable.

## API Endpoints

### POST `/api/ai/generate`

Sends a prompt to the Gemini API and returns generated text.

**Request:**
```json
{
  "prompt": "Summarize the student enrollment trends"
}
```

**Response (Success):**
```json
{
  "response": "Based on the student data, enrollment has shown a steady increase of 15% year-over-year..."
}
```

**Response (Error):**
```json
{
  "response": "Error: Gemini API key is not configured. Set GEMINI_API_KEY environment variable."
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8081/api/ai/generate \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Give a 2-line summary of student management systems"}'
```

## Frontend Usage

### AI Assistant Test Page

A simple web interface is available at: `http://localhost:3000/ai-assistant`

This page allows you to:
- Enter custom prompts
- Test the AI endpoint
- See responses in real-time
- Debug API integration issues

### Using the React Service

```javascript
import AiService from '../services/AiService';

// Send prompt and get response
AiService.generateResponse("Your prompt here")
  .then(response => {
    console.log(response.data.response);
  })
  .catch(error => {
    console.error("AI Error:", error);
  });
```

## Error Handling

The backend includes comprehensive error handling:

### Configuration Errors
```
"Gemini API key is not configured. Set GEMINI_API_KEY environment variable."
```
**Solution:** Set the `GEMINI_API_KEY` environment variable before starting the backend.

### Network Errors
```
"Gemini API client error (4xx): 401 - Invalid API key"
```
**Solution:** Verify your API key is correct and has the necessary permissions.

### API Rate Limiting
```
"Gemini API server error (5xx): 429 - Too Many Requests"
```
**Solution:** Implement rate limiting on the frontend or backend.

## Production Checklist

- [ ] API key is stored in environment variables, not hardcoded
- [ ] HTTPS is enabled for all API calls
- [ ] Rate limiting is configured to prevent abuse
- [ ] Error logging is enabled and monitored
- [ ] API key has minimal required permissions in Google Cloud
- [ ] Frontend includes proper timeout handling
- [ ] Backend includes comprehensive error messages for debugging
- [ ] Documentation is updated for your team

## Troubleshooting

### Backend won't start
**Problem:** `UnsatisfiedDependencyException: No qualifying bean of type RestTemplate`
**Solution:** Ensure `RestTemplateConfig` is present and Spring is loading it. Check build logs.

### 401 Unauthorized from Gemini API
**Problem:** API returns 401 error
**Solution:** Verify API key is correct. Check Google Cloud Console that API key has access to Generative Language API.

### Empty response from AI
**Problem:** Backend returns empty string
**Solution:** Check backend logs for parsing errors. Verify response format matches expected structure.

### CORS errors on frontend
**Problem:** `Access to XMLHttpRequest blocked by CORS policy`
**Solution:** Ensure backend has `@CrossOrigin` annotation. Add to `AiAssistantController` if needed.

## Logs

Check backend logs for detailed debugging:

```
INFO: Sending prompt to Gemini API
INFO: Found text in candidates.content.parts
```

Error-level logs:
```
SEVERE: Gemini API client error (4xx): 401 - Unauthorized
```

## Next Steps

1. Set up the environment variable with your API key
2. Start the backend and frontend
3. Navigate to `http://localhost:3000/ai-assistant`
4. Test with sample prompts
5. Monitor logs for errors
6. Integrate AI features into your application logic

---

For more information on Google Generative Language API, see: https://ai.google.dev/docs
