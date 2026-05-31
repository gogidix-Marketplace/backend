package com.gogidix.shared.infrastructure.services.security.threat.domain.port.out;
import com.gogidix.shared.infrastructure.services.security.threat.domain.model.ThreatIndicator;
import java.util.List;
import java.util.Optional;
/**
 * Output port for ThreatIndicator repository.
 */
public interface ThreatIndicatorRepositoryPort {
    ThreatIndicator save(ThreatIndicator entity);
    Optional<ThreatIndicator> findById(String id);
    List<ThreatIndicator> findAllByTenantId(String tenantId);
    Optional<ThreatIndicator> findByTenantIdAndId(String tenantId, String id);
    List<ThreatIndicator> findByTenantIdAndActiveTrue(String tenantId);
    List<ThreatIndicator> findByTenantIdAndIndicatorType(String tenantId, String indicatorType);
    long countByTenantId(String tenantId);
    void deleteByTenantIdAndId(String tenantId, String id);
    boolean existsByTenantIdAndValue(String tenantId, String value);
}
