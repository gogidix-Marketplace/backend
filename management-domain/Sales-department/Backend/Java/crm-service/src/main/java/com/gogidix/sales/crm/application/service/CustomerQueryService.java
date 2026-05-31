package com.gogidix.sales.crm.application.service;

import com.gogidix.sales.crm.domain.model.Customer;
import com.gogidix.sales.crm.domain.port.in.CustomerQuery;
import com.gogidix.sales.crm.domain.repository.CustomerRepository;
import com.gogidix.sales.crm.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Customer Query Service
 * Handles all read operations for customers
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerQueryService implements CustomerQuery {

    private final CustomerRepository customerRepository;

    @Override
    public Customer getById(String customerId) {
        log.debug("Getting customer by id: {}", customerId);
        return customerRepository.findById(customerId)
            .orElseThrow(() -> new NotFoundException("Customer", customerId));
    }

    @Override
    public Customer getByCustomerIdAndTenantId(String customerId, String tenantId) {
        log.debug("Getting customer: {} for tenant: {}", customerId, tenantId);
        return customerRepository.findByCustomerIdAndTenantId(customerId, tenantId)
            .orElseThrow(() -> new NotFoundException("Customer", customerId));
    }

    @Override
    public List<Customer> getAllForTenant(String tenantId) {
        log.debug("Getting all customers for tenant: {}", tenantId);
        return customerRepository.findByTenantId(tenantId);
    }

    @Override
    public Page<Customer> getPaginatedForTenant(String tenantId, Pageable pageable) {
        log.debug("Getting paginated customers for tenant: {}", tenantId);
        return customerRepository.findByTenantId(tenantId, pageable);
    }

    @Override
    public List<Customer> getByLifecycleStage(String tenantId, Customer.CustomerLifecycleStage stage) {
        log.debug("Getting customers by lifecycle stage: {} for tenant: {}", stage, tenantId);
        return customerRepository.findByTenantIdAndLifecycleStage(tenantId, stage);
    }

    @Override
    public List<Customer> getBySegment(String tenantId, Customer.CustomerSegment segment) {
        log.debug("Getting customers by segment: {} for tenant: {}", segment, tenantId);
        return customerRepository.findByTenantIdAndSegment(tenantId, segment);
    }

    @Override
    public List<Customer> getByIndustry(String tenantId, String industry) {
        log.debug("Getting customers by industry: {} for tenant: {}", industry, tenantId);
        return customerRepository.findByTenantIdAndIndustry(tenantId, industry);
    }

    @Override
    public List<Customer> getByOwner(String tenantId, String ownerId) {
        log.debug("Getting customers by owner: {} for tenant: {}", ownerId, tenantId);
        return customerRepository.findByTenantIdAndOwnerId(tenantId, ownerId);
    }

    @Override
    public List<Customer> getByTerritory(String tenantId, String territory) {
        log.debug("Getting customers by territory: {} for tenant: {}", territory, tenantId);
        return customerRepository.findByTenantIdAndTerritory(tenantId, territory);
    }

    @Override
    public List<Customer> getByLeadSource(String tenantId, String leadSource) {
        log.debug("Getting customers by lead source: {} for tenant: {}", leadSource, tenantId);
        return customerRepository.findByTenantIdAndLeadSource(tenantId, leadSource);
    }

    @Override
    public List<Customer> getActiveCustomers(String tenantId) {
        log.debug("Getting active customers for tenant: {}", tenantId);
        return customerRepository.findByTenantIdAndIsActive(tenantId, true);
    }

    @Override
    public List<Customer> getChurnedCustomers(String tenantId) {
        log.debug("Getting churned customers for tenant: {}", tenantId);
        return customerRepository.findByTenantIdAndLifecycleStage(
            tenantId, Customer.CustomerLifecycleStage.CHURNED);
    }

    @Override
    public List<Customer> getByTag(String tenantId, String tag) {
        log.debug("Getting customers by tag: {} for tenant: {}", tag, tenantId);
        return customerRepository.findByTenantIdAndTagsContaining(tenantId, tag);
    }

    @Override
    public List<Customer> getByCreatedDateRange(String tenantId, LocalDate startDate, LocalDate endDate) {
        log.debug("Getting customers by created date range: {} to {} for tenant: {}",
            startDate, endDate, tenantId);
        return customerRepository.findByTenantIdAndCreatedAtBetween(tenantId, startDate, endDate);
    }

    @Override
    public List<Customer> getByLastContactDateRange(String tenantId, LocalDate startDate, LocalDate endDate) {
        log.debug("Getting customers by last contact date range: {} to {} for tenant: {}",
            startDate, endDate, tenantId);
        return customerRepository.findByTenantIdAndLastContactDateBetween(tenantId, startDate, endDate);
    }

    @Override
    public List<Customer> getByNextFollowUpDate(String tenantId, LocalDate followUpDate) {
        log.debug("Getting customers by next follow-up date: {} for tenant: {}", followUpDate, tenantId);
        return customerRepository.findByTenantIdAndNextFollowUpDate(tenantId, followUpDate);
    }

    @Override
    public List<Customer> getCustomersNeedingFollowUp(String tenantId, LocalDate beforeDate) {
        log.debug("Getting customers needing follow-up before: {} for tenant: {}", beforeDate, tenantId);
        return customerRepository.findByTenantIdAndNextFollowUpDateBefore(tenantId, beforeDate);
    }

    @Override
    public List<Customer> getByParentAccount(String tenantId, String parentAccountId) {
        log.debug("Getting customers by parent account: {} for tenant: {}", parentAccountId, tenantId);
        return customerRepository.findByTenantIdAndParentAccountId(tenantId, parentAccountId);
    }

    @Override
    public List<Customer> searchByName(String tenantId, String searchTerm) {
        log.debug("Searching customers by name: {} for tenant: {}", searchTerm, tenantId);
        return customerRepository.findByTenantIdAndCompanyNameContainingIgnoreCase(tenantId, searchTerm);
    }

    @Override
    public List<Customer> searchByEmail(String tenantId, String email) {
        log.debug("Searching customers by email: {} for tenant: {}", email, tenantId);
        return customerRepository.findByTenantIdAndEmailContainingIgnoreCase(tenantId, email);
    }

    @Override
    public CustomerSummary getSummary(String tenantId) {
        log.debug("Getting customer summary for tenant: {}", tenantId);

        long totalCustomers = customerRepository.countByTenantId(tenantId);
        long totalLeads = customerRepository.countByTenantIdAndLifecycleStage(
            tenantId, Customer.CustomerLifecycleStage.LEAD);
        long totalProspects = customerRepository.countByTenantIdAndLifecycleStage(
            tenantId, Customer.CustomerLifecycleStage.PROSPECT);
        long totalActiveCustomers = customerRepository.countByTenantIdAndLifecycleStage(
            tenantId, Customer.CustomerLifecycleStage.CUSTOMER);
        long totalChurnedCustomers = customerRepository.countByTenantIdAndLifecycleStage(
            tenantId, Customer.CustomerLifecycleStage.CHURNED);
        Double totalRevenue = customerRepository.sumAnnualRevenueByTenantId(tenantId);

        List<SegmentCount> segmentCounts = new ArrayList<>();
        for (Customer.CustomerSegment segment : Customer.CustomerSegment.values()) {
            long count = customerRepository.countByTenantIdAndSegment(tenantId, segment);
            segmentCounts.add(new SegmentCount(segment.name(), count));
        }

        List<LifecycleStageCount> lifecycleStageCounts = new ArrayList<>();
        for (Customer.CustomerLifecycleStage stage : Customer.CustomerLifecycleStage.values()) {
            long count = customerRepository.countByTenantIdAndLifecycleStage(tenantId, stage);
            lifecycleStageCounts.add(new LifecycleStageCount(stage.name(), count));
        }

        return new CustomerSummary(
            totalCustomers,
            totalLeads,
            totalProspects,
            totalActiveCustomers,
            totalChurnedCustomers,
            totalRevenue != null ? totalRevenue : 0.0,
            segmentCounts,
            lifecycleStageCounts
        );
    }

    @Override
    public Page<Customer> searchCustomers(String tenantId, String searchTerm, Pageable pageable) {
        log.debug("Searching customers: {} for tenant: {}", searchTerm, tenantId);
        return customerRepository.findByTenantIdAndCompanyNameContainingIgnoreCase(
            tenantId, searchTerm, pageable);
    }

    @Override
    public boolean existsByCustomerNumberAndTenantId(String accountNumber, String tenantId) {
        return customerRepository.existsByAccountNumberAndTenantId(accountNumber, tenantId);
    }
}
