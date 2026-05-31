package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.infrastructure.persistence.impl;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.model.ServiceLevelAgreement;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.port.out.IServiceLevelAgreementRepository;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.infrastructure.persistence.ServiceLevelAgreementRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * MongoDB implementation of ServiceLevelAgreement repository port
 */
@Repository
public class ServiceLevelAgreementRepositoryImpl implements IServiceLevelAgreementRepository {
    private final ServiceLevelAgreementRepository mongoRepository;
    public ServiceLevelAgreementRepositoryImpl(ServiceLevelAgreementRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }
    @Override
    public ServiceLevelAgreement save(ServiceLevelAgreement entity) {
        return mongoRepository.save(entity);
    }
    @Override
    public Optional<ServiceLevelAgreement> findById(String id) {
        return mongoRepository.findById(id);
    }
    @Override
    public List<ServiceLevelAgreement> findAllByTenantId(String tenantId) {
        return mongoRepository.findByTenantId_Value(tenantId);
    }
    @Override
    public Optional<ServiceLevelAgreement> findByIdAndTenantId(String id, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndId(tenantId, id);
    }
    @Override
    public Optional<ServiceLevelAgreement> findByNameAndTenantId(String name, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndName(tenantId, name);
    }
    @Override
    public List<ServiceLevelAgreement> findByTenantIdAndStatus(String tenantId, String status) {
        return mongoRepository.findByTenantId_ValueAndStatus(tenantId, status);
    }
    @Override
    public List<ServiceLevelAgreement> findByTenantIdAndServiceType(String tenantId, String serviceType) {
        return mongoRepository.findByTenantId_ValueAndServiceType(tenantId, serviceType);
    }
    @Override
    public List<ServiceLevelAgreement> findActiveByTenantAndDate(String tenantId, LocalDateTime date) {
        return mongoRepository.findActiveByTenantAndDate(tenantId, date);
    }
    @Override
    public long countByTenantId(String tenantId) {
        return mongoRepository.countByTenantId_Value(tenantId);
    }
    @Override
    public void deleteByTenantIdAndId(String tenantId, String id) {
        mongoRepository.deleteByTenantId_ValueAndId(tenantId, id);
    }
    @Override
    public boolean existsByNameAndTenantId(String name, String tenantId) {
        return mongoRepository.existsByTenantId_ValueAndName(tenantId, name);
    }
}
