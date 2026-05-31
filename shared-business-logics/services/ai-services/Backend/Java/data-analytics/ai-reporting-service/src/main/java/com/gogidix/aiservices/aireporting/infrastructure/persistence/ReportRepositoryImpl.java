package com.gogidix.aiservices.aireporting.infrastructure.persistence;

import com.gogidix.aiservices.aireporting.domain.aggregate.ReportGeneration;
import com.gogidix.aiservices.aireporting.domain.port.out.ReportRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ReportRepositoryImpl implements ReportRepository {

    private final ReportDataSource dataSource;

    public ReportRepositoryImpl(ReportDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ReportGeneration save(ReportGeneration generation) {
        ReportEntity entity = toEntity(generation);
        dataSource.save(entity);
        return generation;
    }

    @Override
    public Optional<ReportGeneration> findById(String id) {
        return dataSource.findById(id).map(this::toDomain);
    }

    @Override
    public void delete(String id) {
        dataSource.delete(id);
    }

    private ReportEntity toEntity(ReportGeneration generation) {
        ReportEntity entity = new ReportEntity();
        entity.setGenerationId(generation.getGenerationId());
        entity.setType(generation.getType());
        entity.setIncludeMetrics(generation.getIncludeMetrics());
        entity.setFormat(generation.getFormat());
        entity.setDateRangeStart(generation.getDateRangeStart());
        entity.setDateRangeEnd(generation.getDateRangeEnd());
        entity.setStatus(generation.getStatus());
        entity.setProgress(generation.getProgress());
        entity.setDownloadUrl(generation.getDownloadUrl());
        entity.setExpiresAt(generation.getExpiresAt());
        entity.setErrorMessage(generation.getErrorMessage());
        entity.setCreatedAt(generation.getCreatedAt());
        entity.setCompletedAt(generation.getCompletedAt());
        return entity;
    }

    private ReportGeneration toDomain(ReportEntity entity) {
        // Simplified - in real implementation would use a restore method
        ReportGeneration generation = ReportGeneration.create(
                entity.getType(),
                entity.getIncludeMetrics(),
                entity.getFormat()
        );
        // Set additional fields as needed
        return generation;
    }
}
