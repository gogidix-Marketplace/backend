package com.gogidix.sysadmin.userprovisioning.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document(collection = "user_accounts")
public class UserAccount {

    @Id
    private String id;
    @Indexed
    private String tenantId;
    @Indexed
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String displayName;
    private AccountStatus status;
    private String userType;
    private List<String> roles;
    private List<String> groups;
    private String department;
    private String manager;
    private Instant hireDate;
    private Instant terminationDate;
    private String employeeId;
    private String costCenter;
    private String location;
    private String phoneNumber;
    private Map<String, String> attributes;
    private List<String> associatedResourceIds;
    private String lastLoginIp;
    private Instant lastLoginAt;
    private Instant passwordLastChangedAt;
    private Instant passwordExpiresAt;
    private Boolean mfaEnabled;
    private String mfaMethod;
    private String createdBy;
    private String lastModifiedBy;
    private Instant createdAt;
    private Instant updatedAt;

    public enum AccountStatus {
        ACTIVE, INACTIVE, SUSPENDED, LOCKED, PENDING_ACTIVATION, TERMINATED
    }

    public UserAccount() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.status = AccountStatus.PENDING_ACTIVATION;
        this.mfaEnabled = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public List<String> getGroups() { return groups; }
    public void setGroups(List<String> groups) { this.groups = groups; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getManager() { return manager; }
    public void setManager(String manager) { this.manager = manager; }

    public Instant getHireDate() { return hireDate; }
    public void setHireDate(Instant hireDate) { this.hireDate = hireDate; }

    public Instant getTerminationDate() { return terminationDate; }
    public void setTerminationDate(Instant terminationDate) { this.terminationDate = terminationDate; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getCostCenter() { return costCenter; }
    public void setCostCenter(String costCenter) { this.costCenter = costCenter; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }

    public List<String> getAssociatedResourceIds() { return associatedResourceIds; }
    public void setAssociatedResourceIds(List<String> associatedResourceIds) { this.associatedResourceIds = associatedResourceIds; }

    public String getLastLoginIp() { return lastLoginIp; }
    public void setLastLoginIp(String lastLoginIp) { this.lastLoginIp = lastLoginIp; }

    public Instant getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(Instant lastLoginAt) { this.lastLoginAt = lastLoginAt; }

    public Instant getPasswordLastChangedAt() { return passwordLastChangedAt; }
    public void setPasswordLastChangedAt(Instant passwordLastChangedAt) { this.passwordLastChangedAt = passwordLastChangedAt; }

    public Instant getPasswordExpiresAt() { return passwordExpiresAt; }
    public void setPasswordExpiresAt(Instant passwordExpiresAt) { this.passwordExpiresAt = passwordExpiresAt; }

    public Boolean getMfaEnabled() { return mfaEnabled; }
    public void setMfaEnabled(Boolean mfaEnabled) { this.mfaEnabled = mfaEnabled; }

    public String getMfaMethod() { return mfaMethod; }
    public void setMfaMethod(String mfaMethod) { this.mfaMethod = mfaMethod; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public void activate() {
        this.status = AccountStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void suspend() {
        this.status = AccountStatus.SUSPENDED;
        this.updatedAt = Instant.now();
    }

    public void terminate() {
        this.status = AccountStatus.TERMINATED;
        this.terminationDate = Instant.now();
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
