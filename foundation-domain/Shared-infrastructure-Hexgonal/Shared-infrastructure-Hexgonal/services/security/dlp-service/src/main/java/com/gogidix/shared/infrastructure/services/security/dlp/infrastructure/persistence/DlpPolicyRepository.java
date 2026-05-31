package com.gogidix.shared.infrastructure.services.security.dlp.infrastructure.persistence;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.model.DlpPolicy;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.model.DlpPolicy.PolicyStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface DlpPolicyRepository extends MongoRepository<DlpPolicy, String> {
    List<DlpPolicy> findByTenantId_Value(String tenantId);
    Optional<DlpPolicy> findByTenantId_ValueAndId(String tenantId, String id);
    List<DlpPolicy> findByTenantId_ValueAndStatus(String tenantId, PolicyStatus status);
    long countByTenantId_Value(String tenantId);
    void deleteByTenantId_ValueAndId(String tenantId, String id);
}
