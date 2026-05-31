package com.gogidix.globalbusinessmanagement.export.domain.repository;

import com.gogidix.globalbusinessmanagement.export.domain.model.ExportJob;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExportJobRepository extends MongoRepository<ExportJob, String> {
    List<ExportJob> findByTenantId(String tenantId);
}
