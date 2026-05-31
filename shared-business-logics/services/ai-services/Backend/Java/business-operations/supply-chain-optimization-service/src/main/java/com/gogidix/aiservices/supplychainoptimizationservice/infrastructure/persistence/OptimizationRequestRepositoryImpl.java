package com.gogidix.aiservices.supplychainoptimizationservice.infrastructure.persistence;

import com.gogidix.aiservices.supplychainoptimizationservice.domain.aggregate.OptimizationRequest;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationStatus;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationType;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.port.out.OptimizationRequestRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OptimizationRequestRepositoryImpl implements OptimizationRequestRepository {

    private final InMemoryOptimizationRequestDataSource dataSource;

    public OptimizationRequestRepositoryImpl(InMemoryOptimizationRequestDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public OptimizationRequest save(OptimizationRequest request) {
        OptimizationRequestEntity entity = toEntity(request);
        OptimizationRequestEntity saved = dataSource.save(entity);
        return toRequest(saved);
    }

    @Override
    public Optional<OptimizationRequest> findById(String id) {
        return dataSource.findById(id).map(this::toRequest);
    }

    @Override
    public Optional<OptimizationRequest> findByRequestId(UUID requestId) {
        return dataSource.findAll().stream()
                .filter(e -> e.getRequestId().equals(requestId))
                .findFirst()
                .map(this::toRequest);
    }

    @Override
    public List<OptimizationRequest> findByTenantId(String tenantId) {
        return dataSource.findByTenantId(tenantId).stream()
                .map(this::toRequest)
                .collect(Collectors.toList());
    }

    @Override
    public List<OptimizationRequest> findByStatus(OptimizationStatus status) {
        return dataSource.findByStatus(status).stream()
                .map(this::toRequest)
                .collect(Collectors.toList());
    }

    @Override
    public List<OptimizationRequest> findByType(OptimizationType type) {
        return dataSource.findByType(type).stream()
                .map(this::toRequest)
                .collect(Collectors.toList());
    }

    @Override
    public List<OptimizationRequest> findPendingRequests(int limit) {
        return dataSource.findPendingRequests(limit).stream()
                .map(this::toRequest)
                .collect(Collectors.toList());
    }

    @Override
    public List<OptimizationRequest> findByCreatedAtAfter(Instant timestamp) {
        return dataSource.findByCreatedAtAfter(timestamp).stream()
                .map(this::toRequest)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        dataSource.delete(id);
    }

    @Override
    public List<OptimizationRequest> saveAll(List<OptimizationRequest> requests) {
        return dataSource.saveAll(requests.stream()
                .map(this::toEntity)
                .collect(Collectors.toList())).stream()
                .map(this::toRequest)
                .collect(Collectors.toList());
    }

    private OptimizationRequestEntity toEntity(OptimizationRequest request) {
        OptimizationRequestEntity entity = new OptimizationRequestEntity();
        entity.setRequestId(request.getRequestId());
        entity.setTenantId(request.getTenantId());
        entity.setType(request.getType());
        entity.setParameters(request.getParameters());
        entity.setStatus(request.getStatus());
        entity.setPriority(request.getPriority());
        entity.setCreatedAt(request.getCreatedAt());
        entity.setUpdatedAt(request.getUpdatedAt());
        entity.setCompletedAt(request.getCompletedAt());
        entity.setErrorMessage(request.getErrorMessage());
        return entity;
    }

    private OptimizationRequest toRequest(OptimizationRequestEntity entity) {
        return OptimizationRequest.builder()
                .requestId(entity.getRequestId())
                .tenantId(entity.getTenantId())
                .type(entity.getType())
                .parameters(entity.getParameters())
                .priority(entity.getPriority())
                .build();
    }
}
