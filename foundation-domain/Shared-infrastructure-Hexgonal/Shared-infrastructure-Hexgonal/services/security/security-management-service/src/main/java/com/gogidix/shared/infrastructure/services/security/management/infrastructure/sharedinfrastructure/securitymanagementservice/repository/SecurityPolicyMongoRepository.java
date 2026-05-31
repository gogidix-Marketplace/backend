package com.gogidix.shared.infrastructure.services.security.management.infrastructure.sharedinfrastructure.securitymanagementservice.repository;

import com.gogidix.shared.infrastructure.services.security.management.infrastructure.sharedinfrastructure.securitymanagementservice.entity.SecurityPolicy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SecurityPolicyMongoRepository extends MongoRepository<SecurityPolicy, String> {

    // Tenant-aware queries
    List<SecurityPolicy> findByTenantId(String tenantId);

    // Basic queries
    SecurityPolicy findByPolicyNameAndTenantId(String policyName, String tenantId);
    List<SecurityPolicy> findByIsActiveTrueAndPolicyStatusAndTenantId(String policyStatus, String tenantId);
    List<SecurityPolicy> findByPolicyTypeAndIsActiveTrueAndTenantId(String policyType, String tenantId);
    List<SecurityPolicy> findByPriorityLevelAndIsActiveTrueAndTenantId(String priorityLevel, String tenantId);

    // Count queries
    Long countByIsActiveTrueAndPolicyStatusAndTenantId(String policyStatus, String tenantId);
    Long countByPolicyTypeAndIsActiveTrueAndTenantId(String policyType, String tenantId);
    Long countByPriorityLevelAndIsActiveTrueAndTenantId(String priorityLevel, String tenantId);

    // Date-based queries
    List<SecurityPolicy> findByExpiryDateBeforeAndTenantId(LocalDateTime currentDate, String tenantId);
    Long countByExpiryDateBeforeAndTenantId(LocalDateTime currentDate, String tenantId);

    @Query("{ 'expiryDate': { $lte: ?0, $gt: ?1 }, 'tenantId': ?2, 'deleted': false }")
    List<SecurityPolicy> findExpiringPolicies(LocalDateTime threshold, LocalDateTime currentDate, String tenantId);

    @Query("{ 'applicableServices': { $in: [?0] }, 'tenantId': ?1, 'deleted': false }")
    List<SecurityPolicy> findApplicablePoliciesForService(String serviceName, String tenantId);

    @Query("{ 'applicableRoles': { $in: [?0] }, 'tenantId': ?1, 'deleted': false }")
    List<SecurityPolicy> findApplicablePoliciesForRole(String roleName, String tenantId);

    @Query("{ 'violationCount': { $gt: 0 }, 'tenantId': ?0, 'deleted': false }")
    List<SecurityPolicy> findPoliciesWithViolations(String tenantId);

    @Query(value = "{ 'complianceMapping': { $ne: null }, 'tenantId': ?0, 'deleted': false }", fields = "{ 'complianceMapping': 1 }")
    List<SecurityPolicy> getComplianceStatistics(String tenantId);

    // Additional query methods needed by service
    List<SecurityPolicy> findByIsActiveTrueAndPolicyStatus(SecurityPolicy.PolicyStatus policyStatus);
    List<SecurityPolicy> findByPolicyTypeAndIsActiveTrue(SecurityPolicy.PolicyType policyType);
    List<SecurityPolicy> findByPriorityLevelAndIsActiveTrue(SecurityPolicy.PriorityLevel priorityLevel);
    Long countByIsActiveTrueAndPolicyStatus(SecurityPolicy.PolicyStatus policyStatus);
    Long countExpiredPolicies(LocalDateTime currentDate);
    Long countByPriorityLevelAndIsActiveTrue(SecurityPolicy.PriorityLevel priorityLevel);
    Long countByPolicyTypeAndIsActiveTrue(SecurityPolicy.PolicyType policyType);
    List<SecurityPolicy> findByIsActiveTrueOrderByPriorityDesc();
    List<SecurityPolicy> findExpiredPolicies(LocalDateTime currentDate);
}
