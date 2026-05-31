package com.gogidix.aiservices.aifrauddetectionservice.application.query;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.PageResponse;
import com.gogidix.aiservices.aifrauddetectionservice.application.dto.TransactionDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

/**
 * CQRS Query for searching transactions.
 * Used to find transactions matching specific keywords and criteria.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchTransactionsQuery implements Query<PageResponse<TransactionDTO>> {

    @NotBlank(message = "Search keywords are required")
    private String keywords;

    private Instant startDate;

    private Instant endDate;

    private String merchantId;

    private String userId;

    private String tenantId;

    private Pageable pageable;

    private List<String> searchFields;

    @Builder.Default
    private boolean fuzzySearch = false;

    private List<String> riskLevels;

    private List<String> currencies;
}
