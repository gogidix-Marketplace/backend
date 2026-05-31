package com.gogidix.hr.employeeselfservice.domain.repository;

import com.gogidix.hr.employeeselfservice.domain.model.QuickAction;

import java.util.List;
import java.util.Optional;

/**
 * Quick Action Repository Interface (Port)
 * Defines the contract for quick action persistence operations
 */
public interface QuickActionRepository {

    QuickAction save(QuickAction quickAction);

    List<QuickAction> saveAll(List<QuickAction> quickActions);

    Optional<QuickAction> findById(String id);

    Optional<QuickAction> findByActionCodeAndTenantId(String actionCode, String tenantId);

    List<QuickAction> findByTenantId(String tenantId);

    List<QuickAction> findByTenantIdAndEnabledTrue(String tenantId);

    List<QuickAction> findByTenantIdAndCategory(String tenantId, QuickAction.ActionCategory category);

    List<QuickAction> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<QuickAction> findByTenantIdAndEnabledTrueOrderByDisplayOrderAsc(String tenantId);

    List<QuickAction> findByTenantIdAndCategoryAndEnabledTrue(String tenantId, QuickAction.ActionCategory category);

    List<QuickAction> findByTenantIdAndIsFeaturedTrue(String tenantId);

    List<QuickAction> findByTenantIdAndIsNewTrue(String tenantId);

    List<QuickAction> findByTenantIdAndAllowedRolesContaining(String tenantId, String role);

    List<QuickAction> findByParentActionCodeAndTenantId(String parentActionCode, String tenantId);

    List<QuickAction> findDefaultActions();

    boolean existsByActionCodeAndTenantId(String actionCode, String tenantId);

    void deleteById(String id);

    void deleteByActionCodeAndTenantId(String actionCode, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndEnabledTrue(String tenantId);

    List<QuickAction> findByTenantIdAndEnabledTrueAndAllowedRolesContaining(String tenantId, String role);
}
