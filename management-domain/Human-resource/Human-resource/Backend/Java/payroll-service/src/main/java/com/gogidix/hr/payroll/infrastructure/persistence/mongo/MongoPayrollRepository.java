package com.gogidix.hr.payroll.infrastructure.persistence.mongo;

import com.gogidix.hr.payroll.domain.model.Payroll;
import com.gogidix.hr.payroll.domain.enums.PayrollStatus;
import com.gogidix.hr.payroll.domain.repository.PayrollRepository;
import com.gogidix.hr.payroll.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoPayrollRepository implements PayrollRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Payroll save(Payroll payroll) {
        return mongoTemplate.save(payroll);
    }

    @Override
    public Optional<Payroll> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Payroll.class));
    }

    @Override
    public List<Payroll> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("countryCode").is(countryCode)
        );
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findByTenantIdAndStatus(String tenantId, PayrollStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is(status)
        );
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public Optional<Payroll> findByTenantIdAndPayrollPeriod(String tenantId, YearMonth period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("payrollPeriod").is(period)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Payroll.class));
    }

    @Override
    public List<Payroll> findByTenantIdAndDateRange(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("startDate").gte(startDate)
                        .and("endDate").lte(endDate)
        );
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findByStatus(PayrollStatus status) {
        Query query = Query.query(Criteria.where("status").is(status));
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findByBatchId(String batchId) {
        Query query = Query.query(Criteria.where("batchId").is(batchId));
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findPendingApprovalPayrolls(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is(PayrollStatus.PENDING_APPROVAL)
        );
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findProcessedPayrollsPendingPayment(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is(PayrollStatus.PROCESSED)
        );
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findActivePayrollsByTenant(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").in(List.of(PayrollStatus.DRAFT, PayrollStatus.PENDING_APPROVAL, PayrollStatus.APPROVED))
                        .and("isLocked").ne(true)
        );
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findUnlockedPayrolls(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("isLocked").ne(true)
        );
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findByRunType(String tenantId, String runType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("runType").is(runType)
        );
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Payroll.class);
    }

    @Override
    public boolean existsByTenantIdAndPayrollPeriod(String tenantId, YearMonth period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("payrollPeriod").is(period)
        );
        return mongoTemplate.exists(query, Payroll.class);
    }

    @Override
    public List<Payroll> findByEmployeeId(String employeeId) {
        Query query = Query.query(Criteria.where("payrollEntryIds").in(employeeId));
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findRecentPayrolls(String tenantId, int limit) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId))
                .limit(limit)
                .with(org.springframework.data.domain.Sort.by(
                        org.springframework.data.domain.Sort.Direction.DESC, "createdAt"
                ));
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public Optional<Payroll> findByPayrollIdAndTenantId(String payrollId, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(payrollId).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Payroll.class));
    }

    @Override
    public void deleteByPayrollIdAndTenantId(String payrollId, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(payrollId).and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Payroll.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Payroll.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, String status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is(status)
        );
        return mongoTemplate.count(query, Payroll.class);
    }

    @Override
    public Page<Payroll> searchPayrolls(String tenantId, String searchTerm, Pageable pageable) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).orOperator(
                        Criteria.where("payrollName").regex(searchTerm, "i"),
                        Criteria.where("id").regex(searchTerm, "i")
                )
        ).with(pageable);
        List<Payroll> payrolls = mongoTemplate.find(query, Payroll.class);
        return new PageImpl<>(payrolls, pageable, payrolls.size());
    }

    @Override
    public boolean existsByPayrollPeriodAndTenantId(YearMonth period, String tenantId) {
        Query query = Query.query(
                Criteria.where("payrollPeriod").is(period).and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Payroll.class);
    }

    @Override
    public List<Payroll> findByCountryCode(String countryCode) {
        Query query = Query.query(Criteria.where("countryCode").is(countryCode));
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findByBatchIdAndTenantId(String batchId, String tenantId) {
        Query query = Query.query(
                Criteria.where("batchId").is(batchId).and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findByTenantIdAndPaymentDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("paymentDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public Payroll findLatestPayroll(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId))
                .with(org.springframework.data.domain.Sort.by(
                        org.springframework.data.domain.Sort.Direction.DESC, "createdAt"
                )).limit(1);
        return mongoTemplate.findOne(query, Payroll.class);
    }

    @Override
    public List<Payroll> findPayrollsRequiringAction(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").in(List.of(PayrollStatus.PENDING_APPROVAL, PayrollStatus.APPROVED, PayrollStatus.PROCESSED))
                        .and("isLocked").ne(true)
        );
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Payroll.class);
    }

    @Override
    public List<Payroll> findByTenantIdAndStartDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("startDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findPendingApproval(String tenantId, PageRequest pageRequest) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is(PayrollStatus.PENDING_APPROVAL)
        ).with(pageRequest);
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findApprovedPayrolls(String tenantId, PageRequest pageRequest) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is(PayrollStatus.APPROVED)
        ).with(pageRequest);
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findProcessedPayrolls(String tenantId, PageRequest pageRequest) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is(PayrollStatus.PROCESSED)
        ).with(pageRequest);
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findPaidPayrolls(String tenantId, PageRequest pageRequest) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is(PayrollStatus.PAID)
        ).with(pageRequest);
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findByEmployeeId(String employeeId, String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("payrollEntryIds").in(employeeId)
        );
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findByDepartment(String department, String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findByStatus(String status, String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is(status)
        );
        return mongoTemplate.find(query, Payroll.class);
    }

    @Override
    public List<Payroll> findActivePayrolls(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").in(List.of("DRAFT", "PENDING_APPROVAL", "APPROVED"))
                        .and("isLocked").ne(true)
        );
        return mongoTemplate.find(query, Payroll.class);
    }
}
