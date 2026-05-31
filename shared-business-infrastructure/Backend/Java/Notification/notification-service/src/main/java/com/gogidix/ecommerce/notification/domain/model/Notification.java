package com.gogidix.ecommerce.notification.domain.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "notifications")
public class Notification extends BaseEntity {

    private String name;
    private String description;
    private String type;

    @Indexed
    @Field("is_active")
    private Boolean isActive;

    private Integer priority;

    private String recipientId;
    private String notificationType;
    private String message;

    public Notification() {
        this.isActive = true;
        this.priority = 0;
    }

    public Notification(String tenantId) {
        super(tenantId);
        this.isActive = true;
        this.priority = 0;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getRecipientId() { return recipientId; }
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }
    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
