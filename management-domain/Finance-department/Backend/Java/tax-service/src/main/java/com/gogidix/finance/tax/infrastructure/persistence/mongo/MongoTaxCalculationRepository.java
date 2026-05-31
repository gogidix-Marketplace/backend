package com.gogidix.finance.tax.infrastructure.persistence.mongo;

import com.gogidix.finance.tax.domain.model.TaxCalculation;
import com.gogidix.finance.tax.domain.model.TaxRate;
import com.gogidix.finance.tax.domain.repository.TaxCalculationRepository;
import com.gogidix.finance.tax.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Tax Calculation
 * Implements tax calculation persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoTaxCalculationRepository implements TaxCalculationRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public TaxCalculation save(TaxCalculation calculation) {
        log.debug("Saving tax calculation: {} for tenant: {}",
            calculation.getCalculationId(), calculation.getTenantId());
        return mongoTemplate.save(calculation);
    }

    @Override
    public List<TaxCalculation> saveAll(List<TaxCalculation> calculations) {
        return calculations.stream()
            .map(mongoTemplate::save)
            .toList();
    }

    @Override
    public Optional<TaxCalculation> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, TaxCalculation.class));
    }

    @Override
    public Optional<TaxCalculation> findByCalculationIdAndTenantId(String calculationId, String tenantId) {
        Query query = Query.query(
            Criteria.where("calculationId").is(calculationId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, TaxCalculation.class));
    }

    @Override
    public List<TaxCalculation> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, TaxCalculation.class);
    }

    @Override
    public List<TaxCalculation> findByTenantIdAndTransactionId(String tenantId, String transactionId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("transactionId").is(transactionId)
        );
        return mongoTemplate.find(query, TaxCalculation.class);
    }

    @Override
    public List<TaxCalculation> findByTenantIdAndStatus(String tenantId, TaxCalculation.CalculationStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, TaxCalculation.class);
    }

    @Override
    public List<TaxCalculation> findByTenantIdAndTransactionDateBetween(String tenantId, LocalDate startDate,
                                                                       LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(java.time.ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("transactionDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, TaxCalculation.class);
    }

    @Override
    public List<TaxCalculation> findByTenantIdAndPeriod(String tenantId, YearMonth period,
                                                       TaxRate.Jurisdiction jurisdiction, TaxRate.TaxType taxType) {
        LocalDate startDate = period.atDay(1);
        LocalDate endDate = period.atEndOfMonth();

        Criteria criteria = Criteria.where("tenantId").is(tenantId)
            .and("transactionDate").gte(startDate).lte(endDate);

        if (jurisdiction != null) {
            criteria.and("jurisdiction").is(jurisdiction);
        }

        Query query = Query.query(criteria);
        List<TaxCalculation> calculations = mongoTemplate.find(query, TaxCalculation.class);

        // Filter by tax type if specified (since it's in the nested taxBreakdown)
        if (taxType != null) {
            calculations = calculations.stream()
                .filter(c -> c.getTaxBreakdown().stream()
                    .anyMatch(item -> item.getTaxType() == taxType))
                .toList();
        }

        return calculations;
    }

    @Override
    public List<TaxCalculation> findByTenantIdAndPeriodAndStatus(String tenantId, YearMonth period,
                                                                 TaxRate.Jurisdiction jurisdiction,
                                                                 TaxRate.TaxType taxType,
                                                                 TaxCalculation.CalculationStatus status) {
        LocalDate startDate = period.atDay(1);
        LocalDate endDate = period.atEndOfMonth();

        Criteria criteria = Criteria.where("tenantId").is(tenantId)
            .and("transactionDate").gte(startDate).lte(endDate)
            .and("status").is(status);

        if (jurisdiction != null) {
            criteria.and("jurisdiction").is(jurisdiction);
        }

        Query query = Query.query(criteria);
        List<TaxCalculation> calculations = mongoTemplate.find(query, TaxCalculation.class);

        // Filter by tax type if specified
        if (taxType != null) {
            calculations = calculations.stream()
                .filter(c -> c.getTaxBreakdown().stream()
                    .anyMatch(item -> item.getTaxType() == taxType))
                .toList();
        }

        return calculations;
    }

    @Override
    public List<TaxCalculation> findByTenantIdAndFilingId(String tenantId, String filingId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("filingId").is(filingId)
        );
        return mongoTemplate.find(query, TaxCalculation.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), TaxCalculation.class);
    }

    @Override
    public void deleteByCalculationIdAndTenantId(String calculationId, String tenantId) {
        Query query = Query.query(
            Criteria.where("calculationId").is(calculationId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, TaxCalculation.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, TaxCalculation.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, TaxCalculation.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, TaxCalculation.CalculationStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, TaxCalculation.class);
    }

    @Override
    public List<TaxCalculation> findPendingCalculationsForFiling(String tenantId, YearMonth period,
                                                                 TaxRate.Jurisdiction jurisdiction,
                                                                 TaxRate.TaxType taxType) {
        LocalDate startDate = period.atDay(1);
        LocalDate endDate = period.atEndOfMonth();

        Criteria criteria = Criteria.where("tenantId").is(tenantId)
            .and("transactionDate").gte(startDate).lte(endDate)
            .and("status").in(TaxCalculation.CalculationStatus.VERIFIED,
                             TaxCalculation.CalculationStatus.APPLIED);

        if (jurisdiction != null) {
            criteria.and("jurisdiction").is(jurisdiction);
        }

        Query query = Query.query(criteria);
        List<TaxCalculation> calculations = mongoTemplate.find(query, TaxCalculation.class);

        if (taxType != null) {
            calculations = calculations.stream()
                .filter(c -> c.getTaxBreakdown().stream()
                    .anyMatch(item -> item.getTaxType() == taxType))
                .toList();
        }

        return calculations;
    }

    @Override
    public List<TaxCalculation> findReconcilableCalculations(String tenantId, YearMonth period,
                                                            TaxRate.Jurisdiction jurisdiction,
                                                            TaxRate.TaxType taxType) {
        return findByTenantIdAndPeriodAndStatus(tenantId, period, jurisdiction, taxType,
            TaxCalculation.CalculationStatus.VERIFIED);
    }
}
