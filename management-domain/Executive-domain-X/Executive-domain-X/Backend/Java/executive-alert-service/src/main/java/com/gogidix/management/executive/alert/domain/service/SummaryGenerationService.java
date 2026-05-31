package com.gogidix.management.executive.alert.domain.service;

import com.gogidix.management.executive.alert.domain.model.Alert;
import com.gogidix.management.executive.alert.domain.model.ExecutiveSummary;

import java.time.Instant;
import java.util.List;

/**
 * Domain service for Executive Summary generation
 * Handles business logic for creating and managing executive summaries
 */
public interface SummaryGenerationService {

    /**
     * Generate executive summary for a tenant
     */
    ExecutiveSummary generateSummary(String tenantId, Instant forDate);

    /**
     * Generate executive summary for specific alerts
     */
    ExecutiveSummary generateSummaryForDashboards(String tenantId, List<String> dashboardIds, Instant forDate);

    /**
     * Validate summary generation request
     */
    void validateSummaryRequest(String tenantId, Instant forDate);

    /**
     * Check if summary exists for tenant and date
     */
    boolean summaryExists(String tenantId, Instant forDate);

    /**
     * Get latest summary for tenant
     */
    ExecutiveSummary getLatestSummary(String tenantId);

    /**
     * Refresh summary (regenerate for existing date)
     */
    ExecutiveSummary refreshSummary(String summaryId, String tenantId);
}
