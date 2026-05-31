package com.gogidix.hr.notification.domain.model;

import com.gogidix.hr.notification.domain.enums.NotificationChannel;
import com.gogidix.hr.notification.domain.enums.NotificationType;
import com.gogidix.hr.notification.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * NotificationTemplate Domain Entity
 * Represents reusable notification templates
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "notification_templates")
public class NotificationTemplate extends BaseEntity {

    @Indexed(unique = true)
    private String templateCode;

    @Indexed
    private String tenantId;

    private String templateName;
    private String description;

    @Indexed
    private NotificationType type;

    @Indexed
    private NotificationChannel channel;

    private String subject;
    private String body;
    private String htmlBody;

    @Builder.Default
    private Map<String, TemplateVariable> variables = new HashMap<>();

    @Builder.Default
    private List<String> supportedLanguages = new ArrayList<>();

    @Builder.Default
    private Map<String, LocalizedContent> localizedContent = new HashMap<>();

    @Indexed
    private String category;

    @Indexed
    private Boolean isActive;

    @Indexed
    private String createdBy;

    @Indexed
    private Integer version;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Template variable
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateVariable {
        private String name;
        private String type;
        private String description;
        private Boolean isRequired;
        private String defaultValue;
    }

    /**
     * Localized content
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocalizedContent {
        private String language;
        private String subject;
        private String body;
        private String htmlBody;
    }

    /**
     * Creates a new notification template
     */
    public static NotificationTemplate create(String tenantId, String templateName,
                                               NotificationType type, NotificationChannel channel,
                                               String subject, String body, String createdBy) {
        String templateCode = generateTemplateCode(tenantId, type);

        NotificationTemplate template = new NotificationTemplate();
        template.setTenantId(tenantId);
        template.setTemplateName(templateName);
        template.setType(type);
        template.setChannel(channel);
        template.setSubject(subject);
        template.setBody(body);
        template.setTemplateCode(templateCode);
        template.setCreatedBy(createdBy);
        template.setIsActive(true);
        template.setVersion(1);
        template.setVariables(new HashMap<>());
        template.setSupportedLanguages(new ArrayList<>());
        template.setLocalizedContent(new HashMap<>());
        template.setTags(new ArrayList<>());
        template.setMetadata(new HashMap<>());

        return template;
    }

    /**
     * Activates template
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Deactivates template
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Adds variable
     */
    public void addVariable(String name, String type, String description, Boolean isRequired) {
        if (this.variables == null) {
            this.variables = new HashMap<>();
        }
        TemplateVariable variable = new TemplateVariable(name, type, description, isRequired, null);
        this.variables.put(name, variable);
    }

    /**
     * Adds supported language
     */
    public void addSupportedLanguage(String language) {
        if (this.supportedLanguages == null) {
            this.supportedLanguages = new ArrayList<>();
        }
        if (!this.supportedLanguages.contains(language)) {
            this.supportedLanguages.add(language);
        }
    }

    /**
     * Adds localized content
     */
    public void addLocalizedContent(String language, String subject, String body, String htmlBody) {
        if (this.localizedContent == null) {
            this.localizedContent = new HashMap<>();
        }
        LocalizedContent content = new LocalizedContent(language, subject, body, htmlBody);
        this.localizedContent.put(language, content);
    }

    /**
     * Adds tag
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
     * Generates template code
     */
    private static String generateTemplateCode(String tenantId, NotificationType type) {
        String prefix = type.name().substring(0, 3).toUpperCase();
        String uniqueId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "TPL-" + prefix + "-" + uniqueId;
    }

    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
