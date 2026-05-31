package com.gogidix.ecommerce.category.application.dto;

public record CategorySeoDto(
    String metaTitle,
    String metaDescription,
    String metaKeywords,
    String slug
) {}
