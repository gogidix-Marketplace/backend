package com.gogidix.globalbusinessmanagement.localization.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "localization_entries")
public class LocalizationEntry {

    @Id
    private String id;
    private String tenantId;
    private String key;
    private String value;
    private String language;
    private String region;
    private String module;
    private String isActive;
    private Instant createdAt;
    private Instant updatedAt;

    public LocalizationEntry(String tenantId) {
        this.id = UUID.randomUUID().toString();
        this.tenantId = tenantId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
