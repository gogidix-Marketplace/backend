package com.gogidix.sales.crm.domain.port.in;

import com.gogidix.sales.crm.domain.model.Contact;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Contact Commands (Input Port)
 * Defines the input commands for contact operations
 */
public interface ContactCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateContactCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        @NotBlank(message = "First name is required")
        @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
        private String firstName;

        @NotBlank(message = "Last name is required")
        @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
        private String lastName;

        @Email(message = "Email must be valid")
        private String email;

        private String title;

        private String department;

        @NotNull(message = "Contact type is required")
        private Contact.ContactType contactType;

        private String phone;

        private String mobilePhone;

        private String alternatePhone;

        private Boolean isPrimary;

        private Boolean isDecisionMaker;

        private String linkedInUrl;

        private String timezone;

        private String preferredContactMethod;

        private String assistantName;

        private String assistantPhone;

        private String assistantEmail;

        private String reportsToContactId;

        private LocalDate birthDate;

        private String notes;

        private List<String> tags;

        private String addressStreet;

        private String addressCity;

        private String addressState;

        private String addressPostalCode;

        private String addressCountry;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateContactCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Contact ID is required")
        private String contactId;

        private String firstName;

        private String lastName;

        @Email(message = "Email must be valid")
        private String email;

        private String phone;

        private String mobilePhone;

        private String title;

        private String department;

        private String alternatePhone;

        private String linkedInUrl;

        private String timezone;

        private String preferredContactMethod;

        private String notes;

        private List<String> tags;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsPrimaryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Contact ID is required")
        private String contactId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsDecisionMakerCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Contact ID is required")
        private String contactId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeactivateContactCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Contact ID is required")
        private String contactId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ActivateContactCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Contact ID is required")
        private String contactId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteContactCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Contact ID is required")
        private String contactId;
    }
}
