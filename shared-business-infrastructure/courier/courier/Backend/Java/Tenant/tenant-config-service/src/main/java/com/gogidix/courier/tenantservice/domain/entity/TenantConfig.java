package com.gogidix.courier.tenantservice.domain.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Tenant configuration value object.
 * Contains configurable settings for a tenant.
 */
public class TenantConfig {

    @Field("max_drivers")
    private Integer maxDrivers;

    @Field("max_daily_orders")
    private Integer maxDailyOrders;

    @Field("max_daily_deliveries")
    private Integer maxDailyDeliveries;

    @Field("service_radius_km")
    private Double serviceRadiusKm;

    @Field("default_currency")
    private String defaultCurrency;

    @Field("timezone")
    private String timezone;

    @Field("delivery_fee_enabled")
    private Boolean deliveryFeeEnabled;

    @Field("delivery_fee_amount")
    private Double deliveryFeeAmount;

    @Field("tax_rate")
    private Double taxRate;

    @Field("auto_accept_orders")
    private Boolean autoAcceptOrders;

    @Field("require_driver_verification")
    private Boolean requireDriverVerification;

    @Field("custom_settings")
    private Map<String, Object> customSettings;

    /**
     * Default constructor.
     */
    public TenantConfig() {
        this.maxDrivers = 100;
        this.maxDailyOrders = 1000;
        this.maxDailyDeliveries = 1000;
        this.serviceRadiusKm = 50.0;
        this.defaultCurrency = "USD";
        this.timezone = "UTC";
        this.deliveryFeeEnabled = true;
        this.deliveryFeeAmount = 5.0;
        this.taxRate = 0.0;
        this.autoAcceptOrders = false;
        this.requireDriverVerification = true;
        this.customSettings = new HashMap<>();
    }

    /**
     * Copy constructor.
     */
    public TenantConfig(TenantConfig other) {
        this.maxDrivers = other.maxDrivers;
        this.maxDailyOrders = other.maxDailyOrders;
        this.maxDailyDeliveries = other.maxDailyDeliveries;
        this.serviceRadiusKm = other.serviceRadiusKm;
        this.defaultCurrency = other.defaultCurrency;
        this.timezone = other.timezone;
        this.deliveryFeeEnabled = other.deliveryFeeEnabled;
        this.deliveryFeeAmount = other.deliveryFeeAmount;
        this.taxRate = other.taxRate;
        this.autoAcceptOrders = other.autoAcceptOrders;
        this.requireDriverVerification = other.requireDriverVerification;
        this.customSettings = new HashMap<>(other.customSettings);
    }

    /**
     * Set a custom setting.
     *
     * @param key   the setting key
     * @param value the setting value
     */
    public void setSetting(String key, Object value) {
        if (this.customSettings == null) {
            this.customSettings = new HashMap<>();
        }
        this.customSettings.put(key, value);
    }

    /**
     * Get a custom setting.
     *
     * @param key the setting key
     * @return the setting value or null if not found
     */
    public Object getSetting(String key) {
        if (this.customSettings == null) {
            return null;
        }
        return this.customSettings.get(key);
    }

    /**
     * Remove a custom setting.
     *
     * @param key the setting key
     * @return the removed value or null if not found
     */
    public Object removeSetting(String key) {
        if (this.customSettings == null) {
            return null;
        }
        return this.customSettings.remove(key);
    }

    // Getters and Setters
    public Integer getMaxDrivers() {
        return maxDrivers;
    }

    public void setMaxDrivers(Integer maxDrivers) {
        this.maxDrivers = maxDrivers;
    }

    public Integer getMaxDailyOrders() {
        return maxDailyOrders;
    }

    public void setMaxDailyOrders(Integer maxDailyOrders) {
        this.maxDailyOrders = maxDailyOrders;
    }

    public Integer getMaxDailyDeliveries() {
        return maxDailyDeliveries;
    }

    public void setMaxDailyDeliveries(Integer maxDailyDeliveries) {
        this.maxDailyDeliveries = maxDailyDeliveries;
    }

    public Double getServiceRadiusKm() {
        return serviceRadiusKm;
    }

    public void setServiceRadiusKm(Double serviceRadiusKm) {
        this.serviceRadiusKm = serviceRadiusKm;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Boolean getDeliveryFeeEnabled() {
        return deliveryFeeEnabled;
    }

    public void setDeliveryFeeEnabled(Boolean deliveryFeeEnabled) {
        this.deliveryFeeEnabled = deliveryFeeEnabled;
    }

    public Double getDeliveryFeeAmount() {
        return deliveryFeeAmount;
    }

    public void setDeliveryFeeAmount(Double deliveryFeeAmount) {
        this.deliveryFeeAmount = deliveryFeeAmount;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Boolean getAutoAcceptOrders() {
        return autoAcceptOrders;
    }

    public void setAutoAcceptOrders(Boolean autoAcceptOrders) {
        this.autoAcceptOrders = autoAcceptOrders;
    }

    public Boolean getRequireDriverVerification() {
        return requireDriverVerification;
    }

    public void setRequireDriverVerification(Boolean requireDriverVerification) {
        this.requireDriverVerification = requireDriverVerification;
    }

    public Map<String, Object> getCustomSettings() {
        return customSettings;
    }

    public void setCustomSettings(Map<String, Object> customSettings) {
        this.customSettings = customSettings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantConfig that = (TenantConfig) o;
        return Objects.equals(maxDrivers, that.maxDrivers) &&
                Objects.equals(maxDailyOrders, that.maxDailyOrders) &&
                Objects.equals(maxDailyDeliveries, that.maxDailyDeliveries) &&
                Objects.equals(serviceRadiusKm, that.serviceRadiusKm) &&
                Objects.equals(defaultCurrency, that.defaultCurrency) &&
                Objects.equals(timezone, that.timezone) &&
                Objects.equals(deliveryFeeEnabled, that.deliveryFeeEnabled) &&
                Objects.equals(deliveryFeeAmount, that.deliveryFeeAmount) &&
                Objects.equals(taxRate, that.taxRate) &&
                Objects.equals(autoAcceptOrders, that.autoAcceptOrders) &&
                Objects.equals(requireDriverVerification, that.requireDriverVerification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxDrivers, maxDailyOrders, maxDailyDeliveries,
                serviceRadiusKm, defaultCurrency, timezone,
                deliveryFeeEnabled, deliveryFeeAmount, taxRate,
                autoAcceptOrders, requireDriverVerification);
    }

    @Override
    public String toString() {
        return "TenantConfig{" +
                "maxDrivers=" + maxDrivers +
                ", maxDailyOrders=" + maxDailyOrders +
                ", maxDailyDeliveries=" + maxDailyDeliveries +
                ", serviceRadiusKm=" + serviceRadiusKm +
                ", defaultCurrency='" + defaultCurrency + '\'' +
                ", timezone='" + timezone + '\'' +
                ", deliveryFeeEnabled=" + deliveryFeeEnabled +
                ", deliveryFeeAmount=" + deliveryFeeAmount +
                ", taxRate=" + taxRate +
                ", autoAcceptOrders=" + autoAcceptOrders +
                ", requireDriverVerification=" + requireDriverVerification +
                '}';
    }

    /**
     * Builder pattern for TenantConfig.
     */
    public static class Builder {
        private final TenantConfig instance;

        private Builder() {
            this.instance = new TenantConfig();
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder maxDrivers(Integer maxDrivers) {
            this.instance.maxDrivers = maxDrivers;
            return this;
        }

        public Builder maxDailyOrders(Integer maxDailyOrders) {
            this.instance.maxDailyOrders = maxDailyOrders;
            return this;
        }

        public Builder maxDailyDeliveries(Integer maxDailyDeliveries) {
            this.instance.maxDailyDeliveries = maxDailyDeliveries;
            return this;
        }

        public Builder serviceRadiusKm(Double serviceRadiusKm) {
            this.instance.serviceRadiusKm = serviceRadiusKm;
            return this;
        }

        public Builder defaultCurrency(String defaultCurrency) {
            this.instance.defaultCurrency = defaultCurrency;
            return this;
        }

        public Builder timezone(String timezone) {
            this.instance.timezone = timezone;
            return this;
        }

        public Builder deliveryFeeEnabled(Boolean deliveryFeeEnabled) {
            this.instance.deliveryFeeEnabled = deliveryFeeEnabled;
            return this;
        }

        public Builder deliveryFeeAmount(Double deliveryFeeAmount) {
            this.instance.deliveryFeeAmount = deliveryFeeAmount;
            return this;
        }

        public Builder taxRate(Double taxRate) {
            this.instance.taxRate = taxRate;
            return this;
        }

        public Builder autoAcceptOrders(Boolean autoAcceptOrders) {
            this.instance.autoAcceptOrders = autoAcceptOrders;
            return this;
        }

        public Builder requireDriverVerification(Boolean requireDriverVerification) {
            this.instance.requireDriverVerification = requireDriverVerification;
            return this;
        }

        public Builder customSettings(Map<String, Object> customSettings) {
            this.instance.customSettings = customSettings;
            return this;
        }

        public TenantConfig build() {
            return this.instance;
        }
    }
}
