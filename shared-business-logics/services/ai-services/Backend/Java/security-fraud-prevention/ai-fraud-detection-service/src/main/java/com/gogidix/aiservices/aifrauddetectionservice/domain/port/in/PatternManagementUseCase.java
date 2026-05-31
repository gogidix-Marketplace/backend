package com.gogidix.aiservices.aifrauddetectionservice.domain.port.in;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Input port for fraud pattern management operations.
 * This use case provides CRUD operations for managing fraud detection patterns
 * used by the AI fraud detection system.
 *
 * Patterns are reusable rules that define what constitutes fraudulent behavior.
 */
@UseCase(
    value = "Manages fraud detection patterns throughout their lifecycle",
    category = "command"
)
public interface PatternManagementUseCase {

    /**
     * Creates a new fraud detection pattern.
     *
     * @param request Pattern creation request containing all necessary data
     * @return UUID of the newly created pattern
     */
    UUID createPattern(CreatePatternRequest request);

    /**
     * Updates an existing fraud pattern.
     *
     * @param patternId Unique identifier of the pattern to update
     * @param request Updated pattern data
     * @return Updated pattern representation
     */
    Optional<FraudPattern> updatePattern(UUID patternId, UpdatePatternRequest request);

    /**
     * Deletes (deactivates) a fraud pattern.
     * Patterns are soft-deleted to maintain historical analysis accuracy.
     *
     * @param patternId Unique identifier of the pattern to delete
     * @return true if pattern was successfully deleted, false if not found
     */
    boolean deletePattern(UUID patternId);

    /**
     * Lists all fraud patterns, optionally filtered.
     *
     * @param tenantId Optional tenant identifier for filtering
     * @param activeOnly If true, only return active patterns
     * @param patternType Optional filter by pattern type
     * @return List of fraud patterns matching the criteria
     */
    List<FraudPattern> listPatterns(String tenantId, boolean activeOnly, String patternType);

    /**
     * Request object for creating a new fraud pattern.
     */
    record CreatePatternRequest(
        String patternType,
        String name,
        String description,
        String patternDefinition,
        Double riskThreshold,
        boolean active,
        String tenantId,
        String createdBy
    ) {}

    /**
     * Request object for updating an existing fraud pattern.
     */
    record UpdatePatternRequest(
        String name,
        String description,
        String patternDefinition,
        Double riskThreshold,
        Boolean active,
        String updatedBy
    ) {}

    /**
     * Representation of a fraud pattern.
     */
    record FraudPattern(
        UUID patternId,
        String patternType,
        String name,
        String description,
        String patternDefinition,
        Double riskThreshold,
        boolean active,
        String tenantId,
        java.time.Instant createdAt,
        java.time.Instant updatedAt
    ) {}
}
