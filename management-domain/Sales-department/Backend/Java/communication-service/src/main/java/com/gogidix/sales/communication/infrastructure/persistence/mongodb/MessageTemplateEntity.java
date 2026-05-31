package com.gogidix.sales.communication.infrastructure.persistence.mongodb;

import com.gogidix.sales.communication.domain.model.Message;
import com.gogidix.sales.communication.domain.model.MessageTemplate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MongoDB Entity for MessageTemplate
 * Separate from domain model for clean architecture
 */
@Document(collection = "message_templates")
public class MessageTemplateEntity {

    @Id
    private String id;

    @Indexed
    private String templateId;

    @Indexed
    private String tenantId;

    private String name;
    private String description;

    @Indexed
    private String code;

    private Message.ChannelType channelType;
    private MessageTemplate.TemplateType type;
    private String subject;
    private String content;
    private String htmlContent;
    private List<TemplateVariable> variables;
    private MessageTemplate.TemplateStatus status;
    private String category;
    private List<String> tags;
    private String language;
    private String locale;
    private Integer version;
    private String parentTemplateId;
    private Boolean isSystemTemplate;
    private Map<String, Object> metadata;
    private Long usageCount;
    private Instant lastUsedAt;
    private Instant createdAt;
    private Instant updatedAt;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Message.ChannelType getChannelType() { return channelType; }
    public void setChannelType(Message.ChannelType channelType) { this.channelType = channelType; }

    public MessageTemplate.TemplateType getType() { return type; }
    public void setType(MessageTemplate.TemplateType type) { this.type = type; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getHtmlContent() { return htmlContent; }
    public void setHtmlContent(String htmlContent) { this.htmlContent = htmlContent; }

    public List<TemplateVariable> getVariables() { return variables; }
    public void setVariables(List<TemplateVariable> variables) { this.variables = variables; }

    public MessageTemplate.TemplateStatus getStatus() { return status; }
    public void setStatus(MessageTemplate.TemplateStatus status) { this.status = status; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getLocale() { return locale; }
    public void setLocale(String locale) { this.locale = locale; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public String getParentTemplateId() { return parentTemplateId; }
    public void setParentTemplateId(String parentTemplateId) { this.parentTemplateId = parentTemplateId; }

    public Boolean getIsSystemTemplate() { return isSystemTemplate; }
    public void setIsSystemTemplate(Boolean isSystemTemplate) { this.isSystemTemplate = isSystemTemplate; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public Long getUsageCount() { return usageCount; }
    public void setUsageCount(Long usageCount) { this.usageCount = usageCount; }

    public Instant getLastUsedAt() { return lastUsedAt; }
    public void setLastUsedAt(Instant lastUsedAt) { this.lastUsedAt = lastUsedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Embedded classes for MongoDB
    public static class TemplateVariable {
        private String name;
        private String description;
        private VariableType type;
        private Boolean isRequired;
        private String defaultValue;
        private List<String> allowedValues;
        private String regexPattern;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public VariableType getType() { return type; }
        public void setType(VariableType type) { this.type = type; }

        public Boolean getIsRequired() { return isRequired; }
        public void setIsRequired(Boolean isRequired) { this.isRequired = isRequired; }

        public String getDefaultValue() { return defaultValue; }
        public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }

        public List<String> getAllowedValues() { return allowedValues; }
        public void setAllowedValues(List<String> allowedValues) { this.allowedValues = allowedValues; }

        public String getRegexPattern() { return regexPattern; }
        public void setRegexPattern(String regexPattern) { this.regexPattern = regexPattern; }
    }

    public enum VariableType {
        STRING,
        NUMBER,
        DATE,
        BOOLEAN,
        URL,
        EMAIL,
        LIST
    }

    public enum ChannelType {
        EMAIL,
        SMS,
        IN_APP,
        WHATSAPP,
        PUSH_NOTIFICATION
    }

    public enum TemplateType {
        MARKETING,
        TRANSACTIONAL,
        NOTIFICATION,
        ALERT,
        WELCOME,
        PASSWORD_RESET,
        ORDER_CONFIRMATION,
        SHIPPING_NOTIFICATION,
        INVOICE,
        REMINDER,
        PROMOTIONAL,
        NEWSLETTER,
        SURVEY,
        SUPPORT
    }

    public enum TemplateStatus {
        DRAFT,
        ACTIVE,
        INACTIVE,
        ARCHIVED
    }
}
