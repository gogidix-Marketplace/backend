package com.gogidix.finance.accountsreceivable.application.service;

import com.gogidix.finance.accountsreceivable.domain.model.Customer;
import com.gogidix.finance.accountsreceivable.domain.repository.CustomerRepository;
import com.gogidix.finance.accountsreceivable.shared.exception.NotFoundException;
import com.gogidix.finance.accountsreceivable.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Customer Query Service
 * Handles all read operations for customers
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerQueryService {

    private final CustomerRepository customerRepository;

    public Customer getById(String customerId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching customer: {} for tenant: {}", customerId, tenantId);

        return customerRepository.findByCustomerIdAndTenantId(customerId, tenantId)
            .orElseThrow(() -> new NotFoundException("Customer", customerId));
    }

    public Customer getByCode(String customerCode) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching customer by code: {} for tenant: {}", customerCode, tenantId);

        return customerRepository.findByCustomerCodeAndTenantId(customerCode, tenantId)
            .orElseThrow(() -> new NotFoundException("Customer", customerCode));
    }

    public Page<Customer> getByType(String customerType, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching customers by type: {} for tenant: {}", customerType, tenantId);

        Customer.CustomerType typeEnum = Customer.CustomerType.valueOf(customerType);
        List<Customer> customers = customerRepository.findByTenantIdAndCustomerType(tenantId, typeEnum);

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(customers, pageRequest, customers.size());
    }

    public Page<Customer> getByStatus(String status, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching customers by status: {} for tenant: {}", status, tenantId);

        Customer.CustomerStatus statusEnum = Customer.CustomerStatus.valueOf(status);
        List<Customer> customers = customerRepository.findByTenantIdAndStatus(tenantId, statusEnum);

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(customers, pageRequest, customers.size());
    }

    public Page<Customer> getOverdueCustomers(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching overdue customers for tenant: {}", tenantId);

        List<Customer> customers = customerRepository.findOverdueCustomersByTenantId(tenantId);

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(customers, pageRequest, customers.size());
    }

    public Page<Customer> search(String searchTerm, String customerType, String status, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Searching customers for tenant: {} with term: {}", tenantId, searchTerm);

        List<Customer> customers;

        if (searchTerm != null && !searchTerm.isBlank()) {
            customers = customerRepository.findByTenantIdAndCustomerNameContainingIgnoreCase(tenantId, searchTerm);
        } else {
            customers = customerRepository.findByTenantId(tenantId);
        }

        // Filter by type if provided
        if (customerType != null && !customerType.isBlank()) {
            Customer.CustomerType typeEnum = Customer.CustomerType.valueOf(customerType);
            customers = customers.stream()
                .filter(c -> c.getCustomerType() == typeEnum)
                .toList();
        }

        // Filter by status if provided
        if (status != null && !status.isBlank()) {
            Customer.CustomerStatus statusEnum = Customer.CustomerStatus.valueOf(status);
            customers = customers.stream()
                .filter(c -> c.getStatus() == statusEnum)
                .toList();
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(customers, pageRequest, customers.size());
    }

    public List<Customer> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching all customers for tenant: {}", tenantId);

        return customerRepository.findByTenantId(tenantId);
    }

    public List<Customer> getBySalesRepresentative(String salesRepresentative) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching customers by sales rep: {} for tenant: {}", salesRepresentative, tenantId);

        return customerRepository.findByTenantIdAndSalesRepresentative(tenantId, salesRepresentative);
    }

    public List<Customer> getByCollectionStage(String collectionStage) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching customers by collection stage: {} for tenant: {}", collectionStage, tenantId);

        Customer.CollectionStage stageEnum = Customer.CollectionStage.valueOf(collectionStage);
        return customerRepository.findByTenantIdAndCollectionStage(tenantId, stageEnum);
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return customerRepository.countByTenantId(tenantId);
    }

    public long countByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        Customer.CustomerStatus statusEnum = Customer.CustomerStatus.valueOf(status);
        return customerRepository.countByTenantIdAndStatus(tenantId, statusEnum);
    }

    public java.math.BigDecimal sumOutstandingBalance() {
        String tenantId = RequestContextHolder.getTenantId();
        return customerRepository.sumOutstandingBalanceByTenantId(tenantId);
    }
}
