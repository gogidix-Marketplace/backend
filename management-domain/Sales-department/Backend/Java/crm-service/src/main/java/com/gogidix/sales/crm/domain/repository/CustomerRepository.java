package com.gogidix.sales.crm.domain.repository;

import com.gogidix.sales.crm.domain.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    List<Customer> findByTenantId(String tenantId);

    Page<Customer> findByTenantId(String tenantId, Pageable pageable);

    List<Customer> findByTenantIdAndLifecycleStage(String tenantId, Customer.CustomerLifecycleStage stage);

    List<Customer> findByTenantIdAndSegment(String tenantId, Customer.CustomerSegment segment);

    List<Customer> findByTenantIdAndIndustry(String tenantId, String industry);

    List<Customer> findByTenantIdAndOwnerId(String tenantId, String ownerId);

    List<Customer> findByTenantIdAndTerritory(String tenantId, String territory);

    List<Customer> findByTenantIdAndLeadSource(String tenantId, String leadSource);

    List<Customer> findByTenantIdAndIsActive(String tenantId, boolean isActive);

    List<Customer> findByTenantIdAndLifecycleStage(String tenantId, Customer.CustomerLifecycleStage stage, boolean isActive);

    List<Customer> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<Customer> findByTenantIdAndCreatedAtBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Customer> findByTenantIdAndLastContactDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Customer> findByTenantIdAndNextFollowUpDate(String tenantId, LocalDate followUpDate);

    List<Customer> findByTenantIdAndNextFollowUpDateBefore(String tenantId, LocalDate date);

    List<Customer> findByTenantIdAndParentAccountId(String tenantId, String parentAccountId);

    List<Customer> findByTenantIdAndCompanyNameContainingIgnoreCase(String tenantId, String searchTerm);

    Page<Customer> findByTenantIdAndCompanyNameContainingIgnoreCase(String tenantId, String searchTerm, Pageable pageable);

    List<Customer> findByTenantIdAndEmailContainingIgnoreCase(String tenantId, String email);

    boolean existsByAccountNumberAndTenantId(String accountNumber, String tenantId);

    boolean existsByCustomerIdAndTenantId(String customerId, String tenantId);

    void deleteById(String id);

    void deleteByCustomerIdAndTenantId(String customerId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndLifecycleStage(String tenantId, Customer.CustomerLifecycleStage stage);

    long countByTenantIdAndSegment(String tenantId, Customer.CustomerSegment segment);

    Double sumAnnualRevenueByTenantId(String tenantId);

    Double sumAnnualRevenueByTenantIdAndLifecycleStage(String tenantId, Customer.CustomerLifecycleStage stage);
}
