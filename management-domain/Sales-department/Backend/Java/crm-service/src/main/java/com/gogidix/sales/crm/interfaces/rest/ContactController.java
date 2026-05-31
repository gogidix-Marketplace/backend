package com.gogidix.sales.crm.interfaces.rest;

import com.gogidix.sales.crm.application.dto.response.ContactResponseDto;
import com.gogidix.sales.crm.application.service.ContactCommandService;
import com.gogidix.sales.crm.domain.model.Contact;
import com.gogidix.sales.crm.domain.port.in.ContactCommand;
import com.gogidix.sales.crm.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Contact REST Controller
 * Handles HTTP requests for contact operations
 */
@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
@Tag(name = "Contacts", description = "Contact management endpoints")
public class ContactController {

    private final ContactCommandService contactCommandService;

    @PostMapping
    @Operation(summary = "Create a new contact")
    public ResponseEntity<ContactResponseDto> createContact(
            @Valid @RequestBody CreateContactRequestDto request) {
        ContactCommand.CreateContactCommand command = new ContactCommand.CreateContactCommand(
            RequestContextHolder.getTenantId(),
            request.getCustomerId(),
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getTitle(),
            request.getDepartment(),
            request.getContactType(),
            request.getPhone(),
            request.getMobilePhone(),
            request.getAlternatePhone(),
            request.getIsPrimary(),
            request.getIsDecisionMaker(),
            request.getLinkedInUrl(),
            request.getTimezone(),
            request.getPreferredContactMethod(),
            request.getAssistantName(),
            request.getAssistantPhone(),
            request.getAssistantEmail(),
            request.getReportsToContactId(),
            request.getBirthDate(),
            request.getNotes(),
            request.getTags(),
            request.getAddressStreet(),
            request.getAddressCity(),
            request.getAddressState(),
            request.getAddressPostalCode(),
            request.getAddressCountry()
        );

        Contact contact = contactCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(contact));
    }

    @GetMapping("/{contactId}")
    @Operation(summary = "Get contact by ID")
    public ResponseEntity<ContactResponseDto> getContact(
            @Parameter(description = "Contact ID") @PathVariable String contactId) {
        // Note: In real implementation, you would inject ContactQueryService
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get contacts for customer")
    public ResponseEntity<List<ContactResponseDto>> getContactsByCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {
        // Note: In real implementation, you would inject ContactQueryService
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{contactId}")
    @Operation(summary = "Update contact")
    public ResponseEntity<ContactResponseDto> updateContact(
            @Parameter(description = "Contact ID") @PathVariable String contactId,
            @Valid @RequestBody UpdateContactRequestDto request) {
        ContactCommand.UpdateContactCommand command = new ContactCommand.UpdateContactCommand(
            RequestContextHolder.getTenantId(),
            contactId,
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getPhone(),
            request.getMobilePhone(),
            request.getTitle(),
            request.getDepartment(),
            request.getAlternatePhone(),
            request.getLinkedInUrl(),
            request.getTimezone(),
            request.getPreferredContactMethod(),
            request.getNotes(),
            request.getTags()
        );

        Contact contact = contactCommandService.update(command);
        return ResponseEntity.ok(toDto(contact));
    }

    @PostMapping("/{contactId}/mark-primary")
    @Operation(summary = "Mark contact as primary")
    public ResponseEntity<Void> markAsPrimary(
            @Parameter(description = "Contact ID") @PathVariable String contactId) {
        contactCommandService.markAsPrimary(RequestContextHolder.getTenantId(), contactId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{contactId}/mark-decision-maker")
    @Operation(summary = "Mark contact as decision maker")
    public ResponseEntity<Void> markAsDecisionMaker(
            @Parameter(description = "Contact ID") @PathVariable String contactId) {
        contactCommandService.markAsDecisionMaker(RequestContextHolder.getTenantId(), contactId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{contactId}/deactivate")
    @Operation(summary = "Deactivate contact")
    public ResponseEntity<Void> deactivateContact(
            @Parameter(description = "Contact ID") @PathVariable String contactId) {
        contactCommandService.deactivate(RequestContextHolder.getTenantId(), contactId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{contactId}/activate")
    @Operation(summary = "Activate contact")
    public ResponseEntity<Void> activateContact(
            @Parameter(description = "Contact ID") @PathVariable String contactId) {
        contactCommandService.activate(RequestContextHolder.getTenantId(), contactId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{contactId}")
    @Operation(summary = "Delete contact")
    public ResponseEntity<Void> deleteContact(
            @Parameter(description = "Contact ID") @PathVariable String contactId) {
        ContactCommand.DeleteContactCommand command = new ContactCommand.DeleteContactCommand(
            RequestContextHolder.getTenantId(),
            contactId
        );

        contactCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    private ContactResponseDto toDto(Contact contact) {
        return ContactResponseDto.builder()
            .id(contact.getId())
            .contactId(contact.getContactId())
            .tenantId(contact.getTenantId())
            .customerId(contact.getCustomerId())
            .firstName(contact.getFirstName())
            .lastName(contact.getLastName())
            .fullName(contact.getFullName())
            .title(contact.getTitle())
            .department(contact.getDepartment())
            .email(contact.getEmail())
            .phone(contact.getPhone())
            .mobilePhone(contact.getMobilePhone())
            .alternatePhone(contact.getAlternatePhone())
            .contactType(mapContactType(contact.getContactType()))
            .isPrimary(contact.getIsPrimary())
            .isDecisionMaker(contact.getIsDecisionMaker())
            .isActive(contact.getIsActive())
            .linkedInUrl(contact.getLinkedInUrl())
            .timezone(contact.getTimezone())
            .preferredContactMethod(contact.getPreferredContactMethod())
            .lastContactDate(contact.getLastContactDate())
            .interactionCount(contact.getInteractionCount())
            .assistantName(contact.getAssistantName())
            .assistantPhone(contact.getAssistantPhone())
            .assistantEmail(contact.getAssistantEmail())
            .reportsTo(contact.getReportsTo())
            .reportsToContactId(contact.getReportsToContactId())
            .birthDate(contact.getBirthDate())
            .notes(contact.getNotes())
            .tags(contact.getTags())
            .address(mapAddress(
                contact.getAddressStreet(), contact.getAddressCity(),
                contact.getAddressState(), contact.getAddressPostalCode(),
                contact.getAddressCountry()
            ))
            .createdAt(contact.getCreatedAt())
            .updatedAt(contact.getUpdatedAt())
            .build();
    }

    private ContactResponseDto.ContactTypeDto mapContactType(Contact.ContactType type) {
        return type != null ? ContactResponseDto.ContactTypeDto.valueOf(type.name()) : null;
    }

    private ContactResponseDto.AddressDto mapAddress(String street, String city, String state,
                                                      String postalCode, String country) {
        if (street == null && city == null && state == null && postalCode == null && country == null) {
            return null;
        }
        return ContactResponseDto.AddressDto.builder()
            .street(street)
            .city(city)
            .state(state)
            .postalCode(postalCode)
            .country(country)
            .build();
    }

    // Request DTOs
    @Data
    public static class CreateContactRequestDto {
        public String customerId;
        public String firstName;
        public String lastName;
        public String email;
        public String title;
        public String department;
        public Contact.ContactType contactType;
        public String phone;
        public String mobilePhone;
        public String alternatePhone;
        public Boolean isPrimary;
        public Boolean isDecisionMaker;
        public String linkedInUrl;
        public String timezone;
        public String preferredContactMethod;
        public String assistantName;
        public String assistantPhone;
        public String assistantEmail;
        public String reportsToContactId;
        public LocalDate birthDate;
        public String notes;
        public List<String> tags;
        public String addressStreet;
        public String addressCity;
        public String addressState;
        public String addressPostalCode;
        public String addressCountry;
    }

    @Data

    public static class UpdateContactRequestDto {
        public String firstName;
        public String lastName;
        public String email;
        public String phone;
        public String mobilePhone;
        public String title;
        public String department;
        public String alternatePhone;
        public String linkedInUrl;
        public String timezone;
        public String preferredContactMethod;
        public String notes;
        public List<String> tags;
    }
}
