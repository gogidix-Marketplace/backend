package com.gogidix.globalbusinessmanagement.reportbuilder.domain.model;

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
@Document(collection = "reports")
public class Report {

    @Id
    private String id;
    private String tenantId;
    private String name;
    private String reportType;
    private String dataSource;
    private String format;
    private String status;
    private String schedule;
    private Instant createdAt;
    private Instant updatedAt;

    public Report(String tenantId) {
        this.id = UUID.randomUUID().toString();
        this.tenantId = tenantId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
