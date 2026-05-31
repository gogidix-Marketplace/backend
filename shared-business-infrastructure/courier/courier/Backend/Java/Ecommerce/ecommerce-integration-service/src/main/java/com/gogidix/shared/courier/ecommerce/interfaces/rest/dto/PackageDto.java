package com.gogidix.shared.courier.ecommerce.interfaces.rest.dto;

public record PackageDto(
    String description,
    double weight,
    int quantity,
    DimensionsDto dimensions
) {}
