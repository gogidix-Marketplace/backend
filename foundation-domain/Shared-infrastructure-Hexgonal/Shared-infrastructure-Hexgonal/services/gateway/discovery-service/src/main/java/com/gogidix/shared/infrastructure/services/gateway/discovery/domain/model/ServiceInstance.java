package com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Domain model representing a service instance in the registry.
 * This is the core entity for service discovery.
 */
public class ServiceInstance {

    private final String instanceId;
    private final String appName;
    private final String hostName;
    private final String ipAddress;
    private final int port;
    private final boolean secure;
    private final String status;
    private final String zone;
    private final String dataCenter;
    private final Instant registrationTime;
    private final Instant lastRenewalTime;
    private final Integer leaseDuration;
    private final Map<String, String> metadata;

    private ServiceInstance(Builder builder) {
        this.instanceId = builder.instanceId;
        this.appName = builder.appName;
        this.hostName = builder.hostName;
        this.ipAddress = builder.ipAddress;
        this.port = builder.port;
        this.secure = builder.secure;
        this.status = builder.status;
        this.zone = builder.zone;
        this.dataCenter = builder.dataCenter;
        this.registrationTime = builder.registrationTime;
        this.lastRenewalTime = builder.lastRenewalTime;
        this.leaseDuration = builder.leaseDuration;
        this.metadata = Map.copyOf(builder.metadata);
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getAppName() {
        return appName;
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

    public String getStatus() {
        return status;
    }

    public String getZone() {
        return zone;
    }

    public String getDataCenter() {
        return dataCenter;
    }

    public Instant getRegistrationTime() {
        return registrationTime;
    }

    public Instant getLastRenewalTime() {
        return lastRenewalTime;
    }

    public Integer getLeaseDuration() {
        return leaseDuration;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public String getServiceUrl() {
        String protocol = secure ? "https" : "http";
        return String.format("%s://%s:%d", protocol, hostName, port);
    }

    public boolean isExpired() {
        if (lastRenewalTime == null || leaseDuration == null) {
            return false;
        }
        return Instant.now().isAfter(lastRenewalTime.plusSeconds(leaseDuration));
    }

    public boolean isUp() {
        return "UP".equalsIgnoreCase(status);
    }

    public boolean isOutOfService() {
        return "OUT_OF_SERVICE".equalsIgnoreCase(status);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .instanceId(this.instanceId)
                .appName(this.appName)
                .hostName(this.hostName)
                .ipAddress(this.ipAddress)
                .port(this.port)
                .secure(this.secure)
                .status(this.status)
                .zone(this.zone)
                .dataCenter(this.dataCenter)
                .registrationTime(this.registrationTime)
                .lastRenewalTime(this.lastRenewalTime)
                .leaseDuration(this.leaseDuration)
                .metadata(this.metadata);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceInstance that = (ServiceInstance) o;
        return Objects.equals(instanceId, that.instanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceId);
    }

    @Override
    public String toString() {
        return "ServiceInstance{" +
                "instanceId='" + instanceId + '\'' +
                ", appName='" + appName + '\'' +
                ", hostName='" + hostName + '\'' +
                ", port=" + port +
                ", status='" + status + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }

    public static class Builder {
        private String instanceId;
        private String appName;
        private String hostName;
        private String ipAddress;
        private int port;
        private boolean secure = false;
        private String status = "UP";
        private String zone = "default";
        private String dataCenter = "default";
        private Instant registrationTime = Instant.now();
        private Instant lastRenewalTime = Instant.now();
        private Integer leaseDuration = 30;
        private Map<String, String> metadata = Map.of();

        public Builder instanceId(String instanceId) {
            this.instanceId = instanceId;
            return this;
        }

        public Builder appName(String appName) {
            this.appName = appName;
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

        public Builder status(String status) {
            this.status = status;
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

        public Builder registrationTime(Instant registrationTime) {
            this.registrationTime = registrationTime;
            return this;
        }

        public Builder lastRenewalTime(Instant lastRenewalTime) {
            this.lastRenewalTime = lastRenewalTime;
            return this;
        }

        public Builder leaseDuration(Integer leaseDuration) {
            this.leaseDuration = leaseDuration;
            return this;
        }

        public Builder metadata(Map<String, String> metadata) {
            this.metadata = metadata != null ? metadata : Map.of();
            return this;
        }

        public ServiceInstance build() {
            return new ServiceInstance(this);
        }
    }
}
