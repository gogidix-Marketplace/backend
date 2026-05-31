package com.gogidix.monitoring.servicehealthservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreComponentsResponseDto {

    private Double dependencyScore;
    private Double errorRateScore;
    private Double responseTimeScore;
    private Double uptimeScore;
}
