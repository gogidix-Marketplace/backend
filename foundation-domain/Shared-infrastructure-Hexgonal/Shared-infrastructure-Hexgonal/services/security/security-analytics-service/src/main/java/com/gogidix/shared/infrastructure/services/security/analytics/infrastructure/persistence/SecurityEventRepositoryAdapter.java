package com.gogidix.shared.infrastructure.services.security.analytics.infrastructure.persistence;
import com.gogidix.shared.infrastructure.services.security.analytics.domain.model.SecurityEvent;
import com.gogidix.shared.infrastructure.services.security.analytics.domain.port.out.SecurityEventRepositoryPort;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * MongoDB adapter implementation of SecurityEventRepositoryPort.
 */
@Repository
public class SecurityEventRepositoryAdapter implements SecurityEventRepositoryPort {
    private final SecurityEventRepository securityEventRepository;
    public SecurityEventRepositoryAdapter(SecurityEventRepository securityEventRepository) {
        this.securityEventRepository = securityEventRepository;
    }
    @Override
    public SecurityEvent save(SecurityEvent entity) {
        return securityEventRepository.save(entity);
    }
    @Override
    public Optional<SecurityEvent> findById(String id) {
        return securityEventRepository.findById(id);
    }
    @Override
    public List<SecurityEvent> findAllByTenantId(String tenantId) {
        return securityEventRepository.findByTenantId_Value(tenantId);
    }
    @Override
    public Optional<SecurityEvent> findByTenantIdAndId(String tenantId, String id) {
        return securityEventRepository.findByTenantId_Value(tenantId).stream()
            .filter(event -> event.getId().equals(id))
            .findFirst();
    }
    @Override
    public List<SecurityEvent> findByTenantIdAndSeverity(String tenantId, String severity) {
        return securityEventRepository.findByTenantId_ValueAndSeverity(tenantId, severity);
    }
    @Override
    public List<SecurityEvent> findByTenantIdAndTimestampAfter(String tenantId, LocalDateTime timestamp) {
        return securityEventRepository.findByTenantId_ValueAndTimestampAfter(tenantId, timestamp);
    }
    @Override
    public long countByTenantId(String tenantId) {
        return securityEventRepository.countByTenantId_Value(tenantId);
    }
    @Override
    public void deleteByTenantIdAndId(String tenantId, String id) {
        securityEventRepository.deleteByTenantId_ValueAndId(tenantId, id);
    }
}
