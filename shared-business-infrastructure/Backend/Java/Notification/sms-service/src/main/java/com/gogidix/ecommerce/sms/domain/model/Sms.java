package com.gogidix.ecommerce.sms.domain.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "sms_messages")
public class Sms extends BaseEntity {

    private String name;
    private String description;
    private String type;

    @Indexed
    @Field("is_active")
    private Boolean isActive;

    private Integer priority;

    private String phoneNumber;
    private String message;
    private String deliveryStatus;

    public Sms() {
        this.isActive = true;
        this.priority = 0;
    }

    public Sms(String tenantId) {
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
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }
}
