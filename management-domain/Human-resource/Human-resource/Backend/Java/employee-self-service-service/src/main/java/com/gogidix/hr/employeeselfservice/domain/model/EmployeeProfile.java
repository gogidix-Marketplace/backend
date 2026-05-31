package com.gogidix.hr.employeeselfservice.domain.model;

import com.gogidix.hr.employeeselfservice.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Employee Profile Domain Entity
 * Multi-tenant employee profile management for self-service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "employee_profiles")
public class EmployeeProfile extends BaseEntity {

    private String tenantId;

    private String employeeId;

    private String firstName;

    private String lastName;

    private String middleName;

    private String preferredName;

    private String email;

    private String personalEmail;

    private String phone;

    private String mobile;

    private String countryCode;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String postalCode;

    private String nationality;

    private LocalDate dateOfBirth;

    private Gender gender;

    private MaritalStatus maritalStatus;

    private String emergencyContactName;

    private String emergencyContactPhone;

    private String emergencyContactRelationship;

    private String profileImage;

    private Boolean profileCompleted = false;

    private Integer completionPercentage = 0;

    private LocalDate lastUpdated;

    private Map<String, Object> metadata = new HashMap<>();

    private List<String> tags = new ArrayList<>();

    public enum Gender {
        MALE,
        FEMALE,
        OTHER,
        PREFER_NOT_TO_SAY
    }

    public enum MaritalStatus {
        SINGLE,
        MARRIED,
        DIVORCED,
        WIDOWED,
        SEPARATED,
        DOMESTIC_PARTNERSHIP
    }

    /**
     * Creates a new employee profile
     */
    public static EmployeeProfile create(String tenantId, String employeeId,
                                          String firstName, String lastName,
                                          String email, String mobile,
                                          Gender gender, LocalDate dateOfBirth,
                                          String createdBy) {
        EmployeeProfile profile = new EmployeeProfile();
        profile.setTenantId(tenantId);
        profile.setEmployeeId(employeeId);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setEmail(email);
        profile.setMobile(mobile);
        profile.setGender(gender);
        profile.setDateOfBirth(dateOfBirth);
        profile.setProfileCompleted(false);
        profile.setCompletionPercentage(0);
        profile.setLastUpdated(LocalDate.now());
        profile.setMetadata(new HashMap<>());
        profile.setTags(new ArrayList<>());
        profile.setUpdatedBy(createdBy);

        profile.calculateCompletionPercentage();
        return profile;
    }

    /**
     * Updates personal information
     */
    public void updatePersonalInformation(String firstName, String lastName,
                                           String middleName, String preferredName,
                                           LocalDate dateOfBirth, Gender gender,
                                           MaritalStatus maritalStatus, String nationality) {
        if (firstName != null && !firstName.isBlank()) {
            this.firstName = firstName;
        }
        if (lastName != null && !lastName.isBlank()) {
            this.lastName = lastName;
        }
        this.middleName = middleName;
        this.preferredName = preferredName;
        if (dateOfBirth != null) {
            this.dateOfBirth = dateOfBirth;
        }
        if (gender != null) {
            this.gender = gender;
        }
        this.maritalStatus = maritalStatus;
        this.nationality = nationality;
        this.lastUpdated = LocalDate.now();
        calculateCompletionPercentage();
    }

    /**
     * Updates contact information
     */
    public void updateContactInformation(String email, String personalEmail,
                                          String phone, String mobile, String countryCode) {
        if (email != null && !email.isBlank()) {
            this.email = email;
        }
        this.personalEmail = personalEmail;
        this.phone = phone;
        if (mobile != null && !mobile.isBlank()) {
            this.mobile = mobile;
        }
        this.countryCode = countryCode;
        this.lastUpdated = LocalDate.now();
        calculateCompletionPercentage();
    }

    /**
     * Updates address information
     */
    public void updateAddress(String addressLine1, String addressLine2,
                              String city, String state, String postalCode) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.lastUpdated = LocalDate.now();
        calculateCompletionPercentage();
    }

    /**
     * Updates emergency contact information
     */
    public void updateEmergencyContact(String name, String phone, String relationship) {
        this.emergencyContactName = name;
        this.emergencyContactPhone = phone;
        this.emergencyContactRelationship = relationship;
        this.lastUpdated = LocalDate.now();
        calculateCompletionPercentage();
    }

    /**
     * Sets the profile image URL
     */
    public void setProfileImageUrl(String imageUrl) {
        this.profileImage = imageUrl;
        this.lastUpdated = LocalDate.now();
        calculateCompletionPercentage();
    }

    /**
     * Removes the profile image
     */
    public void removeProfileImage() {
        this.profileImage = null;
        this.lastUpdated = LocalDate.now();
        calculateCompletionPercentage();
    }

    /**
     * Calculates the profile completion percentage
     */
    public void calculateCompletionPercentage() {
        int totalFields = 16;
        int completedFields = 0;

        if (firstName != null && !firstName.isBlank()) completedFields++;
        if (lastName != null && !lastName.isBlank()) completedFields++;
        if (email != null && !email.isBlank()) completedFields++;
        if (mobile != null && !mobile.isBlank()) completedFields++;
        if (dateOfBirth != null) completedFields++;
        if (gender != null) completedFields++;
        if (addressLine1 != null && !addressLine1.isBlank()) completedFields++;
        if (city != null && !city.isBlank()) completedFields++;
        if (state != null && !state.isBlank()) completedFields++;
        if (postalCode != null && !postalCode.isBlank()) completedFields++;
        if (nationality != null && !nationality.isBlank()) completedFields++;
        if (emergencyContactName != null && !emergencyContactName.isBlank()) completedFields++;
        if (emergencyContactPhone != null && !emergencyContactPhone.isBlank()) completedFields++;
        if (emergencyContactRelationship != null && !emergencyContactRelationship.isBlank()) completedFields++;
        if (maritalStatus != null) completedFields++;
        if (phone != null && !phone.isBlank()) completedFields++;

        this.completionPercentage = (completedFields * 100) / totalFields;
        this.profileCompleted = this.completionPercentage >= 80;
    }

    /**
     * Gets the full name of the employee
     */
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (firstName != null) {
            fullName.append(firstName);
        }
        if (middleName != null && !middleName.isBlank()) {
            fullName.append(" ").append(middleName);
        }
        if (lastName != null) {
            fullName.append(" ").append(lastName);
        }
        return fullName.toString().trim();
    }

    /**
     * Gets the display name (preferred name or first name)
     */
    public String getDisplayName() {
        if (preferredName != null && !preferredName.isBlank()) {
            return preferredName;
        }
        return firstName;
    }

    /**
     * Checks if profile is complete
     */
    public boolean isProfileComplete() {
        return Boolean.TRUE.equals(profileCompleted);
    }

    /**
     * Adds a tag to the profile
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
     * Removes a tag from the profile
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Adds metadata
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Gets metadata value
     */
    public Object getMetadata(String key) {
        if (this.metadata != null) {
            return this.metadata.get(key);
        }
        return null;
    }
}
