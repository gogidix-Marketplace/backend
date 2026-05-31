package com.gogidix.finance.tax.domain.repository;

import com.gogidix.finance.tax.domain.model.TaxFiling;
import com.gogidix.finance.tax.domain.model.TaxRate;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * Tax Filing Repository Interface (Port)
 * Defines the contract for tax filing persistence operations
 */
public interface TaxFilingRepository {

    TaxFiling save(TaxFiling filing);

    List<TaxFiling> saveAll(List<TaxFiling> filings);

    Optional<TaxFiling> findById(String id);

    Optional<TaxFiling> findByFilingIdAndTenantId(String filingId, String tenantId);

    List<TaxFiling> findByTenantId(String tenantId, TaxRate.Jurisdiction jurisdiction, TaxRate.TaxType taxType);

    List<TaxFiling> findByTenantIdAndFilingPeriod(String tenantId, YearMonth period,
                                                   TaxRate.Jurisdiction jurisdiction,
                                                   TaxRate.TaxType taxType);

    List<TaxFiling> findByTenantIdAndStatus(String tenantId, TaxFiling.FilingStatus status);

    List<TaxFiling> findByTenantIdAndDueBefore(String tenantId, LocalDate dueBefore,
                                               TaxRate.Jurisdiction jurisdiction);

    List<TaxFiling> findByTenantIdAndFilingPeriodBetween(String tenantId, YearMonth startPeriod,
                                                         YearMonth endPeriod,
                                                         TaxRate.Jurisdiction jurisdiction,
                                                         TaxRate.TaxType taxType);

    List<TaxFiling> findByTenantIdAndAcknowledgementNumber(String tenantId, String acknowledgementNumber);

    List<TaxFiling> findOverdueFilings(String tenantId);

    List<TaxFiling> findUpcomingFilings(String tenantId, int daysAhead);

    void deleteById(String id);

    void deleteByFilingIdAndTenantId(String filingId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, TaxFiling.FilingStatus status);

    List<TaxFiling> findByTenantIdAndSubmittedBy(String tenantId, String submittedBy);

    List<TaxFiling> findByTenantIdAndCalculationIdsContaining(String tenantId, String calculationId);

    boolean existsByTenantIdAndPeriodAndJurisdictionAndType(String tenantId, YearMonth period,
                                                            TaxRate.Jurisdiction jurisdiction,
                                                            TaxRate.TaxType taxType);
}
