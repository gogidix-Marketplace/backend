package com.gogidix.ecommerce.customer.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest(
    @NotBlank String name,
    String description,
    String type,
    String email,
    String phone,
    String tier
) {}
