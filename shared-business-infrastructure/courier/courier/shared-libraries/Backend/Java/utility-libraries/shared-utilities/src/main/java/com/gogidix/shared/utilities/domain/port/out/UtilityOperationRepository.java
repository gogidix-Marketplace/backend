package com.gogidix.shared.utilities.domain.port.out;

import com.gogidix.shared.utilities.domain.model.UtilityOperation;
import com.gogidix.shared.utilities.domain.model.OperationStatus;
import com.gogidix.shared.utilities.domain.model.UtilityType;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for utility operations
 * Defines the contract for persisting utility operations
 */
public interface UtilityOperationRepository {
    
    /**
     * Saves a utility operation
     */
    UtilityOperation save(UtilityOperation operation);
    
    /**
     * Finds an operation by ID
     */
    Optional<UtilityOperation> findById(String operationId);
    
    /**
     * Finds operations by user ID with limit
     */
    List<UtilityOperation> findByUserId(String userId, int limit);
    
    /**
     * Finds operations by status with limit
     */
    List<UtilityOperation> findByStatus(OperationStatus status, int limit);
    
    /**
     * Finds operations by type with limit
     */
    List<UtilityOperation> findByType(UtilityType type, int limit);
    
    /**
     * Finds operations by user ID and status
     */
    List<UtilityOperation> findByUserIdAndStatus(String userId, OperationStatus status, int limit);
    
    /**
     * Finds operations by user ID and type
     */
    List<UtilityOperation> findByUserIdAndType(String userId, UtilityType type, int limit);
    
    /**
     * Deletes an operation by ID
     */
    void deleteById(String operationId);
    
    /**
     * Deletes operations older than specified age in days
     */
    int deleteOlderThan(int maxAgeDays);
    
    /**
     * Counts operations by status
     */
    long countByStatus(OperationStatus status);
    
    /**
     * Counts operations by user ID
     */
    long countByUserId(String userId);
    
    /**
     * Counts operations by user ID and status
     */
    long countByUserIdAndStatus(String userId, OperationStatus status);
    
    /**
     * Gets average execution time by user ID
     */
    double getAverageExecutionTimeByUserId(String userId);
    
    /**
     * Gets global average execution time
     */
    double getGlobalAverageExecutionTime();
    
    /**
     * Checks if operation exists by ID
     */
    boolean existsById(String operationId);
    
    /**
     * Updates operation status
     */
    UtilityOperation updateStatus(String operationId, OperationStatus status);
    
    /**
     * Updates operation progress
     */
    UtilityOperation updateProgress(String operationId, int progressPercentage, String currentPhase);
}