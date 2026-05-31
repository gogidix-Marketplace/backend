package com.gogidix.finance.accountsreceivable.infrastructure.persistence.mongo;

import com.gogidix.finance.accountsreceivable.domain.model.Customer;
import com.gogidix.finance.accountsreceivable.domain.repository.CustomerRepository;
import com.gogidix.finance.accountsreceivable.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
        return customers.stream()
            .map(mongoTemplate::save)
            .toList();
    }

    @Override
    public Optional<Customer> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
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
    public Optional<Customer> findByCustomerCodeAndTenantId(String customerCode, String tenantId) {
        Query query = Query.query(
            Criteria.where("customerCode").is(customerCode)
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
    public List<Customer> findByTenantIdAndCustomerType(String tenantId, Customer.CustomerType customerType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("customerType").is(customerType)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndStatus(String tenantId, Customer.CustomerStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndCollectionStage(String tenantId, Customer.CollectionStage collectionStage) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("collectionStage").is(collectionStage)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndSalesRepresentative(String tenantId, String salesRepresentative) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("salesRepresentative").is(salesRepresentative)
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
    public List<Customer> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("tags").is(tag)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findOverdueCustomersByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("overdueInvoicesCount").gt(0)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndCustomerNameContainingIgnoreCase(String tenantId, String name) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("customerName").regex(name, "i")
        );
        return mongoTemplate.find(query, Customer.class);
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
    public boolean existsByCustomerCodeAndTenantId(String customerCode, String tenantId) {
        Query query = Query.query(
            Criteria.where("customerCode").is(customerCode)
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
    public long countByTenantIdAndStatus(String tenantId, Customer.CustomerStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, Customer.class);
    }

    @Override
    public BigDecimal sumOutstandingBalanceByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        List<Customer> customers = mongoTemplate.find(query, Customer.class);
        return customers.stream()
            .map(Customer::getOutstandingBalance)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Customer> findActiveCustomersWithCreditByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(Customer.CustomerStatus.ACTIVE)
                .and("allowCredit").is(true)
        );
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findByTenantIdAndParentCustomerId(String tenantId, String parentCustomerId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("parentCustomerId").is(parentCustomerId)
        );
        return mongoTemplate.find(query, Customer.class);
    }
}
