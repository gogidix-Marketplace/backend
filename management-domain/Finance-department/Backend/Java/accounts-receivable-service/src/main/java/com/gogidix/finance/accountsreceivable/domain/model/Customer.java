package com.gogidix.finance.accountsreceivable.domain.model;

import com.gogidix.finance.accountsreceivable.domain.event.CustomerRegisteredEvent;
import com.gogidix.finance.accountsreceivable.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Customer Domain Entity
 * Multi-tenant customer management for accounts receivable
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ar_customers")
public class Customer extends BaseEntity {

    private String customerId;

    private String tenantId;

    private String customerCode;

    private String customerName;

    private CustomerType customerType;

    private String email;

    private String phone;

    private String website;

    private String taxId;

    private String taxRegistrationNumber;

    private String billingAddressLine1;

    private String billingAddressLine2;

    private String billingCity;

    private String billingState;

    private String billingPostalCode;

    private String billingCountry;

    private String shippingAddressLine1;

    private String shippingAddressLine2;

    private String shippingCity;

    private String shippingState;

    private String shippingPostalCode;

    private String shippingCountry;

    private String currency;

    private String paymentTerms;

    private Integer creditLimit;

    private Integer creditDays;

    private String salesRepresentative;

    private String customerSince;

    private CustomerStatus status;

    private String industry;

    private String notes;

    private String defaultPaymentMethod;

    private String bankAccountNumber;

    private String bankName;

    private String bankRoutingNumber;

    private Boolean allowCredit;

    private Boolean sendElectronicInvoices;

    private String invoiceDeliveryEmail;

    private String parentCustomerId;

    private Boolean isParentCustomer;

    private List<String> childCustomerIds;

    private BigDecimal outstandingBalance;

    private BigDecimal creditUsed;

    private BigDecimal availableCredit;

    private Integer overdueInvoicesCount;

    private Instant lastPaymentDate;

    private Instant lastInvoiceDate;

    private BigDecimal totalPurchases;

    private Integer totalInvoicesIssued;

    private String assignedCollector;

    private CollectionStage collectionStage;

    @Builder.Default
    private List<CustomerRegisteredEvent> domainEvents = new ArrayList<>();

    private List<String> tags;

    private String paymentGatewayCustomerId;

    private Boolean autoChargePaymentMethod;

    public enum CustomerType {
        INDIVIDUAL,
        BUSINESS,
        GOVERNMENT,
        NON_PROFIT,
        RESALE,
        INTERNATIONAL
    }

    public enum CustomerStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED,
        ON_HOLD,
        PENDING_APPROVAL,
        BLOCKED
    }

    public enum CollectionStage {
        CURRENT,
        SOON_DUE,
        OVERDUE_1_30,
        OVERDUE_31_60,
        OVERDUE_61_90,
        OVERDUE_90_PLUS,
        COLLECTIONS
    }

    /**
     * Creates a new customer
     */
    public static Customer create(String tenantId, String customerCode, String customerName,
                                   CustomerType customerType, String currency, String email) {
        Customer customer = Customer.builder()
            .tenantId(tenantId)
            .customerCode(customerCode)
            .customerName(customerName)
            .customerType(customerType)
            .currency(currency)
            .email(email)
            .status(CustomerStatus.ACTIVE)
            .allowCredit(true)
            .creditLimit(0)
            .creditDays(30)
            .outstandingBalance(BigDecimal.ZERO)
            .creditUsed(BigDecimal.ZERO)
            .availableCredit(BigDecimal.ZERO)
            .overdueInvoicesCount(0)
            .totalPurchases(BigDecimal.ZERO)
            .totalInvoicesIssued(0)
            .collectionStage(CollectionStage.CURRENT)
            .sendElectronicInvoices(false)
            .isParentCustomer(false)
            .childCustomerIds(new ArrayList<>())
            .tags(new ArrayList<>())
            .build();

        customer.addDomainEvent(CustomerRegisteredEvent.builder()
            .customerId(customer.getCustomerId())
            .tenantId(tenantId)
            .customerCode(customerCode)
            .customerName(customerName)
            .customerType(customerType.name())
            .eventType("CUSTOMER_CREATED")
            .timestamp(Instant.now())
            .build());

        return customer;
    }

    /**
     * Activates customer
     */
    public void activate() {
        if (this.status == CustomerStatus.ACTIVE) {
            throw new IllegalStateException("Customer is already active");
        }
        this.status = CustomerStatus.ACTIVE;
        addDomainEvent(CustomerRegisteredEvent.builder()
            .customerId(this.customerId)
            .tenantId(this.tenantId)
            .customerCode(this.customerCode)
            .customerName(this.customerName)
            .customerType(this.customerType.name())
            .eventType("CUSTOMER_ACTIVATED")
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Suspends customer
     */
    public void suspend(String reason) {
        if (this.status == CustomerStatus.SUSPENDED) {
            throw new IllegalStateException("Customer is already suspended");
        }
        this.status = CustomerStatus.SUSPENDED;
        this.notes = (this.notes != null ? this.notes + "\n" : "") + "Suspended: " + reason;

        addDomainEvent(CustomerRegisteredEvent.builder()
            .customerId(this.customerId)
            .tenantId(this.tenantId)
            .customerCode(this.customerCode)
            .customerName(this.customerName)
            .customerType(this.customerType.name())
            .eventType("CUSTOMER_SUSPENDED")
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Updates credit information
     */
    public void updateCreditInfo(Integer creditLimit, Integer creditDays) {
        this.creditLimit = creditLimit;
        this.creditDays = creditDays;
        this.allowCredit = creditLimit > 0;
        calculateAvailableCredit();
    }

    /**
     * Updates outstanding balance
     */
    public void updateOutstandingBalance(BigDecimal amount) {
        this.outstandingBalance = this.outstandingBalance.add(amount);
        this.creditUsed = this.outstandingBalance;
        calculateAvailableCredit();
        updateCollectionStage();
    }

    /**
     * Records payment
     */
    public void recordPayment(BigDecimal amount) {
        this.lastPaymentDate = Instant.now();
        updateOutstandingBalance(amount.negate());
    }

    /**
     * Records invoice
     */
    public void recordInvoice(BigDecimal amount) {
        this.lastInvoiceDate = Instant.now();
        this.totalInvoicesIssued++;
        this.totalPurchases = this.totalPurchases.add(amount);
        updateOutstandingBalance(amount);
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
     * Checks if customer can be charged
     */
    public boolean canCharge(BigDecimal amount) {
        if (!this.allowCredit || this.status != CustomerStatus.ACTIVE) {
            return false;
        }
        return this.availableCredit.compareTo(amount) >= 0;
    }

    public void calculateAvailableCredit() {
        if (this.creditLimit == null || this.creditLimit == 0) {
            this.availableCredit = BigDecimal.ZERO;
        } else {
            this.availableCredit = new BigDecimal(this.creditLimit).subtract(this.creditUsed);
        }
    }

    private void updateCollectionStage() {
        if (this.overdueInvoicesCount == 0) {
            this.collectionStage = CollectionStage.CURRENT;
        } else if (this.overdueInvoicesCount <= 2) {
            this.collectionStage = CollectionStage.OVERDUE_1_30;
        } else if (this.overdueInvoicesCount <= 5) {
            this.collectionStage = CollectionStage.OVERDUE_31_60;
        } else if (this.overdueInvoicesCount <= 10) {
            this.collectionStage = CollectionStage.OVERDUE_61_90;
        } else {
            this.collectionStage = CollectionStage.OVERDUE_90_PLUS;
        }
    }

    public void addDomainEvent(CustomerRegisteredEvent event) {
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
