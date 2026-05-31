package com.gogidix.sales.crm.domain.port.in;

import com.gogidix.sales.crm.domain.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Customer Query (Input Port)
 * Defines the query operations for customers
 */
public interface CustomerQuery {

    Customer getById(String customerId);

    Customer getByCustomerIdAndTenantId(String customerId, String tenantId);

    List<Customer> getAllForTenant(String tenantId);

    Page<Customer> getPaginatedForTenant(String tenantId, Pageable pageable);

    List<Customer> getByLifecycleStage(String tenantId, Customer.CustomerLifecycleStage stage);

    List<Customer> getBySegment(String tenantId, Customer.CustomerSegment segment);

    List<Customer> getByIndustry(String tenantId, String industry);

    List<Customer> getByOwner(String tenantId, String ownerId);

    List<Customer> getByTerritory(String tenantId, String territory);

    List<Customer> getByLeadSource(String tenantId, String leadSource);

    List<Customer> getActiveCustomers(String tenantId);

    List<Customer> getChurnedCustomers(String tenantId);

    List<Customer> getByTag(String tenantId, String tag);

    List<Customer> getByCreatedDateRange(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Customer> getByLastContactDateRange(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Customer> getByNextFollowUpDate(String tenantId, LocalDate followUpDate);

    List<Customer> getCustomersNeedingFollowUp(String tenantId, LocalDate beforeDate);

    List<Customer> getByParentAccount(String tenantId, String parentAccountId);

    List<Customer> searchByName(String tenantId, String searchTerm);

    List<Customer> searchByEmail(String tenantId, String email);

    CustomerSummary getSummary(String tenantId);

    Page<Customer> searchCustomers(String tenantId, String searchTerm, Pageable pageable);

    boolean existsByCustomerNumberAndTenantId(String accountNumber, String tenantId);

    /**
     * Customer Summary DTO
     */
    class CustomerSummary {
        private Long totalCustomers;
        private Long totalLeads;
        private Long totalProspects;
        private Long totalActiveCustomers;
        private Long totalChurnedCustomers;
        private Double totalRevenue;
        private List<SegmentCount> segmentCounts;
        private List<LifecycleStageCount> lifecycleStageCounts;

        public CustomerSummary() {}

        public CustomerSummary(Long totalCustomers, Long totalLeads, Long totalProspects,
                               Long totalActiveCustomers, Long totalChurnedCustomers,
                               Double totalRevenue, List<SegmentCount> segmentCounts,
                               List<LifecycleStageCount> lifecycleStageCounts) {
            this.totalCustomers = totalCustomers;
            this.totalLeads = totalLeads;
            this.totalProspects = totalProspects;
            this.totalActiveCustomers = totalActiveCustomers;
            this.totalChurnedCustomers = totalChurnedCustomers;
            this.totalRevenue = totalRevenue;
            this.segmentCounts = segmentCounts;
            this.lifecycleStageCounts = lifecycleStageCounts;
        }

        // Getters and Setters
        public Long getTotalCustomers() { return totalCustomers; }
        public void setTotalCustomers(Long totalCustomers) { this.totalCustomers = totalCustomers; }
        public Long getTotalLeads() { return totalLeads; }
        public void setTotalLeads(Long totalLeads) { this.totalLeads = totalLeads; }
        public Long getTotalProspects() { return totalProspects; }
        public void setTotalProspects(Long totalProspects) { this.totalProspects = totalProspects; }
        public Long getTotalActiveCustomers() { return totalActiveCustomers; }
        public void setTotalActiveCustomers(Long totalActiveCustomers) { this.totalActiveCustomers = totalActiveCustomers; }
        public Long getTotalChurnedCustomers() { return totalChurnedCustomers; }
        public void setTotalChurnedCustomers(Long totalChurnedCustomers) { this.totalChurnedCustomers = totalChurnedCustomers; }
        public Double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }
        public List<SegmentCount> getSegmentCounts() { return segmentCounts; }
        public void setSegmentCounts(List<SegmentCount> segmentCounts) { this.segmentCounts = segmentCounts; }
        public List<LifecycleStageCount> getLifecycleStageCounts() { return lifecycleStageCounts; }
        public void setLifecycleStageCounts(List<LifecycleStageCount> lifecycleStageCounts) { this.lifecycleStageCounts = lifecycleStageCounts; }
    }

    class SegmentCount {
        private String segment;
        private Long count;

        public SegmentCount(String segment, Long count) {
            this.segment = segment;
            this.count = count;
        }

        public String getSegment() { return segment; }
        public Long getCount() { return count; }
    }

    class LifecycleStageCount {
        private String stage;
        private Long count;

        public LifecycleStageCount(String stage, Long count) {
            this.stage = stage;
            this.count = count;
        }

        public String getStage() { return stage; }
        public Long getCount() { return count; }
    }
}
