package com.gogidix.digitalmarketing.countrymarketingdashboard.domain.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "campaigns")
public class Campaign {
    @Id private String id;
    private String tenantId;
    private String name;
    private String type;
    private String status;
    private BigDecimal budget;
    private BigDecimal spent;
    private BigDecimal remainingBudget;
    private Instant startDate;
    private Instant endDate;
    private String country;
    private String region;
    private String channel;
    private Instant createdAt;
    private Instant updatedAt;
}
