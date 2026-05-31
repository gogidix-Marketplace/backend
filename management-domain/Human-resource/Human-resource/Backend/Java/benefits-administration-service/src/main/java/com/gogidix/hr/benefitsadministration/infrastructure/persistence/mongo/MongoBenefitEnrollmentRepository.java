package com.gogidix.hr.benefitsadministration.infrastructure.persistence.mongo;

import com.gogidix.hr.benefitsadministration.domain.enums.EnrollmentStatus;
import com.gogidix.hr.benefitsadministration.domain.model.BenefitEnrollment;
import com.gogidix.hr.benefitsadministration.domain.port.out.BenefitEnrollmentRepositoryPort;
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
 * MongoDB repository implementation for BenefitEnrollment
 * Implements the hexagonal architecture output port
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoBenefitEnrollmentRepository implements BenefitEnrollmentRepositoryPort {

    private final MongoTemplate mongoTemplate;

    @Override
    public BenefitEnrollment save(BenefitEnrollment enrollment) {
        return mongoTemplate.save(enrollment);
    }

    @Override
    public Optional<BenefitEnrollment> findById(String enrollmentId, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(enrollmentId).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, BenefitEnrollment.class));
    }

    @Override
    public List<BenefitEnrollment> findByEmployeeId(String employeeId, String tenantId) {
        Query query = Query.query(
                Criteria.where("employeeId").is(employeeId).and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, BenefitEnrollment.class);
    }

    @Override
    public List<BenefitEnrollment> findActiveByEmployee(String employeeId, String tenantId) {
        Query query = Query.query(
                Criteria.where("employeeId").is(employeeId)
                        .and("tenantId").is(tenantId)
                        .and("status").is(EnrollmentStatus.ACTIVE)
        );
        return mongoTemplate.find(query, BenefitEnrollment.class);
    }

    @Override
    public List<BenefitEnrollment> findByPlanId(String planId, String tenantId) {
        Query query = Query.query(
                Criteria.where("planId").is(planId).and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, BenefitEnrollment.class);
    }

    @Override
    public List<BenefitEnrollment> findByStatus(String tenantId, String status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is(EnrollmentStatus.valueOf(status))
        );
        return mongoTemplate.find(query, BenefitEnrollment.class);
    }

    @Override
    public List<BenefitEnrollment> findPending(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is(EnrollmentStatus.PENDING)
        );
        return mongoTemplate.find(query, BenefitEnrollment.class);
    }

    @Override
    public List<BenefitEnrollment> findEffectiveInPeriod(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("effectiveDate").gte(startDate)
                        .and("effectiveDate").lte(endDate)
        );
        return mongoTemplate.find(query, BenefitEnrollment.class);
    }

    @Override
    public List<BenefitEnrollment> findExpiringBefore(String tenantId, LocalDate expiryDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("expiryDate").lte(expiryDate)
                        .and("status").is(EnrollmentStatus.ACTIVE)
        );
        return mongoTemplate.find(query, BenefitEnrollment.class);
    }

    @Override
    public void deleteById(String enrollmentId, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(enrollmentId).and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, BenefitEnrollment.class);
    }

    @Override
    public boolean isEmployeeEnrolled(String employeeId, String planId, String tenantId) {
        Query query = Query.query(
                Criteria.where("employeeId").is(employeeId)
                        .and("planId").is(planId)
                        .and("tenantId").is(tenantId)
                        .and("status").in(EnrollmentStatus.ACTIVE, EnrollmentStatus.PENDING)
        );
        return mongoTemplate.exists(query, BenefitEnrollment.class);
    }

    @Override
    public long countByPlanId(String planId, String tenantId) {
        Query query = Query.query(
                Criteria.where("planId").is(planId)
                        .and("tenantId").is(tenantId)
                        .and("status").is(EnrollmentStatus.ACTIVE)
        );
        return mongoTemplate.count(query, BenefitEnrollment.class);
    }
}
