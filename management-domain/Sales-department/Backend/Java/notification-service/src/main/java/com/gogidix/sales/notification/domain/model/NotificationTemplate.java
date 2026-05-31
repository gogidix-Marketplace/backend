package com.gogidix.sales.notification.domain.model;

import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.shared.base.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

/**
 * Notification Template Domain Entity
 * Multi-tenant notification template management
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "notification_templates")
@CompoundIndex(def = "{'tenantId': 1, 'code': 1}", unique = true)
public class NotificationTemplate extends AuditableEntity {

    @Indexed
    private String templateId;

    @Indexed
    private String tenantId;

    @Indexed
    private String code;

    private String name;

    private String description;

    @Indexed
    private NotificationChannel channel;

    private String subjectTemplate;

    private String contentTemplate;

    private String htmlContentTemplate;

    private Map<String, TemplateVariable> variables;

    private String locale;

    private Boolean isActive;

    private Instant validFrom;

    private Instant validUntil;

    private String tags;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateVariable {
        private String name;
        private String type;
        private String description;
        private Boolean required;
        private String defaultValue;
    }

    /**
     * Creates a new notification template
     */
    public static NotificationTemplate create(String tenantId, String code, String name,
                                              NotificationChannel channel,
                                              String subjectTemplate, String contentTemplate) {
        NotificationTemplate template = NotificationTemplate.builder()
                .templateId(java.util.UUID.randomUUID().toString())
                .tenantId(tenantId)
                .code(code)
                .name(name)
                .channel(channel)
                .subjectTemplate(subjectTemplate)
                .contentTemplate(contentTemplate)
                .isActive(true)
                .build();

        return template;
    }

    /**
     * Activates the template
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Deactivates the template
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Updates the template content
     */
    public void updateContent(String subjectTemplate, String contentTemplate, String htmlContentTemplate) {
        this.subjectTemplate = subjectTemplate;
        this.contentTemplate = contentTemplate;
        this.htmlContentTemplate = htmlContentTemplate;
        this.version++;
    }

    /**
     * Checks if the template is valid
     */
    public boolean isValid() {
        Instant now = Instant.now();
        return this.isActive &&
               (this.validFrom == null || !now.isBefore(this.validFrom)) &&
               (this.validUntil == null || !now.isAfter(this.validUntil));
    }

    /**
     * Adds a variable to the template
     */
    public void addVariable(String name, String type, String description, Boolean required) {
        TemplateVariable variable = TemplateVariable.builder()
                .name(name)
                .type(type)
                .description(description)
                .required(required != null ? required : false)
                .build();
        this.variables.put(name, variable);
    }
}
