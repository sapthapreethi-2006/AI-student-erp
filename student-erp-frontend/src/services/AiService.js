import axios from "axios";

const AI_API_URL = `${process.env.REACT_APP_API_URL || "http://localhost:8080"}/api/ai`;

class AiService {

    /**
     * Send a prompt to the backend AI endpoint and get a response.
     *
     * @param {string} prompt - The user's prompt
     * @returns {Promise} Promise with AI response
     */
    generateResponse(prompt) {
        return axios.post(`${AI_API_URL}/generate`, { prompt });
    }

}

export default new AiService();
