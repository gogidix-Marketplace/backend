package com.gogidix.hr.globalcompliance.infrastructure.persistence.mongo;

import com.gogidix.hr.globalcompliance.domain.model.ComplianceRequirement;
import com.gogidix.hr.globalcompliance.domain.repository.ComplianceRequirementRepository;
import com.gogidix.hr.globalcompliance.shared.requestcontext.RequestContextHolder;
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
 * MongoDB Repository Implementation - ComplianceRequirement
 * Implements compliance requirement persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoComplianceRequirementRepository implements ComplianceRequirementRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public ComplianceRequirement save(ComplianceRequirement requirement) {
        log.debug("Saving requirement: {} for tenant: {}",
                requirement.getRequirementId(), requirement.getTenantId());
        return mongoTemplate.save(requirement);
    }

    @Override
    public List<ComplianceRequirement> saveAll(List<ComplianceRequirement> requirements) {
        return requirements.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<ComplianceRequirement> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ComplianceRequirement.class));
    }

    @Override
    public Optional<ComplianceRequirement> findByRequirementIdAndTenantId(String requirementId, String tenantId) {
        Query query = Query.query(
                Criteria.where("requirementId").is(requirementId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ComplianceRequirement.class));
    }

    @Override
    public Optional<ComplianceRequirement> findByRequirementCodeAndTenantId(String requirementCode, String tenantId) {
        Query query = Query.query(
                Criteria.where("requirementCode").is(requirementCode)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ComplianceRequirement.class));
    }

    @Override
    public List<ComplianceRequirement> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }

    @Override
    public List<ComplianceRequirement> findByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode)
        );
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }

    @Override
    public List<ComplianceRequirement> findByTenantIdAndCategory(String tenantId, String category) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("category").is(category)
        );
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }

    @Override
    public List<ComplianceRequirement> findByTenantIdAndActive(String tenantId, Boolean active) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("active").is(active)
        );
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }

    @Override
    public List<ComplianceRequirement> findByTenantIdAndType(String tenantId, String type) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("type").is(type)
        );
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }

    @Override
    public List<ComplianceRequirement> findByTenantIdAndSeverity(String tenantId, String severity) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("severity").is(severity)
        );
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }

    @Override
    public List<ComplianceRequirement> findByTenantIdAndOwnerDepartment(String tenantId, String ownerDepartment) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("ownerDepartment").is(ownerDepartment)
        );
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }

    @Override
    public List<ComplianceRequirement> findByTenantIdAndOwnerId(String tenantId, String ownerId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("ownerId").is(ownerId)
        );
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }

    @Override
    public List<ComplianceRequirement> findByTenantIdAndEffectiveFromBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("effectiveFrom").gte(start).lte(end)
        );
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }

    @Override
    public List<ComplianceRequirement> findByTenantIdAndReviewDateBefore(String tenantId, LocalDate reviewDate) {
        Date date = Date.from(reviewDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("reviewDate").lt(date)
        );
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }

    @Override
    public List<ComplianceRequirement> findDueForReview(String tenantId) {
        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(30);
        Date dueDateDate = Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("active").is(true)
                        .and("reviewDate").lte(dueDateDate)
        );
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }

    @Override
    public List<ComplianceRequirement> findActiveRequirements(String tenantId) {
        LocalDate today = LocalDate.now();
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("active").is(true)
                        .orOperator(
                                Criteria.where("effectiveTo").exists(false),
                                Criteria.where("effectiveTo").gte(today)
                        )
        );
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }

    @Override
    public List<ComplianceRequirement> findByTenantIdAndCountryCodeAndActive(String tenantId, String countryCode, Boolean active) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode)
                        .and("active").is(active)
        );
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }

    @Override
    public List<ComplianceRequirement> searchByDescription(String tenantId, String searchTerm) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .orOperator(
                                Criteria.where("description").regex(searchTerm, "i"),
                                Criteria.where("requirementName").regex(searchTerm, "i"),
                                Criteria.where("requirementCode").regex(searchTerm, "i")
                        )
        );
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }

    @Override
    public List<ComplianceRequirement> findByTenantIdAndAuthority(String tenantId, String authority) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("authority").is(authority)
        );
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }

    @Override
    public boolean existsByRequirementCodeAndTenantId(String requirementCode, String tenantId) {
        Query query = Query.query(
                Criteria.where("requirementCode").is(requirementCode)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, ComplianceRequirement.class);
    }

    @Override
    public boolean existsByRequirementIdAndTenantId(String requirementId, String tenantId) {
        Query query = Query.query(
                Criteria.where("requirementId").is(requirementId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, ComplianceRequirement.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), ComplianceRequirement.class);
    }

    @Override
    public void deleteByRequirementIdAndTenantId(String requirementId, String tenantId) {
        Query query = Query.query(
                Criteria.where("requirementId").is(requirementId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, ComplianceRequirement.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, ComplianceRequirement.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, ComplianceRequirement.class);
    }

    @Override
    public long countByTenantIdAndActive(String tenantId, Boolean active) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("active").is(active)
        );
        return mongoTemplate.count(query, ComplianceRequirement.class);
    }

    @Override
    public long countByTenantIdAndCategory(String tenantId, String category) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("category").is(category)
        );
        return mongoTemplate.count(query, ComplianceRequirement.class);
    }

    @Override
    public long countByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode)
        );
        return mongoTemplate.count(query, ComplianceRequirement.class);
    }

    @Override
    public List<ComplianceRequirement> findByTenantIdAndRelatedRequirementsContaining(String tenantId, String requirementId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("relatedRequirements").is(requirementId)
        );
        return mongoTemplate.find(query, ComplianceRequirement.class);
    }
}
