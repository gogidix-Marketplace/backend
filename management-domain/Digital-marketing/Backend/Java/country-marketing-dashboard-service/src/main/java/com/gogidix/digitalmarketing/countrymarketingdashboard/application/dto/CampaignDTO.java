package com.gogidix.digitalmarketing.countrymarketingdashboard.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
public class CampaignDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCampaignRequest {
        private String name;
        private String type;
        private BigDecimal budget;
        private String country;
        private Instant startDate;
        private Instant endDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCampaignRequest {
        private String name;
        private String status;
    }
}
