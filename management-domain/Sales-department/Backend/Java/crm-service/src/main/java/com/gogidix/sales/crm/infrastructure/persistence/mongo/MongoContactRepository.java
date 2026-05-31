package com.gogidix.sales.crm.infrastructure.persistence.mongo;

import com.gogidix.sales.crm.domain.model.Contact;
import com.gogidix.sales.crm.domain.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Contact
 * Implements contact persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoContactRepository implements ContactRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Contact save(Contact contact) {
        log.debug("Saving contact: {} for tenant: {}",
            contact.getContactId(), contact.getTenantId());
        return mongoTemplate.save(contact);
    }

    @Override
    public List<Contact> saveAll(List<Contact> contacts) {
        return contacts.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<Contact> findById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return Optional.ofNullable(mongoTemplate.findOne(query, Contact.class));
    }

    @Override
    public Optional<Contact> findByContactIdAndTenantId(String contactId, String tenantId) {
        Query query = Query.query(
            Criteria.where("contactId").is(contactId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Contact.class));
    }

    @Override
    public List<Contact> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Contact.class);
    }

    @Override
    public List<Contact> findByCustomerIdAndTenantId(String customerId, String tenantId) {
        Query query = Query.query(
            Criteria.where("customerId").is(customerId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Contact.class);
    }

    @Override
    public List<Contact> findByTenantIdAndCustomerId(String tenantId, String customerId) {
        return findByCustomerIdAndTenantId(customerId, tenantId);
    }

    @Override
    public List<Contact> findByTenantIdAndIsActive(String tenantId, boolean isActive) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("isActive").is(isActive)
        );
        return mongoTemplate.find(query, Contact.class);
    }

    @Override
    public List<Contact> findByTenantIdAndIsPrimary(String tenantId, boolean isPrimary) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("isPrimary").is(isPrimary)
        );
        return mongoTemplate.find(query, Contact.class);
    }

    @Override
    public List<Contact> findByTenantIdAndIsDecisionMaker(String tenantId, boolean isDecisionMaker) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("isDecisionMaker").is(isDecisionMaker)
        );
        return mongoTemplate.find(query, Contact.class);
    }

    @Override
    public List<Contact> findByTenantIdAndContactType(String tenantId, Contact.ContactType contactType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("contactType").is(contactType)
        );
        return mongoTemplate.find(query, Contact.class);
    }

    @Override
    public List<Contact> findByTenantIdAndEmailContainingIgnoreCase(String tenantId, String email) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("email").regex(email, "i")
        );
        return mongoTemplate.find(query, Contact.class);
    }

    @Override
    public List<Contact> findByTenantIdAndFullNameContainingIgnoreCase(String tenantId, String name) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("fullName").regex(name, "i")
        );
        return mongoTemplate.find(query, Contact.class);
    }

    @Override
    public List<Contact> findByTenantIdAndTitleContainingIgnoreCase(String tenantId, String title) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("title").regex(title, "i")
        );
        return mongoTemplate.find(query, Contact.class);
    }

    @Override
    public List<Contact> findByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("department").is(department)
        );
        return mongoTemplate.find(query, Contact.class);
    }

    @Override
    public List<Contact> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("tags").is(tag)
        );
        return mongoTemplate.find(query, Contact.class);
    }

    @Override
    public List<Contact> findByCustomerIdAndTenantIdAndIsPrimary(String customerId, String tenantId,
                                                                  boolean isPrimary) {
        Query query = Query.query(
            Criteria.where("customerId").is(customerId)
                .and("tenantId").is(tenantId)
                .and("isPrimary").is(isPrimary)
        );
        return mongoTemplate.find(query, Contact.class);
    }

    @Override
    public boolean existsByContactIdAndTenantId(String contactId, String tenantId) {
        Query query = Query.query(
            Criteria.where("contactId").is(contactId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Contact.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Contact.class);
    }

    @Override
    public void deleteByContactIdAndTenantId(String contactId, String tenantId) {
        Query query = Query.query(
            Criteria.where("contactId").is(contactId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Contact.class);
    }

    @Override
    public void deleteAllByCustomerIdAndTenantId(String customerId, String tenantId) {
        Query query = Query.query(
            Criteria.where("customerId").is(customerId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Contact.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Contact.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Contact.class);
    }

    @Override
    public long countByCustomerIdAndTenantId(String customerId, String tenantId) {
        Query query = Query.query(
            Criteria.where("customerId").is(customerId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.count(query, Contact.class);
    }

    @Override
    public long countByTenantIdAndIsActive(String tenantId, boolean isActive) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("isActive").is(isActive)
        );
        return mongoTemplate.count(query, Contact.class);
    }
}
