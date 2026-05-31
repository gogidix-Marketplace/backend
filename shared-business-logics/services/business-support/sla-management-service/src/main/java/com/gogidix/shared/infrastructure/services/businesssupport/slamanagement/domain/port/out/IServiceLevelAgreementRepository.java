package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.port.out;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.model.ServiceLevelAgreement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * Output port for ServiceLevelAgreement repository
 */
public interface IServiceLevelAgreementRepository {
    ServiceLevelAgreement save(ServiceLevelAgreement entity);
    Optional<ServiceLevelAgreement> findById(String id);
    List<ServiceLevelAgreement> findAllByTenantId(String tenantId);
    Optional<ServiceLevelAgreement> findByIdAndTenantId(String id, String tenantId);
    Optional<ServiceLevelAgreement> findByNameAndTenantId(String name, String tenantId);
    List<ServiceLevelAgreement> findByTenantIdAndStatus(String tenantId, String status);
    List<ServiceLevelAgreement> findByTenantIdAndServiceType(String tenantId, String serviceType);
    List<ServiceLevelAgreement> findActiveByTenantAndDate(String tenantId, LocalDateTime date);
    long countByTenantId(String tenantId);
    void deleteByTenantIdAndId(String tenantId, String id);
    boolean existsByNameAndTenantId(String name, String tenantId);
}
