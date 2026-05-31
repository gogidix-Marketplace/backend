package com.gogidix.sales.communication.domain.model;

import com.gogidix.sales.communication.shared.base.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Message Template Domain Entity
 * Multi-tenant message template for reusable content
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "message_templates")
public class MessageTemplate extends AuditableEntity {

    @Indexed
    private String templateId;

    @Indexed
    private String tenantId;

    private String name;

    private String description;

    @Indexed
    private String code; // Unique code for referencing the template

    private Message.ChannelType channelType;

    private TemplateType type;

    private String subject;

    private String content;

    private String htmlContent;

    private List<TemplateVariable> variables;

    private TemplateStatus status;

    private String category; // MARKETING, TRANSACTIONAL, NOTIFICATION, SUPPORT

    private List<String> tags;

    private String language;

    private String locale;

    private Integer version;

    private String parentTemplateId; // For versioning

    private Boolean isSystemTemplate;

    private Map<String, Object> metadata;

    private Long usageCount;

    private Instant lastUsedAt;

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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateVariable {
        private String name;
        private String description;
        private VariableType type;
        private Boolean isRequired;
        private String defaultValue;
        private List<String> allowedValues;
        private String regexPattern;

        public enum VariableType {
            STRING,
            NUMBER,
            DATE,
            BOOLEAN,
            URL,
            EMAIL,
            LIST
        }
    }

    /**
     * Creates a new message template
     */
    public static MessageTemplate create(String tenantId, String code, String name,
                                          Message.ChannelType channelType, TemplateType type,
                                          String subject, String content,
                                          List<TemplateVariable> variables) {
        return MessageTemplate.builder()
                .tenantId(tenantId)
                .code(code)
                .name(name)
                .channelType(channelType)
                .type(type)
                .subject(subject)
                .content(content)
                .variables(variables != null ? variables : new ArrayList<>())
                .status(TemplateStatus.DRAFT)
                .language("en")
                .locale("en_US")
                .version(1)
                .isSystemTemplate(false)
                .tags(new ArrayList<>())
                .metadata(new HashMap<>())
                .usageCount(0L)
                .build();
    }

    /**
     * Activates the template
     */
    public void activate() {
        this.status = TemplateStatus.ACTIVE;
    }

    /**
     * Deactivates the template
     */
    public void deactivate() {
        this.status = TemplateStatus.INACTIVE;
    }

    /**
     * Archives the template
     */
    public void archive() {
        this.status = TemplateStatus.ARCHIVED;
    }

    /**
     * Creates a new version of the template
     */
    public MessageTemplate createNewVersion(String newContent, String newSubject) {
        MessageTemplate newVersion = MessageTemplate.builder()
                .tenantId(this.tenantId)
                .code(this.code)
                .name(this.name)
                .channelType(this.channelType)
                .type(this.type)
                .subject(newSubject != null ? newSubject : this.subject)
                .content(newContent)
                .htmlContent(this.htmlContent)
                .variables(this.variables)
                .status(TemplateStatus.DRAFT)
                .category(this.category)
                .tags(new ArrayList<>(this.tags))
                .language(this.language)
                .locale(this.locale)
                .version(this.version + 1)
                .parentTemplateId(this.templateId)
                .isSystemTemplate(this.isSystemTemplate)
                .metadata(new HashMap<>(this.metadata))
                .usageCount(0L)
                .build();

        return newVersion;
    }

    /**
     * Records usage of the template
     */
    public void recordUsage() {
        if (this.usageCount == null) {
            this.usageCount = 0L;
        }
        this.usageCount++;
        this.lastUsedAt = Instant.now();
    }

    /**
     * Adds a tag to the template
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Removes a tag from the template
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Validates if all required variables are present
     */
    public boolean validateVariables(Map<String, Object> providedVariables) {
        if (this.variables == null || this.variables.isEmpty()) {
            return true;
        }

        for (TemplateVariable variable : this.variables) {
            if (variable.getIsRequired() &&
                    (providedVariables == null || !providedVariables.containsKey(variable.getName()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets required variables
     */
    public List<TemplateVariable> getRequiredVariables() {
        if (this.variables == null) {
            return new ArrayList<>();
        }
        return this.variables.stream()
                .filter(TemplateVariable::getIsRequired)
                .toList();
    }

    /**
     * Adds metadata
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }
}
