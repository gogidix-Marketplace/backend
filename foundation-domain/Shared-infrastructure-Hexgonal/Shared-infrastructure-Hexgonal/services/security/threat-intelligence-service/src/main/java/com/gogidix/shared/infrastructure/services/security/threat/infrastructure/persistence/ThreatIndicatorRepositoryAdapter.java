package com.gogidix.shared.infrastructure.services.security.threat.infrastructure.persistence;
import com.gogidix.shared.infrastructure.services.security.threat.domain.model.ThreatIndicator;
import com.gogidix.shared.infrastructure.services.security.threat.domain.port.out.ThreatIndicatorRepositoryPort;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
/**
 * MongoDB adapter implementation of ThreatIndicatorRepositoryPort.
 */
@Repository
public class ThreatIndicatorRepositoryAdapter implements ThreatIndicatorRepositoryPort {
    private final ThreatIndicatorRepository threatIndicatorRepository;
    public ThreatIndicatorRepositoryAdapter(ThreatIndicatorRepository threatIndicatorRepository) {
        this.threatIndicatorRepository = threatIndicatorRepository;
    }
    @Override
    public ThreatIndicator save(ThreatIndicator entity) {
        return threatIndicatorRepository.save(entity);
    }
    @Override
    public Optional<ThreatIndicator> findById(String id) {
        return threatIndicatorRepository.findById(id);
    }
    @Override
    public List<ThreatIndicator> findAllByTenantId(String tenantId) {
        return threatIndicatorRepository.findByTenantId_Value(tenantId);
    }
    @Override
    public Optional<ThreatIndicator> findByTenantIdAndId(String tenantId, String id) {
        return threatIndicatorRepository.findByTenantId_ValueAndId(tenantId, id);
    }
    @Override
    public List<ThreatIndicator> findByTenantIdAndActiveTrue(String tenantId) {
        return threatIndicatorRepository.findByTenantId_ValueAndActiveTrue(tenantId);
    }
    @Override
    public List<ThreatIndicator> findByTenantIdAndIndicatorType(String tenantId, String indicatorType) {
        return threatIndicatorRepository.findByTenantId_ValueAndIndicatorType(tenantId, indicatorType);
    }
    @Override
    public long countByTenantId(String tenantId) {
        return threatIndicatorRepository.countByTenantId_Value(tenantId);
    }
    @Override
    public void deleteByTenantIdAndId(String tenantId, String id) {
        threatIndicatorRepository.deleteByTenantId_ValueAndId(tenantId, id);
    }
    @Override
    public boolean existsByTenantIdAndValue(String tenantId, String value) {
        return threatIndicatorRepository.findByTenantId_Value(tenantId).stream()
            .anyMatch(indicator -> indicator.getValue().equals(value));
    }
}
