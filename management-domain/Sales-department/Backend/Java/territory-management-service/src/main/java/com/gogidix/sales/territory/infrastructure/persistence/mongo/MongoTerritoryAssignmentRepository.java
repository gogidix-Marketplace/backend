package com.gogidix.sales.territory.infrastructure.persistence.mongo;

import com.gogidix.sales.territory.domain.model.TerritoryAssignment;
import com.gogidix.sales.territory.domain.repository.TerritoryAssignmentRepository;
import com.gogidix.sales.territory.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Territory Assignment
 * Implements territory assignment persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoTerritoryAssignmentRepository implements TerritoryAssignmentRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public TerritoryAssignment save(TerritoryAssignment assignment) {
        log.debug("Saving assignment: {} for tenant: {}",
            assignment.getAssignmentId(), assignment.getTenantId());
        return mongoTemplate.save(assignment);
    }

    @Override
    public List<TerritoryAssignment> saveAll(List<TerritoryAssignment> assignments) {
        return assignments.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<TerritoryAssignment> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, TerritoryAssignment.class));
    }

    @Override
    public Optional<TerritoryAssignment> findByAssignmentIdAndTenantId(String assignmentId, String tenantId) {
        Query query = Query.query(
            Criteria.where("assignmentId").is(assignmentId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, TerritoryAssignment.class));
    }

    @Override
    public List<TerritoryAssignment> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, TerritoryAssignment.class);
    }

    @Override
    public List<TerritoryAssignment> findByTenantIdAndTerritoryId(String tenantId, String territoryId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("territoryId").is(territoryId)
        );
        return mongoTemplate.find(query, TerritoryAssignment.class);
    }

    @Override
    public List<TerritoryAssignment> findByTenantIdAndSalesRepresentativeId(String tenantId, String salesRepresentativeId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("salesRepresentativeId").is(salesRepresentativeId)
        );
        return mongoTemplate.find(query, TerritoryAssignment.class);
    }

    @Override
    public List<TerritoryAssignment> findByTenantIdAndStatus(String tenantId, TerritoryAssignment.AssignmentStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, TerritoryAssignment.class);
    }

    @Override
    public List<TerritoryAssignment> findActiveByTenantIdAndTerritoryId(String tenantId, String territoryId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("territoryId").is(territoryId)
                .and("status").is(TerritoryAssignment.AssignmentStatus.ACTIVE)
        );
        return mongoTemplate.find(query, TerritoryAssignment.class);
    }

    @Override
    public List<TerritoryAssignment> findActiveByTenantIdAndSalesRepresentativeId(String tenantId, String salesRepresentativeId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("salesRepresentativeId").is(salesRepresentativeId)
                .and("status").is(TerritoryAssignment.AssignmentStatus.ACTIVE)
        );
        return mongoTemplate.find(query, TerritoryAssignment.class);
    }

    @Override
    public List<TerritoryAssignment> findByTenantIdAndEffectiveDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(java.time.ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("effectiveDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, TerritoryAssignment.class);
    }

    @Override
    public List<TerritoryAssignment> findPrimaryAssignmentsByTenantIdAndTerritoryId(String tenantId, String territoryId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("territoryId").is(territoryId)
                .and("primaryAssignment").is(true)
        );
        return mongoTemplate.find(query, TerritoryAssignment.class);
    }

    @Override
    public Optional<TerritoryAssignment> findPrimaryByTenantIdAndSalesRepresentativeId(String tenantId, String salesRepresentativeId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("salesRepresentativeId").is(salesRepresentativeId)
                .and("primaryAssignment").is(true)
                .and("status").is(TerritoryAssignment.AssignmentStatus.ACTIVE)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, TerritoryAssignment.class));
    }

    @Override
    public boolean existsByTerritoryIdAndSalesRepresentativeIdAndStatus(String territoryId, String salesRepresentativeId,
                                                                          TerritoryAssignment.AssignmentStatus status) {
        Query query = Query.query(
            Criteria.where("territoryId").is(territoryId)
                .and("salesRepresentativeId").is(salesRepresentativeId)
                .and("status").is(status)
        );
        return mongoTemplate.exists(query, TerritoryAssignment.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), TerritoryAssignment.class);
    }

    @Override
    public void deleteByAssignmentIdAndTenantId(String assignmentId, String tenantId) {
        Query query = Query.query(
            Criteria.where("assignmentId").is(assignmentId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, TerritoryAssignment.class);
    }

    @Override
    public void deleteByTerritoryIdAndTenantId(String territoryId, String tenantId) {
        Query query = Query.query(
            Criteria.where("territoryId").is(territoryId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, TerritoryAssignment.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, TerritoryAssignment.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, TerritoryAssignment.class);
    }

    @Override
    public long countByTenantIdAndTerritoryId(String tenantId, String territoryId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("territoryId").is(territoryId)
        );
        return mongoTemplate.count(query, TerritoryAssignment.class);
    }

    @Override
    public long countByTenantIdAndSalesRepresentativeId(String tenantId, String salesRepresentativeId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("salesRepresentativeId").is(salesRepresentativeId)
        );
        return mongoTemplate.count(query, TerritoryAssignment.class);
    }

    @Override
    public long countActiveByTenantIdAndTerritoryId(String tenantId, String territoryId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("territoryId").is(territoryId)
                .and("status").is(TerritoryAssignment.AssignmentStatus.ACTIVE)
        );
        return mongoTemplate.count(query, TerritoryAssignment.class);
    }
}
