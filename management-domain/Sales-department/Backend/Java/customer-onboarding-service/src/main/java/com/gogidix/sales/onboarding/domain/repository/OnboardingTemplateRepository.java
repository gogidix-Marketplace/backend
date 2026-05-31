package com.gogidix.sales.onboarding.domain.repository;

import com.gogidix.sales.onboarding.domain.model.OnboardingTemplate;
import com.gogidix.sales.onboarding.domain.valueobject.CustomerType;

import java.util.List;
import java.util.Optional;

/**
 * Onboarding Template Repository Interface (Port)
 * Defines the contract for onboarding template persistence operations
 */
public interface OnboardingTemplateRepository {

    OnboardingTemplate save(OnboardingTemplate template);

    List<OnboardingTemplate> saveAll(List<OnboardingTemplate> templates);

    Optional<OnboardingTemplate> findById(String id);

    Optional<OnboardingTemplate> findByTemplateIdAndTenantId(String templateId, String tenantId);

    List<OnboardingTemplate> findByTenantId(String tenantId);

    List<OnboardingTemplate> findByTenantIdAndCustomerType(String tenantId, CustomerType customerType);

    List<OnboardingTemplate> findByTenantIdAndActive(String tenantId, Boolean active);

    List<OnboardingTemplate> findLatestVersionsByTenantId(String tenantId);

    Optional<OnboardingTemplate> findDefaultTemplateForCustomerType(String tenantId, CustomerType customerType);

    boolean existsByTemplateIdAndTenantId(String templateId, String tenantId);

    void deleteById(String id);

    void deleteByTemplateIdAndTenantId(String templateId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndActive(String tenantId, Boolean active);
}
