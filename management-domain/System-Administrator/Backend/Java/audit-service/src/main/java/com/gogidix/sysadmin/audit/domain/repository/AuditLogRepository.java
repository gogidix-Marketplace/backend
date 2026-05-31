package com.gogidix.sysadmin.audit.domain.repository;

import com.gogidix.sysadmin.audit.domain.model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {}
