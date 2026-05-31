package com.gogidix.shared.audit.application.port.out;

import com.gogidix.shared.audit.domain.AuditEvent;
import com.gogidix.shared.audit.domain.AuditSearchCriteria;

import java.util.List;
import java.util.Optional;

public interface AuditEventRepositoryPort {
    
    AuditEvent save(AuditEvent auditEvent);
    
    Optional<AuditEvent> findById(String eventId);
    
    List<AuditEvent> findByCriteria(AuditSearchCriteria criteria);
    
    long countAll();
    
    void deleteOldEvents(int retentionDays);
}