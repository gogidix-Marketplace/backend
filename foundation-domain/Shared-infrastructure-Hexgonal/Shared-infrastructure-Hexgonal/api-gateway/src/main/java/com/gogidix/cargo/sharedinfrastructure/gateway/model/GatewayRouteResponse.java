package com.gogidix.cargo.sharedinfrastructure.gateway.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatewayRouteResponse {

    private String routeId;
    private String uri;
    private List<String> predicates;
    private List<String> filters;
    private int order;

    public GatewayRouteResponse() {}

    public GatewayRouteResponse(String routeId, String uri, List<String> predicates, List<String> filters, int order) {
        this.routeId = routeId;
        this.uri = uri;
        this.predicates = predicates;
        this.filters = filters;
        this.order = order;
    }

    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }
    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }
    public List<String> getPredicates() { return predicates; }
    public void setPredicates(List<String> predicates) { this.predicates = predicates; }
    public List<String> getFilters() { return filters; }
    public void setFilters(List<String> filters) { this.filters = filters; }
    public int getOrder() { return order; }
    public void setOrder(int order) { this.order = order; }
}