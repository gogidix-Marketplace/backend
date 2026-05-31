package com.gogidix.hr.payroll.infrastructure.persistence.mongo;

import com.gogidix.hr.payroll.domain.model.Payslip;
import com.gogidix.hr.payroll.domain.repository.PayslipRepository;
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
public class MongoPayslipRepository implements PayslipRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Payslip save(Payslip payslip) {
        return mongoTemplate.save(payslip);
    }

    @Override
    public List<Payslip> saveAll(List<Payslip> payslips) {
        return payslips.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<Payslip> findById(String id) {
        String tenantId = com.gogidix.hr.payroll.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Payslip.class));
    }

    @Override
    public Optional<Payslip> findByPayrollIdAndEmployeeId(String payrollId, String employeeId) {
        Query query = Query.query(
                Criteria.where("payrollId").is(payrollId).and("employeeId").is(employeeId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Payslip.class));
    }

    @Override
    public List<Payslip> findByPayrollId(String payrollId) {
        Query query = Query.query(Criteria.where("payrollId").is(payrollId));
        return mongoTemplate.find(query, Payslip.class);
    }

    @Override
    public List<Payslip> findByEmployeeId(String employeeId) {
        Query query = Query.query(Criteria.where("employeeId").is(employeeId));
        return mongoTemplate.find(query, Payslip.class);
    }

    @Override
    public List<Payslip> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Payslip.class);
    }

    @Override
    public List<Payslip> findByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("countryCode").is(countryCode)
        );
        return mongoTemplate.find(query, Payslip.class);
    }

    @Override
    public List<Payslip> findByEmployeeIdAndPayPeriod(String employeeId, YearMonth payPeriod) {
        Query query = Query.query(
                Criteria.where("employeeId").is(employeeId).and("payPeriod").is(payPeriod)
        );
        return mongoTemplate.find(query, Payslip.class);
    }

    @Override
    public List<Payslip> findRecentPayslips(String employeeId, int limit) {
        Query query = Query.query(Criteria.where("employeeId").is(employeeId))
                .limit(limit)
                .with(org.springframework.data.domain.Sort.by(
                        org.springframework.data.domain.Sort.Direction.DESC, "createdAt"
                ));
        return mongoTemplate.find(query, Payslip.class);
    }

    @Override
    public List<Payslip> findYearToDatePayslips(String employeeId, int year) {
        Query query = Query.query(
                Criteria.where("employeeId").is(employeeId)
                        .and("payPeriod").gte(YearMonth.of(year, 1)).lte(YearMonth.of(year, 12))
        );
        return mongoTemplate.find(query, Payslip.class);
    }

    @Override
    public List<Payslip> findByPayPeriod(YearMonth payPeriod) {
        Query query = Query.query(Criteria.where("payPeriod").is(payPeriod));
        return mongoTemplate.find(query, Payslip.class);
    }

    @Override
    public List<Payslip> findByStatus(String status) {
        Query query = Query.query(Criteria.where("status").is(status));
        return mongoTemplate.find(query, Payslip.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Payslip.class);
    }

    @Override
    public void deleteByPayrollId(String payrollId) {
        Query query = Query.query(Criteria.where("payrollId").is(payrollId));
        mongoTemplate.remove(query, Payslip.class);
    }

    @Override
    public boolean existsByPayrollIdAndEmployeeId(String payrollId, String employeeId) {
        Query query = Query.query(
                Criteria.where("payrollId").is(payrollId).and("employeeId").is(employeeId)
        );
        return mongoTemplate.exists(query, Payslip.class);
    }

    @Override
    public List<Payslip> findByEmployeeIdAndYear(String employeeId, int year) {
        Query query = Query.query(
                Criteria.where("employeeId").is(employeeId)
                        .and("payPeriod").gte(YearMonth.of(year, 1)).lte(YearMonth.of(year, 12))
        );
        return mongoTemplate.find(query, Payslip.class);
    }

    @Override
    public List<Payslip> findIssuedPayslips(String tenantId, YearMonth payPeriod) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("payPeriod").is(payPeriod).and("status").is("ISSUED")
        );
        return mongoTemplate.find(query, Payslip.class);
    }

    @Override
    public List<Payslip> findByTenantIdAndEmployeeIdAndPeriod(String tenantId, String employeeId, YearMonth period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("employeeId").is(employeeId)
                        .and("payPeriod").is(period)
        );
        return mongoTemplate.find(query, Payslip.class);
    }

    @Override
    public List<Payslip> getByEmployeeIdAndPeriod(String employeeId, YearMonth period) {
        Query query = Query.query(
                Criteria.where("employeeId").is(employeeId).and("payPeriod").is(period)
        );
        return mongoTemplate.find(query, Payslip.class);
    }

    @Override
    public Optional<Payslip> findByPayslipIdAndTenantId(String payslipId, String tenantId) {
        Query query = Query.query(
                Criteria.where("payslipId").is(payslipId).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Payslip.class));
    }

    @Override
    public void deleteByPayslipIdAndTenantId(String payslipId, String tenantId) {
        Query query = Query.query(
                Criteria.where("payslipId").is(payslipId).and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Payslip.class);
    }

    @Override
    public List<Payslip> findByPayrollIdAndTenantId(String payrollId, String tenantId) {
        Query query = Query.query(
                Criteria.where("payrollId").is(payrollId).and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Payslip.class);
    }

    @Override
    public List<Payslip> findByTenantIdAndEmployeeId(String tenantId, String employeeId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("employeeId").is(employeeId)
        );
        return mongoTemplate.find(query, Payslip.class);
    }
}
