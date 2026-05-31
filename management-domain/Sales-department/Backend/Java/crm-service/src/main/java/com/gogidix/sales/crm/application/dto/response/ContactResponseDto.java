package com.gogidix.sales.crm.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Contact Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponseDto {

    private String id;
    private String contactId;
    private String tenantId;
    private String customerId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String title;
    private String department;
    private String email;
    private String phone;
    private String mobilePhone;
    private String alternatePhone;
    private ContactTypeDto contactType;
    private Boolean isPrimary;
    private Boolean isDecisionMaker;
    private Boolean isActive;
    private String linkedInUrl;
    private String timezone;
    private String preferredContactMethod;
    private LocalDate lastContactDate;
    private Integer interactionCount;
    private String assistantName;
    private String assistantPhone;
    private String assistantEmail;
    private String reportsTo;
    private String reportsToContactId;
    private LocalDate birthDate;
    private String notes;
    private List<String> tags;
    private AddressDto address;
    private Instant createdAt;
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

    public enum ContactTypeDto {
        DECISION_MAKER,
        INFLUENCER,
        TECHNICAL_CONTACT,
        BILLING_CONTACT,
        EXECUTIVE_SPONSOR,
        USER,
        OTHER
    }
}
