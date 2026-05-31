package com.gogidix.customersupport.customerportal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "customer_profiles")
public class CustomerProfile extends BaseEntity {

    @Field("customer_id")
    @Indexed(unique = true)
    private String customerId;

    @Field("user_id")
    @Indexed
    private String userId;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("email")
    @Indexed
    private String email;

    @Field("phone")
    private String phone;

    @Field("secondary_phone")
    private String secondaryPhone;

    @Field("company_name")
    private String companyName;

    @Field("company_id")
    private String companyId;

    @Field("customer_type")
    private String customerType;

    @Field("tier")
    private String tier;

    @Field("preferred_language")
    private String preferredLanguage;

    @Field("timezone")
    private String timezone;

    @Field("country")
    private String country;

    @Field("address")
    private Address address;

    @Field("preferences")
    private CustomerPreferences preferences;

    @Field("communication_channels")
    private List<String> communicationChannels;

    @Field("tags")
    private List<String> tags;

    @Field("custom_fields")
    private Map<String, Object> customFields;

    @Field("is_active")
    private Boolean isActive;

    @Field("last_login_at")
    private Instant lastLoginAt;

    @Field("account_created_at")
    private Instant accountCreatedAt;

    public static CustomerProfile create(String customerId, String email, String firstName, String lastName) {
        CustomerProfile profile = new CustomerProfile();
        profile.setId(java.util.UUID.randomUUID().toString());
        profile.setCustomerId(customerId);
        profile.setEmail(email);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setIsActive(true);
        profile.setAccountCreatedAt(Instant.now());
        profile.setCreatedAt(Instant.now());
        profile.setUpdatedAt(Instant.now());
        return profile;
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
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerPreferences {
        private Boolean emailNotifications;
        private Boolean smsNotifications;
        private Boolean phoneNotifications;
        private Boolean pushNotifications;
        private String preferredContactMethod;
        private Map<String, String> notificationPreferences;
    }
}
