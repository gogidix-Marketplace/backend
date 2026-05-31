package com.gogidix.finance.currency.domain.model;

import com.gogidix.finance.currency.domain.event.CurrencyActivatedEvent;
import com.gogidix.finance.currency.domain.event.CurrencyCreatedEvent;
import com.gogidix.finance.currency.domain.event.CurrencyDeactivatedEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Domain Entity - Currency
 * Multi-tenant SaaS entity with mandatory tenantId field
 * ZERO framework dependencies - pure domain logic
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"domainEvents"})
@Document(collection = "currencies")
public class Currency extends BaseEntity {

    @Indexed(unique = true)
    @Field("currency_code")
    private String currencyCode;

    @Field("name")
    private String name;

    @Field("symbol")
    private String symbol;

    @Field("decimal_places")
    private int decimalPlaces;

    @Field("iso_numeric_code")
    private String isoNumericCode;

    @Field("status")
    private CurrencyStatus status;

    @Field("country_codes")
    private List<String> countryCodes;

    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    public enum CurrencyStatus {
        ACTIVE,
        INACTIVE,
        PENDING_ACTIVATION,
        SUSPENDED
    }

    /**
     * Creates a new currency
     */
    public static Currency create(String tenantId, String currencyCode, String name,
                                  String symbol, int decimalPlaces, String isoNumericCode,
                                  List<String> countryCodes) {
        Currency currency = Currency.builder()
            .currencyCode(currencyCode)
            .name(name)
            .symbol(symbol)
            .decimalPlaces(decimalPlaces)
            .isoNumericCode(isoNumericCode)
            .status(CurrencyStatus.ACTIVE)
            .countryCodes(countryCodes)
            .domainEvents(new ArrayList<>())
            .build();
        currency.tenantId = tenantId;

        currency.addDomainEvent(new CurrencyCreatedEvent(tenantId, currencyCode, name));

        return currency;
    }

    /**
     * Activates the currency
     */
    public void activate() {
        if (this.status == CurrencyStatus.ACTIVE) {
            throw new IllegalStateException("Currency is already active");
        }

        this.status = CurrencyStatus.ACTIVE;
        this.updateTimestamp();

        addDomainEvent(new CurrencyActivatedEvent(
            this.tenantId,
            this.id,
            this.currencyCode
        ));
    }

    /**
     * Deactivates the currency
     */
    public void deactivate() {
        if (this.status == CurrencyStatus.INACTIVE) {
            throw new IllegalStateException("Currency is already inactive");
        }

        this.status = CurrencyStatus.INACTIVE;
        this.updateTimestamp();

        addDomainEvent(new CurrencyDeactivatedEvent(
            this.tenantId,
            this.id,
            this.currencyCode
        ));
    }

    /**
     * Update currency details
     */
    public void updateDetails(String name, String symbol, int decimalPlaces, String isoNumericCode) {
        this.name = name;
        this.symbol = symbol;
        this.decimalPlaces = decimalPlaces;
        this.isoNumericCode = isoNumericCode;
        this.updateTimestamp();
    }

    /**
     * Validate currency code format
     */
    public void validateCurrencyCode() {
        if (currencyCode == null || currencyCode.length() != 3) {
            throw new IllegalArgumentException("Currency code must be exactly 3 characters (ISO 4217)");
        }
        if (!currencyCode.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Currency code must contain only uppercase letters");
        }
    }

    /**
     * Check if currency is active
     */
    public boolean isActive() {
        return status == CurrencyStatus.ACTIVE;
    }

    /**
     * Add a domain event
     */
    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    /**
     * Clear domain events
     */
    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }

    /**
     * Get uncommitted events
     */
    public List<Object> getUncommittedEvents() {
        return domainEvents != null ? new ArrayList<>(domainEvents) : List.of();
    }

    /**
     * Mark events as committed
     */
    public void markEventsAsCommitted() {
        clearDomainEvents();
    }

    /**
     * Check if has uncommitted events
     */
    public boolean hasUncommittedEvents() {
        return domainEvents != null && !domainEvents.isEmpty();
    }

    // Public setters for inherited fields (to make them accessible)
    public void setCurrencyId(String id) {
        this.id = id;
    }

    public void setCurrencyTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public void setCurrencyCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setCurrencyUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
