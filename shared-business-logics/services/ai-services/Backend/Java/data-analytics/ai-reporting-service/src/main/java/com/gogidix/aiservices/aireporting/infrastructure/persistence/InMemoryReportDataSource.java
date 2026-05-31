package com.gogidix.aiservices.aireporting.infrastructure.persistence;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryReportDataSource implements ReportDataSource {

    private final Map<String, ReportEntity> reports = new ConcurrentHashMap<>();

    @Override
    public ReportEntity save(ReportEntity entity) {
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(Instant.now());
        }
        reports.put(entity.getGenerationId(), entity);
        return entity;
    }

    @Override
    public Optional<ReportEntity> findById(String id) {
        return Optional.ofNullable(reports.get(id));
    }

    @Override
    public void delete(String id) {
        reports.remove(id);
    }
}
