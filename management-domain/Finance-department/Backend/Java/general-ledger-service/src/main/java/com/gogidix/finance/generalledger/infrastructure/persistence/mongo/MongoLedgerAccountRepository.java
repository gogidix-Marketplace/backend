package com.gogidix.finance.generalledger.infrastructure.persistence.mongo;

import com.gogidix.finance.generalledger.domain.repository.LedgerAccountRepository;
import com.gogidix.finance.generalledger.shared.requestcontext.RequestContextHolder;
import com.gogidix.finance.ledger.domain.model.LedgerAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Ledger Account
 * Implements ledger account persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoLedgerAccountRepository implements LedgerAccountRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public LedgerAccount save(LedgerAccount ledgerAccount) {
        log.debug("Saving ledger account: {} for tenant: {}",
            ledgerAccount.getAccountId(), ledgerAccount.getTenantId());
        return mongoTemplate.save(ledgerAccount);
    }

    @Override
    public List<LedgerAccount> saveAll(List<LedgerAccount> ledgerAccounts) {
        return ledgerAccounts.stream()
            .map(mongoTemplate::save)
            .toList();
    }

    @Override
    public Optional<LedgerAccount> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, LedgerAccount.class));
    }

    @Override
    public Optional<LedgerAccount> findByAccountIdAndTenantId(String accountId, String tenantId) {
        Query query = Query.query(
            Criteria.where("accountId").is(accountId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, LedgerAccount.class));
    }

    @Override
    public Optional<LedgerAccount> findByAccountNumberAndTenantId(String accountNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("accountNumber").is(accountNumber)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, LedgerAccount.class));
    }

    @Override
    public List<LedgerAccount> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, LedgerAccount.class);
    }

    @Override
    public List<LedgerAccount> findByTenantIdAndAccountType(String tenantId, LedgerAccount.AccountType accountType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("accountType").is(accountType)
        );
        return mongoTemplate.find(query, LedgerAccount.class);
    }

    @Override
    public List<LedgerAccount> findByTenantIdAndAccountSubType(String tenantId, LedgerAccount.AccountSubType accountSubType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("accountSubType").is(accountSubType)
        );
        return mongoTemplate.find(query, LedgerAccount.class);
    }

    @Override
    public List<LedgerAccount> findByTenantIdAndStatus(String tenantId, LedgerAccount.AccountStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, LedgerAccount.class);
    }

    @Override
    public List<LedgerAccount> findByTenantIdAndParentAccountId(String tenantId, String parentAccountId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("parentAccountId").is(parentAccountId)
        );
        return mongoTemplate.find(query, LedgerAccount.class);
    }

    @Override
    public List<LedgerAccount> findByTenantIdAndStatusIs(String tenantId, LedgerAccount.AccountStatus status) {
        return findByTenantIdAndStatus(tenantId, status);
    }

    @Override
    public List<LedgerAccount> findBalanceSheetAccountsByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("accountType").in(List.of(
                    LedgerAccount.AccountType.ASSET,
                    LedgerAccount.AccountType.LIABILITY,
                    LedgerAccount.AccountType.EQUITY
                ))
        );
        return mongoTemplate.find(query, LedgerAccount.class);
    }

    @Override
    public List<LedgerAccount> findIncomeStatementAccountsByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("accountType").in(List.of(
                    LedgerAccount.AccountType.REVENUE,
                    LedgerAccount.AccountType.EXPENSE
                ))
        );
        return mongoTemplate.find(query, LedgerAccount.class);
    }

    @Override
    public List<LedgerAccount> findCashAccountsByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("isCashAccount").is(true)
        );
        return mongoTemplate.find(query, LedgerAccount.class);
    }

    @Override
    public List<LedgerAccount> findReconcilableAccountsByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("isReconcilable").is(true)
                .and("status").is(LedgerAccount.AccountStatus.ACTIVE)
        );
        return mongoTemplate.find(query, LedgerAccount.class);
    }

    @Override
    public List<LedgerAccount> searchByTenantIdAndAccountNameContaining(String tenantId, String searchTerm) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("accountName").regex(searchTerm, "i")
        );
        return mongoTemplate.find(query, LedgerAccount.class);
    }

    @Override
    public List<LedgerAccount> findByTenantIdAndCostCenter(String tenantId, String costCenter) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("costCenter").is(costCenter)
        );
        return mongoTemplate.find(query, LedgerAccount.class);
    }

    @Override
    public List<LedgerAccount> findByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("department").is(department)
        );
        return mongoTemplate.find(query, LedgerAccount.class);
    }

    @Override
    public boolean existsByAccountIdAndTenantId(String accountId, String tenantId) {
        Query query = Query.query(
            Criteria.where("accountId").is(accountId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, LedgerAccount.class);
    }

    @Override
    public boolean existsByAccountNumberAndTenantId(String accountNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("accountNumber").is(accountNumber)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, LedgerAccount.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), LedgerAccount.class);
    }

    @Override
    public void deleteByAccountIdAndTenantId(String accountId, String tenantId) {
        Query query = Query.query(
            Criteria.where("accountId").is(accountId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, LedgerAccount.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, LedgerAccount.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, LedgerAccount.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, LedgerAccount.AccountStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, LedgerAccount.class);
    }

    @Override
    public BigDecimal sumCurrentBalanceByTenantIdAndAccountType(String tenantId, LedgerAccount.AccountType accountType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("accountType").is(accountType)
                .and("status").is(LedgerAccount.AccountStatus.ACTIVE)
        );

        List<LedgerAccount> accounts = mongoTemplate.find(query, LedgerAccount.class);
        return accounts.stream()
            .map(a -> a.getCurrentBalance() != null ? a.getCurrentBalance() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
