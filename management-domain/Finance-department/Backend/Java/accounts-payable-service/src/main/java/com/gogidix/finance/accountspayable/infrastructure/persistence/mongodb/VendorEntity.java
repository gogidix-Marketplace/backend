package com.gogidix.finance.accountspayable.infrastructure.persistence.mongodb;

import com.gogidix.finance.accountspayable.domain.model.Vendor;
import com.gogidix.finance.accountspayable.domain.event.VendorRegisteredEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "vendors")
public class VendorEntity {

    @Id
    private String id;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Indexed
    @Field("vendor_id")
    private String vendorId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("vendor_code")
    private String vendorCode;

    @Field("vendor_name")
    private String vendorName;

    @Field("vendor_type")
    private String vendorType;

    @Field("tax_id")
    private String taxId;

    @Field("currency")
    private String currency;

    @Field("payment_terms")
    private String paymentTerms;

    @Field("payment_days")
    private Integer paymentDays;

    @Field("contact_person")
    private String contactPerson;

    @Field("email")
    private String email;

    @Field("phone")
    private String phone;

    @Field("website")
    private String website;

    @Field("billing_address")
    private AddressEmbed billingAddress;

    @Field("shipping_address")
    private AddressEmbed shippingAddress;

    @Indexed
    @Field("status")
    private String status;

    @Field("created_by")
    private String createdBy;

    @Field("activated_at")
    private Instant activatedAt;

    @Field("deactivated_at")
    private Instant deactivatedAt;

    @Field("deactivation_reason")
    private String deactivationReason;

    @Field("bank_account_number")
    private String bankAccountNumber;

    @Field("bank_routing_number")
    private String bankRoutingNumber;

    @Field("bank_name")
    private String bankName;

    @Field("bank_account_type")
    private String bankAccountType;

    @Field("credit_limit")
    private LocalDate creditLimit;

    @Field("notes")
    private String notes;

    @Field("tags")
    private List<String> tags;

    @Field("parent_vendor_id")
    private String parentVendorId;

    @Field("is_preferred_vendor")
    private Boolean isPreferredVendor;

    @Field("discount_percentage")
    private Double discountPercentage;

    @Field("valid_from")
    private LocalDate validFrom;

    @Field("valid_until")
    private LocalDate validUntil;

    @Field("domain_events")
    private List<VendorRegisteredEvent> domainEvents;

    public VendorEntity() {
    }

    public VendorEntity(Vendor vendor) {
        this.id = vendor.getId();
        this.createdAt = vendor.getCreatedAt();
        this.updatedAt = vendor.getUpdatedAt();
        this.vendorId = vendor.getVendorId();
        this.tenantId = vendor.getTenantId();
        this.vendorCode = vendor.getVendorCode();
        this.vendorName = vendor.getVendorName();
        this.vendorType = vendor.getVendorType() != null ? vendor.getVendorType().name() : null;
        this.taxId = vendor.getTaxId();
        this.currency = vendor.getCurrency();
        this.paymentTerms = vendor.getPaymentTerms();
        this.paymentDays = vendor.getPaymentDays();
        this.contactPerson = vendor.getContactPerson();
        this.email = vendor.getEmail();
        this.phone = vendor.getPhone();
        this.website = vendor.getWebsite();
        this.billingAddress = vendor.getBillingAddress() != null ? new AddressEmbed(vendor.getBillingAddress()) : null;
        this.shippingAddress = vendor.getShippingAddress() != null ? new AddressEmbed(vendor.getShippingAddress()) : null;
        this.status = vendor.getStatus() != null ? vendor.getStatus().name() : null;
        this.createdBy = vendor.getCreatedBy();
        this.activatedAt = vendor.getActivatedAt();
        this.deactivatedAt = vendor.getDeactivatedAt();
        this.deactivationReason = vendor.getDeactivationReason();
        this.bankAccountNumber = vendor.getBankAccountNumber();
        this.bankRoutingNumber = vendor.getBankRoutingNumber();
        this.bankName = vendor.getBankName();
        this.bankAccountType = vendor.getBankAccountType();
        this.creditLimit = vendor.getCreditLimit();
        this.notes = vendor.getNotes();
        this.tags = vendor.getTags() != null ? new ArrayList<>(vendor.getTags()) : new ArrayList<>();
        this.parentVendorId = vendor.getParentVendorId();
        this.isPreferredVendor = vendor.getIsPreferredVendor();
        this.discountPercentage = vendor.getDiscountPercentage();
        this.validFrom = vendor.getValidFrom();
        this.validUntil = vendor.getValidUntil();
        this.domainEvents = vendor.getDomainEvents() != null ? new ArrayList<>(vendor.getDomainEvents()) : new ArrayList<>();
    }

    public Vendor toDomainModel() {
        Vendor vendor = Vendor.builder()
                .vendorId(this.vendorId)
                .tenantId(this.tenantId)
                .vendorCode(this.vendorCode)
                .vendorName(this.vendorName)
                .vendorType(this.vendorType != null ? Vendor.VendorType.valueOf(this.vendorType) : null)
                .taxId(this.taxId)
                .currency(this.currency)
                .paymentTerms(this.paymentTerms)
                .paymentDays(this.paymentDays)
                .contactPerson(this.contactPerson)
                .email(this.email)
                .phone(this.phone)
                .website(this.website)
                .billingAddress(this.billingAddress != null ? this.billingAddress.toDomainModel() : null)
                .shippingAddress(this.shippingAddress != null ? this.shippingAddress.toDomainModel() : null)
                .status(this.status != null ? Vendor.VendorStatus.valueOf(this.status) : null)
                .createdBy(this.createdBy)
                .activatedAt(this.activatedAt)
                .deactivatedAt(this.deactivatedAt)
                .deactivationReason(this.deactivationReason)
                .bankAccountNumber(this.bankAccountNumber)
                .bankRoutingNumber(this.bankRoutingNumber)
                .bankName(this.bankName)
                .bankAccountType(this.bankAccountType)
                .creditLimit(this.creditLimit)
                .notes(this.notes)
                .tags(this.tags != null ? new ArrayList<>(this.tags) : new ArrayList<>())
                .parentVendorId(this.parentVendorId)
                .isPreferredVendor(this.isPreferredVendor)
                .discountPercentage(this.discountPercentage)
                .validFrom(this.validFrom)
                .validUntil(this.validUntil)
                .domainEvents(this.domainEvents != null ? new ArrayList<>(this.domainEvents) : new ArrayList<>())
                .build();
        vendor.setId(this.id);
        vendor.setCreatedAt(this.createdAt);
        vendor.setUpdatedAt(this.updatedAt);
        return vendor;
    }

    public static class AddressEmbed {
        private String street;
        private String city;
        private String state;
        private String postalCode;
        private String country;
        private String addressLine1;
        private String addressLine2;

        public AddressEmbed() {
        }

        public AddressEmbed(Vendor.Address address) {
            this.street = address.getStreet();
            this.city = address.getCity();
            this.state = address.getState();
            this.postalCode = address.getPostalCode();
            this.country = address.getCountry();
            this.addressLine1 = address.getAddressLine1();
            this.addressLine2 = address.getAddressLine2();
        }

        public Vendor.Address toDomainModel() {
            return Vendor.Address.builder()
                    .street(this.street)
                    .city(this.city)
                    .state(this.state)
                    .postalCode(this.postalCode)
                    .country(this.country)
                    .addressLine1(this.addressLine1)
                    .addressLine2(this.addressLine2)
                    .build();
        }

        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getPostalCode() { return postalCode; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public String getAddressLine1() { return addressLine1; }
        public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
        public String getAddressLine2() { return addressLine2; }
        public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getVendorCode() { return vendorCode; }
    public void setVendorCode(String vendorCode) { this.vendorCode = vendorCode; }
    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }
    public String getVendorType() { return vendorType; }
    public void setVendorType(String vendorType) { this.vendorType = vendorType; }
    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getPaymentTerms() { return paymentTerms; }
    public void setPaymentTerms(String paymentTerms) { this.paymentTerms = paymentTerms; }
    public Integer getPaymentDays() { return paymentDays; }
    public void setPaymentDays(Integer paymentDays) { this.paymentDays = paymentDays; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public AddressEmbed getBillingAddress() { return billingAddress; }
    public void setBillingAddress(AddressEmbed billingAddress) { this.billingAddress = billingAddress; }
    public AddressEmbed getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(AddressEmbed shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public Instant getActivatedAt() { return activatedAt; }
    public void setActivatedAt(Instant activatedAt) { this.activatedAt = activatedAt; }
    public Instant getDeactivatedAt() { return deactivatedAt; }
    public void setDeactivatedAt(Instant deactivatedAt) { this.deactivatedAt = deactivatedAt; }
    public String getDeactivationReason() { return deactivationReason; }
    public void setDeactivationReason(String deactivationReason) { this.deactivationReason = deactivationReason; }
    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }
    public String getBankRoutingNumber() { return bankRoutingNumber; }
    public void setBankRoutingNumber(String bankRoutingNumber) { this.bankRoutingNumber = bankRoutingNumber; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getBankAccountType() { return bankAccountType; }
    public void setBankAccountType(String bankAccountType) { this.bankAccountType = bankAccountType; }
    public LocalDate getCreditLimit() { return creditLimit; }
    public void setCreditLimit(LocalDate creditLimit) { this.creditLimit = creditLimit; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public String getParentVendorId() { return parentVendorId; }
    public void setParentVendorId(String parentVendorId) { this.parentVendorId = parentVendorId; }
    public Boolean getIsPreferredVendor() { return isPreferredVendor; }
    public void setIsPreferredVendor(Boolean isPreferredVendor) { this.isPreferredVendor = isPreferredVendor; }
    public Double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(Double discountPercentage) { this.discountPercentage = discountPercentage; }
    public LocalDate getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDate validFrom) { this.validFrom = validFrom; }
    public LocalDate getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }
    public List<VendorRegisteredEvent> getDomainEvents() { return domainEvents; }
    public void setDomainEvents(List<VendorRegisteredEvent> domainEvents) { this.domainEvents = domainEvents; }
}
