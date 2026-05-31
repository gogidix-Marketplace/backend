package com.gogidix.aiservices.aichurnpredictionservice.domain.model;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Value Object representing a customer's profile for segmentation.
 * Contains demographic, behavioral, and transactional attributes.
 */
public class ChurnModel {

    @Field("customer_id")
    private final String customerId;

    @Field("tenant_id")
    private final String tenantId;

    @Field("email")
    private final String email;

    @Field("first_name")
    private final String firstName;

    @Field("last_name")
    private final String lastName;

    @Field("age")
    private final Integer age;

    @Field("gender")
    private final String gender;

    @Field("country")
    private final String country;

    @Field("city")
    private final String city;

    @Field("registered_at")
    private final Instant registeredAt;

    @Field("last_activity_at")
    private final Instant lastActivityAt;

    @Field("total_purchases")
    private final Integer totalPurchases;

    @Field("total_spent")
    private final Double totalSpent;

    @Field("average_order_value")
    private final Double averageOrderValue;

    @Field("loyalty_tier")
    private final String loyaltyTier;

    @Field("attributes")
    private final Map<String, Object> customAttributes;

    // Private constructor
    private ChurnModel(Builder builder) {
        this.customerId = Objects.requireNonNull(builder.customerId, "customerId is required");
        this.tenantId = Objects.requireNonNull(builder.tenantId, "tenantId is required");
        this.email = builder.email;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.age = builder.age;
        this.gender = builder.gender;
        this.country = builder.country;
        this.city = builder.city;
        this.registeredAt = builder.registeredAt;
        this.lastActivityAt = builder.lastActivityAt;
        this.totalPurchases = builder.totalPurchases;
        this.totalSpent = builder.totalSpent;
        this.averageOrderValue = builder.averageOrderValue;
        this.loyaltyTier = builder.loyaltyTier;
        this.customAttributes = builder.customAttributes;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getCustomerId() {
        return customerId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (firstName != null) {
            sb.append(firstName);
        }
        if (lastName != null) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(lastName);
        }
        return sb.length() > 0 ? sb.toString() : email;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public Instant getRegisteredAt() {
        return registeredAt;
    }

    public Instant getLastActivityAt() {
        return lastActivityAt;
    }

    public Integer getTotalPurchases() {
        return totalPurchases;
    }

    public Double getTotalSpent() {
        return totalSpent;
    }

    public Double getAverageOrderValue() {
        return averageOrderValue;
    }

    public String getLoyaltyTier() {
        return loyaltyTier;
    }

    public Map<String, Object> getCustomAttributes() {
        return customAttributes;
    }

    /**
     * Get a custom attribute value.
     *
     * @param key the attribute key
     * @return the attribute value, or null if not found
     */
    public Object getCustomAttribute(String key) {
        return customAttributes != null ? customAttributes.get(key) : null;
    }

    /**
     * Check if customer is active (activity within last 30 days).
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        if (lastActivityAt == null) {
            return false;
        }
        return lastActivityAt.isAfter(Instant.now().minus(30, java.time.temporal.ChronoUnit.DAYS));
    }

    /**
     * Check if customer is VIP (high spending or loyalty tier).
     *
     * @return true if VIP, false otherwise
     */
    public boolean isVip() {
        return "GOLD".equals(loyaltyTier) || "PLATINUM".equals(loyaltyTier) ||
                (totalSpent != null && totalSpent >= 10000);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChurnModel that = (ChurnModel) o;
        return Objects.equals(customerId, that.customerId) &&
                Objects.equals(tenantId, that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, tenantId);
    }

    @Override
    public String toString() {
        return "ChurnModel{" +
                "customerId='" + customerId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", email='" + email + '\'' +
                ", loyaltyTier='" + loyaltyTier + '\'' +
                ", totalPurchases=" + totalPurchases +
                ", totalSpent=" + totalSpent +
                '}';
    }

    /**
     * Builder for ChurnModel.
     */
    public static class Builder {
        private String customerId;
        private String tenantId;
        private String email;
        private String firstName;
        private String lastName;
        private Integer age;
        private String gender;
        private String country;
        private String city;
        private Instant registeredAt;
        private Instant lastActivityAt;
        private Integer totalPurchases;
        private Double totalSpent;
        private Double averageOrderValue;
        private String loyaltyTier;
        private Map<String, Object> customAttributes;

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder age(Integer age) {
            this.age = age;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder registeredAt(Instant registeredAt) {
            this.registeredAt = registeredAt;
            return this;
        }

        public Builder lastActivityAt(Instant lastActivityAt) {
            this.lastActivityAt = lastActivityAt;
            return this;
        }

        public Builder totalPurchases(Integer totalPurchases) {
            this.totalPurchases = totalPurchases;
            return this;
        }

        public Builder totalSpent(Double totalSpent) {
            this.totalSpent = totalSpent;
            return this;
        }

        public Builder averageOrderValue(Double averageOrderValue) {
            this.averageOrderValue = averageOrderValue;
            return this;
        }

        public Builder loyaltyTier(String loyaltyTier) {
            this.loyaltyTier = loyaltyTier;
            return this;
        }

        public Builder customAttributes(Map<String, Object> customAttributes) {
            this.customAttributes = customAttributes;
            return this;
        }

        public ChurnModel build() {
            return new ChurnModel(this);
        }
    }
}
