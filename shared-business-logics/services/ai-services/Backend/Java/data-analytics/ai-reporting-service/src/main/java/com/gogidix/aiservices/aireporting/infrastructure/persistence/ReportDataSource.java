package com.gogidix.aiservices.aireporting.infrastructure.persistence;

import java.util.Optional;

public interface ReportDataSource {
    ReportEntity save(ReportEntity entity);
    Optional<ReportEntity> findById(String id);
    void delete(String id);
}
