package com.gogidix.cargo.ecommerce.gateway.model;

public record GatewayRouteResponse(String routeId, String uri, String predicate, String[] filters) {}