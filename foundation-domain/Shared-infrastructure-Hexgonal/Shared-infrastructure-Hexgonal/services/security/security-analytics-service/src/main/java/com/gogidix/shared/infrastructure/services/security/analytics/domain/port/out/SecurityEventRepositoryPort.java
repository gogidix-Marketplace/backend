package com.gogidix.shared.infrastructure.services.security.analytics.domain.port.out;
import com.gogidix.shared.infrastructure.services.security.analytics.domain.model.SecurityEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * Output port for SecurityEvent repository.
 */
public interface SecurityEventRepositoryPort {
    SecurityEvent save(SecurityEvent entity);
    Optional<SecurityEvent> findById(String id);
    List<SecurityEvent> findAllByTenantId(String tenantId);
    Optional<SecurityEvent> findByTenantIdAndId(String tenantId, String id);
    List<SecurityEvent> findByTenantIdAndSeverity(String tenantId, String severity);
    List<SecurityEvent> findByTenantIdAndTimestampAfter(String tenantId, LocalDateTime timestamp);
    long countByTenantId(String tenantId);
    void deleteByTenantIdAndId(String tenantId, String id);
}
