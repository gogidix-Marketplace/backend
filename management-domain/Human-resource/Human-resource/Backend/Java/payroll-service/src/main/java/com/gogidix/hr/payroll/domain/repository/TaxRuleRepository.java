package com.gogidix.hr.payroll.domain.repository;

import com.gogidix.hr.payroll.domain.model.TaxRule;
import com.gogidix.hr.payroll.domain.enums.TaxType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for TaxRule
 */
public interface TaxRuleRepository {

    TaxRule save(TaxRule taxRule);

    List<TaxRule> saveAll(List<TaxRule> taxRules);

    Optional<TaxRule> findById(String id);

    Optional<TaxRule> findByTaxCode(String taxCode);

    List<TaxRule> findByTenantId(String tenantId);

    List<TaxRule> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<TaxRule> findByTenantIdAndIsActive(String tenantId, Boolean isActive);

    List<TaxRule> findByTaxType(TaxType taxType);

    List<TaxRule> findByCountryCodeAndIsActive(String countryCode, Boolean isActive);

    List<TaxRule> findByJurisdictionLevel(String jurisdictionLevel);

    List<TaxRule> findEffectiveRules(String countryCode, LocalDate date);

    List<TaxRule> findApplicableRules(String tenantId, String countryCode, LocalDate date);

    List<TaxRule> findByStateCode(String stateCode);

    List<TaxRule> findFederalTaxes(String countryCode);

    List<TaxRule> findStateTaxes(String countryCode, String stateCode);

    List<TaxRule> findLocalTaxes(String countryCode, String stateCode, String localCode);

    void deleteById(String id);

    List<TaxRule> findAllActive();

    boolean existsByTaxCode(String taxCode);
}
