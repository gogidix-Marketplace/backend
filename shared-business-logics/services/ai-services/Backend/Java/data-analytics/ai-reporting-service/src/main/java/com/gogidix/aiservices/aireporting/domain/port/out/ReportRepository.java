package com.gogidix.aiservices.aireporting.domain.port.out;

import com.gogidix.aiservices.aireporting.domain.aggregate.ReportGeneration;

import java.util.Optional;

public interface ReportRepository {
    ReportGeneration save(ReportGeneration generation);
    Optional<ReportGeneration> findById(String id);
    void delete(String id);
}
