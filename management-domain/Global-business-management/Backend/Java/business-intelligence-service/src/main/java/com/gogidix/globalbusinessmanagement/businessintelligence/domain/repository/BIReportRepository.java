package com.gogidix.globalbusinessmanagement.businessintelligence.domain.repository;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.BIReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BIReportRepository extends MongoRepository<BIReport, String> {
    List<BIReport> findByTenantId(String tenantId);
}
