package com.gogidix.customersupport.slamanagement.domain.repository;

import com.gogidix.customersupport.slamanagement.domain.model.SLAPolicy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SLAPolicyRepository extends MongoRepository<SLAPolicy, String> {

    List<SLAPolicy> findByTenantId(String tenantId);

    Optional<SLAPolicy> findByTenantIdAndId(String tenantId, String id);

    Optional<SLAPolicy> findByPolicyCode(String policyCode);

    List<SLAPolicy> findByTenantIdAndIsActive(String tenantId, Boolean isActive);

    List<SLAPolicy> findByTenantIdAndIsActiveTrue(String tenantId);

    List<SLAPolicy> findByTenantIdAndApplicableCategoriesContaining(String tenantId, String category);

    List<SLAPolicy> findByTenantIdAndApplicablePrioritiesContaining(String tenantId, String priority);

    void deleteByTenantIdAndId(String tenantId, String id);

    boolean existsByPolicyCode(String policyCode);
}
