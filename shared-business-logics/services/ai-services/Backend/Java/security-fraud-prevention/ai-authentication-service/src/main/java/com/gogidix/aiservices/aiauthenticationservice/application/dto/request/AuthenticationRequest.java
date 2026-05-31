package com.gogidix.aiservices.aiauthenticationservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class AuthenticationRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    private String mfaCode;

    private String ipAddress;

    private String userAgent;

    private String deviceFingerprint;

    private String location;

    private AuthenticationRequest(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.mfaCode = builder.mfaCode;
        this.ipAddress = builder.ipAddress;
        this.userAgent = builder.userAgent;
        this.deviceFingerprint = builder.deviceFingerprint;
        this.location = builder.location;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMfaCode() {
        return mfaCode;
    }

    public void setMfaCode(String mfaCode) {
        this.mfaCode = mfaCode;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getDeviceFingerprint() {
        return deviceFingerprint;
    }

    public void setDeviceFingerprint(String deviceFingerprint) {
        this.deviceFingerprint = deviceFingerprint;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static class Builder {
        private String username;
        private String password;
        private String mfaCode;
        private String ipAddress;
        private String userAgent;
        private String deviceFingerprint;
        private String location;

        public Builder() {}

        public Builder(AuthenticationRequest request) {
            this.username = request.username;
            this.password = request.password;
            this.mfaCode = request.mfaCode;
            this.ipAddress = request.ipAddress;
            this.userAgent = request.userAgent;
            this.deviceFingerprint = request.deviceFingerprint;
            this.location = request.location;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder mfaCode(String mfaCode) {
            this.mfaCode = mfaCode;
            return this;
        }

        public Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder deviceFingerprint(String deviceFingerprint) {
            this.deviceFingerprint = deviceFingerprint;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public AuthenticationRequest build() {
            return new AuthenticationRequest(this);
        }
    }
}
