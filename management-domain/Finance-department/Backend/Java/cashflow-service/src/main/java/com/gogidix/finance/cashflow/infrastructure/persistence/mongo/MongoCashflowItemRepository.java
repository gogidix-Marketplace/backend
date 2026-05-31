package com.gogidix.finance.cashflow.infrastructure.persistence.mongo;

import com.gogidix.finance.cashflow.domain.model.CashflowItem;
import com.gogidix.finance.cashflow.domain.repository.CashflowItemRepository;
import com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Cashflow Item
 * Implements cashflow item persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoCashflowItemRepository implements CashflowItemRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public CashflowItem save(CashflowItem item) {
        log.debug("Saving cashflow item: {} for tenant: {}",
                item.getCashflowItemId(), item.getTenantId());
        return mongoTemplate.save(item);
    }

    @Override
    public List<CashflowItem> saveAll(List<CashflowItem> items) {
        return items.stream()
                .map(mongoTemplate::save)
                .toList();
    }

    @Override
    public Optional<CashflowItem> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CashflowItem.class));
    }

    @Override
    public Optional<CashflowItem> findByCashflowItemIdAndTenantId(String cashflowItemId, String tenantId) {
        Query query = Query.query(
                Criteria.where("cashflowItemId").is(cashflowItemId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CashflowItem.class));
    }

    @Override
    public List<CashflowItem> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndType(String tenantId, CashflowItem.CashflowType type) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("type").is(type)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndCategory(String tenantId, CashflowItem.CashflowCategory category) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("category").is(category)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndStatus(String tenantId, CashflowItem.ItemStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndTransactionDateBetween(String tenantId, LocalDate startDate,
                                                                        LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("transactionDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndExpectedDateBetween(String tenantId, LocalDate startDate,
                                                                     LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("expectedDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndSettledDateBetween(String tenantId, LocalDate startDate,
                                                                    LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("settledDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndCostCenter(String tenantId, String costCenter) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("costCenter").is(costCenter)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndProjectId(String tenantId, String projectId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("projectId").is(projectId)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndAccount(String tenantId, String account) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("account").is(account)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndRecurringTrue(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("recurring").is(true)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndParentRecurringItemId(String tenantId, String parentRecurringItemId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("parentRecurringItemId").is(parentRecurringItemId)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndStatusAndExpectedDateBefore(
            String tenantId, CashflowItem.ItemStatus status, LocalDate date) {
        Date targetDate = Date.from(date.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
                        .and("expectedDate").lte(targetDate)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndTypeIn(String tenantId, List<CashflowItem.CashflowType> types) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("type").in(types)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndCategoryIn(String tenantId, List<CashflowItem.CashflowCategory> categories) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("category").in(categories)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndReference(String tenantId, String reference) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("reference").is(reference)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndCounterparty(String tenantId, String counterparty) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("counterparty").is(counterparty)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndLinkedExpenseId(String tenantId, String linkedExpenseId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("linkedExpenseId").is(linkedExpenseId)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndLinkedRevenueId(String tenantId, String linkedRevenueId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("linkedRevenueId").is(linkedRevenueId)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }

    @Override
    public boolean existsByCashflowItemIdAndTenantId(String cashflowItemId, String tenantId) {
        Query query = Query.query(
                Criteria.where("cashflowItemId").is(cashflowItemId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, CashflowItem.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), CashflowItem.class);
    }

    @Override
    public void deleteByCashflowItemIdAndTenantId(String cashflowItemId, String tenantId) {
        Query query = Query.query(
                Criteria.where("cashflowItemId").is(cashflowItemId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, CashflowItem.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, CashflowItem.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, CashflowItem.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, CashflowItem.ItemStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, CashflowItem.class);
    }

    @Override
    public long countByTenantIdAndType(String tenantId, CashflowItem.CashflowType type) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("type").is(type)
        );
        return mongoTemplate.count(query, CashflowItem.class);
    }

    @Override
    public BigDecimal sumAmountByTenantIdAndTypeAndStatus(
            String tenantId, CashflowItem.CashflowType type, CashflowItem.ItemStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("type").is(type)
                        .and("status").is(status)
        );

        List<CashflowItem> items = mongoTemplate.find(query, CashflowItem.class);
        return items.stream()
                .map(CashflowItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal sumAmountByTenantIdAndDateRange(
            String tenantId, LocalDate startDate, LocalDate endDate, CashflowItem.CashflowType type) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("transactionDate").gte(start).lte(end)
                        .and("type").is(type)
        );

        List<CashflowItem> items = mongoTemplate.find(query, CashflowItem.class);
        return items.stream()
                .map(CashflowItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<CashflowItem> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("tags").is(tag)
        );
        return mongoTemplate.find(query, CashflowItem.class);
    }
}
