package com.gogidix.shared.infrastructure.services.gateway.discovery.domain.registry;

import java.util.Map;

/**
 * Value object representing service registry statistics.
 */
public class ServiceRegistryStatistics {

    private final int totalApplications;
    private final int totalInstances;
    private final int upInstances;
    private final int downInstances;
    private final Map<String, Long> instancesPerApp;
    private final Map<String, Long> instancesPerZone;

    public ServiceRegistryStatistics(int totalApplications, int totalInstances, int upInstances,
                                    int downInstances, Map<String, Long> instancesPerApp,
                                    Map<String, Long> instancesPerZone) {
        this.totalApplications = totalApplications;
        this.totalInstances = totalInstances;
        this.upInstances = upInstances;
        this.downInstances = downInstances;
        this.instancesPerApp = Map.copyOf(instancesPerApp);
        this.instancesPerZone = Map.copyOf(instancesPerZone);
    }

    public int getTotalApplications() {
        return totalApplications;
    }

    public int getTotalInstances() {
        return totalInstances;
    }

    public int getUpInstances() {
        return upInstances;
    }

    public int getDownInstances() {
        return downInstances;
    }

    public Map<String, Long> getInstancesPerApp() {
        return instancesPerApp;
    }

    public Map<String, Long> getInstancesPerZone() {
        return instancesPerZone;
    }

    public double getUpPercentage() {
        if (totalInstances == 0) {
            return 0.0;
        }
        return (upInstances * 100.0) / totalInstances;
    }

    @Override
    public String toString() {
        return "ServiceRegistryStatistics{" +
                "totalApplications=" + totalApplications +
                ", totalInstances=" + totalInstances +
                ", upInstances=" + upInstances +
                ", downInstances=" + downInstances +
                ", upPercentage=" + String.format("%.2f", getUpPercentage()) + "%" +
                '}';
    }
}
