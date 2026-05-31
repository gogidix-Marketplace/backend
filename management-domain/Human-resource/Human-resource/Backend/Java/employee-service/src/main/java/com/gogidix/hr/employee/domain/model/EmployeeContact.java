package com.gogidix.hr.employee.domain.model;

import com.gogidix.hr.employee.shared.base.BaseEntity;
import com.gogidix.hr.employee.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Employee Contact Domain Entity
 * Multi-tenant emergency and personal contact management
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "employee_contacts")
public class EmployeeContact extends BaseEntity {

    @Indexed
    private String tenantId;

    @Indexed
    private String employeeId;

    @Indexed
    private ContactType type;

    private String name;
    private String relationship;
    private String phone;
    private String mobile;
    private String email;
    private String address;

    private Boolean primary;

    private Integer priority;

    public enum ContactType {
        EMERGENCY,
        PERSONAL,
        WORK
    }

    /**
     * Creates a new employee contact
     */
    public static EmployeeContact create(String tenantId, String employeeId,
                                          ContactType type, String name, String relationship) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("name", "Contact name is required");
        }
        if (relationship == null || relationship.isBlank()) {
            throw new ValidationException("relationship", "Relationship is required");
        }

        return EmployeeContact.builder()
                .tenantId(tenantId)
                .employeeId(employeeId)
                .type(type)
                .name(name)
                .relationship(relationship)
                .primary(false)
                .priority(0)
                .build();
    }

    /**
     * Creates an emergency contact
     */
    public static EmployeeContact createEmergencyContact(String tenantId, String employeeId,
                                                           String name, String relationship,
                                                           String phone, String mobile) {
        EmployeeContact contact = create(tenantId, employeeId, ContactType.EMERGENCY, name, relationship);
        contact.setPhone(phone);
        contact.setMobile(mobile);
        contact.setPriority(1);
        return contact;
    }

    /**
     * Sets as primary contact
     */
    public void setAsPrimary() {
        this.primary = true;
    }

    /**
     * Removes primary status
     */
    public void removePrimary() {
        this.primary = false;
    }

    /**
     * Sets priority
     */
    public void setPriority(Integer priority) {
        if (priority == null || priority < 0) {
            this.priority = 0;
        } else {
            this.priority = priority;
        }
    }

    /**
     * Increases priority
     */
    public void increasePriority() {
        this.priority = this.priority == null ? 1 : this.priority + 1;
    }

    /**
     * Decreases priority
     */
    public void decreasePriority() {
        this.priority = this.priority == null || this.priority <= 0 ? 0 : this.priority - 1;
    }

    /**
     * Gets best contact number
     */
    public String getBestContactNumber() {
        if (type == ContactType.EMERGENCY) {
            return mobile != null && !mobile.isBlank() ? mobile : phone;
        }
        return phone != null && !phone.isBlank() ? phone : mobile;
    }

    /**
     * Checks if has email
     */
    public boolean hasEmail() {
        return email != null && !email.isBlank();
    }

    /**
     * Checks if has phone
     */
    public boolean hasPhone() {
        return (phone != null && !phone.isBlank()) || (mobile != null && !mobile.isBlank());
    }

    /**
     * Checks if has address
     */
    public boolean hasAddress() {
        return address != null && !address.isBlank();
    }

    /**
     * Updates contact details
     */
    public void updateContact(String name, String relationship, String phone,
                              String mobile, String email, String address) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (relationship != null && !relationship.isBlank()) {
            this.relationship = relationship;
        }
        if (phone != null) {
            this.phone = phone;
        }
        if (mobile != null) {
            this.mobile = mobile;
        }
        if (email != null) {
            this.email = email;
        }
        if (address != null) {
            this.address = address;
        }
    }

    /**
     * Validates emergency contact has at least one phone number
     */
    public void validateEmergencyContact() {
        if (type == ContactType.EMERGENCY) {
            if ((phone == null || phone.isBlank()) && (mobile == null || mobile.isBlank())) {
                throw new ValidationException("phone", "Emergency contact must have at least one phone number");
            }
        }
    }

    /**
     * Gets contact summary
     */
    public String getContactSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (").append(relationship).append(")");
        if (mobile != null && !mobile.isBlank()) {
            sb.append(" - Mobile: ").append(mobile);
        }
        if (phone != null && !phone.isBlank()) {
            sb.append(" - Phone: ").append(phone);
        }
        return sb.toString();
    }

    /**
     * Checks if is emergency contact
     */
    public boolean isEmergencyContact() {
        return type == ContactType.EMERGENCY;
    }

    /**
     * Checks if can be contacted
     */
    public boolean canBeContacted() {
        return hasPhone() || hasEmail();
    }
}
