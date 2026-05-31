package com.gogidix.sales.territory.domain.repository;

import com.gogidix.sales.territory.domain.model.TerritoryAssignment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Territory Assignment Repository Interface (Port)
 * Defines the contract for territory assignment persistence operations
 */
public interface TerritoryAssignmentRepository {

    TerritoryAssignment save(TerritoryAssignment assignment);

    List<TerritoryAssignment> saveAll(List<TerritoryAssignment> assignments);

    Optional<TerritoryAssignment> findById(String id);

    Optional<TerritoryAssignment> findByAssignmentIdAndTenantId(String assignmentId, String tenantId);

    List<TerritoryAssignment> findByTenantId(String tenantId);

    List<TerritoryAssignment> findByTenantIdAndTerritoryId(String tenantId, String territoryId);

    List<TerritoryAssignment> findByTenantIdAndSalesRepresentativeId(String tenantId, String salesRepresentativeId);

    List<TerritoryAssignment> findByTenantIdAndStatus(String tenantId, TerritoryAssignment.AssignmentStatus status);

    List<TerritoryAssignment> findActiveByTenantIdAndTerritoryId(String tenantId, String territoryId);

    List<TerritoryAssignment> findActiveByTenantIdAndSalesRepresentativeId(String tenantId, String salesRepresentativeId);

    List<TerritoryAssignment> findByTenantIdAndEffectiveDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<TerritoryAssignment> findPrimaryAssignmentsByTenantIdAndTerritoryId(String tenantId, String territoryId);

    Optional<TerritoryAssignment> findPrimaryByTenantIdAndSalesRepresentativeId(String tenantId, String salesRepresentativeId);

    boolean existsByTerritoryIdAndSalesRepresentativeIdAndStatus(String territoryId, String salesRepresentativeId,
                                                                   TerritoryAssignment.AssignmentStatus status);

    void deleteById(String id);

    void deleteByAssignmentIdAndTenantId(String assignmentId, String tenantId);

    void deleteByTerritoryIdAndTenantId(String territoryId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndTerritoryId(String tenantId, String territoryId);

    long countByTenantIdAndSalesRepresentativeId(String tenantId, String salesRepresentativeId);

    long countActiveByTenantIdAndTerritoryId(String tenantId, String territoryId);
}
