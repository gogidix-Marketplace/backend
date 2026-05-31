package com.gogidix.finance.bankreconciliation.infrastructure.persistence.mongo;

import com.gogidix.finance.bankreconciliation.domain.model.BankStatement;
import com.gogidix.finance.bankreconciliation.domain.repository.BankStatementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Bank Statement
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoBankStatementRepository implements BankStatementRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public BankStatement save(BankStatement statement) {
        log.debug("Saving bank statement: {} for tenant: {}",
                statement.getStatementId(), statement.getTenantId());
        return mongoTemplate.save(statement);
    }

    @Override
    public List<BankStatement> saveAll(List<BankStatement> statements) {
        return statements.stream()
                .map(mongoTemplate::save)
                .toList();
    }

    @Override
    public Optional<BankStatement> findById(String id) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, BankStatement.class));
    }

    @Override
    public Optional<BankStatement> findByStatementIdAndTenantId(String statementId, String tenantId) {
        Query query = Query.query(
                Criteria.where("statementId").is(statementId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, BankStatement.class));
    }

    @Override
    public List<BankStatement> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, BankStatement.class);
    }

    @Override
    public List<BankStatement> findByTenantIdAndAccountId(String tenantId, String accountId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountId").is(accountId)
        );
        return mongoTemplate.find(query, BankStatement.class);
    }

    @Override
    public List<BankStatement> findByTenantIdAndAccountNumber(String tenantId, String accountNumber) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountNumber").is(accountNumber)
        );
        return mongoTemplate.find(query, BankStatement.class);
    }

    @Override
    public List<BankStatement> findByTenantIdAndStatementDateBetween(String tenantId, LocalDate startDate,
                                                                      LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("statementDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, BankStatement.class);
    }

    @Override
    public List<BankStatement> findByTenantIdAndImportStatus(String tenantId,
                                                              BankStatement.ImportStatus importStatus) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("importStatus").is(importStatus)
        );
        return mongoTemplate.find(query, BankStatement.class);
    }

    @Override
    public List<BankStatement> findByTenantIdAndImportSource(String tenantId,
                                                              BankStatement.ImportSource importSource) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("importSource").is(importSource)
        );
        return mongoTemplate.find(query, BankStatement.class);
    }

    @Override
    public List<BankStatement> findByTenantIdAndStatementType(String tenantId,
                                                              BankStatement.StatementType statementType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("statementType").is(statementType)
        );
        return mongoTemplate.find(query, BankStatement.class);
    }

    @Override
    public List<BankStatement> findByTenantIdAndReconciled(String tenantId, Boolean reconciled) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("reconciled").is(reconciled)
        );
        return mongoTemplate.find(query, BankStatement.class);
    }

    @Override
    public Optional<BankStatement> findByTenantIdAndAccountIdAndStatementDate(
            String tenantId, String accountId, LocalDate statementDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountId").is(accountId)
                        .and("statementDate").is(statementDate)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, BankStatement.class));
    }

    @Override
    public List<BankStatement> findUnreconciledByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("reconciled").is(false)
                        .and("importStatus").is(BankStatement.ImportStatus.COMPLETED)
        );
        return mongoTemplate.find(query, BankStatement.class);
    }

    @Override
    public List<BankStatement> findByTenantIdAndAccountIdAndReconciled(
            String tenantId, String accountId, Boolean reconciled) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountId").is(accountId)
                        .and("reconciled").is(reconciled)
        );
        return mongoTemplate.find(query, BankStatement.class);
    }

    @Override
    public boolean existsByStatementIdAndTenantId(String statementId, String tenantId) {
        Query query = Query.query(
                Criteria.where("statementId").is(statementId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, BankStatement.class);
    }

    @Override
    public void deleteById(String id) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, BankStatement.class);
    }

    @Override
    public void deleteByStatementIdAndTenantId(String statementId, String tenantId) {
        Query query = Query.query(
                Criteria.where("statementId").is(statementId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, BankStatement.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, BankStatement.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, BankStatement.class);
    }

    @Override
    public long countByTenantIdAndImportStatus(String tenantId, BankStatement.ImportStatus importStatus) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("importStatus").is(importStatus)
        );
        return mongoTemplate.count(query, BankStatement.class);
    }

    @Override
    public long countByTenantIdAndAccountId(String tenantId, String accountId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountId").is(accountId)
        );
        return mongoTemplate.count(query, BankStatement.class);
    }

    @Override
    public List<BankStatement> findPendingProcessingByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("importStatus").is(BankStatement.ImportStatus.PENDING)
        );
        return mongoTemplate.find(query, BankStatement.class);
    }

    @Override
    public List<BankStatement> findByTenantIdAndBankReference(String tenantId, String bankReference) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("bankReference").is(bankReference)
        );
        return mongoTemplate.find(query, BankStatement.class);
    }
}
