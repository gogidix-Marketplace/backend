package com.gogidix.aiservices.aifrauddetectionservice.application.query;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.FraudPatternDTO;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * CQRS Query for retrieving fraud patterns.
 * Used to fetch fraud patterns based on type and active status.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetFraudPatternsQuery implements Query<List<FraudPatternDTO>> {

    private String patternType;

    @Builder.Default
    private final Boolean isActive = true;

    private String tenantId;

    private Pageable pageable;

    @Min(value = 0, message = "Minimum confidence score must be non-negative")
    private Double minConfidenceScore;
}
