package com.gogidix.cargo.courier.gateway.model;

public record GatewayRouteResponse(String routeId, String uri, String predicate, String[] filters) {}