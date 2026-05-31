package com.gogidix.aiservices.aicontentgenerationservice.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Domain model representing a content generation request.
 */
public class ContentGenerationRequest {

    private ContentGenerationId id;
    private String tenantId;
    private String customerId;
    private ContentType contentType;
    private String prompt;
    private String tone;
    private Integer targetLength;
    private List<String> keywords;
    private String language;
    private RequestStatus status;
    private String generatedContent;
    private Integer tokensUsed;
    private Long createdAt;
    private Long completedAt;

    public ContentGenerationRequest() {
        this.id = ContentGenerationId.randomId();
        this.status = RequestStatus.PENDING;
        this.keywords = new ArrayList<>();
        this.language = "en";
        this.createdAt = System.currentTimeMillis();
    }

    public ContentGenerationRequest(ContentGenerationId id, String tenantId, String customerId, ContentType contentType, String prompt) {
        this.id = id;
        this.tenantId = tenantId;
        this.customerId = customerId;
        this.contentType = contentType;
        this.prompt = prompt;
        this.status = RequestStatus.PENDING;
        this.keywords = new ArrayList<>();
        this.language = "en";
        this.createdAt = System.currentTimeMillis();
    }

    private ContentGenerationRequest(Builder builder) {
        this.id = builder.id;
        this.tenantId = builder.tenantId;
        this.customerId = builder.customerId;
        this.contentType = builder.contentType;
        this.prompt = builder.prompt;
        this.tone = builder.tone;
        this.targetLength = builder.targetLength;
        this.keywords = builder.keywords != null ? builder.keywords : new ArrayList<>();
        this.language = builder.language != null ? builder.language : "en";
        this.status = builder.status != null ? builder.status : RequestStatus.PENDING;
        this.generatedContent = builder.generatedContent;
        this.tokensUsed = builder.tokensUsed;
        this.createdAt = builder.createdAt != null ? builder.createdAt : System.currentTimeMillis();
        this.completedAt = builder.completedAt;
    }

    public ContentGenerationId getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getTone() {
        return tone;
    }

    public Integer getTargetLength() {
        return targetLength;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getLanguage() {
        return language;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public String getGeneratedContent() {
        return generatedContent;
    }

    public Integer getTokensUsed() {
        return tokensUsed;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getCompletedAt() {
        return completedAt;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
        if (status == RequestStatus.COMPLETED && this.completedAt == null) {
            this.completedAt = System.currentTimeMillis();
        }
    }

    public void setGeneratedContent(String content) {
        this.generatedContent = content;
    }

    public void setTokensUsed(Integer tokens) {
        this.tokensUsed = tokens;
    }

    public boolean isPending() {
        return status == RequestStatus.PENDING;
    }

    public boolean isCompleted() {
        return status == RequestStatus.COMPLETED;
    }

    public boolean isFailed() {
        return status == RequestStatus.FAILED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentGenerationRequest that = (ContentGenerationRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ContentGenerationRequest{" +
                "id='" + id + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", contentType=" + contentType +
                ", tone='" + tone + '\'' +
                ", targetLength=" + targetLength +
                ", keywords=" + keywords.size() + " items" +
                ", language='" + language + '\'' +
                ", status=" + status +
                ", tokensUsed=" + tokensUsed +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ContentGenerationId id;
        private String tenantId;
        private String customerId;
        private ContentType contentType;
        private String prompt;
        private String tone;
        private Integer targetLength;
        private List<String> keywords;
        private String language;
        private RequestStatus status;
        private String generatedContent;
        private Integer tokensUsed;
        private Long createdAt;
        private Long completedAt;

        public Builder id(ContentGenerationId id) {
            this.id = id;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder contentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder tone(String tone) {
            this.tone = tone;
            return this;
        }

        public Builder targetLength(Integer targetLength) {
            this.targetLength = targetLength;
            return this;
        }

        public Builder keywords(List<String> keywords) {
            this.keywords = keywords;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder status(RequestStatus status) {
            this.status = status;
            return this;
        }

        public Builder generatedContent(String generatedContent) {
            this.generatedContent = generatedContent;
            return this;
        }

        public Builder tokensUsed(Integer tokensUsed) {
            this.tokensUsed = tokensUsed;
            return this;
        }

        public Builder createdAt(Long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder completedAt(Long completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public ContentGenerationRequest build() {
            return new ContentGenerationRequest(this);
        }
    }
}
