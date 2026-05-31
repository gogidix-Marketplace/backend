package com.gogidix.customersupport.customerportal.domain.repository;

import com.gogidix.customersupport.customerportal.domain.model.CustomerProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerProfileRepository extends MongoRepository<CustomerProfile, String> {

    List<CustomerProfile> findByTenantId(String tenantId);

    Optional<CustomerProfile> findByTenantIdAndId(String tenantId, String id);

    Optional<CustomerProfile> findByCustomerId(String customerId);

    List<CustomerProfile> findByTenantIdAndEmail(String tenantId, String email);

    List<CustomerProfile> findByTenantIdAndCompanyId(String tenantId, String companyId);

    List<CustomerProfile> findByTenantIdAndCustomerType(String tenantId, String customerType);

    List<CustomerProfile> findByTenantIdAndTier(String tenantId, String tier);

    List<CustomerProfile> findByTenantIdAndIsActiveTrue(String tenantId);

    void deleteByTenantIdAndId(String tenantId, String id);

    boolean existsByCustomerId(String customerId);

    boolean existsByEmail(String email);
}
