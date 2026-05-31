package com.gogidix.hr.employeeselfservice.domain.model;

import com.gogidix.hr.employeeselfservice.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Bank Details Domain Entity
 * Multi-tenant bank account management for payroll
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "bank_details")
public class BankDetails extends BaseEntity {

    private String tenantId;

    private String employeeId;

    private String bankName;

    private String bankCode;

    private String branchName;

    private String accountNumber;

    private AccountType accountType;

    private String routingNumber;

    private String swiftCode;

    private String iban;

    private String currency;

    private Boolean primaryAccount = false;

    private Boolean verified = false;

    private LocalDate verifiedDate;

    private String verifiedBy;

    private String accountNickname;

    private String bankAddress;

    private String accountHolderName;

    private Boolean active;

    private LocalDate validFrom;

    private LocalDate validUntil;

    public enum AccountType {
        SAVINGS,
        CHECKING,
        CURRENT,
        SALARY
    }

    /**
     * Creates a new bank detail entry
     */
    public static BankDetails create(String tenantId, String employeeId,
                                      String bankName, String accountNumber,
                                      AccountType accountType, String currency,
                                      String accountHolderName) {
        BankDetails details = new BankDetails();
        details.setTenantId(tenantId);
        details.setEmployeeId(employeeId);
        details.setBankName(bankName);
        details.setAccountNumber(accountNumber);
        details.setAccountType(accountType);
        details.setCurrency(currency);
        details.setAccountHolderName(accountHolderName);
        details.setPrimaryAccount(false);
        details.setVerified(false);
        details.setActive(true);
        return details;
    }

    /**
     * Updates bank information
     */
    public void updateBankInfo(String bankName, String bankCode, String branchName,
                                String accountNumber, AccountType accountType,
                                String routingNumber, String swiftCode, String iban) {
        if (bankName != null && !bankName.isBlank()) {
            this.bankName = bankName;
        }
        this.bankCode = bankCode;
        this.branchName = branchName;
        if (accountNumber != null && !accountNumber.isBlank()) {
            this.accountNumber = accountNumber;
            this.verified = false;
        }
        if (accountType != null) {
            this.accountType = accountType;
        }
        this.routingNumber = routingNumber;
        this.swiftCode = swiftCode;
        this.iban = iban;
    }

    /**
     * Sets this account as primary
     */
    public void setAsPrimary() {
        this.primaryAccount = true;
    }

    /**
     * Removes primary status from this account
     */
    public void removeAsPrimary() {
        this.primaryAccount = false;
    }

    /**
     * Verifies the bank account
     */
    public void verify(String verifiedBy) {
        this.verified = true;
        this.verifiedDate = LocalDate.now();
        this.verifiedBy = verifiedBy;
    }

    /**
     * Unverifies the bank account (used when details change)
     */
    public void unverify() {
        this.verified = false;
        this.verifiedDate = null;
        this.verifiedBy = null;
    }

    /**
     * Activates the bank account
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Deactivates the bank account
     */
    public void deactivate() {
        this.active = false;
        this.primaryAccount = false;
    }

    /**
     * Checks if the bank account is active
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(this.active);
    }

    /**
     * Checks if the bank account is verified
     */
    public boolean isVerified() {
        return Boolean.TRUE.equals(this.verified);
    }

    /**
     * Checks if the bank account is primary
     */
    public boolean isPrimary() {
        return Boolean.TRUE.equals(this.primaryAccount);
    }

    /**
     * Checks if the bank account is valid
     */
    public boolean isValid() {
        if (!isActive()) {
            return false;
        }
        LocalDate now = LocalDate.now();
        if (validFrom != null && now.isBefore(validFrom)) {
            return false;
        }
        if (validUntil != null && now.isAfter(validUntil)) {
            return false;
        }
        return true;
    }

    /**
     * Sets the validity period
     */
    public void setValidityPeriod(LocalDate validFrom, LocalDate validUntil) {
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    /**
     * Updates the account nickname
     */
    public void updateNickname(String nickname) {
        this.accountNickname = nickname;
    }

    /**
     * Updates the account holder name
     */
    public void updateAccountHolderName(String holderName) {
        if (holderName != null && !holderName.isBlank()) {
            this.accountHolderName = holderName;
            this.verified = false;
        }
    }

    /**
     * Gets the masked account number for display
     */
    public String getMaskedAccountNumber() {
        if (accountNumber == null || accountNumber.length() < 4) {
            return "****";
        }
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }

    /**
     * Checks if this account can be used for payroll
     */
    public boolean canBeUsedForPayroll() {
        return isActive() && isVerified() && isPrimary();
    }

    /**
     * Sets the currency for the account
     */
    public void setAccountCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Gets the bank identifier (either bank code or swift code)
     */
    public String getBankIdentifier() {
        if (bankCode != null && !bankCode.isBlank()) {
            return bankCode;
        }
        if (swiftCode != null && !swiftCode.isBlank()) {
            return swiftCode;
        }
        return bankName;
    }

    /**
     * Updates the bank address
     */
    public void updateBankAddress(String address) {
        this.bankAddress = address;
    }
}
