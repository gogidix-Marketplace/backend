package com.gogidix.globalbusinessmanagement.reportbuilder.domain.repository;

import com.gogidix.globalbusinessmanagement.reportbuilder.domain.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {
    List<Report> findByTenantId(String tenantId);
}
