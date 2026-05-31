package com.gogidix.finance.bankreconciliation.infrastructure.persistence.mongo;

import com.gogidix.finance.bankreconciliation.domain.model.BankAccount;
import com.gogidix.finance.bankreconciliation.domain.repository.BankAccountRepository;
import com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Bank Account
 * Implements bank account persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoBankAccountRepository implements BankAccountRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public BankAccount save(BankAccount account) {
        log.debug("Saving bank account: {} for tenant: {}",
                account.getAccountNumber(), account.getTenantId());
        return mongoTemplate.save(account);
    }

    @Override
    public List<BankAccount> saveAll(List<BankAccount> accounts) {
        return accounts.stream()
                .map(mongoTemplate::save)
                .toList();
    }

    @Override
    public Optional<BankAccount> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, BankAccount.class));
    }

    @Override
    public Optional<BankAccount> findByAccountNumberAndTenantId(String accountNumber, String tenantId) {
        Query query = Query.query(
                Criteria.where("accountNumber").is(accountNumber)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, BankAccount.class));
    }

    @Override
    public List<BankAccount> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, BankAccount.class);
    }

    @Override
    public List<BankAccount> findByTenantIdAndStatus(String tenantId, BankAccount.AccountStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, BankAccount.class);
    }

    @Override
    public List<BankAccount> findByTenantIdAndAccountType(String tenantId, BankAccount.AccountType accountType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountType").is(accountType)
        );
        return mongoTemplate.find(query, BankAccount.class);
    }

    @Override
    public List<BankAccount> findByTenantIdAndIsPrimary(String tenantId, Boolean isPrimary) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isPrimary").is(isPrimary)
        );
        return mongoTemplate.find(query, BankAccount.class);
    }

    @Override
    public List<BankAccount> findByTenantIdAndCurrency(String tenantId, String currency) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("currency").is(currency)
        );
        return mongoTemplate.find(query, BankAccount.class);
    }

    @Override
    public List<BankAccount> findByTenantIdAndBankName(String tenantId, String bankName) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("bankName").is(bankName)
        );
        return mongoTemplate.find(query, BankAccount.class);
    }

    @Override
    public List<BankAccount> findByTenantIdAndStatusNot(String tenantId, BankAccount.AccountStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").ne(status)
        );
        return mongoTemplate.find(query, BankAccount.class);
    }

    @Override
    public boolean existsByAccountNumberAndTenantId(String accountNumber, String tenantId) {
        Query query = Query.query(
                Criteria.where("accountNumber").is(accountNumber)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, BankAccount.class);
    }

    @Override
    public void deleteById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, BankAccount.class);
    }

    @Override
    public void deleteByAccountNumberAndTenantId(String accountNumber, String tenantId) {
        Query query = Query.query(
                Criteria.where("accountNumber").is(accountNumber)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, BankAccount.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, BankAccount.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, BankAccount.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, BankAccount.AccountStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, BankAccount.class);
    }

    @Override
    public Optional<BankAccount> findPrimaryByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isPrimary").is(true)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, BankAccount.class));
    }

    @Override
    public List<BankAccount> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("tags").is(tag)
        );
        return mongoTemplate.find(query, BankAccount.class);
    }

    @Override
    public List<BankAccount> findByTenantIdAndLastReconciledAtBefore(String tenantId, Instant date) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("lastReconciledAt").lt(date)
        );
        return mongoTemplate.find(query, BankAccount.class);
    }
}
