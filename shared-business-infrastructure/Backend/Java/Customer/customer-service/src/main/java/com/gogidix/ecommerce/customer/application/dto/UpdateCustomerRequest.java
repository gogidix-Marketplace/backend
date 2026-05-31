package com.gogidix.ecommerce.customer.application.dto;

public record UpdateCustomerRequest(
    String name,
    String description,
    String type,
    String email,
    String phone,
    String tier,
    Boolean isActive
) {}
