package com.gogidix.finance.currency.infrastructure.persistence.mongodb;

import com.gogidix.finance.currency.domain.model.Currency;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB document entity for storing Currency domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "currencies")
public class CurrencyEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("currency_code")
    private String currencyCode;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("name")
    private String name;

    @Field("symbol")
    private String symbol;

    @Field("decimal_places")
    private int decimalPlaces;

    @Field("iso_numeric_code")
    private String isoNumericCode;

    @Field("status")
    private String status;

    @Field("country_codes")
    private List<String> countryCodes;

    @Field("domain_events")
    private List<Object> domainEvents;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // Default constructor for MongoDB
    public CurrencyEntity() {
    }

    // Constructor from domain model
    public CurrencyEntity(Currency currency) {
        this.id = currency.getId();
        this.tenantId = currency.getTenantId();
        this.currencyCode = currency.getCurrencyCode();
        this.name = currency.getName();
        this.symbol = currency.getSymbol();
        this.decimalPlaces = currency.getDecimalPlaces();
        this.isoNumericCode = currency.getIsoNumericCode();
        this.status = currency.getStatus() != null ? currency.getStatus().name() : null;
        this.countryCodes = currency.getCountryCodes() != null ? new ArrayList<>(currency.getCountryCodes()) : new ArrayList<>();
        this.domainEvents = currency.getDomainEvents() != null ? new ArrayList<>(currency.getDomainEvents()) : new ArrayList<>();
        this.createdAt = currency.getCreatedAt();
        this.updatedAt = currency.getUpdatedAt();
    }

    // Convert to domain model
    public Currency toDomainModel() {
        Currency currency = Currency.builder()
                .currencyCode(this.currencyCode)
                .name(this.name)
                .symbol(this.symbol)
                .decimalPlaces(this.decimalPlaces)
                .isoNumericCode(this.isoNumericCode)
                .status(this.status != null ? Currency.CurrencyStatus.valueOf(this.status) : null)
                .countryCodes(this.countryCodes != null ? new ArrayList<>(this.countryCodes) : new ArrayList<>())
                .domainEvents(this.domainEvents != null ? new ArrayList<>(this.domainEvents) : new ArrayList<>())
                .build();
        // Set inherited fields manually
        currency.setCurrencyId(this.id);
        currency.setCurrencyTenantId(this.tenantId);
        currency.setCurrencyCreatedAt(this.createdAt);
        currency.setCurrencyUpdatedAt(this.updatedAt);
        return currency;
    }

    // Update from domain model (for partial updates)
    public void updateFrom(Currency currency) {
        this.currencyCode = currency.getCurrencyCode();
        this.name = currency.getName();
        this.symbol = currency.getSymbol();
        this.decimalPlaces = currency.getDecimalPlaces();
        this.isoNumericCode = currency.getIsoNumericCode();
        this.status = currency.getStatus() != null ? currency.getStatus().name() : null;
        this.countryCodes = currency.getCountryCodes() != null ? new ArrayList<>(currency.getCountryCodes()) : new ArrayList<>();
        this.domainEvents = currency.getDomainEvents() != null ? new ArrayList<>(currency.getDomainEvents()) : new ArrayList<>();
        this.updatedAt = currency.getUpdatedAt();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public String getIsoNumericCode() {
        return isoNumericCode;
    }

    public void setIsoNumericCode(String isoNumericCode) {
        this.isoNumericCode = isoNumericCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getCountryCodes() {
        return countryCodes;
    }

    public void setCountryCodes(List<String> countryCodes) {
        this.countryCodes = countryCodes;
    }

    public List<Object> getDomainEvents() {
        return domainEvents;
    }

    public void setDomainEvents(List<Object> domainEvents) {
        this.domainEvents = domainEvents;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
