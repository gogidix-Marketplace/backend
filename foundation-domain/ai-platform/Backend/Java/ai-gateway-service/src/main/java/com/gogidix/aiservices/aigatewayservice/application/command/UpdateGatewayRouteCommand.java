package com.gogidix.aiservices.aigatewayservice.application.command;

import com.gogidix.aiservices.aigatewayservice.domain.model.LoadBalancingStrategy;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteFilter;
import com.gogidix.aiservices.aigatewayservice.domain.port.in.UpdateRouteCommand;

import java.util.List;

/**
 * Application command for updating a gateway route.
 */
public class UpdateGatewayRouteCommand implements UpdateRouteCommand {

    private final String routeId;
    private final String tenantId;
    private final String path;
    private final String targetService;
    private final List<String> targetUrls;
    private final List<RouteFilter> filters;
    private final Integer rateLimit;
    private final LoadBalancingStrategy loadBalancingStrategy;
    private final Integer requestTimeout;

    public UpdateGatewayRouteCommand(
            String routeId,
            String tenantId,
            String path,
            String targetService,
            List<String> targetUrls,
            List<RouteFilter> filters,
            Integer rateLimit,
            LoadBalancingStrategy loadBalancingStrategy,
            Integer requestTimeout
    ) {
        this.routeId = routeId;
        this.tenantId = tenantId;
        this.path = path;
        this.targetService = targetService;
        this.targetUrls = targetUrls;
        this.filters = filters;
        this.rateLimit = rateLimit;
        this.loadBalancingStrategy = loadBalancingStrategy;
        this.requestTimeout = requestTimeout;
    }

    @Override
    public String routeId() {
        return routeId;
    }

    @Override
    public String tenantId() {
        return tenantId;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public String targetService() {
        return targetService;
    }

    @Override
    public List<String> targetUrls() {
        return targetUrls;
    }

    @Override
    public List<RouteFilter> filters() {
        return filters;
    }

    @Override
    public Integer rateLimit() {
        return rateLimit;
    }

    @Override
    public LoadBalancingStrategy loadBalancingStrategy() {
        return loadBalancingStrategy;
    }

    @Override
    public Integer requestTimeout() {
        return requestTimeout;
    }
}
