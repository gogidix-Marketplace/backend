package com.gogidix.shared.infrastructure.services.security.management.domain.port.in;

import com.gogidix.shared.infrastructure.services.security.management.domain.model.SecurityManagement;
import java.util.List;
import java.util.Optional;

/**
 * Input Port: ManageSecurityUseCase
 * Defines the contract for security management operations
 */
public interface ManageSecurityUseCase {

    // Basic CRUD Operations
    SecurityManagement create(SecurityManagement securityManagement);
    Optional<SecurityManagement> findById(String id);
    List<SecurityManagement> findAll();
    SecurityManagement update(SecurityManagement securityManagement);
    void deleteById(String id);

    // Query Operations
    List<SecurityManagement> findByStatus(String status);
    List<SecurityManagement> findByType(String type);
    List<SecurityManagement> search(String keyword);

    // Business Operations
    SecurityManagement activatePolicy(String id);
    SecurityManagement deactivatePolicy(String id);
    SecurityManagement updatePolicy(String id, String newPolicy);
    List<SecurityManagement> getActivePolicies();
    List<SecurityManagement> getExpiredPolicies();
}
