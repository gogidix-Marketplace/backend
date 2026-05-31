package com.gogidix.globalbusinessmanagement.multicurrency.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Domain model representing a multi-currency account.
 * Manages balances across multiple currencies for an entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "multi_currency_accounts")
@CompoundIndex(name = "account_id_currency_idx", def = "{'accountId': 1, 'balances.currency': 1}", unique = false)
public class MultiCurrencyAccount {

    @Id
    private String id;

    @NotBlank(message = "Account ID is required")
    @Indexed
    private String accountId;

    @NotBlank(message = "Account name is required")
    private String accountName;

    @Indexed
    private String ownerType;

    @Indexed
    private String ownerId;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotNull(message = "Balances are required")
    @Valid
    private List<CurrencyBalance> balances;

    @NotNull(message = "Base currency is required")
    @Indexed
    private String baseCurrency;

    @Indexed
    private String baseCurrencyCode;

    private BigDecimal totalBalanceInBase;

    private BigDecimal availableBalanceInBase;

    private BigDecimal frozenBalanceInBase;

    @NotNull(message = "Account status is required")
    @Builder.Default
    private AccountStatus status = AccountStatus.ACTIVE;

    @Indexed
    private Boolean isDefault;

    @Indexed
    private Boolean isPrimary;

    private AccountLimits limits;

    private AccountSettings settings;

    private AccountPreferences preferences;

    private List<AccountTransaction> recentTransactions;

    private Map<String, Object> metadata;

    private String description;

    @Indexed
    private Instant lastActivityAt;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Indexed
    private String region;

    @Indexed
    private String businessUnit;

    private ComplianceInfo complianceInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrencyBalance {
        @NotBlank(message = "Currency code is required")
        private String currency;

        @NotNull(message = "Balance is required")
        private BigDecimal balance;

        @NotNull(message = "Available balance is required")
        private BigDecimal availableBalance;

        private BigDecimal frozenBalance;

        private BigDecimal pendingBalance;

        private BigDecimal creditLimit;

        @Indexed
        private Instant lastUpdatedAt;

        private BalanceStatus status;

        private Boolean isPrimary;

        private Integer displayOrder;

        public enum BalanceStatus {
            ACTIVE,
            FROZEN,
            CLOSED,
            PENDING
        }

        public boolean isActive() {
            return BalanceStatus.ACTIVE.equals(status);
        }

        public boolean hasSufficientFunds(BigDecimal amount) {
            return availableBalance != null && amount != null &&
                availableBalance.compareTo(amount) >= 0;
        }

        public BigDecimal getTotalBalance() {
            BigDecimal total = balance != null ? balance : BigDecimal.ZERO;
            BigDecimal pending = pendingBalance != null ? pendingBalance : BigDecimal.ZERO;
            return total.add(pending);
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountLimits {
        private BigDecimal maxBalancePerCurrency;

        private BigDecimal maxTotalBalance;

        private Integer maxActiveCurrencies;

        private BigDecimal dailyTransferLimit;

        private BigDecimal monthlyTransferLimit;

        private BigDecimal dailyWithdrawalLimit;

        private BigDecimal monthlyWithdrawalLimit;

        private BigDecimal minTransferAmount;

        private Integer maxTransactionsPerDay;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountSettings {
        private Boolean autoConversion;

        private String autoConversionTargetCurrency;

        private BigDecimal autoConversionThreshold;

        private Boolean roundingEnabled;

        private Integer roundingDecimalPlaces;

        private Boolean requireApprovalForTransfers;

        private Boolean allowNegativeBalances;

        private BigDecimal overdraftLimit;

        private Boolean multiSigRequired;

        private Integer requiredApprovals;

        private String timeZone;

        private String notificationEmail;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountPreferences {
        private String defaultDisplayCurrency;

        private Boolean showAllBalances;

        private Boolean hideZeroBalances;

        private String language;

        private String dateFormat;

        private String numberFormat;

        private List<String> favoriteCurrencies;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountTransaction {
        private String transactionId;

        private String type;

        private String fromCurrency;

        private String toCurrency;

        private BigDecimal fromAmount;

        private BigDecimal toAmount;

        private BigDecimal exchangeRate;

        private Instant timestamp;

        private String status;

        private String reference;

        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComplianceInfo {
        private String taxId;

        private String regulatoryJurisdiction;

        private Instant kycVerifiedAt;

        private Instant amlCheckedAt;

        private String riskLevel;

        private Boolean requiresEnhancedDiligence;

        private Map<String, Object> additionalInfo;
    }

    public enum AccountType {
        CHECKING,
        SAVINGS,
        BUSINESS,
        CORPORATE,
        INVESTMENT,
        TRADING,
        ESCROW,
        MULTI_CURRENCY
    }

    public enum AccountStatus {
        ACTIVE,
        INACTIVE,
        FROZEN,
        CLOSED,
        PENDING_APPROVAL,
        SUSPENDED,
        UNDER_REVIEW
    }

    public boolean isActive() {
        return AccountStatus.ACTIVE.equals(status);
    }

    public boolean isDefaultAccount() {
        return Boolean.TRUE.equals(isDefault);
    }

    public CurrencyBalance getBalance(String currencyCode) {
        return balances.stream()
            .filter(b -> b.getCurrency().equals(currencyCode))
            .findFirst()
            .orElse(null);
    }

    public boolean hasBalance(String currencyCode) {
        return balances.stream().anyMatch(b -> b.getCurrency().equals(currencyCode) && b.isActive());
    }

    public BigDecimal getBalanceAmount(String currencyCode) {
        CurrencyBalance balance = getBalance(currencyCode);
        return balance != null ? balance.getBalance() : BigDecimal.ZERO;
    }

    public BigDecimal getAvailableBalanceAmount(String currencyCode) {
        CurrencyBalance balance = getBalance(currencyCode);
        return balance != null ? balance.getAvailableBalance() : BigDecimal.ZERO;
    }

    public boolean hasSufficientFunds(String currencyCode, BigDecimal amount) {
        CurrencyBalance balance = getBalance(currencyCode);
        return balance != null && balance.hasSufficientFunds(amount);
    }

    public void updateBalance(String currencyCode, BigDecimal newBalance) {
        CurrencyBalance balance = getBalance(currencyCode);
        if (balance != null) {
            balance.setBalance(newBalance);
            balance.setLastUpdatedAt(Instant.now());
        }
    }

    public void addCurrency(String currencyCode) {
        if (!hasBalance(currencyCode)) {
            CurrencyBalance newBalance = CurrencyBalance.builder()
                .currency(currencyCode)
                .balance(BigDecimal.ZERO)
                .availableBalance(BigDecimal.ZERO)
                .frozenBalance(BigDecimal.ZERO)
                .status(CurrencyBalance.BalanceStatus.ACTIVE)
                .lastUpdatedAt(Instant.now())
                .build();
            balances.add(newBalance);
        }
    }

    public BigDecimal calculateTotalBalanceInBase(Map<String, BigDecimal> exchangeRates) {
        BigDecimal total = BigDecimal.ZERO;
        for (CurrencyBalance balance : balances) {
            if (balance.isActive() && balance.getBalance() != null) {
                BigDecimal rate = exchangeRates.get(balance.getCurrency());
                if (rate != null && rate.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal converted = balance.getBalance().multiply(rate);
                    total = total.add(converted);
                }
            }
        }
        this.totalBalanceInBase = total.setScale(2, RoundingMode.HALF_UP);
        return this.totalBalanceInBase;
    }

    public List<String> getActiveCurrencies() {
        return balances.stream()
            .filter(CurrencyBalance::isActive)
            .map(CurrencyBalance::getCurrency)
            .toList();
    }

    public long getActiveCurrencyCount() {
        return balances.stream()
            .filter(CurrencyBalance::isActive)
            .count();
    }

    public boolean canAddCurrency() {
        if (limits == null || limits.getMaxActiveCurrencies() == null) {
            return true;
        }
        return getActiveCurrencyCount() < limits.getMaxActiveCurrencies();
    }

    public boolean isFrozen() {
        return AccountStatus.FROZEN.equals(status);
    }

    public boolean isClosed() {
        return AccountStatus.CLOSED.equals(status);
    }

    public boolean canPerformTransactions() {
        return isActive() && !isFrozen();
    }

    public BigDecimal getAvailableBalanceInBase() {
        BigDecimal available = BigDecimal.ZERO;
        for (CurrencyBalance balance : balances) {
            if (balance.isActive() && balance.getAvailableBalance() != null) {
                available = available.add(balance.getAvailableBalance());
            }
        }
        this.availableBalanceInBase = available;
        return available;
    }

    public BigDecimal getFrozenBalanceInBase() {
        BigDecimal frozen = BigDecimal.ZERO;
        for (CurrencyBalance balance : balances) {
            if (balance.getFrozenBalance() != null) {
                frozen = frozen.add(balance.getFrozenBalance());
            }
        }
        this.frozenBalanceInBase = frozen;
        return frozen;
    }
}
