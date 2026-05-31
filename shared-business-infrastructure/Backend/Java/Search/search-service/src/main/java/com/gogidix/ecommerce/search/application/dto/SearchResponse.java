package com.gogidix.ecommerce.search.application.dto;

import java.time.Instant;

public record SearchResponse(
    String id,
    String name,
    String description,
    String type,
    String indexName,
    String queryType,
    Integer resultCount,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
