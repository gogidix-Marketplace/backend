package com.gogidix.ecommerce.category.application.dto;

import java.util.List;

public record UpdateCategoryRequest(
    String name,
    String description,
    Integer displayOrder,
    String iconUrl,
    String bannerUrl,
    String imageUrl,
    List<String> imageUrls,
    Boolean isActive,
    Boolean isVisible,
    CategorySeoDto seo,
    List<CategoryAttributeDto> attributes
) {}
