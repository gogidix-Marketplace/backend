package com.gogidix.shared.infrastructure.services.gateway.discovery.domain.aggregate;

import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.event.ServiceDeregisteredEvent;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.event.ServiceRegisteredEvent;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.event.ServiceRenewedEvent;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model.ServiceInstance;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model.ServiceRegistration;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.registry.ServiceRegistryStatistics;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Aggregate root for the Service Registry.
 * Manages service registration, renewal, and discovery.
 */
public class ServiceRegistry {

    private final Map<String, ServiceInstance> instances = new ConcurrentHashMap<>();
    private final Map<String, List<String>> appInstances = new ConcurrentHashMap<>();
    private final List<Consumer<ServiceRegisteredEvent>> registeredEventHandlers = new ArrayList<>();
    private final List<Consumer<ServiceDeregisteredEvent>> deregisteredEventHandlers = new ArrayList<>();
    private final List<Consumer<ServiceRenewedEvent>> renewedEventHandlers = new ArrayList<>();

    /**
     * Register a new service instance.
     */
    public ServiceInstance register(ServiceRegistration registration, String instanceId) {
        ServiceInstance instance = registration.toInstance(instanceId, "UP");

        instances.put(instanceId, instance);
        appInstances.computeIfAbsent(registration.getAppName(), k -> new ArrayList<>()).add(instanceId);

        publishEvent(new ServiceRegisteredEvent(instance));

        return instance;
    }

    /**
     * Deregister a service instance.
     */
    public Optional<ServiceInstance> deregister(String instanceId) {
        ServiceInstance instance = instances.remove(instanceId);
        if (instance != null) {
            List<String> instanceList = appInstances.get(instance.getAppName());
            if (instanceList != null) {
                instanceList.remove(instanceId);
                if (instanceList.isEmpty()) {
                    appInstances.remove(instance.getAppName());
                }
            }

            publishEvent(new ServiceDeregisteredEvent(instanceId, instance.getAppName(), "Explicit deregistration"));

            return Optional.of(instance);
        }
        return Optional.empty();
    }

    /**
     * Renew the lease for a service instance.
     */
    public Optional<ServiceInstance> renew(String instanceId) {
        ServiceInstance existing = instances.get(instanceId);
        if (existing != null) {
            ServiceInstance renewed = existing.toBuilder()
                    .lastRenewalTime(Instant.now())
                    .build();

            instances.put(instanceId, renewed);

            publishEvent(new ServiceRenewedEvent(instanceId, renewed.getAppName()));

            return Optional.of(renewed);
        }
        return Optional.empty();
    }

    /**
     * Update the status of a service instance.
     */
    public Optional<ServiceInstance> updateStatus(String instanceId, String status) {
        ServiceInstance existing = instances.get(instanceId);
        if (existing != null) {
            ServiceInstance updated = existing.toBuilder()
                    .status(status)
                    .lastRenewalTime(Instant.now())
                    .build();

            instances.put(instanceId, updated);

            return Optional.of(updated);
        }
        return Optional.empty();
    }

    /**
     * Get a specific service instance by ID.
     */
    public Optional<ServiceInstance> getInstance(String instanceId) {
        return Optional.ofNullable(instances.get(instanceId));
    }

    /**
     * Get all instances for a specific application.
     */
    public List<ServiceInstance> getInstancesByApp(String appName) {
        return appInstances.getOrDefault(appName, List.of()).stream()
                .map(instances::get)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Get all UP instances for a specific application.
     */
    public List<ServiceInstance> getUpInstancesByApp(String appName) {
        return getInstancesByApp(appName).stream()
                .filter(ServiceInstance::isUp)
                .collect(Collectors.toList());
    }

    /**
     * Get all instance IDs for an application.
     */
    public List<String> getInstanceIdsByApp(String appName) {
        return List.copyOf(appInstances.getOrDefault(appName, List.of()));
    }

    /**
     * Get all registered application names.
     */
    public Collection<String> getAllApplications() {
        return new ArrayList<>(appInstances.keySet());
    }

    /**
     * Get all service instances.
     */
    public Collection<ServiceInstance> getAllInstances() {
        return new ArrayList<>(instances.values());
    }

    /**
     * Get instances by zone.
     */
    public List<ServiceInstance> getInstancesByZone(String zone) {
        return instances.values().stream()
                .filter(i -> zone.equals(i.getZone()))
                .collect(Collectors.toList());
    }

    /**
     * Get instances by data center.
     */
    public List<ServiceInstance> getInstancesByDataCenter(String dataCenter) {
        return instances.values().stream()
                .filter(i -> dataCenter.equals(i.getDataCenter()))
                .collect(Collectors.toList());
    }

    /**
     * Remove expired instances based on lease duration.
     */
    public List<ServiceInstance> evictExpired() {
        List<ServiceInstance> expired = instances.values().stream()
                .filter(ServiceInstance::isExpired)
                .toList();

        expired.forEach(instance -> {
            instances.remove(instance.getInstanceId());
            List<String> instanceList = appInstances.get(instance.getAppName());
            if (instanceList != null) {
                instanceList.remove(instance.getInstanceId());
                if (instanceList.isEmpty()) {
                    appInstances.remove(instance.getAppName());
                }
            }

            publishEvent(new ServiceDeregisteredEvent(
                    instance.getInstanceId(),
                    instance.getAppName(),
                    "Lease expired"
            ));
        });

        return expired;
    }

    /**
     * Get registry statistics.
     */
    public ServiceRegistryStatistics getStatistics() {
        int totalInstances = instances.size();
        int totalApplications = appInstances.size();
        int upInstances = (int) instances.values().stream().filter(ServiceInstance::isUp).count();

        Map<String, Long> instancesPerApp = appInstances.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> (long) e.getValue().size()
                ));

        Map<String, Long> instancesPerZone = instances.values().stream()
                .collect(Collectors.groupingBy(
                        ServiceInstance::getZone,
                        Collectors.counting()
                ));

        return new ServiceRegistryStatistics(
                totalApplications,
                totalInstances,
                upInstances,
                totalInstances - upInstances,
                instancesPerApp,
                instancesPerZone
        );
    }

    /**
     * Clear all registered instances (for testing).
     */
    public void clear() {
        instances.clear();
        appInstances.clear();
    }

    /**
     * Get total number of instances.
     */
    public int size() {
        return instances.size();
    }

    /**
     * Check if registry is empty.
     */
    public boolean isEmpty() {
        return instances.isEmpty();
    }

    /**
     * Register event handler for service registered events.
     */
    public void onServiceRegistered(Consumer<ServiceRegisteredEvent> handler) {
        registeredEventHandlers.add(handler);
    }

    /**
     * Register event handler for service deregistered events.
     */
    public void onServiceDeregistered(Consumer<ServiceDeregisteredEvent> handler) {
        deregisteredEventHandlers.add(handler);
    }

    /**
     * Register event handler for service renewed events.
     */
    public void onServiceRenewed(Consumer<ServiceRenewedEvent> handler) {
        renewedEventHandlers.add(handler);
    }

    private void publishEvent(ServiceRegisteredEvent event) {
        registeredEventHandlers.forEach(h -> h.accept(event));
    }

    private void publishEvent(ServiceDeregisteredEvent event) {
        deregisteredEventHandlers.forEach(h -> h.accept(event));
    }

    private void publishEvent(ServiceRenewedEvent event) {
        renewedEventHandlers.forEach(h -> h.accept(event));
    }
}
