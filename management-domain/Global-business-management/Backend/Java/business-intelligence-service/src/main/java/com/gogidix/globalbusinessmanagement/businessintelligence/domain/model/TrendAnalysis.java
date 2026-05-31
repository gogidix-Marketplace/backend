package com.gogidix.globalbusinessmanagement.businessintelligence.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "trend_analyses")
public class TrendAnalysis {
    @Id private String id;
    private String analysisName;
    private String metric;
    private String direction;
    private BigDecimal magnitude;
    private BigDecimal confidence;
    private String period;
    private Instant startDate;
    private Instant endDate;
    private List<DataPoint> dataPoints;
    private String category;
    private String regionCode;
    private String tenantId;
    private Map<String, Object> metadata;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataPoint {
        private Instant timestamp;
        private BigDecimal value;
        private String label;
    }
}
