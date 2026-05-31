package com.gogidix.finance.budgettracking.infrastructure.persistence.mongo;

import com.gogidix.finance.budgettracking.domain.model.BudgetMonitor;
import com.gogidix.finance.budgettracking.domain.repository.BudgetMonitorRepository;
import com.gogidix.finance.budgettracking.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoBudgetMonitorRepository implements BudgetMonitorRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public BudgetMonitor save(BudgetMonitor monitor) {
        return mongoTemplate.save(monitor);
    }

    @Override
    public List<BudgetMonitor> saveAll(List<BudgetMonitor> monitors) {
        return monitors.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<BudgetMonitor> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(Criteria.where("id").is(id).and("tenantId").is(tenantId));
        return Optional.ofNullable(mongoTemplate.findOne(query, BudgetMonitor.class));
    }

    @Override
    public Optional<BudgetMonitor> findByMonitorIdAndTenantId(String monitorId, String tenantId) {
        Query query = Query.query(Criteria.where("monitorId").is(monitorId).and("tenantId").is(tenantId));
        return Optional.ofNullable(mongoTemplate.findOne(query, BudgetMonitor.class));
    }

    @Override
    public List<BudgetMonitor> findByTenantId(String tenantId) {
        return mongoTemplate.find(Query.query(Criteria.where("tenantId").is(tenantId)), BudgetMonitor.class);
    }

    @Override
    public List<BudgetMonitor> findByTenantIdAndBudgetId(String tenantId, String budgetId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("budgetId").is(budgetId));
        return mongoTemplate.find(query, BudgetMonitor.class);
    }

    @Override
    public List<BudgetMonitor> findByTenantIdAndBudgetCode(String tenantId, String budgetCode) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("budgetCode").is(budgetCode));
        return mongoTemplate.find(query, BudgetMonitor.class);
    }

    @Override
    public List<BudgetMonitor> findByTenantIdAndPeriod(String tenantId, YearMonth period) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("period").is(period));
        return mongoTemplate.find(query, BudgetMonitor.class);
    }

    @Override
    public List<BudgetMonitor> findByTenantIdAndStatus(String tenantId, BudgetMonitor.MonitorStatus status) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("status").is(status));
        return mongoTemplate.find(query, BudgetMonitor.class);
    }

    @Override
    public List<BudgetMonitor> findByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("department").is(department));
        return mongoTemplate.find(query, BudgetMonitor.class);
    }

    @Override
    public List<BudgetMonitor> findByTenantIdAndCategory(String tenantId, String category) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("category").is(category));
        return mongoTemplate.find(query, BudgetMonitor.class);
    }

    @Override
    public List<BudgetMonitor> findByTenantIdAndPeriodBetween(String tenantId, YearMonth startPeriod, YearMonth endPeriod) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("period").gte(startPeriod).lte(endPeriod));
        return mongoTemplate.find(query, BudgetMonitor.class);
    }

    @Override
    public List<BudgetMonitor> findByTenantIdAndThresholdBreached(String tenantId, boolean breached) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("thresholdBreached").is(breached));
        return mongoTemplate.find(query, BudgetMonitor.class);
    }

    @Override
    public List<BudgetMonitor> findByTenantIdAndStatusIn(String tenantId, List<BudgetMonitor.MonitorStatus> statuses) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("status").in(statuses));
        return mongoTemplate.find(query, BudgetMonitor.class);
    }

    @Override
    public Optional<BudgetMonitor> findByTenantIdAndBudgetIdAndPeriod(String tenantId, String budgetId, YearMonth period) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("budgetId").is(budgetId).and("period").is(period));
        return Optional.ofNullable(mongoTemplate.findOne(query, BudgetMonitor.class));
    }

    @Override
    public boolean existsByMonitorIdAndTenantId(String monitorId, String tenantId) {
        Query query = Query.query(Criteria.where("monitorId").is(monitorId).and("tenantId").is(tenantId));
        return mongoTemplate.exists(query, BudgetMonitor.class);
    }

    @Override
    public boolean existsByTenantIdAndBudgetIdAndPeriod(String tenantId, String budgetId, YearMonth period) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("budgetId").is(budgetId).and("period").is(period));
        return mongoTemplate.exists(query, BudgetMonitor.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), BudgetMonitor.class);
    }

    @Override
    public void deleteByMonitorIdAndTenantId(String monitorId, String tenantId) {
        Query query = Query.query(Criteria.where("monitorId").is(monitorId).and("tenantId").is(tenantId));
        mongoTemplate.remove(query, BudgetMonitor.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        mongoTemplate.remove(Query.query(Criteria.where("tenantId").is(tenantId)), BudgetMonitor.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        return mongoTemplate.count(Query.query(Criteria.where("tenantId").is(tenantId)), BudgetMonitor.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, BudgetMonitor.MonitorStatus status) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("status").is(status));
        return mongoTemplate.count(query, BudgetMonitor.class);
    }

    @Override
    public List<BudgetMonitor> findCriticalBudgets(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("status").in(
            BudgetMonitor.MonitorStatus.CRITICAL, BudgetMonitor.MonitorStatus.OVER_BUDGET));
        return mongoTemplate.find(query, BudgetMonitor.class);
    }

    @Override
    public List<BudgetMonitor> findOverBudgetMonitors(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("utilizationPercentage").gte(100));
        return mongoTemplate.find(query, BudgetMonitor.class);
    }
}
