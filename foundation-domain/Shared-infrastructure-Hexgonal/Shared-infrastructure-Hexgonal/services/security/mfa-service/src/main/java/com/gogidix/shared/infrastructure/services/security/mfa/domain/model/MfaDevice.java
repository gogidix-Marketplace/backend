package com.gogidix.shared.infrastructure.services.security.mfa.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Domain entity representing MFA device registration.
 */
@Document(collection = "mfa_devices")
public class MfaDevice {

    @Id
    private String id;

    @Indexed
    private TenantId tenantId;

    @Indexed
    private String userId;

    @Indexed
    private String deviceId;

    @Indexed
    private MfaType mfaType;

    @Indexed
    private DeviceStatus status;

    private String secretKey;
    private String phoneNumber;
    private String email;

    @Indexed
    private Boolean isPrimary;

    @Indexed
    private Boolean isBackup;

    private LocalDateTime lastUsedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer failedAttempts;
    private LocalDateTime lockedUntil;

    public enum MfaType {
        TOTP,
        SMS,
        EMAIL,
        BACKUP_CODE,
        HARDWARE_TOKEN,
        BIOMETRIC
    }

    public enum DeviceStatus {
        ACTIVE,
        INACTIVE,
        REVOKED,
        LOCKED,
        PENDING_VERIFICATION
    }

    public MfaDevice() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public TenantId getTenantId() { return tenantId; }
    public void setTenantId(TenantId tenantId) { this.tenantId = tenantId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public MfaType getMfaType() { return mfaType; }
    public void setMfaType(MfaType mfaType) { this.mfaType = mfaType; }

    public DeviceStatus getStatus() { return status; }
    public void setStatus(DeviceStatus status) { this.status = status; }

    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }

    public Boolean getIsBackup() { return isBackup; }
    public void setIsBackup(Boolean isBackup) { this.isBackup = isBackup; }

    public LocalDateTime getLastUsedAt() { return lastUsedAt; }
    public void setLastUsedAt(LocalDateTime lastUsedAt) { this.lastUsedAt = lastUsedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Integer getFailedAttempts() { return failedAttempts; }
    public void setFailedAttempts(Integer failedAttempts) { this.failedAttempts = failedAttempts; }

    public LocalDateTime getLockedUntil() { return lockedUntil; }
    public void setLockedUntil(LocalDateTime lockedUntil) { this.lockedUntil = lockedUntil; }

    // Domain methods
    public void markAsUsed() {
        this.lastUsedAt = LocalDateTime.now();
        this.failedAttempts = 0;
    }

    public void recordFailedAttempt() {
        this.failedAttempts = (this.failedAttempts == null) ? 1 : this.failedAttempts + 1;
        if (this.failedAttempts >= 5) {
            this.status = DeviceStatus.LOCKED;
            this.lockedUntil = LocalDateTime.now().plusHours(1);
        }
    }

    public void revoke() {
        this.status = DeviceStatus.REVOKED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.status = DeviceStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }
}
