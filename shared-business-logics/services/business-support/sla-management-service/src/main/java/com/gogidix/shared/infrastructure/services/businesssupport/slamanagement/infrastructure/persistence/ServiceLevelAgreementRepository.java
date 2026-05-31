package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.infrastructure.persistence;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.model.ServiceLevelAgreement;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
public interface ServiceLevelAgreementRepository extends MongoRepository<ServiceLevelAgreement, String> {
    // Find by tenant
    List<ServiceLevelAgreement> findByTenantId_Value(String tenantId);
    // Find by tenant and ID
    Optional<ServiceLevelAgreement> findByTenantId_ValueAndId(String tenantId, String id);
    // Find by tenant and name
    Optional<ServiceLevelAgreement> findByTenantId_ValueAndName(String tenantId, String name);
    // Find by tenant and status
    List<ServiceLevelAgreement> findByTenantId_ValueAndStatus(String tenantId, String status);
    // Find by tenant and service type
    List<ServiceLevelAgreement> findByTenantId_ValueAndServiceType(String tenantId, String serviceType);
    // Delete by tenant and ID
    void deleteByTenantId_ValueAndId(String tenantId, String id);
    // Count by tenant
    long countByTenantId_Value(String tenantId);
    // Check exists by tenant and name
    boolean existsByTenantId_ValueAndName(String tenantId, String name);
    // Find active SLAs by tenant
    @Query("{'tenantId.value': ?0, 'status': 'ACTIVE', 'validFrom': {$lte: ?1}, 'validUntil': {$gte: ?1}}")
    List<ServiceLevelAgreement> findActiveByTenantAndDate(String tenantId, LocalDateTime date);
}
