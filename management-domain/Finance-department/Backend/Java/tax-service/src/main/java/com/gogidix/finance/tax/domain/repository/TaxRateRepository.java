package com.gogidix.finance.tax.domain.repository;

import com.gogidix.finance.tax.domain.model.TaxRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Tax Rate Repository Interface (Port)
 * Defines the contract for tax rate persistence operations
 */
public interface TaxRateRepository {

    TaxRate save(TaxRate taxRate);

    List<TaxRate> saveAll(List<TaxRate> taxRates);

    Optional<TaxRate> findById(String id);

    Optional<TaxRate> findByTaxRateIdAndTenantId(String taxRateId, String tenantId);

    List<TaxRate> findByTenantId(String tenantId);

    List<TaxRate> findByTenantIdAndJurisdiction(String tenantId, TaxRate.Jurisdiction jurisdiction);

    List<TaxRate> findByTenantIdAndTaxType(String tenantId, TaxRate.TaxType taxType);

    List<TaxRate> findByTenantIdAndJurisdictionAndTaxType(String tenantId, TaxRate.Jurisdiction jurisdiction,
                                                            TaxRate.TaxType taxType);

    Optional<TaxRate> findEffectiveRateForDate(String tenantId, TaxRate.Jurisdiction jurisdiction,
                                                TaxRate.TaxType taxType, String taxCode, LocalDate date);

    List<TaxRate> findByTenantIdAndEffectiveDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<TaxRate> findByTenantIdAndStatus(String tenantId, TaxRate.TaxRateStatus status);

    List<TaxRate> findByTaxCodeAndTenantId(String taxCode, String tenantId);

    List<TaxRate> findActiveRatesForJurisdiction(String tenantId, TaxRate.Jurisdiction jurisdiction);

    List<TaxRate> findActiveRatesForJurisdictionAndType(String tenantId, TaxRate.Jurisdiction jurisdiction,
                                                         TaxRate.TaxType taxType);

    List<TaxRate> findExpiringBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    boolean existsByTaxRateIdAndTenantId(String taxRateId, String tenantId);

    boolean existsByTaxCodeAndJurisdictionAndTenantId(String taxCode, TaxRate.Jurisdiction jurisdiction,
                                                       String tenantId);

    void deleteById(String id);

    void deleteByTaxRateIdAndTenantId(String taxRateId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, TaxRate.TaxRateStatus status);

    List<TaxRate> findByTenantIdAndTaxCode(String tenantId, String taxCode);

    List<TaxRate> findVersionsByTaxCode(String tenantId, String taxCode);
}
