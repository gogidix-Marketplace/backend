package com.gogidix.shared.servicediscovery.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import com.gogidix.shared.servicediscovery.domain.model.ServiceMetadata;
import com.gogidix.shared.servicediscovery.interfaces.dto.ServiceDiscoveryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service Discovery Client
 *
 * <p>Client for discovering services registered with the Foundation service registry.
 * Supports:</p>
 *
 * <ul>
 *   <li>Discovery by service name</li>
 *   <li>Discovery by tenant ID</li>
 *   <li>Discovery by domain</li>
 *   <li>Discovery by capability</li>
 *   <li>Load balancing across multiple instances</li>
 *   <li>Caching for performance</li>
 * </ul>
 *
 * @author Foundation Team
 * @version 1.0.0
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "gogidix.service-discovery.enabled", havingValue = "true", matchIfMissing = true)
public class ServiceDiscoveryClient {

    @Value("${gogidix.service-discovery.registry-url:http://localhost:8761}")
    private String registryUrl;

    @Value("${gogidix.service-discovery.cache-ttl:30000}")
    private long cacheTtl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Local cache with expiration
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public ServiceDiscoveryClient() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Discover service by name
     */
    public List<ServiceInstance> discoverService(String serviceName) {
        return discoverService(serviceName, null, null, null, false);
    }

    /**
     * Discover service by name and tenant
     */
    public List<ServiceInstance> discoverService(String serviceName, String tenantId) {
        return discoverService(serviceName, tenantId, null, null, false);
    }

    /**
     * Discover service by name, tenant, and domain
     */
    public List<ServiceInstance> discoverService(String serviceName, String tenantId,
                                                  ServiceMetadata.ServiceDomain domain) {
        return discoverService(serviceName, tenantId, domain, null, false);
    }

    /**
     * Discover service by name, tenant, domain, and capability
     */
    public List<ServiceInstance> discoverService(String serviceName, String tenantId,
                                                  ServiceMetadata.ServiceDomain domain,
                                                  String capability, boolean healthyOnly) {
        String cacheKey = buildCacheKey(serviceName, tenantId, domain, capability, healthyOnly);

        // Check cache
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && !cached.isExpired()) {
            log.debug("Cache hit for key: {}", cacheKey);
            return cached.getInstances();
        }

        // Build query params
        StringBuilder url = new StringBuilder(registryUrl + "/api/v1/services?");

        if (serviceName != null) {
            url.append("serviceName=").append(serviceName).append("&");
        }
        if (tenantId != null) {
            url.append("tenantId=").append(tenantId).append("&");
        }
        if (domain != null) {
            url.append("domain=").append(domain).append("&");
        }
        if (capability != null) {
            url.append("capability=").append(capability).append("&");
        }
        if (healthyOnly) {
            url.append("healthyOnly=true").append("&");
        }

        // Remove trailing &
        if (url.charAt(url.length() - 1) == '&') {
            url.setLength(url.length() - 1);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<List<ServiceDiscoveryResponse>> response = restTemplate.exchange(
                    url.toString(),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<ServiceDiscoveryResponse>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<ServiceInstance> instances = response.getBody().stream()
                        .map(this::toServiceInstance)
                        .toList();

                // Cache the result
                cache.put(cacheKey, new CacheEntry(instances, System.currentTimeMillis() + cacheTtl));

                log.debug("Discovered {} instances for service: {}", instances.size(), serviceName);
                return instances;
            }

        } catch (Exception e) {
            log.error("Failed to discover service: {}", serviceName, e);
        }

        return Collections.emptyList();
    }

    /**
     * Get a single service instance (load balanced)
     */
    public Optional<ServiceInstance> getSingleInstance(String serviceName) {
        return getSingleInstance(serviceName, null, null, null, true);
    }

    /**
     * Get a single service instance for specific tenant
     */
    public Optional<ServiceInstance> getSingleInstance(String serviceName, String tenantId) {
        return getSingleInstance(serviceName, tenantId, null, null, true);
    }

    /**
     * Get a single service instance with all filters
     */
    public Optional<ServiceInstance> getSingleInstance(String serviceName, String tenantId,
                                                       ServiceMetadata.ServiceDomain domain,
                                                       String capability, boolean healthyOnly) {
        List<ServiceInstance> instances = discoverService(serviceName, tenantId, domain, capability, healthyOnly);

        if (instances.isEmpty()) {
            return Optional.empty();
        }

        // Load balance using weighted random based on priority
        return Optional.of(weightedRandomSelect(instances));
    }

    /**
     * Get service instance by instance ID
     */
    public Optional<ServiceInstance> getInstanceById(String instanceId) {
        String cacheKey = "instance:" + instanceId;

        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && !cached.isExpired()) {
            return cached.getInstances().isEmpty() ? Optional.empty() :
                    Optional.of(cached.getInstances().get(0));
        }

        try {
            ResponseEntity<ServiceDiscoveryResponse> response = restTemplate.getForEntity(
                    registryUrl + "/api/v1/services/" + instanceId,
                    ServiceDiscoveryResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ServiceInstance instance = toServiceInstance(response.getBody());
                cache.put(cacheKey, new CacheEntry(List.of(instance), System.currentTimeMillis() + cacheTtl));
                return Optional.of(instance);
            }

        } catch (Exception e) {
            log.error("Failed to get service instance: {}", instanceId, e);
        }

        return Optional.empty();
    }

    /**
     * Get all services for a tenant
     */
    public List<ServiceInstance> getTenantServices(String tenantId) {
        return discoverService(null, tenantId, null, null, false);
    }

    /**
     * Get all services in a domain
     */
    public List<ServiceInstance> getDomainServices(ServiceMetadata.ServiceDomain domain) {
        return discoverService(null, null, domain, null, false);
    }

    /**
     * Get all services with a specific capability
     */
    public List<ServiceInstance> getServicesByCapability(String capability) {
        return discoverService(null, null, null, capability, true);
    }

    /**
     * Get aggregated health status
     */
    public Map<String, Object> getHealthStatus() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    registryUrl + "/api/v1/health",
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }

        } catch (Exception e) {
            log.error("Failed to get health status", e);
        }

        return Collections.emptyMap();
    }

    /**
     * Trigger health check for all services
     */
    public void triggerHealthCheck() {
        try {
            restTemplate.postForEntity(
                    registryUrl + "/api/v1/health/check",
                    null,
                    Void.class
            );
            log.info("Health check triggered");
        } catch (Exception e) {
            log.error("Failed to trigger health check", e);
        }
    }

    /**
     * Invalidate cache entry
     */
    public void invalidateCache(String serviceName) {
        if (serviceName == null) {
            cache.clear();
            log.debug("All cache invalidated");
        } else {
            cache.entrySet().removeIf(entry -> entry.getKey().contains(serviceName));
            log.debug("Cache invalidated for service: {}", serviceName);
        }
    }

    // ==============================================
    // PRIVATE METHODS
    // ==============================================

    private String buildCacheKey(String serviceName, String tenantId,
                                   ServiceMetadata.ServiceDomain domain,
                                   String capability, boolean healthyOnly) {
        return String.format("%s|%s|%s|%s|%b",
                serviceName != null ? serviceName : "*",
                tenantId != null ? tenantId : "*",
                domain != null ? domain.name() : "*",
                capability != null ? capability : "*",
                healthyOnly);
    }

    private ServiceInstance toServiceInstance(ServiceDiscoveryResponse response) {
        return new ServiceInstance(
                response.getInstanceId(),
                response.getServiceName(),
                response.getTenantId(),
                response.getServiceDomain(),
                response.getServiceUrl(),
                response.getHealthCheckUrl(),
                response.getProtocol(),
                response.getHost(),
                response.getPort(),
                response.getApiVersion(),
                response.getCapabilities(),
                response.getTags(),
                response.isRequiresAuth(),
                response.getAuthMethods(),
                response.getPriority(),
                response.isHealthy()
        );
    }

    private ServiceInstance weightedRandomSelect(List<ServiceInstance> instances) {
        // Calculate total weight
        int totalWeight = instances.stream()
                .mapToInt(ServiceInstance::getPriority)
                .sum();

        if (totalWeight == 0) {
            // Equal probability if no priority
            return instances.get(ThreadLocalRandom.current().nextInt(instances.size()));
        }

        // Weighted random selection
        int random = ThreadLocalRandom.current().nextInt(totalWeight);
        int currentWeight = 0;

        for (ServiceInstance instance : instances) {
            currentWeight += instance.getPriority();
            if (random < currentWeight) {
                return instance;
            }
        }

        // Fallback to last instance
        return instances.get(instances.size() - 1);
    }

    // ==============================================
    // INNER CLASSES
    // ==============================================

    /**
     * Service instance representation
     */
    public static class ServiceInstance {
        private final String instanceId;
        private final String serviceName;
        private final String tenantId;
        private final ServiceMetadata.ServiceDomain domain;
        private final String serviceUrl;
        private final String healthCheckUrl;
        private final String protocol;
        private final String host;
        private final int port;
        private final String apiVersion;
        private final Set<String> capabilities;
        private final Set<String> tags;
        private final boolean requiresAuth;
        private final Set<String> authMethods;
        private final int priority;
        private final boolean healthy;

        public ServiceInstance(String instanceId, String serviceName, String tenantId,
                              ServiceMetadata.ServiceDomain domain, String serviceUrl,
                              String healthCheckUrl, String protocol, String host,
                              int port, String apiVersion, Set<String> capabilities,
                              Set<String> tags, boolean requiresAuth, Set<String> authMethods,
                              int priority, boolean healthy) {
            this.instanceId = instanceId;
            this.serviceName = serviceName;
            this.tenantId = tenantId;
            this.domain = domain;
            this.serviceUrl = serviceUrl;
            this.healthCheckUrl = healthCheckUrl;
            this.protocol = protocol;
            this.host = host;
            this.port = port;
            this.apiVersion = apiVersion;
            this.capabilities = capabilities != null ? capabilities : Set.of();
            this.tags = tags != null ? tags : Set.of();
            this.requiresAuth = requiresAuth;
            this.authMethods = authMethods != null ? authMethods : Set.of();
            this.priority = priority;
            this.healthy = healthy;
        }

        public String getInstanceId() { return instanceId; }
        public String getServiceName() { return serviceName; }
        public String getTenantId() { return tenantId; }
        public ServiceMetadata.ServiceDomain getDomain() { return domain; }
        public String getServiceUrl() { return serviceUrl; }
        public String getHealthCheckUrl() { return healthCheckUrl; }
        public String getProtocol() { return protocol; }
        public String getHost() { return host; }
        public int getPort() { return port; }
        public String getApiVersion() { return apiVersion; }
        public Set<String> getCapabilities() { return capabilities; }
        public Set<String> getTags() { return tags; }
        public boolean isRequiresAuth() { return requiresAuth; }
        public Set<String> getAuthMethods() { return authMethods; }
        public int getPriority() { return priority; }
        public boolean isHealthy() { return healthy; }
    }

    /**
     * Cache entry with expiration
     */
    private static class CacheEntry {
        private final List<ServiceInstance> instances;
        private final long expirationTime;

        public CacheEntry(List<ServiceInstance> instances, long expirationTime) {
            this.instances = instances;
            this.expirationTime = expirationTime;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }

        public List<ServiceInstance> getInstances() {
            return instances;
        }
    }
}
