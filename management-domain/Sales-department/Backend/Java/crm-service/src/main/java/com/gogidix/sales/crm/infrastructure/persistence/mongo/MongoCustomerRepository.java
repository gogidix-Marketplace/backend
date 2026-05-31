package com.gogidix.sales.crm.infrastructure.persistence.mongo;

import com.gogidix.sales.crm.domain.model.Customer;
import com.gogidix.sales.crm.domain.repository.CustomerRepository;
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
 * MongoDB Repository Implementation - Customer
 * Implements customer persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoCustomerRepository implements CustomerRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Customer save(Customer customer) {
        log.debug("Saving customer: {} for tenant: {}",
            customer.getCustomerId(), customer.getTenantId());
        return mongoTemplate.save(customer);
    }

    @Override
    public List<Customer> saveAll(List<Customer> customers) {
        return customers.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<Customer> findById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return Optional.ofNullable(mongoTemplate.findOne(query, Customer.class));
    }

    @Override
    public Optional<Customer> findByCustomerIdAndTenantId(String customerId, String tenantId) {
        Query query = Query.query(
            Criteria.where("customerId").is(customerId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Customer.class));
    }

    @Override
    public List<Customer> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public Page<Customer> findByTenantId(String tenantId, Pageable pageable) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        query.with(pageable);
        List<Customer> customers = mongoTemplate.find(query, Customer.class);
        long count = mongoTemplate.count(query, Customer.class);
        return new PageImpl<>(customers, pageable, count);
    }

    @Override
    public List<Customer> findByTenantIdAndLifecycleStage(String tenantId, Customer.CustomerLifecycleStage stage) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("lifecycleStage").is(stage)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndSegment(String tenantId, Customer.CustomerSegment segment) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("segment").is(segment)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndIndustry(String tenantId, String industry) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("industry").is(industry)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndOwnerId(String tenantId, String ownerId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("ownerId").is(ownerId)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndTerritory(String tenantId, String territory) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("territory").is(territory)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndLeadSource(String tenantId, String leadSource) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("leadSource").is(leadSource)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndIsActive(String tenantId, boolean isActive) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("isActive").is(isActive)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndLifecycleStage(String tenantId, Customer.CustomerLifecycleStage stage,
                                                           boolean isActive) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("lifecycleStage").is(stage)
                .and("isActive").is(isActive)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("tags").is(tag)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndCreatedAtBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("createdAt").gte(start).lte(end)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndLastContactDateBetween(String tenantId, LocalDate startDate,
                                                                   LocalDate endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("lastContactDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndNextFollowUpDate(String tenantId, LocalDate followUpDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("nextFollowUpDate").is(followUpDate)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndNextFollowUpDateBefore(String tenantId, LocalDate date) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("nextFollowUpDate").lte(date)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndParentAccountId(String tenantId, String parentAccountId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("parentAccountId").is(parentAccountId)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndCompanyNameContainingIgnoreCase(String tenantId, String searchTerm) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("companyName").regex(searchTerm, "i")
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public Page<Customer> findByTenantIdAndCompanyNameContainingIgnoreCase(String tenantId, String searchTerm, Pageable pageable) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("companyName").regex(searchTerm, "i")
        ).with(pageable);
        List<Customer> customers = mongoTemplate.find(query, Customer.class);
        long total = mongoTemplate.count(Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("companyName").regex(searchTerm, "i")
        ), Customer.class);
        return new org.springframework.data.domain.PageImpl<>(customers, pageable, total);
    }

    @Override
    public List<Customer> findByTenantIdAndEmailContainingIgnoreCase(String tenantId, String email) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("email").regex(email, "i")
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public boolean existsByAccountNumberAndTenantId(String accountNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("accountNumber").is(accountNumber)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Customer.class);
    }

    @Override
    public boolean existsByCustomerIdAndTenantId(String customerId, String tenantId) {
        Query query = Query.query(
            Criteria.where("customerId").is(customerId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Customer.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Customer.class);
    }

    @Override
    public void deleteByCustomerIdAndTenantId(String customerId, String tenantId) {
        Query query = Query.query(
            Criteria.where("customerId").is(customerId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Customer.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Customer.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Customer.class);
    }

    @Override
    public long countByTenantIdAndLifecycleStage(String tenantId, Customer.CustomerLifecycleStage stage) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("lifecycleStage").is(stage)
        );
        return mongoTemplate.count(query, Customer.class);
    }

    @Override
    public long countByTenantIdAndSegment(String tenantId, Customer.CustomerSegment segment) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("segment").is(segment)
        );
        return mongoTemplate.count(query, Customer.class);
    }

    @Override
    public Double sumAnnualRevenueByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        List<Customer> customers = mongoTemplate.find(query, Customer.class);
        return customers.stream()
            .map(Customer::getAnnualRevenue)
            .filter(revenue -> revenue != null)
            .reduce(0.0, Double::sum);
    }

    @Override
    public Double sumAnnualRevenueByTenantIdAndLifecycleStage(String tenantId,
                                                               Customer.CustomerLifecycleStage stage) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("lifecycleStage").is(stage)
        );
        List<Customer> customers = mongoTemplate.find(query, Customer.class);
        return customers.stream()
            .map(Customer::getAnnualRevenue)
            .filter(revenue -> revenue != null)
            .reduce(0.0, Double::sum);
    }
}
