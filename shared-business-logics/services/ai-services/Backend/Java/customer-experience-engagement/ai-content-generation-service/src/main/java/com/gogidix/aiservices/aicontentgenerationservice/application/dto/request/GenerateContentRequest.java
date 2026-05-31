package com.gogidix.aiservices.aicontentgenerationservice.application.dto.request;

import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentTone;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

public class GenerateContentRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Prompt is required")
    @Size(min = 10, max = 5000, message = "Prompt must be between 10 and 5000 characters")
    private String prompt;

    @NotNull(message = "Content type is required")
    private ContentType contentType;

    private ContentTone tone;

    private String language;

    private Integer variations;

    private Map<String, String> parameters;

    public GenerateContentRequest() {
    }

    public GenerateContentRequest(String userId, String prompt, ContentType contentType, ContentTone tone, String language, Integer variations, Map<String, String> parameters) {
        this.userId = userId;
        this.prompt = prompt;
        this.contentType = contentType;
        this.tone = tone;
        this.language = language;
        this.variations = variations;
        this.parameters = parameters;
    }

    public static GenerateContentRequestBuilder builder() {
        return new GenerateContentRequestBuilder();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public ContentTone getTone() {
        return tone;
    }

    public void setTone(ContentTone tone) {
        this.tone = tone;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getVariations() {
        return variations;
    }

    public void setVariations(Integer variations) {
        this.variations = variations;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public static class GenerateContentRequestBuilder {
        private String userId;
        private String prompt;
        private ContentType contentType;
        private ContentTone tone;
        private String language;
        private Integer variations;
        private Map<String, String> parameters;

        public GenerateContentRequestBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public GenerateContentRequestBuilder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public GenerateContentRequestBuilder contentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public GenerateContentRequestBuilder tone(ContentTone tone) {
            this.tone = tone;
            return this;
        }

        public GenerateContentRequestBuilder language(String language) {
            this.language = language;
            return this;
        }

        public GenerateContentRequestBuilder variations(Integer variations) {
            this.variations = variations;
            return this;
        }

        public GenerateContentRequestBuilder parameters(Map<String, String> parameters) {
            this.parameters = parameters;
            return this;
        }

        public GenerateContentRequest build() {
            return new GenerateContentRequest(userId, prompt, contentType, tone, language, variations, parameters);
        }
    }
}
