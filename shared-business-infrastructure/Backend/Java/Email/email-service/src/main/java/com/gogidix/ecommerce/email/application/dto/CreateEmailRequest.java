package com.gogidix.ecommerce.email.application.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateEmailRequest {

    @NotBlank
    private String name;

    private String description;

    private String type;

    private String recipient;
    private String subject;
    private String bodyTemplate;
    private String status;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBodyTemplate() { return bodyTemplate; }
    public void setBodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}