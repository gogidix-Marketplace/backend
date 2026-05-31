package com.gogidix.sales.crm.domain.model;

import com.gogidix.sales.crm.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Account Domain Entity
 * Represents the account hierarchy and relationships
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "accounts")
public class Account extends BaseEntity {

    private String accountId;

    private String tenantId;

    private String accountName;

    private String accountNumber;

    private AccountType accountType;

    private String parentAccountId;

    private String parentAccountName;

    private List<String> childAccountIds;

    private List<String> siblingAccountIds;

    private AccountHierarchyLevel hierarchyLevel;

    private String industry;

    private String territory;

    private String ownerId;

    private String ownerName;

    private String website;

    private String description;

    private Double annualRevenue;

    private Integer employeeCount;

    private Integer customerCount;

    private LocalDate establishedDate;

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

    private String taxId;

    private String paymentTerms;

    private String currency;

    private Double creditLimit;

    private Double creditUsed;

    private LocalDate lastPaymentDate;

    private Double totalDealValue;

    private Integer openDealsCount;

    private Integer wonDealsCount;

    private Integer lostDealsCount;

    private LocalDate lastActivityDate;

    private LocalDate nextReviewDate;

    private List<String> tags;

    private String notes;

    private Boolean isActive;

    private LocalDate inactiveSince;

    private String inactiveReason;

    public enum AccountType {
        STRATEGIC,
        ENTERPRISE,
        MID_MARKET,
        SMALL_BUSINESS,
        PARTNER
    }

    public enum AccountHierarchyLevel {
        HEADQUARTERS,
        REGIONAL_OFFICE,
        BRANCH,
        SUBSIDIARY,
        DIVISION,
        DEPARTMENT
    }

    /**
     * Creates a new account
     */
    public static Account create(String tenantId, String accountName, AccountType accountType,
                                   AccountHierarchyLevel hierarchyLevel) {
        return Account.builder()
            .tenantId(tenantId)
            .accountName(accountName)
            .accountType(accountType)
            .hierarchyLevel(hierarchyLevel)
            .isActive(true)
            .childAccountIds(new ArrayList<>())
            .siblingAccountIds(new ArrayList<>())
            .tags(new ArrayList<>())
            .build();
    }

    /**
     * Sets the parent account
     */
    public void setParentAccount(String parentAccountId, String parentAccountName) {
        this.parentAccountId = parentAccountId;
        this.parentAccountName = parentAccountName;
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
     * Removes a child account
     */
    public void removeChildAccount(String childAccountId) {
        if (this.childAccountIds != null) {
            this.childAccountIds.remove(childAccountId);
        }
    }

    /**
     * Adds a sibling account
     */
    public void addSiblingAccount(String siblingAccountId) {
        if (this.siblingAccountIds == null) {
            this.siblingAccountIds = new ArrayList<>();
        }
        if (!this.siblingAccountIds.contains(siblingAccountId)) {
            this.siblingAccountIds.add(siblingAccountId);
        }
    }

    /**
     * Updates credit information
     */
    public void updateCredit(Double creditLimit, Double creditUsed) {
        this.creditLimit = creditLimit;
        this.creditUsed = creditUsed;
    }

    /**
     * Checks if credit is available
     */
    public boolean hasAvailableCredit(Double amount) {
        Double available = (this.creditLimit != null ? this.creditLimit : 0) -
                          (this.creditUsed != null ? this.creditUsed : 0);
        return available >= amount;
    }

    /**
     * Uses credit
     */
    public void useCredit(Double amount) {
        if (!hasAvailableCredit(amount)) {
            throw new IllegalStateException("Insufficient credit available");
        }
        this.creditUsed = (this.creditUsed != null ? this.creditUsed : 0) + amount;
    }

    /**
     * Releases credit
     */
    public void releaseCredit(Double amount) {
        this.creditUsed = Math.max(0, (this.creditUsed != null ? this.creditUsed : 0) - amount);
    }

    /**
     * Updates deal statistics
     */
    public void updateDealStats(Double dealValue, String outcome) {
        if ("WON".equalsIgnoreCase(outcome)) {
            this.wonDealsCount = (this.wonDealsCount != null ? this.wonDealsCount : 0) + 1;
            this.totalDealValue = (this.totalDealValue != null ? this.totalDealValue : 0) + dealValue;
        } else if ("LOST".equalsIgnoreCase(outcome)) {
            this.lostDealsCount = (this.lostDealsCount != null ? this.lostDealsCount : 0) + 1;
        } else if ("OPEN".equalsIgnoreCase(outcome)) {
            this.openDealsCount = (this.openDealsCount != null ? this.openDealsCount : 0) + 1;
        }
    }

    /**
     * Updates activity date
     */
    public void updateLastActivityDate(LocalDate activityDate) {
        this.lastActivityDate = activityDate;
    }

    /**
     * Sets review date
     */
    public void setReviewDate(LocalDate reviewDate) {
        this.nextReviewDate = reviewDate;
    }

    /**
     * Assigns owner
     */
    public void assignOwner(String ownerId, String ownerName, String territory) {
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.territory = territory;
    }

    /**
     * Adds a tag
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
     * Removes a tag
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Deactivates the account
     */
    public void deactivate(String reason) {
        this.isActive = false;
        this.inactiveSince = LocalDate.now();
        this.inactiveReason = reason;
    }

    /**
     * Reactivates the account
     */
    public void reactivate() {
        this.isActive = true;
        this.inactiveSince = null;
        this.inactiveReason = null;
    }

    /**
     * Gets available credit
     */
    public Double getAvailableCredit() {
        Double limit = this.creditLimit != null ? this.creditLimit : 0;
        Double used = this.creditUsed != null ? this.creditUsed : 0;
        return limit - used;
    }

    /**
     * Checks if account is active
     */
    public boolean isAccountActive() {
        return this.isActive != null && this.isActive;
    }

    /**
     * Checks if review is due
     */
    public boolean isReviewDue() {
        return this.nextReviewDate != null &&
               !this.nextReviewDate.isAfter(LocalDate.now());
    }

    /**
     * Gets total customers count
     */
    public Integer getTotalCustomers() {
        return this.customerCount != null ? this.customerCount : 0;
    }
}
