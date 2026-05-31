package com.gogidix.shared.audit.adapter.out.persistence;

import com.gogidix.shared.audit.application.port.out.AuditEventRepositoryPort;
import com.gogidix.shared.audit.domain.AuditEvent;
import com.gogidix.shared.audit.domain.AuditSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuditEventRepositoryAdapter implements AuditEventRepositoryPort {

    private final AuditEventJpaRepository jpaRepository;

    @Override
    public AuditEvent save(AuditEvent auditEvent) {
        return jpaRepository.save(auditEvent);
    }

    @Override
    public Optional<AuditEvent> findById(String eventId) {
        return jpaRepository.findById(eventId);
    }

    @Override
    public List<AuditEvent> findByCriteria(AuditSearchCriteria criteria) {
        // For now, return all events. In production, implement proper criteria filtering
        return jpaRepository.findAll();
    }

    @Override
    public long countAll() {
        return jpaRepository.count();
    }

    @Override
    public void deleteOldEvents(int retentionDays) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
        jpaRepository.deleteByTimestampBefore(cutoffDate);
    }
}