package com.gogidix.ecommerce.category.application.dto;

import java.time.Instant;
import java.util.List;

public record CategoryDto(
    String id, String tenantId, String name, String code,
    String parentId, List<String> childrenIds, int level,
    boolean active, Instant createdAt, Instant updatedAt
) {}
