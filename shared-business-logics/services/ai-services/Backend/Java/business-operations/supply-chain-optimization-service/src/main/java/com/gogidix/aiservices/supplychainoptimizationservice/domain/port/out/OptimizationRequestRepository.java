package com.gogidix.aiservices.supplychainoptimizationservice.domain.port.out;

import com.gogidix.aiservices.supplychainoptimizationservice.domain.aggregate.OptimizationRequest;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationStatus;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationType;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OptimizationRequestRepository {
    OptimizationRequest save(OptimizationRequest request);
    Optional<OptimizationRequest> findById(String id);
    Optional<OptimizationRequest> findByRequestId(UUID requestId);
    List<OptimizationRequest> findByTenantId(String tenantId);
    List<OptimizationRequest> findByStatus(OptimizationStatus status);
    List<OptimizationRequest> findByType(OptimizationType type);
    List<OptimizationRequest> findPendingRequests(int limit);
    List<OptimizationRequest> findByCreatedAtAfter(Instant timestamp);
    void delete(String id);
    List<OptimizationRequest> saveAll(List<OptimizationRequest> requests);
}
