package com.gogidix.shared.infrastructure.services.security.threat.infrastructure.persistence;
import com.gogidix.shared.infrastructure.services.security.threat.domain.model.ThreatIndicator;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface ThreatIndicatorRepository extends MongoRepository<ThreatIndicator, String> {
    List<ThreatIndicator> findByTenantId_Value(String tenantId);
    Optional<ThreatIndicator> findByTenantId_ValueAndId(String tenantId, String id);
    List<ThreatIndicator> findByTenantId_ValueAndActiveTrue(String tenantId);
    List<ThreatIndicator> findByTenantId_ValueAndIndicatorType(String tenantId, String indicatorType);
    long countByTenantId_Value(String tenantId);
    void deleteByTenantId_ValueAndId(String tenantId, String id);
}
