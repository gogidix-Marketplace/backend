package com.gogidix.finance.accountsreceivable.domain.repository;

import com.gogidix.finance.accountsreceivable.domain.model.Customer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Customer Repository Interface (Port)
 * Defines the contract for customer persistence operations
 */
public interface CustomerRepository {

    Customer save(Customer customer);

    List<Customer> saveAll(List<Customer> customers);

    Optional<Customer> findById(String id);

    Optional<Customer> findByCustomerIdAndTenantId(String customerId, String tenantId);

    Optional<Customer> findByCustomerCodeAndTenantId(String customerCode, String tenantId);

    List<Customer> findByTenantId(String tenantId);

    List<Customer> findByTenantIdAndCustomerType(String tenantId, Customer.CustomerType customerType);

    List<Customer> findByTenantIdAndStatus(String tenantId, Customer.CustomerStatus status);

    List<Customer> findByTenantIdAndCollectionStage(String tenantId, Customer.CollectionStage collectionStage);

    List<Customer> findByTenantIdAndSalesRepresentative(String tenantId, String salesRepresentative);

    List<Customer> findByTenantIdAndIndustry(String tenantId, String industry);

    List<Customer> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<Customer> findOverdueCustomersByTenantId(String tenantId);

    List<Customer> findByTenantIdAndCustomerNameContainingIgnoreCase(String tenantId, String name);

    List<Customer> findByTenantIdAndEmailContainingIgnoreCase(String tenantId, String email);

    boolean existsByCustomerCodeAndTenantId(String customerCode, String tenantId);

    void deleteById(String id);

    void deleteByCustomerIdAndTenantId(String customerId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Customer.CustomerStatus status);

    BigDecimal sumOutstandingBalanceByTenantId(String tenantId);

    List<Customer> findActiveCustomersWithCreditByTenantId(String tenantId);

    List<Customer> findByTenantIdAndParentCustomerId(String tenantId, String parentCustomerId);
}
