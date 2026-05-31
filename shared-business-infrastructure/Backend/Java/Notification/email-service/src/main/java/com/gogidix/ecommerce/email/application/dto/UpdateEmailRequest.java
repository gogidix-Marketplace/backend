package com.gogidix.ecommerce.email.application.dto;

public record UpdateEmailRequest(
    String name,
    String description,
    String type,
    String recipient,
    String subject,
    String status,
    Boolean isActive
) {}
