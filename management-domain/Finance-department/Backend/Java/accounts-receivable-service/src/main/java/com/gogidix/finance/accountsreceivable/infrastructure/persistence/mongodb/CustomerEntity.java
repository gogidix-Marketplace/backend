package com.gogidix.finance.accountsreceivable.infrastructure.persistence.mongodb;

import com.gogidix.finance.accountsreceivable.domain.event.CustomerRegisteredEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "ar_customers")
public class CustomerEntity {

    @Id
    private String id;

    @Indexed
    @Field("customer_id")
    private String customerId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("customer_code")
    private String customerCode;

    @Field("customer_name")
    private String customerName;

    @Field("customer_type")
    private String customerType;

    @Field("email")
    private String email;

    @Field("phone")
    private String phone;

    @Field("website")
    private String website;

    @Field("tax_id")
    private String taxId;

    @Field("tax_registration_number")
    private String taxRegistrationNumber;

    @Field("billing_address_line1")
    private String billingAddressLine1;

    @Field("billing_address_line2")
    private String billingAddressLine2;

    @Field("billing_city")
    private String billingCity;

    @Field("billing_state")
    private String billingState;

    @Field("billing_postal_code")
    private String billingPostalCode;

    @Field("billing_country")
    private String billingCountry;

    @Field("shipping_address_line1")
    private String shippingAddressLine1;

    @Field("shipping_address_line2")
    private String shippingAddressLine2;

    @Field("shipping_city")
    private String shippingCity;

    @Field("shipping_state")
    private String shippingState;

    @Field("shipping_postal_code")
    private String shippingPostalCode;

    @Field("shipping_country")
    private String shippingCountry;

    @Field("currency")
    private String currency;

    @Field("payment_terms")
    private String paymentTerms;

    @Field("credit_limit")
    private Integer creditLimit;

    @Field("credit_days")
    private Integer creditDays;

    @Field("sales_representative")
    private String salesRepresentative;

    @Field("customer_since")
    private String customerSince;

    @Indexed
    @Field("status")
    private String status;

    @Field("industry")
    private String industry;

    @Field("notes")
    private String notes;

    @Field("default_payment_method")
    private String defaultPaymentMethod;

    @Field("bank_account_number")
    private String bankAccountNumber;

    @Field("bank_name")
    private String bankName;

    @Field("bank_routing_number")
    private String bankRoutingNumber;

    @Field("allow_credit")
    private Boolean allowCredit;

    @Field("send_electronic_invoices")
    private Boolean sendElectronicInvoices;

    @Field("invoice_delivery_email")
    private String invoiceDeliveryEmail;

    @Field("parent_customer_id")
    private String parentCustomerId;

    @Field("is_parent_customer")
    private Boolean isParentCustomer;

    @Field("child_customer_ids")
    private List<String> childCustomerIds;

    @Field("outstanding_balance")
    private BigDecimal outstandingBalance;

    @Field("credit_used")
    private BigDecimal creditUsed;

    @Field("available_credit")
    private BigDecimal availableCredit;

    @Field("overdue_invoices_count")
    private Integer overdueInvoicesCount;

    @Field("last_payment_date")
    private Instant lastPaymentDate;

    @Field("last_invoice_date")
    private Instant lastInvoiceDate;

    @Field("total_purchases")
    private BigDecimal totalPurchases;

    @Field("total_invoices_issued")
    private Integer totalInvoicesIssued;

    @Field("assigned_collector")
    private String assignedCollector;

    @Field("collection_stage")
    private String collectionStage;

    @Field("domain_events")
    private List<CustomerRegisteredEvent> domainEvents;

    @Field("tags")
    private List<String> tags;

    @Field("payment_gateway_customer_id")
    private String paymentGatewayCustomerId;

    @Field("auto_charge_payment_method")
    private Boolean autoChargePaymentMethod;

    public CustomerEntity() {
    }

    public CustomerEntity(com.gogidix.finance.accountsreceivable.domain.model.Customer customer) {
        this.customerId = customer.getCustomerId();
        this.tenantId = customer.getTenantId();
        this.customerCode = customer.getCustomerCode();
        this.customerName = customer.getCustomerName();
        this.customerType = customer.getCustomerType() != null ? customer.getCustomerType().name() : null;
        this.email = customer.getEmail();
        this.phone = customer.getPhone();
        this.website = customer.getWebsite();
        this.taxId = customer.getTaxId();
        this.taxRegistrationNumber = customer.getTaxRegistrationNumber();
        this.billingAddressLine1 = customer.getBillingAddressLine1();
        this.billingAddressLine2 = customer.getBillingAddressLine2();
        this.billingCity = customer.getBillingCity();
        this.billingState = customer.getBillingState();
        this.billingPostalCode = customer.getBillingPostalCode();
        this.billingCountry = customer.getBillingCountry();
        this.shippingAddressLine1 = customer.getShippingAddressLine1();
        this.shippingAddressLine2 = customer.getShippingAddressLine2();
        this.shippingCity = customer.getShippingCity();
        this.shippingState = customer.getShippingState();
        this.shippingPostalCode = customer.getShippingPostalCode();
        this.shippingCountry = customer.getShippingCountry();
        this.currency = customer.getCurrency();
        this.paymentTerms = customer.getPaymentTerms();
        this.creditLimit = customer.getCreditLimit();
        this.creditDays = customer.getCreditDays();
        this.salesRepresentative = customer.getSalesRepresentative();
        this.customerSince = customer.getCustomerSince();
        this.status = customer.getStatus() != null ? customer.getStatus().name() : null;
        this.industry = customer.getIndustry();
        this.notes = customer.getNotes();
        this.defaultPaymentMethod = customer.getDefaultPaymentMethod();
        this.bankAccountNumber = customer.getBankAccountNumber();
        this.bankName = customer.getBankName();
        this.bankRoutingNumber = customer.getBankRoutingNumber();
        this.allowCredit = customer.getAllowCredit();
        this.sendElectronicInvoices = customer.getSendElectronicInvoices();
        this.invoiceDeliveryEmail = customer.getInvoiceDeliveryEmail();
        this.parentCustomerId = customer.getParentCustomerId();
        this.isParentCustomer = customer.getIsParentCustomer();
        this.childCustomerIds = customer.getChildCustomerIds() != null ? new ArrayList<>(customer.getChildCustomerIds()) : new ArrayList<>();
        this.outstandingBalance = customer.getOutstandingBalance();
        this.creditUsed = customer.getCreditUsed();
        this.availableCredit = customer.getAvailableCredit();
        this.overdueInvoicesCount = customer.getOverdueInvoicesCount();
        this.lastPaymentDate = customer.getLastPaymentDate();
        this.lastInvoiceDate = customer.getLastInvoiceDate();
        this.totalPurchases = customer.getTotalPurchases();
        this.totalInvoicesIssued = customer.getTotalInvoicesIssued();
        this.assignedCollector = customer.getAssignedCollector();
        this.collectionStage = customer.getCollectionStage() != null ? customer.getCollectionStage().name() : null;
        this.domainEvents = customer.getDomainEvents() != null ? new ArrayList<>(customer.getDomainEvents()) : new ArrayList<>();
        this.tags = customer.getTags() != null ? new ArrayList<>(customer.getTags()) : new ArrayList<>();
        this.paymentGatewayCustomerId = customer.getPaymentGatewayCustomerId();
        this.autoChargePaymentMethod = customer.getAutoChargePaymentMethod();
    }

    public com.gogidix.finance.accountsreceivable.domain.model.Customer toDomainModel() {
        return com.gogidix.finance.accountsreceivable.domain.model.Customer.builder()
                .customerId(this.customerId)
                .tenantId(this.tenantId)
                .customerCode(this.customerCode)
                .customerName(this.customerName)
                .customerType(this.customerType != null ? com.gogidix.finance.accountsreceivable.domain.model.Customer.CustomerType.valueOf(this.customerType) : null)
                .email(this.email)
                .phone(this.phone)
                .website(this.website)
                .taxId(this.taxId)
                .taxRegistrationNumber(this.taxRegistrationNumber)
                .billingAddressLine1(this.billingAddressLine1)
                .billingAddressLine2(this.billingAddressLine2)
                .billingCity(this.billingCity)
                .billingState(this.billingState)
                .billingPostalCode(this.billingPostalCode)
                .billingCountry(this.billingCountry)
                .shippingAddressLine1(this.shippingAddressLine1)
                .shippingAddressLine2(this.shippingAddressLine2)
                .shippingCity(this.shippingCity)
                .shippingState(this.shippingState)
                .shippingPostalCode(this.shippingPostalCode)
                .shippingCountry(this.shippingCountry)
                .currency(this.currency)
                .paymentTerms(this.paymentTerms)
                .creditLimit(this.creditLimit)
                .creditDays(this.creditDays)
                .salesRepresentative(this.salesRepresentative)
                .customerSince(this.customerSince)
                .status(this.status != null ? com.gogidix.finance.accountsreceivable.domain.model.Customer.CustomerStatus.valueOf(this.status) : null)
                .industry(this.industry)
                .notes(this.notes)
                .defaultPaymentMethod(this.defaultPaymentMethod)
                .bankAccountNumber(this.bankAccountNumber)
                .bankName(this.bankName)
                .bankRoutingNumber(this.bankRoutingNumber)
                .allowCredit(this.allowCredit)
                .sendElectronicInvoices(this.sendElectronicInvoices)
                .invoiceDeliveryEmail(this.invoiceDeliveryEmail)
                .parentCustomerId(this.parentCustomerId)
                .isParentCustomer(this.isParentCustomer)
                .childCustomerIds(this.childCustomerIds != null ? new ArrayList<>(this.childCustomerIds) : new ArrayList<>())
                .outstandingBalance(this.outstandingBalance)
                .creditUsed(this.creditUsed)
                .availableCredit(this.availableCredit)
                .overdueInvoicesCount(this.overdueInvoicesCount)
                .lastPaymentDate(this.lastPaymentDate)
                .lastInvoiceDate(this.lastInvoiceDate)
                .totalPurchases(this.totalPurchases)
                .totalInvoicesIssued(this.totalInvoicesIssued)
                .assignedCollector(this.assignedCollector)
                .collectionStage(this.collectionStage != null ? com.gogidix.finance.accountsreceivable.domain.model.Customer.CollectionStage.valueOf(this.collectionStage) : null)
                .domainEvents(this.domainEvents != null ? new ArrayList<>(this.domainEvents) : new ArrayList<>())
                .tags(this.tags != null ? new ArrayList<>(this.tags) : new ArrayList<>())
                .paymentGatewayCustomerId(this.paymentGatewayCustomerId)
                .autoChargePaymentMethod(this.autoChargePaymentMethod)
                .build();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerType() { return customerType; }
    public void setCustomerType(String customerType) { this.customerType = customerType; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
    public String getTaxRegistrationNumber() { return taxRegistrationNumber; }
    public void setTaxRegistrationNumber(String taxRegistrationNumber) { this.taxRegistrationNumber = taxRegistrationNumber; }
    public String getBillingAddressLine1() { return billingAddressLine1; }
    public void setBillingAddressLine1(String billingAddressLine1) { this.billingAddressLine1 = billingAddressLine1; }
    public String getBillingAddressLine2() { return billingAddressLine2; }
    public void setBillingAddressLine2(String billingAddressLine2) { this.billingAddressLine2 = billingAddressLine2; }
    public String getBillingCity() { return billingCity; }
    public void setBillingCity(String billingCity) { this.billingCity = billingCity; }
    public String getBillingState() { return billingState; }
    public void setBillingState(String billingState) { this.billingState = billingState; }
    public String getBillingPostalCode() { return billingPostalCode; }
    public void setBillingPostalCode(String billingPostalCode) { this.billingPostalCode = billingPostalCode; }
    public String getBillingCountry() { return billingCountry; }
    public void setBillingCountry(String billingCountry) { this.billingCountry = billingCountry; }
    public String getShippingAddressLine1() { return shippingAddressLine1; }
    public void setShippingAddressLine1(String shippingAddressLine1) { this.shippingAddressLine1 = shippingAddressLine1; }
    public String getShippingAddressLine2() { return shippingAddressLine2; }
    public void setShippingAddressLine2(String shippingAddressLine2) { this.shippingAddressLine2 = shippingAddressLine2; }
    public String getShippingCity() { return shippingCity; }
    public void setShippingCity(String shippingCity) { this.shippingCity = shippingCity; }
    public String getShippingState() { return shippingState; }
    public void setShippingState(String shippingState) { this.shippingState = shippingState; }
    public String getShippingPostalCode() { return shippingPostalCode; }
    public void setShippingPostalCode(String shippingPostalCode) { this.shippingPostalCode = shippingPostalCode; }
    public String getShippingCountry() { return shippingCountry; }
    public void setShippingCountry(String shippingCountry) { this.shippingCountry = shippingCountry; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getPaymentTerms() { return paymentTerms; }
    public void setPaymentTerms(String paymentTerms) { this.paymentTerms = paymentTerms; }
    public Integer getCreditLimit() { return creditLimit; }
    public void setCreditLimit(Integer creditLimit) { this.creditLimit = creditLimit; }
    public Integer getCreditDays() { return creditDays; }
    public void setCreditDays(Integer creditDays) { this.creditDays = creditDays; }
    public String getSalesRepresentative() { return salesRepresentative; }
    public void setSalesRepresentative(String salesRepresentative) { this.salesRepresentative = salesRepresentative; }
    public String getCustomerSince() { return customerSince; }
    public void setCustomerSince(String customerSince) { this.customerSince = customerSince; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getDefaultPaymentMethod() { return defaultPaymentMethod; }
    public void setDefaultPaymentMethod(String defaultPaymentMethod) { this.defaultPaymentMethod = defaultPaymentMethod; }
    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getBankRoutingNumber() { return bankRoutingNumber; }
    public void setBankRoutingNumber(String bankRoutingNumber) { this.bankRoutingNumber = bankRoutingNumber; }
    public Boolean getAllowCredit() { return allowCredit; }
    public void setAllowCredit(Boolean allowCredit) { this.allowCredit = allowCredit; }
    public Boolean getSendElectronicInvoices() { return sendElectronicInvoices; }
    public void setSendElectronicInvoices(Boolean sendElectronicInvoices) { this.sendElectronicInvoices = sendElectronicInvoices; }
    public String getInvoiceDeliveryEmail() { return invoiceDeliveryEmail; }
    public void setInvoiceDeliveryEmail(String invoiceDeliveryEmail) { this.invoiceDeliveryEmail = invoiceDeliveryEmail; }
    public String getParentCustomerId() { return parentCustomerId; }
    public void setParentCustomerId(String parentCustomerId) { this.parentCustomerId = parentCustomerId; }
    public Boolean getIsParentCustomer() { return isParentCustomer; }
    public void setIsParentCustomer(Boolean isParentCustomer) { this.isParentCustomer = isParentCustomer; }
    public List<String> getChildCustomerIds() { return childCustomerIds; }
    public void setChildCustomerIds(List<String> childCustomerIds) { this.childCustomerIds = childCustomerIds; }
    public BigDecimal getOutstandingBalance() { return outstandingBalance; }
    public void setOutstandingBalance(BigDecimal outstandingBalance) { this.outstandingBalance = outstandingBalance; }
    public BigDecimal getCreditUsed() { return creditUsed; }
    public void setCreditUsed(BigDecimal creditUsed) { this.creditUsed = creditUsed; }
    public BigDecimal getAvailableCredit() { return availableCredit; }
    public void setAvailableCredit(BigDecimal availableCredit) { this.availableCredit = availableCredit; }
    public Integer getOverdueInvoicesCount() { return overdueInvoicesCount; }
    public void setOverdueInvoicesCount(Integer overdueInvoicesCount) { this.overdueInvoicesCount = overdueInvoicesCount; }
    public Instant getLastPaymentDate() { return lastPaymentDate; }
    public void setLastPaymentDate(Instant lastPaymentDate) { this.lastPaymentDate = lastPaymentDate; }
    public Instant getLastInvoiceDate() { return lastInvoiceDate; }
    public void setLastInvoiceDate(Instant lastInvoiceDate) { this.lastInvoiceDate = lastInvoiceDate; }
    public BigDecimal getTotalPurchases() { return totalPurchases; }
    public void setTotalPurchases(BigDecimal totalPurchases) { this.totalPurchases = totalPurchases; }
    public Integer getTotalInvoicesIssued() { return totalInvoicesIssued; }
    public void setTotalInvoicesIssued(Integer totalInvoicesIssued) { this.totalInvoicesIssued = totalInvoicesIssued; }
    public String getAssignedCollector() { return assignedCollector; }
    public void setAssignedCollector(String assignedCollector) { this.assignedCollector = assignedCollector; }
    public String getCollectionStage() { return collectionStage; }
    public void setCollectionStage(String collectionStage) { this.collectionStage = collectionStage; }
    public List<CustomerRegisteredEvent> getDomainEvents() { return domainEvents; }
    public void setDomainEvents(List<CustomerRegisteredEvent> domainEvents) { this.domainEvents = domainEvents; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public String getPaymentGatewayCustomerId() { return paymentGatewayCustomerId; }
    public void setPaymentGatewayCustomerId(String paymentGatewayCustomerId) { this.paymentGatewayCustomerId = paymentGatewayCustomerId; }
    public Boolean getAutoChargePaymentMethod() { return autoChargePaymentMethod; }
    public void setAutoChargePaymentMethod(Boolean autoChargePaymentMethod) { this.autoChargePaymentMethod = autoChargePaymentMethod; }
}
