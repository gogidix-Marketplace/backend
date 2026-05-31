package com.gogidix.customersupport.customerportal.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileRequestDto {

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    private String userId;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
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
