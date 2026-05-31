package com.gogidix.sales.crm.infrastructure.persistence.mongo;

import com.gogidix.sales.crm.domain.model.Interaction;
import com.gogidix.sales.crm.domain.repository.InteractionRepository;
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
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Interaction
 * Implements interaction persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoInteractionRepository implements InteractionRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Interaction save(Interaction interaction) {
        log.debug("Saving interaction: {} for tenant: {}",
            interaction.getInteractionId(), interaction.getTenantId());
        return mongoTemplate.save(interaction);
    }

    @Override
    public List<Interaction> saveAll(List<Interaction> interactions) {
        return interactions.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<Interaction> findById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return Optional.ofNullable(mongoTemplate.findOne(query, Interaction.class));
    }

    @Override
    public Optional<Interaction> findByInteractionIdAndTenantId(String interactionId, String tenantId) {
        Query query = Query.query(
            Criteria.where("interactionId").is(interactionId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Interaction.class));
    }

    @Override
    public List<Interaction> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public Page<Interaction> findByTenantId(String tenantId, Pageable pageable) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        query.with(pageable);
        List<Interaction> interactions = mongoTemplate.find(query, Interaction.class);
        long count = mongoTemplate.count(query, Interaction.class);
        return new PageImpl<>(interactions, pageable, count);
    }

    @Override
    public List<Interaction> findByCustomerIdAndTenantId(String customerId, String tenantId) {
        Query query = Query.query(
            Criteria.where("customerId").is(customerId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public List<Interaction> findByTenantIdAndCustomerId(String tenantId, String customerId) {
        return findByCustomerIdAndTenantId(customerId, tenantId);
    }

    @Override
    public List<Interaction> findByContactIdAndTenantId(String contactId, String tenantId) {
        Query query = Query.query(
            Criteria.where("contactId").is(contactId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public List<Interaction> findByTenantIdAndType(String tenantId, Interaction.InteractionType type) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("type").is(type)
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public List<Interaction> findByTenantIdAndDirection(String tenantId, Interaction.InteractionDirection direction) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("direction").is(direction)
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public List<Interaction> findByTenantIdAndStatus(String tenantId, Interaction.InteractionStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public List<Interaction> findByTenantIdAndAssignedTo(String tenantId, String assignedTo) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("assignedTo").is(assignedTo)
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public List<Interaction> findByTenantIdAndInteractionDateBetween(String tenantId, java.time.LocalDateTime startDate,
                                                                      java.time.LocalDateTime endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("interactionDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public List<Interaction> findByTenantIdAndInteractionDateAfter(String tenantId, java.time.LocalDateTime date) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("interactionDate").gte(date)
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public List<Interaction> findByTenantIdAndInteractionDateBefore(String tenantId, java.time.LocalDateTime date) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("interactionDate").lte(date)
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public List<Interaction> findByTenantIdAndStatusAndInteractionDateBefore(String tenantId,
                                                                               Interaction.InteractionStatus status,
                                                                               java.time.LocalDateTime date) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
                .and("interactionDate").lte(date)
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public List<Interaction> findByTenantIdAndHasFollowUpTrueAndFollowUpDateBefore(String tenantId, LocalDate date) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("hasFollowUp").is(true)
                .and("followUpDate").lte(date)
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public List<Interaction> findByTenantIdAndDealId(String tenantId, String dealId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("dealId").is(dealId)
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public List<Interaction> findByTenantIdAndCampaignId(String tenantId, String campaignId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("campaignId").is(campaignId)
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public List<Interaction> findByTenantIdAndIsHighPriorityTrue(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("isHighPriority").is(true)
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public List<Interaction> findByTenantIdAndSubjectContainingIgnoreCase(String tenantId, String searchTerm) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("subject").regex(searchTerm, "i")
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public Page<Interaction> findByTenantIdAndSubjectContainingIgnoreCase(String tenantId, String searchTerm, Pageable pageable) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("subject").regex(searchTerm, "i")
        ).with(pageable);
        List<Interaction> interactions = mongoTemplate.find(query, Interaction.class);
        long total = mongoTemplate.count(Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("subject").regex(searchTerm, "i")
        ), Interaction.class);
        return new org.springframework.data.domain.PageImpl<>(interactions, pageable, total);
    }

    @Override
    public List<Interaction> findByTenantIdAndDescriptionContainingIgnoreCase(String tenantId, String searchTerm) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("description").regex(searchTerm, "i")
        );
        return mongoTemplate.find(query, Interaction.class);
    }

    @Override
    public boolean existsByInteractionIdAndTenantId(String interactionId, String tenantId) {
        Query query = Query.query(
            Criteria.where("interactionId").is(interactionId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Interaction.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Interaction.class);
    }

    @Override
    public void deleteByInteractionIdAndTenantId(String interactionId, String tenantId) {
        Query query = Query.query(
            Criteria.where("interactionId").is(interactionId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Interaction.class);
    }

    @Override
    public void deleteAllByCustomerIdAndTenantId(String customerId, String tenantId) {
        Query query = Query.query(
            Criteria.where("customerId").is(customerId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Interaction.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Interaction.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Interaction.class);
    }

    @Override
    public long countByCustomerIdAndTenantId(String customerId, String tenantId) {
        Query query = Query.query(
            Criteria.where("customerId").is(customerId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.count(query, Interaction.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, Interaction.InteractionStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, Interaction.class);
    }

    @Override
    public long countByTenantIdAndType(String tenantId, Interaction.InteractionType type) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("type").is(type)
        );
        return mongoTemplate.count(query, Interaction.class);
    }

    @Override
    public long countByTenantIdAndAssignedTo(String tenantId, String assignedTo) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("assignedTo").is(assignedTo)
        );
        return mongoTemplate.count(query, Interaction.class);
    }

    @Override
    public long countByTenantIdAndInteractionDateBetween(String tenantId, java.time.LocalDateTime startDate,
                                                          java.time.LocalDateTime endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("interactionDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.count(query, Interaction.class);
    }

    @Override
    public List<Interaction> findByTenantIdOrderByInteractionDateDesc(String tenantId, Pageable pageable) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        query.with(pageable);
        return mongoTemplate.find(query, Interaction.class);
    }
}
