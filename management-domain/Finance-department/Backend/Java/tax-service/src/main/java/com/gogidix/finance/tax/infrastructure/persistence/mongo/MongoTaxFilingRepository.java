package com.gogidix.finance.tax.infrastructure.persistence.mongo;

import com.gogidix.finance.tax.domain.model.TaxFiling;
import com.gogidix.finance.tax.domain.model.TaxRate;
import com.gogidix.finance.tax.domain.repository.TaxFilingRepository;
import com.gogidix.finance.tax.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Tax Filing
 * Implements tax filing persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoTaxFilingRepository implements TaxFilingRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public TaxFiling save(TaxFiling filing) {
        log.debug("Saving tax filing: {} for tenant: {}",
            filing.getFilingId(), filing.getTenantId());
        return mongoTemplate.save(filing);
    }

    @Override
    public List<TaxFiling> saveAll(List<TaxFiling> filings) {
        return filings.stream()
            .map(mongoTemplate::save)
            .toList();
    }

    @Override
    public Optional<TaxFiling> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, TaxFiling.class));
    }

    @Override
    public Optional<TaxFiling> findByFilingIdAndTenantId(String filingId, String tenantId) {
        Query query = Query.query(
            Criteria.where("filingId").is(filingId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, TaxFiling.class));
    }

    @Override
    public List<TaxFiling> findByTenantId(String tenantId, TaxRate.Jurisdiction jurisdiction,
                                         TaxRate.TaxType taxType) {
        Criteria criteria = Criteria.where("tenantId").is(tenantId);

        if (jurisdiction != null) {
            criteria.and("jurisdiction").is(jurisdiction);
        }

        if (taxType != null) {
            criteria.and("taxType").is(taxType);
        }

        Query query = Query.query(criteria);
        return mongoTemplate.find(query, TaxFiling.class);
    }

    @Override
    public List<TaxFiling> findByTenantIdAndFilingPeriod(String tenantId, YearMonth period,
                                                         TaxRate.Jurisdiction jurisdiction,
                                                         TaxRate.TaxType taxType) {
        Criteria criteria = Criteria.where("tenantId").is(tenantId)
            .and("filingPeriod").is(period);

        if (jurisdiction != null) {
            criteria.and("jurisdiction").is(jurisdiction);
        }

        if (taxType != null) {
            criteria.and("taxType").is(taxType);
        }

        Query query = Query.query(criteria);
        return mongoTemplate.find(query, TaxFiling.class);
    }

    @Override
    public List<TaxFiling> findByTenantIdAndStatus(String tenantId, TaxFiling.FilingStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, TaxFiling.class);
    }

    @Override
    public List<TaxFiling> findByTenantIdAndDueBefore(String tenantId, LocalDate dueBefore,
                                                     TaxRate.Jurisdiction jurisdiction) {
        Criteria criteria = Criteria.where("tenantId").is(tenantId)
            .and("dueDate").lte(dueBefore)
            .and("status").in(TaxFiling.FilingStatus.DRAFT,
                             TaxFiling.FilingStatus.PENDING_REVIEW,
                             TaxFiling.FilingStatus.PENDING_SUBMISSION);

        if (jurisdiction != null) {
            criteria.and("jurisdiction").is(jurisdiction);
        }

        Query query = Query.query(criteria);
        return mongoTemplate.find(query, TaxFiling.class);
    }

    @Override
    public List<TaxFiling> findByTenantIdAndFilingPeriodBetween(String tenantId, YearMonth startPeriod,
                                                                YearMonth endPeriod,
                                                                TaxRate.Jurisdiction jurisdiction,
                                                                TaxRate.TaxType taxType) {
        Criteria criteria = Criteria.where("tenantId").is(tenantId)
            .and("filingPeriod").gte(startPeriod).lte(endPeriod);

        if (jurisdiction != null) {
            criteria.and("jurisdiction").is(jurisdiction);
        }

        if (taxType != null) {
            criteria.and("taxType").is(taxType);
        }

        Query query = Query.query(criteria);
        return mongoTemplate.find(query, TaxFiling.class);
    }

    @Override
    public List<TaxFiling> findByTenantIdAndAcknowledgementNumber(String tenantId, String acknowledgementNumber) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("acknowledgementNumber").is(acknowledgementNumber)
        );
        return mongoTemplate.find(query, TaxFiling.class);
    }

    @Override
    public List<TaxFiling> findOverdueFilings(String tenantId) {
        LocalDate today = LocalDate.now();

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("dueDate").lt(today)
                .and("status").nin(TaxFiling.FilingStatus.PAID,
                                 TaxFiling.FilingStatus.ARCHIVED,
                                 TaxFiling.FilingStatus.CANCELLED)
        );

        List<TaxFiling> filings = mongoTemplate.find(query, TaxFiling.class);

        // Update status to OVERDUE for those that qualify
        for (TaxFiling filing : filings) {
            if (filing.isOverdue() && filing.getStatus() != TaxFiling.FilingStatus.OVERDUE) {
                filing.setStatus(TaxFiling.FilingStatus.OVERDUE);
                mongoTemplate.save(filing);
            }
        }

        return filings;
    }

    @Override
    public List<TaxFiling> findUpcomingFilings(String tenantId, int daysAhead) {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(daysAhead);

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("dueDate").gte(today).lte(futureDate)
                .and("status").in(TaxFiling.FilingStatus.DRAFT,
                                 TaxFiling.FilingStatus.PENDING_REVIEW,
                                 TaxFiling.FilingStatus.PENDING_SUBMISSION)
        );

        return mongoTemplate.find(query, TaxFiling.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), TaxFiling.class);
    }

    @Override
    public void deleteByFilingIdAndTenantId(String filingId, String tenantId) {
        Query query = Query.query(
            Criteria.where("filingId").is(filingId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, TaxFiling.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, TaxFiling.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, TaxFiling.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, TaxFiling.FilingStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, TaxFiling.class);
    }

    @Override
    public List<TaxFiling> findByTenantIdAndSubmittedBy(String tenantId, String submittedBy) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("submittedBy").is(submittedBy)
        );
        return mongoTemplate.find(query, TaxFiling.class);
    }

    @Override
    public List<TaxFiling> findByTenantIdAndCalculationIdsContaining(String tenantId, String calculationId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("calculationIds").is(calculationId)
        );
        return mongoTemplate.find(query, TaxFiling.class);
    }

    @Override
    public boolean existsByTenantIdAndPeriodAndJurisdictionAndType(String tenantId, YearMonth period,
                                                                   TaxRate.Jurisdiction jurisdiction,
                                                                   TaxRate.TaxType taxType) {
        Criteria criteria = Criteria.where("tenantId").is(tenantId)
            .and("filingPeriod").is(period)
            .and("status").ne(TaxFiling.FilingStatus.CANCELLED);

        if (jurisdiction != null) {
            criteria.and("jurisdiction").is(jurisdiction);
        }

        if (taxType != null) {
            criteria.and("taxType").is(taxType);
        }

        Query query = Query.query(criteria);
        return mongoTemplate.exists(query, TaxFiling.class);
    }
}
