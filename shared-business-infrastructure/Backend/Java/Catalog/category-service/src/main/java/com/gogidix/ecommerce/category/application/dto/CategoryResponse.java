package com.gogidix.ecommerce.category.application.dto;

import java.time.Instant;
import java.util.List;

public record CategoryResponse(
    String id,
    String categoryCode,
    String name,
    String description,
    String parentId,
    Integer level,
    String path,
    List<String> ancestorIds,
    Integer displayOrder,
    String iconUrl,
    String bannerUrl,
    String imageUrl,
    List<String> imageUrls,
    CategorySeoDto seo,
    List<CategoryAttributeDto> attributes,
    Boolean isLeaf,
    Boolean isActive,
    Boolean isVisible,
    List<CategoryResponse> children,
    Instant createdAt,
    Instant updatedAt
) {}
