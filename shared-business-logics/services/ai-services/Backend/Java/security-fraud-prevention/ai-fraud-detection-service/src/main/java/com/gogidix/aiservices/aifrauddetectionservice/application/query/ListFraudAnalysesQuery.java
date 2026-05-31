package com.gogidix.aiservices.aifrauddetectionservice.application.query;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.FraudAnalysisDTO;
import com.gogidix.aiservices.aifrauddetectionservice.application.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Map;

/**
 * CQRS Query for listing fraud analyses with pagination and filtering.
 * Used to retrieve paginated lists of fraud analyses based on various criteria.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListFraudAnalysesQuery implements Query<PageResponse<FraudAnalysisDTO>> {

    @Valid
    private Pageable pageable;

    private String transactionId;

    private String userId;

    private String tenantId;

    private String riskLevel;

    private Instant startDate;

    private Instant endDate;

    private Double minFraudScore;

    private Double maxFraudScore;

    private String action;

    private Map<String, Object> additionalFilters;
}
