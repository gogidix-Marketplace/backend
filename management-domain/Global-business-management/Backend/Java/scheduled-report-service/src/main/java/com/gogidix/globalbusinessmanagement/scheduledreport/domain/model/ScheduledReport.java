package com.gogidix.globalbusinessmanagement.scheduledreport.domain.model;

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
@Document(collection = "scheduled_reports")
public class ScheduledReport {

    @Id
    private String id;
    private String tenantId;
    private String name;
    private String reportId;
    private String cronExpression;
    private String recipients;
    private String format;
    private String isActive;
    private String lastRunAt;
    private Instant createdAt;
    private Instant updatedAt;

    public ScheduledReport(String tenantId) {
        this.id = UUID.randomUUID().toString();
        this.tenantId = tenantId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
