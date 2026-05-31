package com.gogidix.finance.budgettracking.infrastructure.persistence.mongo;

import com.gogidix.finance.budgettracking.domain.model.BudgetTransaction;
import com.gogidix.finance.budgettracking.domain.repository.BudgetTransactionRepository;
import com.gogidix.finance.budgettracking.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoBudgetTransactionRepository implements BudgetTransactionRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public BudgetTransaction save(BudgetTransaction transaction) {
        return mongoTemplate.save(transaction);
    }

    @Override
    public List<BudgetTransaction> saveAll(List<BudgetTransaction> transactions) {
        return transactions.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<BudgetTransaction> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(Criteria.where("id").is(id).and("tenantId").is(tenantId));
        return Optional.ofNullable(mongoTemplate.findOne(query, BudgetTransaction.class));
    }

    @Override
    public Optional<BudgetTransaction> findByTransactionIdAndTenantId(String transactionId, String tenantId) {
        Query query = Query.query(Criteria.where("transactionId").is(transactionId).and("tenantId").is(tenantId));
        return Optional.ofNullable(mongoTemplate.findOne(query, BudgetTransaction.class));
    }

    @Override
    public List<BudgetTransaction> findByTenantId(String tenantId) {
        return mongoTemplate.find(Query.query(Criteria.where("tenantId").is(tenantId)), BudgetTransaction.class);
    }

    @Override
    public List<BudgetTransaction> findByTenantIdAndBudgetId(String tenantId, String budgetId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("budgetId").is(budgetId));
        return mongoTemplate.find(query, BudgetTransaction.class);
    }

    @Override
    public List<BudgetTransaction> findByTenantIdAndBudgetCode(String tenantId, String budgetCode) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("budgetCode").is(budgetCode));
        return mongoTemplate.find(query, BudgetTransaction.class);
    }

    @Override
    public List<BudgetTransaction> findByTenantIdAndStatus(String tenantId, BudgetTransaction.TransactionStatus status) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("status").is(status));
        return mongoTemplate.find(query, BudgetTransaction.class);
    }

    @Override
    public List<BudgetTransaction> findByTenantIdAndTransactionType(String tenantId, BudgetTransaction.TransactionType type) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("transactionType").is(type));
        return mongoTemplate.find(query, BudgetTransaction.class);
    }

    @Override
    public List<BudgetTransaction> findByTenantIdAndTransactionDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("transactionDate").gte(startDate).lte(endDate));
        return mongoTemplate.find(query, BudgetTransaction.class);
    }

    @Override
    public List<BudgetTransaction> findByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("department").is(department));
        return mongoTemplate.find(query, BudgetTransaction.class);
    }

    @Override
    public List<BudgetTransaction> findByTenantIdAndCategory(String tenantId, String category) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("category").is(category));
        return mongoTemplate.find(query, BudgetTransaction.class);
    }

    @Override
    public List<BudgetTransaction> findByTenantIdAndCostCenter(String tenantId, String costCenter) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("costCenter").is(costCenter));
        return mongoTemplate.find(query, BudgetTransaction.class);
    }

    @Override
    public List<BudgetTransaction> findByTenantIdAndReferenceId(String tenantId, String referenceId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("referenceId").is(referenceId));
        return mongoTemplate.find(query, BudgetTransaction.class);
    }

    @Override
    public List<BudgetTransaction> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("tags").is(tag));
        return mongoTemplate.find(query, BudgetTransaction.class);
    }

    @Override
    public boolean existsByTransactionIdAndTenantId(String transactionId, String tenantId) {
        Query query = Query.query(Criteria.where("transactionId").is(transactionId).and("tenantId").is(tenantId));
        return mongoTemplate.exists(query, BudgetTransaction.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), BudgetTransaction.class);
    }

    @Override
    public void deleteByTransactionIdAndTenantId(String transactionId, String tenantId) {
        Query query = Query.query(Criteria.where("transactionId").is(transactionId).and("tenantId").is(tenantId));
        mongoTemplate.remove(query, BudgetTransaction.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        mongoTemplate.remove(Query.query(Criteria.where("tenantId").is(tenantId)), BudgetTransaction.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        return mongoTemplate.count(Query.query(Criteria.where("tenantId").is(tenantId)), BudgetTransaction.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, BudgetTransaction.TransactionStatus status) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("status").is(status));
        return mongoTemplate.count(query, BudgetTransaction.class);
    }

    @Override
    public List<BudgetTransaction> findByTenantIdAndTransactionTypeAndStatus(
            String tenantId, BudgetTransaction.TransactionType type, BudgetTransaction.TransactionStatus status) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("transactionType").is(type).and("status").is(status));
        return mongoTemplate.find(query, BudgetTransaction.class);
    }
}
