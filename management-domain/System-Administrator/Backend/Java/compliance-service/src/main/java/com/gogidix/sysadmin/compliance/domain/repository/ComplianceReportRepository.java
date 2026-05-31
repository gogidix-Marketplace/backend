package com.gogidix.sysadmin.compliance.domain.repository;
import com.gogidix.sysadmin.compliance.domain.model.ComplianceReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ComplianceReportRepository extends MongoRepository<ComplianceReport, String> {}
