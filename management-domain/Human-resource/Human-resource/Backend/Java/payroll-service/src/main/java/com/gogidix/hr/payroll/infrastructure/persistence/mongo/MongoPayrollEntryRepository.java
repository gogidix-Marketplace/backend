package com.gogidix.hr.payroll.infrastructure.persistence.mongo;

import com.gogidix.hr.payroll.domain.model.PayrollEntry;
import com.gogidix.hr.payroll.domain.repository.PayrollEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - PayrollEntry
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoPayrollEntryRepository implements PayrollEntryRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public PayrollEntry save(PayrollEntry entry) {
        return mongoTemplate.save(entry);
    }

    @Override
    public Optional<PayrollEntry> findByEntryIdAndTenantId(String entryId, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(entryId).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, PayrollEntry.class));
    }

    @Override
    public List<PayrollEntry> saveAll(List<PayrollEntry> entries) {
        return List.copyOf(mongoTemplate.insertAll(entries));
    }

    @Override
    public Optional<PayrollEntry> findById(String id) {
        String tenantId = com.gogidix.hr.payroll.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, PayrollEntry.class));
    }

    @Override
    public List<PayrollEntry> findByPayrollId(String payrollId) {
        Query query = Query.query(Criteria.where("payrollId").is(payrollId));
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findByEmployeeId(String employeeId) {
        String tenantId = com.gogidix.hr.payroll.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("employeeId").is(employeeId)
        );
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("countryCode").is(countryCode)
        );
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findByPayrollIdAndEmployeeId(String payrollId, String employeeId) {
        Query query = Query.query(
                Criteria.where("payrollId").is(payrollId).and("employeeId").is(employeeId)
        );
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findUnpaidEntries(String payrollId) {
        Query query = Query.query(
                Criteria.where("payrollId").is(payrollId).and("paid").is(false)
        );
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findPaidEntries(String payrollId) {
        Query query = Query.query(
                Criteria.where("payrollId").is(payrollId).and("paid").is(true)
        );
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findEntriesOnHold(String payrollId) {
        Query query = Query.query(
                Criteria.where("payrollId").is(payrollId).and("isHold").is(true)
        );
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
                Criteria.where("paymentDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findByEmployeeIdAndDateRange(String employeeId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
                Criteria.where("employeeId").is(employeeId)
                        .and("paymentDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), PayrollEntry.class);
    }

    @Override
    public void deleteByPayrollId(String payrollId) {
        Query query = Query.query(Criteria.where("payrollId").is(payrollId));
        mongoTemplate.remove(query, PayrollEntry.class);
    }

    @Override
    public boolean existsByPayrollIdAndEmployeeId(String payrollId, String employeeId) {
        Query query = Query.query(
                Criteria.where("payrollId").is(payrollId).and("employeeId").is(employeeId)
        );
        return mongoTemplate.exists(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findByBankAccountNumber(String accountNumber) {
        Query query = Query.query(Criteria.where("bankAccountNumber").is(accountNumber));
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findRecentEntries(String employeeId, int limit) {
        Query query = Query.query(
                Criteria.where("employeeId").is(employeeId)
        ).limit(limit);
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findByPayrollIdAndTenantId(String payrollId, String tenantId) {
        Query query = Query.query(
                Criteria.where("payrollId").is(payrollId).and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findByTenantIdAndEmployeeId(String tenantId, String employeeId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("employeeId").is(employeeId)
        );
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("department").is(department)
        );
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findUnpaidEntries(String tenantId, String payrollId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("payrollId").is(payrollId)
                        .and("paid").ne(true)
        );
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findHeldEntries(String tenantId, String employeeId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("employeeId").is(employeeId)
                        .and("onHold").is(true)
        );
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public List<PayrollEntry> findEntriesWithIssues(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .orOperator(
                                Criteria.where("hasErrors").is(true),
                                Criteria.where("onHold").is(true)
                        )
        );
        return mongoTemplate.find(query, PayrollEntry.class);
    }

    @Override
    public long countByPayrollId(String payrollId) {
        Query query = Query.query(Criteria.where("payrollId").is(payrollId));
        return mongoTemplate.count(query, PayrollEntry.class);
    }

    @Override
    public long countByTenantIdAndEmployeeId(String tenantId, String employeeId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("employeeId").is(employeeId)
        );
        return mongoTemplate.count(query, PayrollEntry.class);
    }

    @Override
    public org.springframework.data.domain.Page<PayrollEntry> searchEntries(String tenantId, String searchTerm, org.springframework.data.domain.PageRequest pageRequest) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .orOperator(
                                Criteria.where("employeeName").regex(searchTerm, "i"),
                                Criteria.where("employeeCode").regex(searchTerm, "i"),
                                Criteria.where("department").regex(searchTerm, "i")
                        )
        ).with(pageRequest);
        List<PayrollEntry> entries = mongoTemplate.find(query, PayrollEntry.class);
        return new org.springframework.data.domain.PageImpl<>(entries, pageRequest, entries.size());
    }

    @Override
    public void deleteByEntryIdAndTenantId(String entryId, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(entryId).and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, PayrollEntry.class);
    }
}
