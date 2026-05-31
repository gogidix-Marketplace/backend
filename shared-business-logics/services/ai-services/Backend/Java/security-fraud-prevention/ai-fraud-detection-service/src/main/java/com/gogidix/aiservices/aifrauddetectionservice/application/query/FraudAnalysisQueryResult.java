package com.gogidix.aiservices.aifrauddetectionservice.application.query;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.FraudAnalysisDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Result object for GetFraudAnalysisQuery.
 * Contains the fraud analysis details along with history if requested.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FraudAnalysisQueryResult {

    private FraudAnalysisDTO analysis;

    private List<AuditEntry> history;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuditEntry {
        private String timestamp;
        private String action;
        private String performedBy;
        private String details;
    }
}
