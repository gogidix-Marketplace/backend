package com.gogidix.globalbusinessmanagement.scheduledreport.domain.repository;

import com.gogidix.globalbusinessmanagement.scheduledreport.domain.model.ScheduledReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduledReportRepository extends MongoRepository<ScheduledReport, String> {
    List<ScheduledReport> findByTenantId(String tenantId);
}
