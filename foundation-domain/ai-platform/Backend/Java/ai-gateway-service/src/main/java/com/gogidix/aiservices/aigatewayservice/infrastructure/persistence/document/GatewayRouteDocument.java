package com.gogidix.aiservices.aigatewayservice.infrastructure.persistence.document;

import com.gogidix.aiservices.aigatewayservice.domain.model.LoadBalancingStrategy;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteFilter;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

/**
 * MongoDB document for GatewayRoute.
 */
@Document(collection = "gateway_routes")
@CompoundIndex(name = "idx_route_tenant", def = "{'tenantId': 1, 'routeId': 1}")
public class GatewayRouteDocument {

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

    public GatewayRouteDocument() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTargetService() {
        return targetService;
    }

    public void setTargetService(String targetService) {
        this.targetService = targetService;
    }

    public List<String> getTargetUrls() {
        return targetUrls;
    }

    public void setTargetUrls(List<String> targetUrls) {
        this.targetUrls = targetUrls;
    }

    public List<RouteFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<RouteFilter> filters) {
        this.filters = filters;
    }

    public Integer getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(Integer rateLimit) {
        this.rateLimit = rateLimit;
    }

    public LoadBalancingStrategy getLoadBalancingStrategy() {
        return loadBalancingStrategy;
    }

    public void setLoadBalancingStrategy(LoadBalancingStrategy loadBalancingStrategy) {
        this.loadBalancingStrategy = loadBalancingStrategy;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public RouteStatus getStatus() {
        return status;
    }

    public void setStatus(RouteStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCircuitBreakerFailureCount() {
        return circuitBreakerFailureCount;
    }

    public void setCircuitBreakerFailureCount(Long circuitBreakerFailureCount) {
        this.circuitBreakerFailureCount = circuitBreakerFailureCount;
    }

    public Integer getCircuitBreakerThreshold() {
        return circuitBreakerThreshold;
    }

    public void setCircuitBreakerThreshold(Integer circuitBreakerThreshold) {
        this.circuitBreakerThreshold = circuitBreakerThreshold;
    }

    public Boolean getCircuitBreakerOpen() {
        return circuitBreakerOpen;
    }

    public void setCircuitBreakerOpen(Boolean circuitBreakerOpen) {
        this.circuitBreakerOpen = circuitBreakerOpen;
    }
}
