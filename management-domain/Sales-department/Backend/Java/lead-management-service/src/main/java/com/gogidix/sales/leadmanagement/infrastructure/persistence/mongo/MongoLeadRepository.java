package com.gogidix.sales.leadmanagement.infrastructure.persistence.mongo;

import com.gogidix.sales.leadmanagement.domain.model.Lead;
import com.gogidix.sales.leadmanagement.domain.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Lead Repository Implementation
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoLeadRepository implements LeadRepository {

    private final LeadMongoRepository mongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public Lead save(Lead lead) {
        return mongoRepository.save(lead);
    }

    @Override
    public Optional<Lead> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<Lead> findByLeadIdAndTenantId(String leadId, String tenantId) {
        return mongoRepository.findByLeadIdAndTenantId(leadId, tenantId);
    }

    @Override
    public List<Lead> findAllByTenantId(String tenantId) {
        return mongoRepository.findAllByTenantId(tenantId);
    }

    @Override
    public List<Lead> findByTenantIdAndStatus(String tenantId, Lead.LeadStatus status) {
        return mongoRepository.findByTenantIdAndStatus(tenantId, status);
    }

    @Override
    public List<Lead> findByTenantIdAndStage(String tenantId, Lead.LeadStage stage) {
        return mongoRepository.findByTenantIdAndStage(tenantId, stage);
    }

    @Override
    public List<Lead> findByOwnerIdAndTenantId(String ownerId, String tenantId) {
        return mongoRepository.findByOwnerIdAndTenantId(ownerId, tenantId);
    }

    @Override
    public List<Lead> findByTenantIdAndSource(String tenantId, Lead.LeadSource source) {
        return mongoRepository.findByTenantIdAndSource(tenantId, source);
    }

    @Override
    public List<Lead> findByTenantIdAndQuality(String tenantId, Lead.LeadQuality quality) {
        return mongoRepository.findByTenantIdAndQuality(tenantId, quality);
    }

    @Override
    public List<Lead> findByTenantIdAndScoreGreaterThanEqual(String tenantId, Integer minScore) {
        return mongoRepository.findByTenantIdAndScoreGreaterThanEqual(tenantId, minScore);
    }

    @Override
    public List<Lead> findByTenantIdAndEmail(String tenantId, String email) {
        return mongoRepository.findByTenantIdAndEmail(tenantId, email);
    }

    @Override
    public List<Lead> findByTenantIdAndPhone(String tenantId, String phone) {
        return mongoRepository.findByTenantIdAndPhone(tenantId, phone);
    }

    @Override
    public List<Lead> findByTenantIdAndCompany(String tenantId, String company) {
        return mongoRepository.findByTenantIdAndCompany(tenantId, company);
    }

    @Override
    public List<Lead> findByTenantIdAndLastActivityDateAfter(String tenantId, LocalDate date) {
        return mongoRepository.findByTenantIdAndLastActivityDateAfter(tenantId, date);
    }

    @Override
    public List<Lead> findDuplicateLeads(String tenantId, String email, String phone, String firstName, String lastName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("email").is(email),
                Criteria.where("phone").is(phone),
                Criteria.where("firstName").is(firstName).and("lastName").is(lastName)
        ));
        return mongoTemplate.find(query, Lead.class);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public void deleteByLeadIdAndTenantId(String leadId, String tenantId) {
        mongoRepository.deleteByLeadIdAndTenantId(leadId, tenantId);
    }

    @Override
    public boolean existsByLeadIdAndTenantId(String leadId, String tenantId) {
        return mongoRepository.existsByLeadIdAndTenantId(leadId, tenantId);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, Lead.LeadStatus status) {
        return mongoRepository.countByTenantIdAndStatus(tenantId, status);
    }

    @Override
    public long countByTenantIdAndStage(String tenantId, Lead.LeadStage stage) {
        return mongoRepository.countByTenantIdAndStage(tenantId, stage);
    }

    @Override
    public long countByOwnerIdAndTenantId(String ownerId, String tenantId) {
        return mongoRepository.countByOwnerIdAndTenantId(ownerId, tenantId);
    }

    @Override
    public List<Lead> findLeadsNeedingFollowUp(String tenantId, LocalDate since) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("status").in(
                Lead.LeadStatus.ACTIVE,
                Lead.LeadStatus.CONTACTED,
                Lead.LeadStatus.ENGAGED
        ));
        query.addCriteria(Criteria.where("lastActivityDate").lt(since));
        return mongoTemplate.find(query, Lead.class);
    }

    @Override
    public List<Lead> searchLeads(String tenantId, String searchTerm) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(searchTerm);
        Query query = TextQuery.queryText(textCriteria).addCriteria(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Lead.class);
    }

    @Override
    public List<Lead> findByTenantIdAndTagListContaining(String tenantId, String tag) {
        return mongoRepository.findByTenantIdAndTagListContaining(tenantId, tag);
    }

    @Override
    public List<Lead> findStaleLeads(String tenantId, int staleDays) {
        LocalDate staleDate = LocalDate.now().minusDays(staleDays);
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("lastActivityDate").lt(staleDate));
        query.addCriteria(Criteria.where("stage").ne(Lead.LeadStage.CONVERTED));
        query.addCriteria(Criteria.where("stage").ne(Lead.LeadStage.LOST));
        return mongoTemplate.find(query, Lead.class);
    }

    /**
     * Spring Data MongoDB inner interface
     */
    interface LeadMongoRepository extends MongoRepository<Lead, String> {

        Optional<Lead> findByLeadIdAndTenantId(String leadId, String tenantId);

        List<Lead> findAllByTenantId(String tenantId);

        List<Lead> findByTenantIdAndStatus(String tenantId, Lead.LeadStatus status);

        List<Lead> findByTenantIdAndStage(String tenantId, Lead.LeadStage stage);

        List<Lead> findByOwnerIdAndTenantId(String ownerId, String tenantId);

        List<Lead> findByTenantIdAndSource(String tenantId, Lead.LeadSource source);

        List<Lead> findByTenantIdAndQuality(String tenantId, Lead.LeadQuality quality);

        List<Lead> findByTenantIdAndScoreGreaterThanEqual(String tenantId, Integer minScore);

        List<Lead> findByTenantIdAndEmail(String tenantId, String email);

        List<Lead> findByTenantIdAndPhone(String tenantId, String phone);

        List<Lead> findByTenantIdAndCompany(String tenantId, String company);

        List<Lead> findByTenantIdAndLastActivityDateAfter(String tenantId, LocalDate lastActivityDate);

        void deleteByLeadIdAndTenantId(String leadId, String tenantId);

        boolean existsByLeadIdAndTenantId(String leadId, String tenantId);

        long countByTenantIdAndStatus(String tenantId, Lead.LeadStatus status);

        long countByTenantIdAndStage(String tenantId, Lead.LeadStage stage);

        long countByOwnerIdAndTenantId(String ownerId, String tenantId);

        List<Lead> findByTenantIdAndTagListContaining(String tenantId, String tag);

        Page<Lead> findByTenantId(String tenantId, Pageable pageable);
    }
}
