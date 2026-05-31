package com.gogidix.finance.accountspayable.domain.model;

import com.gogidix.finance.accountspayable.domain.event.VendorRegisteredEvent;
import com.gogidix.finance.accountspayable.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Vendor Domain Entity
 * Multi-tenant vendor management for accounts payable
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "vendors")
public class Vendor extends BaseEntity {

    private String vendorId;

    private String tenantId;

    private String vendorCode;

    private String vendorName;

    private VendorType vendorType;

    private String taxId;

    private String currency;

    private String paymentTerms;

    private Integer paymentDays;

    private String contactPerson;

    private String email;

    private String phone;

    private String website;

    private Address billingAddress;

    private Address shippingAddress;

    private VendorStatus status;

    private String createdBy;

    private Instant activatedAt;

    private Instant deactivatedAt;

    private String deactivationReason;

    private String bankAccountNumber;

    private String bankRoutingNumber;

    private String bankName;

    private String bankAccountType;

    private LocalDate creditLimit;

    private String notes;

    private List<String> tags;

    private String parentVendorId;

    private Boolean isPreferredVendor;

    private Double discountPercentage;

    private LocalDate validFrom;

    private LocalDate validUntil;

    @Builder.Default
    private List<VendorRegisteredEvent> domainEvents = new ArrayList<>();

    public enum VendorType {
        INDIVIDUAL,
        CORPORATION,
        PARTNERSHIP,
        LLC,
        NON_PROFIT,
        GOVERNMENT,
        FOREIGN_ENTITY
    }

    public enum VendorStatus {
        ACTIVE,
        INACTIVE,
        PENDING_APPROVAL,
        SUSPENDED,
        BLACKLISTED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String street;
        private String city;
        private String state;
        private String postalCode;
        private String country;
        private String addressLine1;
        private String addressLine2;
    }

    /**
     * Creates a new vendor
     */
    public static Vendor create(String tenantId, String vendorCode, String vendorName,
                                 VendorType vendorType, String taxId, String currency,
                                 Integer paymentDays, String contactPerson, String email,
                                 Address billingAddress, String createdBy) {
        Vendor vendor = Vendor.builder()
            .tenantId(tenantId)
            .vendorCode(vendorCode)
            .vendorName(vendorName)
            .vendorType(vendorType)
            .taxId(taxId)
            .currency(currency)
            .paymentDays(paymentDays)
            .contactPerson(contactPerson)
            .email(email)
            .billingAddress(billingAddress)
            .status(VendorStatus.PENDING_APPROVAL)
            .createdBy(createdBy)
            .tags(new ArrayList<>())
            .isPreferredVendor(false)
            .build();

        vendor.addDomainEvent(VendorRegisteredEvent.builder()
            .vendorId(vendor.getVendorId())
            .tenantId(tenantId)
            .vendorCode(vendorCode)
            .vendorName(vendorName)
            .vendorType(vendorType.name())
            .timestamp(Instant.now())
            .eventType("VENDOR_CREATED")
            .build());

        return vendor;
    }

    /**
     * Updates vendor information
     */
    public void update(String vendorName, String contactPerson, String email,
                       String phone, Address billingAddress) {
        if (this.status == VendorStatus.BLACKLISTED) {
            throw new IllegalStateException("Cannot update blacklisted vendor");
        }

        if (vendorName != null && !vendorName.isBlank()) {
            this.vendorName = vendorName;
        }
        if (contactPerson != null && !contactPerson.isBlank()) {
            this.contactPerson = contactPerson;
        }
        if (email != null && !email.isBlank()) {
            this.email = email;
        }
        if (phone != null) {
            this.phone = phone;
        }
        if (billingAddress != null) {
            this.billingAddress = billingAddress;
        }

        addDomainEvent(VendorRegisteredEvent.builder()
            .vendorId(this.vendorId)
            .tenantId(this.tenantId)
            .vendorCode(this.vendorCode)
            .vendorName(this.vendorName)
            .vendorType(this.vendorType.name())
            .timestamp(Instant.now())
            .eventType("VENDOR_UPDATED")
            .build());
    }

    /**
     * Activates the vendor
     */
    public void activate() {
        if (this.status == VendorStatus.BLACKLISTED) {
            throw new IllegalStateException("Cannot activate blacklisted vendor");
        }

        this.status = VendorStatus.ACTIVE;
        this.activatedAt = Instant.now();

        addDomainEvent(VendorRegisteredEvent.builder()
            .vendorId(this.vendorId)
            .tenantId(this.tenantId)
            .vendorCode(this.vendorCode)
            .vendorName(this.vendorName)
            .vendorType(this.vendorType.name())
            .timestamp(Instant.now())
            .eventType("VENDOR_ACTIVATED")
            .build());
    }

    /**
     * Deactivates the vendor
     */
    public void deactivate(String reason) {
        if (this.status == VendorStatus.INACTIVE) {
            throw new IllegalStateException("Vendor is already inactive");
        }

        this.status = VendorStatus.INACTIVE;
        this.deactivatedAt = Instant.now();
        this.deactivationReason = reason;

        addDomainEvent(VendorRegisteredEvent.builder()
            .vendorId(this.vendorId)
            .tenantId(this.tenantId)
            .vendorCode(this.vendorCode)
            .vendorName(this.vendorName)
            .vendorType(this.vendorType.name())
            .timestamp(Instant.now())
            .eventType("VENDOR_DEACTIVATED")
            .build());
    }

    /**
     * Suspends the vendor
     */
    public void suspend(String reason) {
        if (this.status != VendorStatus.ACTIVE) {
            throw new IllegalStateException("Can only suspend active vendors");
        }

        this.status = VendorStatus.SUSPENDED;

        addDomainEvent(VendorRegisteredEvent.builder()
            .vendorId(this.vendorId)
            .tenantId(this.tenantId)
            .vendorCode(this.vendorCode)
            .vendorName(this.vendorName)
            .vendorType(this.vendorType.name())
            .timestamp(Instant.now())
            .eventType("VENDOR_SUSPENDED")
            .build());
    }

    /**
     * Blacklists the vendor
     */
    public void blacklist(String reason) {
        this.status = VendorStatus.BLACKLISTED;

        addDomainEvent(VendorRegisteredEvent.builder()
            .vendorId(this.vendorId)
            .tenantId(this.tenantId)
            .vendorCode(this.vendorCode)
            .vendorName(this.vendorName)
            .vendorType(this.vendorType.name())
            .timestamp(Instant.now())
            .eventType("VENDOR_BLACKLISTED")
            .build());
    }

    /**
     * Sets as preferred vendor
     */
    public void setPreferred(boolean preferred) {
        if (this.status != VendorStatus.ACTIVE) {
            throw new IllegalStateException("Can only set active vendors as preferred");
        }
        this.isPreferredVendor = preferred;
    }

    /**
     * Updates payment terms
     */
    public void updatePaymentTerms(String paymentTerms, Integer paymentDays) {
        this.paymentTerms = paymentTerms;
        this.paymentDays = paymentDays;
    }

    /**
     * Updates bank information
     */
    public void updateBankInfo(String bankAccountNumber, String bankRoutingNumber,
                               String bankName, String bankAccountType) {
        this.bankAccountNumber = bankAccountNumber;
        this.bankRoutingNumber = bankRoutingNumber;
        this.bankName = bankName;
        this.bankAccountType = bankAccountType;
    }

    /**
     * Adds a tag to the vendor
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
     * Removes a tag from the vendor
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Checks if vendor is active
     */
    public boolean isActive() {
        return this.status == VendorStatus.ACTIVE;
    }

    /**
     * Checks if vendor can receive payments
     */
    public boolean canReceivePayment() {
        return this.status == VendorStatus.ACTIVE &&
               this.bankAccountNumber != null &&
               !this.bankAccountNumber.isBlank();
    }

    public void addDomainEvent(VendorRegisteredEvent event) {
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
