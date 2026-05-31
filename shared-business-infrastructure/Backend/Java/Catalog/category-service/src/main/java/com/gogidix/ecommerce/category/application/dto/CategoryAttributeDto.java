package com.gogidix.ecommerce.category.application.dto;

import java.util.List;

public record CategoryAttributeDto(
    String name,
    String code,
    String type,
    Boolean isRequired,
    Boolean isFilterable,
    List<String> options
) {}
