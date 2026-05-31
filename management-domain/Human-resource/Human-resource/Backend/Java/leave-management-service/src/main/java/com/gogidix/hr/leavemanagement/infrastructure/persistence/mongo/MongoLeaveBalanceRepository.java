package com.gogidix.hr.leavemanagement.infrastructure.persistence.mongo;

import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.domain.model.LeaveBalance;
import com.gogidix.hr.leavemanagement.domain.repository.LeaveBalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
public class MongoLeaveBalanceRepository implements LeaveBalanceRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public LeaveBalance save(LeaveBalance balance) {
        return mongoTemplate.save(balance);
    }

    @Override
    public List<LeaveBalance> saveAll(List<LeaveBalance> balances) {
        return balances.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<LeaveBalance> findById(String id) {
        String tenantId = com.gogidix.hr.leavemanagement.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(Criteria.where("id").is(id).and("tenantId").is(tenantId));
        return Optional.ofNullable(mongoTemplate.findOne(query, LeaveBalance.class));
    }

    @Override
    public Optional<LeaveBalance> findByBalanceIdAndTenantId(String balanceId, String tenantId) {
        Query query = Query.query(Criteria.where("balanceId").is(balanceId).and("tenantId").is(tenantId));
        return Optional.ofNullable(mongoTemplate.findOne(query, LeaveBalance.class));
    }

    @Override
    public Optional<LeaveBalance> findByEmployeeIdAndLeaveTypeAndYear(String employeeId, LeaveType leaveType, String year) {
        Query query = Query.query(Criteria.where("employeeId").is(employeeId)
                .and("leaveType").is(leaveType)
                .and("year").is(year));
        return Optional.ofNullable(mongoTemplate.findOne(query, LeaveBalance.class));
    }

    @Override
    public List<LeaveBalance> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, LeaveBalance.class);
    }

    @Override
    public List<LeaveBalance> findByTenantIdAndEmployeeId(String tenantId, String employeeId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("employeeId").is(employeeId));
        return mongoTemplate.find(query, LeaveBalance.class);
    }

    @Override
    public List<LeaveBalance> findByTenantIdAndEmployeeIdAndYear(String tenantId, String employeeId, String year) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)
                .and("employeeId").is(employeeId)
                .and("year").is(year));
        return mongoTemplate.find(query, LeaveBalance.class);
    }

    @Override
    public List<LeaveBalance> findByTenantIdAndYear(String tenantId, String year) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("year").is(year));
        return mongoTemplate.find(query, LeaveBalance.class);
    }

    @Override
    public List<LeaveBalance> findByTenantIdAndLeaveType(String tenantId, LeaveType leaveType) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("leaveType").is(leaveType));
        return mongoTemplate.find(query, LeaveBalance.class);
    }

    @Override
    public List<LeaveBalance> findByTenantIdAndPeriod(String tenantId, YearMonth period) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("period").is(period));
        return mongoTemplate.find(query, LeaveBalance.class);
    }

    @Override
    public List<LeaveBalance> findLowBalances(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).where("available").lt(5.0));
        return mongoTemplate.find(query, LeaveBalance.class);
    }

    @Override
    public List<LeaveBalance> findExpiringCarryForward(String tenantId) {
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)
                .and("carryForwardExpiryDate").exists(true)
                .and("carryForwardExpiryDate").lte(thirtyDaysFromNow)
                .and("carriedForward").gt(0.0));
        return mongoTemplate.find(query, LeaveBalance.class);
    }

    @Override
    public List<LeaveBalance> findNegativeBalances(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("available").lt(0.0));
        return mongoTemplate.find(query, LeaveBalance.class);
    }

    @Override
    public Page<LeaveBalance> searchBalances(String tenantId, String searchTerm, Pageable pageable) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)
                .orOperator(
                        Criteria.where("employeeName").regex(searchTerm, "i"),
                        Criteria.where("employeeCode").regex(searchTerm, "i"),
                        Criteria.where("balanceId").regex(searchTerm, "i")
                )).with(pageable);
        List<LeaveBalance> balances = mongoTemplate.find(query, LeaveBalance.class);
        return new PageImpl<>(balances, pageable, balances.size());
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, LeaveBalance.class);
    }

    @Override
    public long countByTenantIdAndYear(String tenantId, String year) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("year").is(year));
        return mongoTemplate.count(query, LeaveBalance.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), LeaveBalance.class);
    }

    @Override
    public void deleteByBalanceIdAndTenantId(String balanceId, String tenantId) {
        Query query = Query.query(Criteria.where("balanceId").is(balanceId).and("tenantId").is(tenantId));
        mongoTemplate.remove(query, LeaveBalance.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, LeaveBalance.class);
    }

    @Override
    public List<LeaveBalance> findByEmployeeIdAndLeaveType(String employeeId, LeaveType leaveType) {
        Query query = Query.query(Criteria.where("employeeId").is(employeeId).and("leaveType").is(leaveType));
        return mongoTemplate.find(query, LeaveBalance.class);
    }

    @Override
    public List<LeaveBalance> findAllByEmployeeId(String employeeId) {
        Query query = Query.query(Criteria.where("employeeId").is(employeeId));
        return mongoTemplate.find(query, LeaveBalance.class);
    }
}
