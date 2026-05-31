package com.gogidix.sales.crm.infrastructure.persistence.mongo;

import com.gogidix.sales.crm.domain.model.Account;
import com.gogidix.sales.crm.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Account
 * Implements account persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoAccountRepository implements AccountRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Account save(Account account) {
        log.debug("Saving account: {} for tenant: {}",
            account.getAccountId(), account.getTenantId());
        return mongoTemplate.save(account);
    }

    @Override
    public List<Account> saveAll(List<Account> accounts) {
        return accounts.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<Account> findById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return Optional.ofNullable(mongoTemplate.findOne(query, Account.class));
    }

    @Override
    public Optional<Account> findByAccountIdAndTenantId(String accountId, String tenantId) {
        Query query = Query.query(
            Criteria.where("accountId").is(accountId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Account.class));
    }

    @Override
    public Optional<Account> findByAccountNumberAndTenantId(String accountNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("accountNumber").is(accountNumber)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Account.class));
    }

    @Override
    public List<Account> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Account.class);
    }

    @Override
    public List<Account> findByTenantIdAndAccountType(String tenantId, Account.AccountType accountType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("accountType").is(accountType)
        );
        return mongoTemplate.find(query, Account.class);
    }

    @Override
    public List<Account> findByTenantIdAndHierarchyLevel(String tenantId, Account.AccountHierarchyLevel hierarchyLevel) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("hierarchyLevel").is(hierarchyLevel)
        );
        return mongoTemplate.find(query, Account.class);
    }

    @Override
    public List<Account> findByTenantIdAndOwnerId(String tenantId, String ownerId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("ownerId").is(ownerId)
        );
        return mongoTemplate.find(query, Account.class);
    }

    @Override
    public List<Account> findByTenantIdAndTerritory(String tenantId, String territory) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("territory").is(territory)
        );
        return mongoTemplate.find(query, Account.class);
    }

    @Override
    public List<Account> findByTenantIdAndParentAccountId(String tenantId, String parentAccountId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("parentAccountId").is(parentAccountId)
        );
        return mongoTemplate.find(query, Account.class);
    }

    @Override
    public List<Account> findByTenantIdAndIndustry(String tenantId, String industry) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("industry").is(industry)
        );
        return mongoTemplate.find(query, Account.class);
    }

    @Override
    public List<Account> findByTenantIdAndIsActive(String tenantId, boolean isActive) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("isActive").is(isActive)
        );
        return mongoTemplate.find(query, Account.class);
    }

    @Override
    public List<Account> findByTenantIdAndAccountNameContainingIgnoreCase(String tenantId, String searchTerm) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("accountName").regex(searchTerm, "i")
        );
        return mongoTemplate.find(query, Account.class);
    }

    @Override
    public boolean existsByAccountNumberAndTenantId(String accountNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("accountNumber").is(accountNumber)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Account.class);
    }

    @Override
    public boolean existsByAccountIdAndTenantId(String accountId, String tenantId) {
        Query query = Query.query(
            Criteria.where("accountId").is(accountId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Account.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Account.class);
    }

    @Override
    public void deleteByAccountIdAndTenantId(String accountId, String tenantId) {
        Query query = Query.query(
            Criteria.where("accountId").is(accountId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Account.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Account.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Account.class);
    }

    @Override
    public long countByTenantIdAndAccountType(String tenantId, Account.AccountType accountType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("accountType").is(accountType)
        );
        return mongoTemplate.count(query, Account.class);
    }

    @Override
    public long countByTenantIdAndIsActive(String tenantId, boolean isActive) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("isActive").is(isActive)
        );
        return mongoTemplate.count(query, Account.class);
    }

    @Override
    public long countByParentAccountIdAndTenantId(String parentAccountId, String tenantId) {
        Query query = Query.query(
            Criteria.where("parentAccountId").is(parentAccountId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.count(query, Account.class);
    }
}
