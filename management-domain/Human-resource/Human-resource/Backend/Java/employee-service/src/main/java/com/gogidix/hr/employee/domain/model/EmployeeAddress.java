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

import java.time.LocalDate;

/**
 * Employee Address Domain Entity
 * Multi-tenant address management for employees
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "employee_addresses")
public class EmployeeAddress extends BaseEntity {

    @Indexed
    private String tenantId;

    @Indexed
    private String employeeId;

    @Indexed
    private AddressType type;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;

    @Indexed
    private String countryCode;

    private Boolean primary;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    public enum AddressType {
        RESIDENTIAL,
        MAILING,
        WORK,
        EMERGENCY
    }

    /**
     * Creates a new employee address
     */
    public static EmployeeAddress create(String tenantId, String employeeId,
                                          AddressType type, String addressLine1,
                                          String city, String state, String postalCode,
                                          String countryCode) {
        EmployeeAddress address = EmployeeAddress.builder()
                .tenantId(tenantId)
                .employeeId(employeeId)
                .type(type)
                .addressLine1(addressLine1)
                .city(city)
                .state(state)
                .postalCode(postalCode)
                .countryCode(countryCode)
                .primary(false)
                .effectiveFrom(LocalDate.now())
                .build();

        address.validate();

        return address;
    }

    /**
     * Sets as primary address
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
     * Checks if address is currently effective
     */
    public boolean isCurrentlyEffective() {
        LocalDate now = LocalDate.now();
        boolean afterFromDate = effectiveFrom == null || !now.isBefore(effectiveFrom);
        boolean beforeToDate = effectiveTo == null || !now.isAfter(effectiveTo);
        return afterFromDate && beforeToDate;
    }

    /**
     * Sets effective dates
     */
    public void setEffectiveDates(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new ValidationException("effectiveFrom", "Effective from date must be before effective to date");
        }
        this.effectiveFrom = fromDate;
        this.effectiveTo = toDate;
    }

    /**
     * Gets full address as formatted string
     */
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (addressLine1 != null && !addressLine1.isBlank()) {
            sb.append(addressLine1);
        }
        if (addressLine2 != null && !addressLine2.isBlank()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(addressLine2);
        }
        if (city != null && !city.isBlank()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(city);
        }
        if (state != null && !state.isBlank()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(state);
        }
        if (postalCode != null && !postalCode.isBlank()) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(postalCode);
        }
        if (countryCode != null && !countryCode.isBlank()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(countryCode);
        }
        return sb.toString();
    }

    /**
     * Gets short address (city, state)
     */
    public String getShortAddress() {
        StringBuilder sb = new StringBuilder();
        if (city != null && !city.isBlank()) {
            sb.append(city);
        }
        if (state != null && !state.isBlank()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(state);
        }
        return sb.toString();
    }

    /**
     * Validates address fields
     */
    public void validate() {
        if (addressLine1 == null || addressLine1.isBlank()) {
            throw new ValidationException("addressLine1", "Address line 1 is required");
        }
        if (city == null || city.isBlank()) {
            throw new ValidationException("city", "City is required");
        }
        if (countryCode == null || countryCode.isBlank()) {
            throw new ValidationException("countryCode", "Country code is required");
        }
    }

    /**
     * Updates address details
     */
    public void updateAddress(String addressLine1, String addressLine2, String city,
                              String state, String postalCode, String countryCode) {
        if (addressLine1 != null) {
            this.addressLine1 = addressLine1;
        }
        if (addressLine2 != null) {
            this.addressLine2 = addressLine2;
        }
        if (city != null) {
            this.city = city;
        }
        if (state != null) {
            this.state = state;
        }
        if (postalCode != null) {
            this.postalCode = postalCode;
        }
        if (countryCode != null) {
            this.countryCode = countryCode;
        }
        validate();
    }
}
