package com.gogidix.aiservices.intelligenceanalysisservice.domain.port.in;

/**
 * Input port for updating an existing customer segment.
 */
public interface UpdateAnalysisCommand {

    /**
     * Get the segment ID to update.
     *
     * @return the segment ID
     */
    String getAnalysisId();

    /**
     * Get the new segment name.
     *
     * @return the name
     */
    String getName();

    /**
     * Get the new segment description.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Get the updated segment criteria.
     *
     * @return the criteria
     */
    com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisCriteria getCriteria();

    /**
     * Get the new status.
     *
     * @return the status
     */
    String getStatus();

    /**
     * Get the tenant ID from context.
     *
     * @return the tenant ID
     */
    String getTenantId();

    /**
     * Get the user ID from context.
     *
     * @return the user ID
     */
    String getUserId();
}
