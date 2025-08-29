package com.example.bajajtest;

import com.fasterxml.jackson.annotation.JsonProperty;

// Maps the JSON response containing the webhook URL and access token [cite: 17, 18]
public class WebhookResponse {
    @JsonProperty("webhookURL") // Ensure mapping from JSON key to field
    private String webhookURL;
    
    @JsonProperty("accessToken")
    private String accessToken;

    // Getters and Setters
    public String getWebhookURL() { return webhookURL; }
    public void setWebhookURL(String webhookURL) { this.webhookURL = webhookURL; }
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
}