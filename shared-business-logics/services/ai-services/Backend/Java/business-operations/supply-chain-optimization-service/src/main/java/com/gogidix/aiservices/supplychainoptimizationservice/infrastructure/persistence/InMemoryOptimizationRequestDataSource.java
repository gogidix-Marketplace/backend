package com.gogidix.aiservices.supplychainoptimizationservice.infrastructure.persistence;

import com.gogidix.aiservices.supplychainoptimizationservice.domain.aggregate.OptimizationRequest;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationStatus;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationType;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class InMemoryOptimizationRequestDataSource {

    private final Map<String, OptimizationRequestEntity> byId = new ConcurrentHashMap<>();
    private final Map<String, List<OptimizationRequestEntity>> byTenant = new ConcurrentHashMap<>();
    private final Map<String, List<OptimizationRequestEntity>> byStatus = new ConcurrentHashMap<>();
    private final Map<String, List<OptimizationRequestEntity>> byType = new ConcurrentHashMap<>();

    public OptimizationRequestEntity save(OptimizationRequestEntity entity) {
        byId.put(entity.getId(), entity);

        byTenant.computeIfAbsent(entity.getTenantId(), k -> new ArrayList<>())
                .removeIf(e -> e.getId().equals(entity.getId()));
        byTenant.get(entity.getTenantId()).add(entity);

        byStatus.computeIfAbsent(entity.getStatus().toString(), k -> new ArrayList<>())
                .removeIf(e -> e.getId().equals(entity.getId()));
        byStatus.get(entity.getStatus().toString()).add(entity);

        byType.computeIfAbsent(entity.getType().toString(), k -> new ArrayList<>())
                .removeIf(e -> e.getId().equals(entity.getId()));
        byType.get(entity.getType().toString()).add(entity);

        return entity;
    }

    public Optional<OptimizationRequestEntity> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    public List<OptimizationRequestEntity> findByTenantId(String tenantId) {
        return byTenant.getOrDefault(tenantId, new ArrayList<>());
    }

    public List<OptimizationRequestEntity> findByStatus(OptimizationStatus status) {
        return byStatus.getOrDefault(status.toString(), new ArrayList<>());
    }

    public List<OptimizationRequestEntity> findByType(OptimizationType type) {
        return byType.getOrDefault(type.toString(), new ArrayList<>());
    }

    public List<OptimizationRequestEntity> findPendingRequests(int limit) {
        return byId.values().stream()
                .filter(e -> e.getStatus() == OptimizationStatus.PENDING)
                .sorted(Comparator.comparing(OptimizationRequestEntity::getPriority).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<OptimizationRequestEntity> findByCreatedAtAfter(Instant timestamp) {
        return byId.values().stream()
                .filter(e -> e.getCreatedAt().isAfter(timestamp))
                .collect(Collectors.toList());
    }

    public void delete(String id) {
        OptimizationRequestEntity entity = byId.remove(id);
        if (entity != null) {
            byTenant.getOrDefault(entity.getTenantId(), new ArrayList<>())
                    .removeIf(e -> e.getId().equals(id));
            byStatus.getOrDefault(entity.getStatus().toString(), new ArrayList<>())
                    .removeIf(e -> e.getId().equals(id));
            byType.getOrDefault(entity.getType().toString(), new ArrayList<>())
                    .removeIf(e -> e.getId().equals(id));
        }
    }

    public List<OptimizationRequestEntity> findAll() {
        return new ArrayList<>(byId.values());
    }

    public List<OptimizationRequestEntity> saveAll(List<OptimizationRequestEntity> entities) {
        List<OptimizationRequestEntity> saved = new ArrayList<>();
        for (OptimizationRequestEntity entity : entities) {
            saved.add(save(entity));
        }
        return saved;
    }
}
