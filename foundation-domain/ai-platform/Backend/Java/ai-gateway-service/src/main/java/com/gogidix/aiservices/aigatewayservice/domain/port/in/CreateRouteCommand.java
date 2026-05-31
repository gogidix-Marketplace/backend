package com.gogidix.aiservices.aigatewayservice.domain.port.in;

import com.gogidix.aiservices.aigatewayservice.domain.model.LoadBalancingStrategy;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteFilter;

import java.util.List;

/**
 * Input port for creating a new route.
 */
public interface CreateRouteCommand {

    String tenantId();

    String path();

    String targetService();

    List<String> targetUrls();

    List<RouteFilter> filters();

    Integer rateLimit();

    LoadBalancingStrategy loadBalancingStrategy();

    Integer requestTimeout();
}
