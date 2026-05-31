package com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Domain model representing a service registration request.
 * Contains all information needed to register a service instance.
 */
public class ServiceRegistration {

    private final String appName;
    private final String instanceId;
    private final String hostName;
    private final String ipAddress;
    private final int port;
    private final boolean secure;
    private final String zone;
    private final String dataCenter;
    private final Integer leaseDuration;
    private final Map<String, String> metadata;
    private final Instant registrationTime;

    private ServiceRegistration(Builder builder) {
        this.appName = Objects.requireNonNull(builder.appName, "appName is required");
        this.instanceId = builder.instanceId;
        this.hostName = Objects.requireNonNull(builder.hostName, "hostName is required");
        this.ipAddress = builder.ipAddress;
        this.port = builder.port;
        this.secure = builder.secure;
        this.zone = builder.zone;
        this.dataCenter = builder.dataCenter;
        this.leaseDuration = builder.leaseDuration;
        this.metadata = builder.metadata != null ? Map.copyOf(builder.metadata) : Map.of();
        this.registrationTime = builder.registrationTime != null ? builder.registrationTime : Instant.now();
    }

    public String getAppName() {
        return appName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getHostName() {
        return hostName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public boolean isSecure() {
        return secure;
    }

    public String getZone() {
        return zone;
    }

    public String getDataCenter() {
        return dataCenter;
    }

    public Integer getLeaseDuration() {
        return leaseDuration;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public Instant getRegistrationTime() {
        return registrationTime;
    }

    public ServiceInstance toInstance(String instanceId, String status) {
        return ServiceInstance.builder()
                .instanceId(instanceId)
                .appName(this.appName)
                .hostName(this.hostName)
                .ipAddress(this.ipAddress)
                .port(this.port)
                .secure(this.secure)
                .status(status)
                .zone(this.zone)
                .dataCenter(this.dataCenter)
                .registrationTime(this.registrationTime)
                .lastRenewalTime(Instant.now())
                .leaseDuration(this.leaseDuration)
                .metadata(this.metadata)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String appName;
        private String instanceId;
        private String hostName;
        private String ipAddress;
        private int port = 8080;
        private boolean secure = false;
        private String zone = "default";
        private String dataCenter = "default";
        private Integer leaseDuration = 30;
        private Map<String, String> metadata;
        private Instant registrationTime;

        public Builder appName(String appName) {
            this.appName = appName;
            return this;
        }

        public Builder instanceId(String instanceId) {
            this.instanceId = instanceId;
            return this;
        }

        public Builder hostName(String hostName) {
            this.hostName = hostName;
            return this;
        }

        public Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder secure(boolean secure) {
            this.secure = secure;
            return this;
        }

        public Builder zone(String zone) {
            this.zone = zone;
            return this;
        }

        public Builder dataCenter(String dataCenter) {
            this.dataCenter = dataCenter;
            return this;
        }

        public Builder leaseDuration(Integer leaseDuration) {
            this.leaseDuration = leaseDuration;
            return this;
        }

        public Builder metadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder registrationTime(Instant registrationTime) {
            this.registrationTime = registrationTime;
            return this;
        }

        public ServiceRegistration build() {
            return new ServiceRegistration(this);
        }
    }
}
