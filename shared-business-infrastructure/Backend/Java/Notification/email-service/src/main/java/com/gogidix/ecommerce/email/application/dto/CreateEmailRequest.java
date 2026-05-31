package com.gogidix.ecommerce.email.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateEmailRequest(
    @NotBlank String name,
    String description,
    String type,
    String recipient,
    String subject,
    String status
) {}
