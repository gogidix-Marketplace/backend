package com.gogidix.globalbusinessmanagement.export.domain.model;

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
@Document(collection = "export_jobs")
public class ExportJob {

    @Id
    private String id;
    private String tenantId;
    private String name;
    private String exportType;
    private String format;
    private String status;
    private String filePath;
    private String recordCount;
    private Instant createdAt;
    private Instant updatedAt;

    public ExportJob(String tenantId) {
        this.id = UUID.randomUUID().toString();
        this.tenantId = tenantId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
