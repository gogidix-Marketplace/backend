package com.gogidix.aiservices.aigatewayservice.domain.model;

import com.gogidix.aiservices.aigatewayservice.shared.exception.ValidationException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Domain Entity representing a Gateway Route.
 * Manages API routing configuration for the AI Gateway Service.
 */
@Document(collection = "gateway_routes")
@CompoundIndex(name = "idx_route_tenant", def = "{'tenantId': 1, 'routeId': 1}")
public class GatewayRoute {

    @Id
    private String id;

    @Indexed
    private String routeId;

    @Indexed
    private String tenantId;

    private String path;

    private String targetService;

    private List<String> targetUrls;

    private List<RouteFilter> filters;

    private Integer rateLimit;

    private LoadBalancingStrategy loadBalancingStrategy;

    private Integer requestTimeout;

    private RouteStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    private Long circuitBreakerFailureCount;

    private Integer circuitBreakerThreshold;

    private Boolean circuitBreakerOpen;

    // Private constructor for persistence
    private GatewayRoute() {
        this.filters = new ArrayList<>();
        this.targetUrls = new ArrayList<>();
        this.circuitBreakerFailureCount = 0L;
        this.circuitBreakerThreshold = 5;
        this.circuitBreakerOpen = false;
    }

    /**
     * Create a new GatewayRoute.
     *
     * @param tenantId       the tenant ID
     * @param path           the route path
     * @param targetService  the target service
     * @param targetUrls     the target URLs
     */
    public GatewayRoute(String tenantId, String path, String targetService, List<String> targetUrls) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.routeId = generateRouteId();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.path = Objects.requireNonNull(path, "path is required");
        this.targetService = Objects.requireNonNull(targetService, "targetService is required");
        this.targetUrls = new ArrayList<>(Objects.requireNonNull(targetUrls, "targetUrls is required"));
        this.status = RouteStatus.INACTIVE;
        this.rateLimit = 1000;
        this.loadBalancingStrategy = LoadBalancingStrategy.ROUND_ROBIN;
        this.requestTimeout = 30;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    private String generateRouteId() {
        return "route_" + java.util.UUID.randomUUID().toString().substring(0, 8);
    }

    // Domain Logic Methods

    /**
     * Activate the route.
     *
     * @throws IllegalStateException if route is already active
     */
    public void activate() {
        if (this.status == RouteStatus.ACTIVE) {
            throw new IllegalStateException("Route is already active");
        }
        if (this.targetUrls == null || this.targetUrls.isEmpty()) {
            throw new IllegalStateException("Cannot activate route without target URLs");
        }
        this.status = RouteStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    /**
     * Deactivate the route.
     */
    public void deactivate() {
        if (this.status == RouteStatus.INACTIVE) {
            throw new IllegalStateException("Route is already inactive");
        }
        this.status = RouteStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    /**
     * Put route in maintenance mode.
     */
    public void enableMaintenance() {
        this.status = RouteStatus.MAINTENANCE;
        this.updatedAt = Instant.now();
    }

    /**
     * Add a filter to the route.
     *
     * @param filter the filter to add
     */
    public void addFilter(RouteFilter filter) {
        Objects.requireNonNull(filter, "filter is required");
        if (this.filters == null) {
            this.filters = new ArrayList<>();
        }
        if (!hasFilter(filter.getName())) {
            this.filters.add(filter);
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Remove a filter from the route.
     *
     * @param filterName the name of the filter to remove
     */
    public void removeFilter(String filterName) {
        if (this.filters != null) {
            this.filters.removeIf(f -> f.getName().equals(filterName));
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Check if route has a specific filter.
     *
     * @param filterName the filter name
     * @return true if filter exists, false otherwise
     */
    public boolean hasFilter(String filterName) {
        return this.filters != null && this.filters.stream()
                .anyMatch(f -> f.getName().equals(filterName));
    }

    /**
     * Update rate limit.
     *
     * @param rateLimit the new rate limit (requests per minute)
     */
    public void updateRateLimit(Integer rateLimit) {
        if (rateLimit == null || rateLimit <= 0) {
            throw new ValidationException("rateLimit", "Rate limit must be positive");
        }
        this.rateLimit = rateLimit;
        this.updatedAt = Instant.now();
    }

    /**
     * Update request timeout.
     *
     * @param timeout the new timeout in seconds
     */
    public void updateRequestTimeout(Integer timeout) {
        if (timeout == null || timeout <= 0 || timeout > 300) {
            throw new ValidationException("timeout", "Timeout must be between 1 and 300 seconds");
        }
        this.requestTimeout = timeout;
        this.updatedAt = Instant.now();
    }

    /**
     * Add target URL.
     *
     * @param url the URL to add
     */
    public void addTargetUrl(String url) {
        Objects.requireNonNull(url, "url is required");
        if (this.targetUrls == null) {
            this.targetUrls = new ArrayList<>();
        }
        if (!this.targetUrls.contains(url)) {
            this.targetUrls.add(url);
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Remove target URL.
     *
     * @param url the URL to remove
     */
    public void removeTargetUrl(String url) {
        if (this.targetUrls != null) {
            this.targetUrls.remove(url);
            if (this.targetUrls.isEmpty() && this.status == RouteStatus.ACTIVE) {
                throw new IllegalStateException("Cannot remove last target URL from active route");
            }
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Record a circuit breaker failure.
     */
    public void recordFailure() {
        this.circuitBreakerFailureCount++;
        if (this.circuitBreakerFailureCount >= this.circuitBreakerThreshold) {
            this.circuitBreakerOpen = true;
        }
        this.updatedAt = Instant.now();
    }

    /**
     * Record a circuit breaker success and reset if threshold reached.
     */
    public void recordSuccess() {
        this.circuitBreakerFailureCount = 0L;
        this.circuitBreakerOpen = false;
        this.updatedAt = Instant.now();
    }

    /**
     * Reset circuit breaker.
     */
    public void resetCircuitBreaker() {
        this.circuitBreakerFailureCount = 0L;
        this.circuitBreakerOpen = false;
        this.updatedAt = Instant.now();
    }

    /**
     * Validate the route state.
     *
     * @throws ValidationException if validation fails
     */
    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new ValidationException("tenantId is required");
        }
        if (path == null || path.isBlank()) {
            throw new ValidationException("path is required");
        }
        if (!path.startsWith("/")) {
            throw new ValidationException("path", "Path must start with /");
        }
        if (targetService == null || targetService.isBlank()) {
            throw new ValidationException("targetService is required");
        }
        if (targetUrls == null || targetUrls.isEmpty()) {
            throw new ValidationException("targetUrls is required");
        }
    }

    /**
     * Check if route can handle requests.
     *
     * @return true if route is active and circuit breaker is closed
     */
    public boolean canHandleRequests() {
        return this.status == RouteStatus.ACTIVE &&
                !Boolean.TRUE.equals(this.circuitBreakerOpen);
    }

    /**
     * Check if circuit breaker is open.
     *
     * @return true if circuit breaker is open
     */
    public boolean isCircuitBreakerOpen() {
        return Boolean.TRUE.equals(this.circuitBreakerOpen);
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getPath() {
        return path;
    }

    public String getTargetService() {
        return targetService;
    }

    public List<String> getTargetUrls() {
        return targetUrls != null ? Collections.unmodifiableList(targetUrls) : Collections.emptyList();
    }

    public List<RouteFilter> getFilters() {
        return filters != null ? Collections.unmodifiableList(filters) : Collections.emptyList();
    }

    public Integer getRateLimit() {
        return rateLimit;
    }

    public LoadBalancingStrategy getLoadBalancingStrategy() {
        return loadBalancingStrategy;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public RouteStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getCircuitBreakerFailureCount() {
        return circuitBreakerFailureCount;
    }

    public Integer getCircuitBreakerThreshold() {
        return circuitBreakerThreshold;
    }

    // Setters for persistence and application layer
    public void setId(String id) {
        this.id = id;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTargetService(String targetService) {
        this.targetService = targetService;
    }

    public void setTargetUrls(List<String> targetUrls) {
        this.targetUrls = targetUrls;
    }

    public void setFilters(List<RouteFilter> filters) {
        this.filters = filters;
    }

    public void setRateLimit(Integer rateLimit) {
        this.rateLimit = rateLimit;
    }

    public void setLoadBalancingStrategy(LoadBalancingStrategy loadBalancingStrategy) {
        this.loadBalancingStrategy = loadBalancingStrategy;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public void setStatus(RouteStatus status) {
        this.status = status;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCircuitBreakerFailureCount(Long circuitBreakerFailureCount) {
        this.circuitBreakerFailureCount = circuitBreakerFailureCount;
    }

    public void setCircuitBreakerThreshold(Integer circuitBreakerThreshold) {
        this.circuitBreakerThreshold = circuitBreakerThreshold;
    }

    public void setCircuitBreakerOpen(Boolean circuitBreakerOpen) {
        this.circuitBreakerOpen = circuitBreakerOpen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GatewayRoute that = (GatewayRoute) o;
        return Objects.equals(routeId, that.routeId) &&
                Objects.equals(tenantId, that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeId, tenantId);
    }

    @Override
    public String toString() {
        return "GatewayRoute{" +
                "routeId='" + routeId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", path='" + path + '\'' +
                ", targetService='" + targetService + '\'' +
                ", status=" + status +
                ", circuitBreakerOpen=" + circuitBreakerOpen +
                '}';
    }

    /**
     * Builder pattern for GatewayRoute.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final GatewayRoute instance;

        private Builder() {
            this.instance = new GatewayRoute();
        }

        public Builder id(String id) {
            this.instance.id = id;
            return this;
        }

        public Builder routeId(String routeId) {
            this.instance.routeId = routeId;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.instance.tenantId = tenantId;
            return this;
        }

        public Builder path(String path) {
            this.instance.path = path;
            return this;
        }

        public Builder targetService(String targetService) {
            this.instance.targetService = targetService;
            return this;
        }

        public Builder targetUrls(List<String> targetUrls) {
            this.instance.targetUrls = targetUrls;
            return this;
        }

        public Builder filters(List<RouteFilter> filters) {
            this.instance.filters = filters;
            return this;
        }

        public Builder rateLimit(Integer rateLimit) {
            this.instance.rateLimit = rateLimit;
            return this;
        }

        public Builder loadBalancingStrategy(LoadBalancingStrategy loadBalancingStrategy) {
            this.instance.loadBalancingStrategy = loadBalancingStrategy;
            return this;
        }

        public Builder requestTimeout(Integer requestTimeout) {
            this.instance.requestTimeout = requestTimeout;
            return this;
        }

        public Builder status(RouteStatus status) {
            this.instance.status = status;
            return this;
        }

        public Builder circuitBreakerThreshold(Integer circuitBreakerThreshold) {
            this.instance.circuitBreakerThreshold = circuitBreakerThreshold;
            return this;
        }

        public GatewayRoute build() {
            if (this.instance.tenantId == null || this.instance.tenantId.isBlank()) {
                throw new IllegalArgumentException("tenantId is required");
            }
            if (this.instance.path == null || this.instance.path.isBlank()) {
                throw new IllegalArgumentException("path is required");
            }
            if (this.instance.targetService == null || this.instance.targetService.isBlank()) {
                throw new IllegalArgumentException("targetService is required");
            }
            if (this.instance.targetUrls == null || this.instance.targetUrls.isEmpty()) {
                throw new IllegalArgumentException("targetUrls is required");
            }
            if (this.instance.status == null) {
                this.instance.status = RouteStatus.INACTIVE;
            }
            if (this.instance.rateLimit == null) {
                this.instance.rateLimit = 1000;
            }
            if (this.instance.loadBalancingStrategy == null) {
                this.instance.loadBalancingStrategy = LoadBalancingStrategy.ROUND_ROBIN;
            }
            if (this.instance.requestTimeout == null) {
                this.instance.requestTimeout = 30;
            }
            if (this.instance.createdAt == null) {
                this.instance.createdAt = Instant.now();
            }
            if (this.instance.updatedAt == null) {
                this.instance.updatedAt = Instant.now();
            }
            return this.instance;
        }
    }
}
