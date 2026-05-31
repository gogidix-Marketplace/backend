package com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "regional_dashboards")
public class RegionalDashboard {
    @Id private String id;
    private String dashboardId;
    private String name;
    private String description;
    private String regionCode;
    private String regionName;
    private String owner;
    private Boolean isPublic;
    private List<String> allowedRoles;
    private List<Widget> widgets;
    private DashboardConfig config;
    private String status;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}
