package com.gogidix.ecommerce.search.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSearchRequest(
    @NotBlank String name,
    String description,
    String type,
    String indexName,
    String queryType,
    Integer resultCount
) {}
