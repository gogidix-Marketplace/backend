package com.gogidix.hr.employee.infrastructure.persistence.mongo;

import com.gogidix.hr.employee.domain.model.EmploymentRecord;
import com.gogidix.hr.employee.domain.repository.EmploymentRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - EmploymentRecord
 */
@Repository
@RequiredArgsConstructor
public class MongoEmploymentRecordRepository implements EmploymentRecordRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public EmploymentRecord save(EmploymentRecord record) {
        return mongoTemplate.save(record);
    }

    @Override
    public List<EmploymentRecord> saveAll(List<EmploymentRecord> records) {
        return records.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<EmploymentRecord> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, EmploymentRecord.class));
    }

    @Override
    public List<EmploymentRecord> findByEmployeeId(String employeeId) {
        Query query = Query.query(Criteria.where("employeeId").is(employeeId));
        return mongoTemplate.find(query, EmploymentRecord.class);
    }

    @Override
    public List<EmploymentRecord> findByEmployeeIdAndTenantId(String employeeId, String tenantId) {
        Query query = Query.query(
            Criteria.where("employeeId").is(employeeId).and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, EmploymentRecord.class);
    }

    @Override
    public List<EmploymentRecord> findByTenantIdAndEventType(String tenantId, String eventType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("eventType").is(eventType)
        );
        return mongoTemplate.find(query, EmploymentRecord.class);
    }

    @Override
    public List<EmploymentRecord> findByEmployeeIdAndEventType(String employeeId, String eventType) {
        Query query = Query.query(
            Criteria.where("employeeId").is(employeeId).and("eventType").is(eventType)
        );
        return mongoTemplate.find(query, EmploymentRecord.class);
    }

    @Override
    public List<EmploymentRecord> findByEmployeeIdAndEventDateBetween(String employeeId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
            Criteria.where("employeeId").is(employeeId)
                .and("eventDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, EmploymentRecord.class);
    }

    @Override
    public List<EmploymentRecord> findByTenantIdAndEventDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("eventDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, EmploymentRecord.class);
    }

    @Override
    public List<EmploymentRecord> findByEmployeeIdOrderByEventDateDesc(String employeeId) {
        Query query = Query.query(Criteria.where("employeeId").is(employeeId))
            .with(Sort.by(Sort.Direction.DESC, "eventDate"));
        return mongoTemplate.find(query, EmploymentRecord.class);
    }

    @Override
    public List<EmploymentRecord> findRecentByEmployeeId(String employeeId, int limit) {
        Query query = Query.query(Criteria.where("employeeId").is(employeeId))
            .with(Sort.by(Sort.Direction.DESC, "eventDate"))
            .limit(limit);
        return mongoTemplate.find(query, EmploymentRecord.class);
    }

    @Override
    public List<EmploymentRecord> findByTenantIdAndApprovedBy(String tenantId, String approvedBy) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("approvedBy").is(approvedBy)
        );
        return mongoTemplate.find(query, EmploymentRecord.class);
    }

    @Override
    public List<EmploymentRecord> findTerminationsByTenantIdAndDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("eventType").is(EmploymentRecord.EVENT_TERMINATION)
                .and("eventDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, EmploymentRecord.class);
    }

    @Override
    public List<EmploymentRecord> findHiresByTenantIdAndDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("eventType").is(EmploymentRecord.EVENT_HIRE)
                .and("eventDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, EmploymentRecord.class);
    }

    @Override
    public List<EmploymentRecord> findPromotionsByTenantIdAndDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("eventType").is(EmploymentRecord.EVENT_PROMOTION)
                .and("eventDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, EmploymentRecord.class);
    }

    @Override
    public List<EmploymentRecord> findSalaryChangesByTenantIdAndDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("eventType").is(EmploymentRecord.EVENT_SALARY_CHANGE)
                .and("eventDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, EmploymentRecord.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), EmploymentRecord.class);
    }

    @Override
    public void deleteByEmployeeId(String employeeId) {
        mongoTemplate.remove(Query.query(Criteria.where("employeeId").is(employeeId)), EmploymentRecord.class);
    }

    @Override
    public void deleteByEmployeeIdAndTenantId(String employeeId, String tenantId) {
        Query query = Query.query(
            Criteria.where("employeeId").is(employeeId).and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, EmploymentRecord.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        mongoTemplate.remove(Query.query(Criteria.where("tenantId").is(tenantId)), EmploymentRecord.class);
    }

    @Override
    public long countByEmployeeId(String employeeId) {
        Query query = Query.query(Criteria.where("employeeId").is(employeeId));
        return mongoTemplate.count(query, EmploymentRecord.class);
    }

    @Override
    public long countByTenantIdAndEventType(String tenantId, String eventType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("eventType").is(eventType)
        );
        return mongoTemplate.count(query, EmploymentRecord.class);
    }

    @Override
    public long countByTenantIdAndEventDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("eventDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.count(query, EmploymentRecord.class);
    }

    @Override
    public Optional<EmploymentRecord> findLatestByEmployeeId(String employeeId) {
        Query query = Query.query(Criteria.where("employeeId").is(employeeId))
            .with(Sort.by(Sort.Direction.DESC, "eventDate"))
            .limit(1);
        return Optional.ofNullable(mongoTemplate.findOne(query, EmploymentRecord.class));
    }
}
