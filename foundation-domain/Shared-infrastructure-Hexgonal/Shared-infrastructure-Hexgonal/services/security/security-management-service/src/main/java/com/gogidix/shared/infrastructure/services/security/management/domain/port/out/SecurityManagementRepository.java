package com.gogidix.shared.infrastructure.services.security.management.domain.port.out;

import com.gogidix.shared.infrastructure.services.security.management.domain.model.SecurityManagement;
import java.util.List;
import java.util.Optional;

/**
 * Output Port: SecurityManagementRepository
 * Repository interface for security management persistence operations
 */
public interface SecurityManagementRepository {

    // Basic CRUD Operations
    SecurityManagement save(SecurityManagement securityManagement);
    Optional<SecurityManagement> findById(String id);
    List<SecurityManagement> findAll();
    void deleteById(String id);
    boolean existsById(String id);

    // Query Operations
    List<SecurityManagement> findByStatus(String status);
    List<SecurityManagement> findByType(String type);
    List<SecurityManagement> findByCreatedBy(String createdBy);
    List<SecurityManagement> search(String keyword);

    // Advanced Queries
    List<SecurityManagement> findByCreatedAtBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
    List<SecurityManagement> findActivePolicies();
    List<SecurityManagement> findExpiredPolicies();

    // Statistics
    long count();
    long countByStatus(String status);
    long countByType(String type);
}
