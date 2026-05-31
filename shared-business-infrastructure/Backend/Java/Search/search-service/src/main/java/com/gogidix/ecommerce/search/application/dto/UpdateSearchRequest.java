package com.gogidix.ecommerce.search.application.dto;

public record UpdateSearchRequest(
    String name,
    String description,
    String type,
    String indexName,
    String queryType,
    Integer resultCount,
    Boolean isActive
) {}
