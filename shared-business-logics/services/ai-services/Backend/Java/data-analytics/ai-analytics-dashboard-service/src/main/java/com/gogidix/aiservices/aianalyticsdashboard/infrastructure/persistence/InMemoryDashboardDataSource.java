package com.gogidix.aiservices.aianalyticsdashboard.infrastructure.persistence;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class InMemoryDashboardDataSource implements DashboardDataSource {

    private final Map<String, DashboardEntity> byId = new ConcurrentHashMap<>();

    @Override
    public DashboardEntity save(DashboardEntity entity) {
        byId.put(entity.getDashboardId(), entity);
        entity.setUpdatedAt(Instant.now());
        return entity;
    }

    @Override
    public Optional<DashboardEntity> findById(String dashboardId) {
        return Optional.ofNullable(byId.get(dashboardId));
    }

    @Override
    public List<DashboardEntity> findByUserId(String userId) {
        return byId.values().stream()
                .filter(e -> e.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<DashboardEntity> findPublicDashboards() {
        return byId.values().stream()
                .filter(DashboardEntity::isPublic)
                .collect(Collectors.toList());
    }

    @Override
    public List<DashboardEntity> findUpdatedAfter(Instant timestamp) {
        return byId.values().stream()
                .filter(e -> e.getUpdatedAt().isAfter(timestamp))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String dashboardId) {
        byId.remove(dashboardId);
    }
}
