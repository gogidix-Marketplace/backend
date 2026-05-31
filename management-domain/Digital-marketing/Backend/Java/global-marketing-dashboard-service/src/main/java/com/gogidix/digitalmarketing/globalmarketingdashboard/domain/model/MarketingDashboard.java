package com.gogidix.digitalmarketing.globalmarketingdashboard.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "marketing_dashboards")
public class MarketingDashboard {
    @Id
    private String id;
    private String tenantId;
    private String name;
    private String description;
    private String region;
    private Instant createdAt;
}
