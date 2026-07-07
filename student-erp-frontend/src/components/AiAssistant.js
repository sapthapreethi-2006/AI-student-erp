import React, { useState } from "react";
import AiService from "../services/AiService";
import "./AiAssistant.css";

/**
 * AiAssistant Component
 * 
 * Simple test interface for the Gemini AI endpoint.
 * Allows users to send prompts and see AI-generated responses.
 * Includes error handling and loading states.
 */
function AiAssistant() {
  const [prompt, setPrompt] = useState("");
  const [response, setResponse] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  /**
   * Handle form submission to send prompt to AI service
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!prompt.trim()) {
      setError("Please enter a prompt");
      return;
    }

    setLoading(true);
    setError("");
    setResponse("");

    try {
      const result = await AiService.generateResponse(prompt);
      setResponse(result.data.response || result.data);
    } catch (err) {
      console.error("Error calling AI service:", err);
      setError(
        err.response?.data?.message || 
        err.message || 
        "Failed to get AI response. Check console for details."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="ai-assistant-container">
      <div className="ai-card">
        <h2>AI Assistant (Powered by Gemini)</h2>
        <p className="ai-subtitle">Test the Gemini API integration</p>

        {error && (
          <div className="alert alert-danger">
            <strong>Error:</strong> {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="prompt">Enter your prompt:</label>
            <textarea
              id="prompt"
              className="form-control"
              rows="4"
              placeholder="Ask the AI something... (e.g., 'Summarize the importance of student management systems')"
              value={prompt}
              onChange={(e) => setPrompt(e.target.value)}
              disabled={loading}
            />
          </div>

          <button
            type="submit"
            className="btn btn-primary"
            disabled={loading}
          >
            {loading ? "Generating response..." : "Send Prompt"}
          </button>
        </form>

        {response && (
          <div className="response-section">
            <h4>AI Response:</h4>
            <div className="response-box">
              <p>{response}</p>
            </div>
          </div>
        )}

        <div className="info-section">
          <h5>Setup Instructions:</h5>
          <ul>
            <li>
              Set the <code>GEMINI_API_KEY</code> environment variable before starting the backend
            </li>
            <li>
              Example (PowerShell):
              <code className="code-block">$env:GEMINI_API_KEY="your_key_here"</code>
            </li>
            <li>
              Restart the backend application
            </li>
            <li>
              Use this page to test the AI integration
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
}

export default AiAssistant;
