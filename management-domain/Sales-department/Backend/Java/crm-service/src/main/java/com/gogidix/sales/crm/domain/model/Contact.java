package com.gogidix.sales.crm.domain.model;

import com.gogidix.sales.crm.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Contact Domain Entity
 * Represents individual contacts associated with customers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "contacts")
public class Contact extends BaseEntity {

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

    private ContactType contactType;

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

    private String addressStreet;

    private String addressCity;

    private String addressState;

    private String addressPostalCode;

    private String addressCountry;

    public enum ContactType {
        DECISION_MAKER,
        INFLUENCER,
        TECHNICAL_CONTACT,
        BILLING_CONTACT,
        EXECUTIVE_SPONSOR,
        USER,
        OTHER
    }

    /**
     * Creates a new contact
     */
    public static Contact create(String tenantId, String customerId, String firstName,
                                  String lastName, String email, String title,
                                  ContactType contactType) {
        Contact contact = Contact.builder()
            .tenantId(tenantId)
            .customerId(customerId)
            .firstName(firstName)
            .lastName(lastName)
            .fullName(firstName + " " + lastName)
            .email(email)
            .title(title)
            .contactType(contactType)
            .isActive(true)
            .isPrimary(false)
            .isDecisionMaker(false)
            .interactionCount(0)
            .tags(new ArrayList<>())
            .build();

        return contact;
    }

    /**
     * Marks contact as primary
     */
    public void markAsPrimary() {
        this.isPrimary = true;
    }

    /**
     * Removes primary status
     */
    public void removePrimaryStatus() {
        this.isPrimary = false;
    }

    /**
     * Marks contact as decision maker
     */
    public void markAsDecisionMaker() {
        this.isDecisionMaker = true;
    }

    /**
     * Updates contact information
     */
    public void updateContact(String firstName, String lastName, String email,
                               String phone, String title, String department) {
        if (firstName != null) {
            this.firstName = firstName;
        }
        if (lastName != null) {
            this.lastName = lastName;
        }
        if (firstName != null || lastName != null) {
            this.fullName = this.firstName + " " + this.lastName;
        }
        if (email != null) {
            this.email = email;
        }
        if (phone != null) {
            this.phone = phone;
        }
        if (title != null) {
            this.title = title;
        }
        if (department != null) {
            this.department = department;
        }
    }

    /**
     * Updates last contact date
     */
    public void updateLastContactDate(LocalDate contactDate) {
        this.lastContactDate = contactDate;
        if (this.interactionCount == null) {
            this.interactionCount = 0;
        }
        this.interactionCount++;
    }

    /**
     * Deactivates the contact
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Activates the contact
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Adds a tag to the contact
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Removes a tag from the contact
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Sets the reporting relationship
     */
    public void setReportsTo(String reportsToContactId, String reportsToName) {
        this.reportsToContactId = reportsToContactId;
        this.reportsTo = reportsToName;
    }

    /**
     * Checks if contact is active
     */
    public boolean isContactActive() {
        return this.isActive != null && this.isActive;
    }

    /**
     * Gets the display name
     */
    public String getDisplayName() {
        if (this.fullName != null && !this.fullName.isBlank()) {
            return this.fullName;
        }
        return this.email;
    }
}
