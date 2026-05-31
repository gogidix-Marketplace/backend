package com.gogidix.courier.etaservice.application.dto;

import java.util.List;

/**
 * Response DTO for batch ETA calculation.
 */
public record BatchEtaResponse(

        int totalCount,
        int successCount,
        int failureCount,
        List<ResultItem> results

) {

    /**
     * Individual result item.
     */
    public record ResultItem(
            String dispatchId,
            boolean success,
            EtaResponse eta,
            String errorMessage
    ) {}
}
