package com.gogidix.hr.leavemanagement.infrastructure.persistence.mongo;

import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.domain.model.LeavePolicy;
import com.gogidix.hr.leavemanagement.domain.repository.LeavePolicyRepository;
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
public class MongoLeavePolicyRepository implements LeavePolicyRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public LeavePolicy save(LeavePolicy policy) {
        return mongoTemplate.save(policy);
    }

    @Override
    public List<LeavePolicy> saveAll(List<LeavePolicy> policies) {
        return policies.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<LeavePolicy> findById(String id) {
        String tenantId = com.gogidix.hr.leavemanagement.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(Criteria.where("id").is(id).and("tenantId").is(tenantId));
        return Optional.ofNullable(mongoTemplate.findOne(query, LeavePolicy.class));
    }

    @Override
    public Optional<LeavePolicy> findByPolicyIdAndTenantId(String policyId, String tenantId) {
        Query query = Query.query(Criteria.where("policyId").is(policyId).and("tenantId").is(tenantId));
        return Optional.ofNullable(mongoTemplate.findOne(query, LeavePolicy.class));
    }

    @Override
    public Optional<LeavePolicy> findByPolicyCodeAndTenantId(String policyCode, String tenantId) {
        Query query = Query.query(Criteria.where("policyCode").is(policyCode).and("tenantId").is(tenantId));
        return Optional.ofNullable(mongoTemplate.findOne(query, LeavePolicy.class));
    }

    @Override
    public List<LeavePolicy> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, LeavePolicy.class);
    }

    @Override
    public List<LeavePolicy> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("isActive").is(isActive));
        return mongoTemplate.find(query, LeavePolicy.class);
    }

    @Override
    public List<LeavePolicy> findByTenantIdAndLeaveType(String tenantId, LeaveType leaveType) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("leaveType").is(leaveType));
        return mongoTemplate.find(query, LeavePolicy.class);
    }

    @Override
    public List<LeavePolicy> findByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("countryCode").is(countryCode));
        return mongoTemplate.find(query, LeavePolicy.class);
    }

    @Override
    public List<LeavePolicy> findActivePoliciesForDate(String tenantId, LocalDate date) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)
                .and("isActive").is(true)
                .and("effectiveFrom").lte(date)
                .orOperator(
                        Criteria.where("effectiveTo").exists(false),
                        Criteria.where("effectiveTo").gte(date)
                ));
        return mongoTemplate.find(query, LeavePolicy.class);
    }

    @Override
    public List<LeavePolicy> findPoliciesForEmployee(String tenantId, String employeeId, LocalDate date) {
        // Simplified - would need to join with employee for department/location
        return findActivePoliciesForDate(tenantId, date);
    }

    @Override
    public boolean existsByPolicyCodeAndTenantId(String policyCode, String tenantId) {
        Query query = Query.query(Criteria.where("policyCode").is(policyCode).and("tenantId").is(tenantId));
        return mongoTemplate.exists(query, LeavePolicy.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), LeavePolicy.class);
    }

    @Override
    public void deleteByPolicyIdAndTenantId(String policyId, String tenantId) {
        Query query = Query.query(Criteria.where("policyId").is(policyId).and("tenantId").is(tenantId));
        mongoTemplate.remove(query, LeavePolicy.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, LeavePolicy.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, LeavePolicy.class);
    }
}
