package com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Value object representing a route definition for creating or updating routes.
 * Used as input to the route management use cases.
 */
public class RouteDefinition {

    private final String routeId;
    private final String path;
    private final String serviceId;
    private final String uri;
    private final Integer order;
    private final Set<String> predicates;
    private final Set<String> filters;
    private final Boolean enabled;
    private final String description;

    private RouteDefinition(Builder builder) {
        this.routeId = builder.routeId;
        this.path = builder.path;
        this.serviceId = builder.serviceId;
        this.uri = builder.uri;
        this.order = builder.order;
        this.predicates = builder.predicates != null ? new HashSet<>(builder.predicates) : new HashSet<>();
        this.filters = builder.filters != null ? new HashSet<>(builder.filters) : new HashSet<>();
        this.enabled = builder.enabled;
        this.description = builder.description;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getPath() {
        return path;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getUri() {
        return uri;
    }

    public Integer getOrder() {
        return order;
    }

    public Set<String> getPredicates() {
        return new HashSet<>(predicates);
    }

    public Set<String> getFilters() {
        return new HashSet<>(filters);
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public String getDescription() {
        return description;
    }

    public Route toRoute() {
        return Route.builder()
                .routeId(this.routeId)
                .path(this.path)
                .serviceId(this.serviceId)
                .uri(this.uri)
                .order(this.order != null ? this.order : 0)
                .predicates(this.predicates)
                .filters(this.filters)
                .enabled(this.enabled != null ? this.enabled : true)
                .description(this.description)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String routeId;
        private String path;
        private String serviceId;
        private String uri;
        private Integer order;
        private Set<String> predicates = new HashSet<>();
        private Set<String> filters = new HashSet<>();
        private Boolean enabled;
        private String description;

        public Builder routeId(String routeId) {
            this.routeId = routeId;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder serviceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder order(Integer order) {
            this.order = order;
            return this;
        }

        public Builder predicates(Set<String> predicates) {
            this.predicates = predicates != null ? predicates : new HashSet<>();
            return this;
        }

        public Builder addPredicate(String predicate) {
            if (this.predicates == null) {
                this.predicates = new HashSet<>();
            }
            this.predicates.add(predicate);
            return this;
        }

        public Builder filters(Set<String> filters) {
            this.filters = filters != null ? filters : new HashSet<>();
            return this;
        }

        public Builder addFilter(String filter) {
            if (this.filters == null) {
                this.filters = new HashSet<>();
            }
            this.filters.add(filter);
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public RouteDefinition build() {
            if (routeId == null) {
                throw new IllegalArgumentException("routeId is required");
            }
            if (path == null) {
                throw new IllegalArgumentException("path is required");
            }
            if (uri == null) {
                throw new IllegalArgumentException("uri is required");
            }
            return new RouteDefinition(this);
        }
    }

    @Override
    public String toString() {
        return "RouteDefinition{" +
                "routeId='" + routeId + '\'' +
                ", path='" + path + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
