package com.gogidix.sales.crm.domain.model;

import com.gogidix.sales.crm.domain.event.CustomerCreatedEvent;
import com.gogidix.sales.crm.domain.event.CustomerUpdatedEvent;
import com.gogidix.sales.crm.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Customer Domain Entity
 * Multi-tenant customer management with lifecycle stages and segmentation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "contacts")
@Document(collection = "customers")
public class Customer extends BaseEntity {

    private String customerId;

    private String tenantId;

    private String accountNumber;

    private String companyName;

    private String industry;

    private CustomerSegment segment;

    private CustomerLifecycleStage lifecycleStage;

    private String website;

    private String description;

    private Integer employeeCount;

    private Double annualRevenue;

    private String leadSource;

    private LocalDate leadDate;

    private LocalDate convertedDate;

    private String ownerId;

    private String ownerName;

    private String territory;

    private String billingAddressStreet;

    private String billingAddressCity;

    private String billingAddressState;

    private String billingAddressPostalCode;

    private String billingAddressCountry;

    private String shippingAddressStreet;

    private String shippingAddressCity;

    private String shippingAddressState;

    private String shippingAddressPostalCode;

    private String shippingAddressCountry;

    private String phoneNumber;

    private String email;

    private Boolean isActive;

    private LocalDate lastContactDate;

    private LocalDate nextFollowUpDate;

    private Integer totalInteractions;

    private Double totalDealValue;

    private Integer openDealsCount;

    private String parentAccountId;

    private List<String> childAccountIds;

    private AccountType accountType;

    private String taxId;

    private String paymentTerms;

    private String currency;

    private Double creditLimit;

    private List<String> tags;

    private String notes;

    private Integer satisfactionScore;

    private LocalDate churnDate;

    private String churnReason;

    @Builder.Default
    @Transient
    private List<Object> domainEvents = new ArrayList<>();

    @DBRef(lazy = true)
    @Builder.Default
    private List<Contact> contacts = new ArrayList<>();

    public enum CustomerSegment {
        ENTERPRISE,
        MID_MARKET,
        SMALL_BUSINESS,
        STARTUP
    }

    public enum CustomerLifecycleStage {
        LEAD,
        PROSPECT,
        QUALIFIED_LEAD,
        OPPORTUNITY,
        CUSTOMER,
        CHURNED
    }

    public enum AccountType {
        STRATEGIC,
        ENTERPRISE,
        MID_MARKET,
        SMALL_BUSINESS,
        PARTNER
    }

    /**
     * Creates a new customer
     */
    public static Customer create(String tenantId, String createdBy, String companyName,
                                   String industry, CustomerSegment segment,
                                   CustomerLifecycleStage lifecycleStage, String leadSource) {
        Customer customer = Customer.builder()
            .tenantId(tenantId)
            .companyName(companyName)
            .industry(industry)
            .segment(segment)
            .lifecycleStage(lifecycleStage)
            .leadSource(leadSource)
            .isActive(true)
            .contacts(new ArrayList<>())
            .childAccountIds(new ArrayList<>())
            .tags(new ArrayList<>())
            .build();

        if (lifecycleStage == CustomerLifecycleStage.LEAD) {
            customer.setLeadDate(LocalDate.now());
        }

        customer.addDomainEvent(CustomerCreatedEvent.builder()
            .customerId(customer.getCustomerId())
            .tenantId(tenantId)
            .companyName(companyName)
            .segment(segment != null ? segment.name() : null)
            .lifecycleStage(lifecycleStage.name())
            .createdBy(createdBy)
            .timestamp(Instant.now())
            .eventType("CUSTOMER_CREATED")
            .build());

        return customer;
    }

    /**
     * Updates customer information
     */
    public void updateCustomer(String updatedBy, String companyName, String industry,
                                CustomerSegment segment, String description, String website,
                                Integer employeeCount, Double annualRevenue) {
        this.companyName = companyName;
        this.industry = industry;
        this.segment = segment;
        this.description = description;
        this.website = website;
        this.employeeCount = employeeCount;
        this.annualRevenue = annualRevenue;

        addDomainEvent(CustomerUpdatedEvent.builder()
            .customerId(this.customerId)
            .tenantId(this.tenantId)
            .companyName(companyName)
            .segment(segment != null ? segment.name() : null)
            .updatedBy(updatedBy)
            .timestamp(Instant.now())
            .eventType("CUSTOMER_UPDATED")
            .build());
    }

    /**
     * Advances the customer lifecycle stage
     */
    public void advanceLifecycleStage(CustomerLifecycleStage newStage, String updatedBy) {
        if (!canAdvanceTo(newStage)) {
            throw new IllegalStateException(
                "Cannot advance from " + this.lifecycleStage + " to " + newStage);
        }

        CustomerLifecycleStage previousStage = this.lifecycleStage;
        this.lifecycleStage = newStage;

        if (newStage == CustomerLifecycleStage.CUSTOMER && this.convertedDate == null) {
            this.convertedDate = LocalDate.now();
        }

        if (newStage == CustomerLifecycleStage.CHURNED) {
            this.isActive = false;
            this.churnDate = LocalDate.now();
        }

        addDomainEvent(CustomerUpdatedEvent.builder()
            .customerId(this.customerId)
            .tenantId(this.tenantId)
            .companyName(this.companyName)
            .previousStage(previousStage.name())
            .newStage(newStage.name())
            .updatedBy(updatedBy)
            .timestamp(Instant.now())
            .eventType("LIFECYCLE_STAGE_CHANGED")
            .build());
    }

    /**
     * Adds a contact to the customer
     */
    public void addContact(Contact contact) {
        if (this.contacts == null) {
            this.contacts = new ArrayList<>();
        }
        this.contacts.add(contact);
    }

    /**
     * Removes a contact from the customer
     */
    public void removeContact(String contactId) {
        if (this.contacts != null) {
            this.contacts.removeIf(c -> c.getContactId().equals(contactId));
        }
    }

    /**
     * Assigns an owner to the customer
     */
    public void assignOwner(String ownerId, String ownerName, String territory) {
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        if (territory != null) {
            this.territory = territory;
        }
    }

    /**
     * Updates the last contact date
     */
    public void updateLastContactDate(LocalDate contactDate) {
        this.lastContactDate = contactDate;
        if (this.totalInteractions == null) {
            this.totalInteractions = 0;
        }
        this.totalInteractions++;
    }

    /**
     * Sets the next follow-up date
     */
    public void setFollowUpDate(LocalDate followUpDate) {
        this.nextFollowUpDate = followUpDate;
    }

    /**
     * Adds a tag to the customer
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Removes a tag from the customer
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Marks customer as churned
     */
    public void markAsChurned(String reason, String updatedBy) {
        if (this.lifecycleStage == CustomerLifecycleStage.CHURNED) {
            throw new IllegalStateException("Customer is already marked as churned");
        }

        this.lifecycleStage = CustomerLifecycleStage.CHURNED;
        this.isActive = false;
        this.churnDate = LocalDate.now();
        this.churnReason = reason;

        addDomainEvent(CustomerUpdatedEvent.builder()
            .customerId(this.customerId)
            .tenantId(this.tenantId)
            .companyName(this.companyName)
            .churnReason(reason)
            .updatedBy(updatedBy)
            .timestamp(Instant.now())
            .eventType("CUSTOMER_CHURNED")
            .build());
    }

    /**
     * Reactivates a churned customer
     */
    public void reactivate(String updatedBy) {
        if (this.lifecycleStage != CustomerLifecycleStage.CHURNED) {
            throw new IllegalStateException("Can only reactivate churned customers");
        }

        this.lifecycleStage = CustomerLifecycleStage.CUSTOMER;
        this.isActive = true;
        this.churnDate = null;
        this.churnReason = null;

        addDomainEvent(CustomerUpdatedEvent.builder()
            .customerId(this.customerId)
            .tenantId(this.tenantId)
            .companyName(this.companyName)
            .updatedBy(updatedBy)
            .timestamp(Instant.now())
            .eventType("CUSTOMER_REACTIVATED")
            .build());
    }

    /**
     * Updates deal statistics
     */
    public void updateDealStats(Double dealValue, Integer openDeals) {
        if (dealValue != null) {
            this.totalDealValue = (this.totalDealValue == null ? 0 : this.totalDealValue) + dealValue;
        }
        if (openDeals != null) {
            this.openDealsCount = openDeals;
        }
    }

    /**
     * Adds a child account
     */
    public void addChildAccount(String childAccountId) {
        if (this.childAccountIds == null) {
            this.childAccountIds = new ArrayList<>();
        }
        if (!this.childAccountIds.contains(childAccountId)) {
            this.childAccountIds.add(childAccountId);
        }
    }

    /**
     * Sets the parent account
     */
    public void setParentAccount(String parentAccountId) {
        this.parentAccountId = parentAccountId;
    }

    private boolean canAdvanceTo(CustomerLifecycleStage newStage) {
        return getStageOrder(newStage) > getStageOrder(this.lifecycleStage);
    }

    private int getStageOrder(CustomerLifecycleStage stage) {
        return switch (stage) {
            case LEAD -> 1;
            case PROSPECT -> 2;
            case QUALIFIED_LEAD -> 3;
            case OPPORTUNITY -> 4;
            case CUSTOMER -> 5;
            case CHURNED -> 6;
        };
    }

    @SuppressWarnings("unchecked")
    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
