package com.gogidix.customersupport.customerportal.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileResponseDto {

    private String id;
    private String tenantId;
    private String customerId;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String secondaryPhone;
    private String companyName;
    private String companyId;
    private String customerType;
    private String tier;
    private String preferredLanguage;
    private String timezone;
    private String country;
    private AddressDto address;
    private CustomerPreferencesDto preferences;
    private List<String> communicationChannels;
    private List<String> tags;
    private Map<String, Object> customFields;
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant lastLoginAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant accountCreatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDto {
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
    public static class CustomerPreferencesDto {
        private Boolean emailNotifications;
        private Boolean smsNotifications;
        private Boolean phoneNotifications;
        private Boolean pushNotifications;
        private String preferredContactMethod;
        private Map<String, String> notificationPreferences;
    }
}
