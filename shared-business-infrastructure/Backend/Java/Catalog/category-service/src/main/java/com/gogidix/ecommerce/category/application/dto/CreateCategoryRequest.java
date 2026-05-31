package com.gogidix.ecommerce.category.application.dto;

import java.util.List;

public record CreateCategoryRequest(
    String categoryCode,
    String name,
    String description,
    String parentId,
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
