package com.gogidix.shared.infrastructure.services.gateway.apigateway.domain.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Domain model representing a route configuration in the API Gateway.
 * Encapsulates the routing rules for directing requests to backend services.
 */
public class Route {

    private final String routeId;
    private final String path;
    private final String serviceId;
    private final String uri;
    private final int order;
    private final Set<String> predicates;
    private final Set<String> filters;
    private final boolean enabled;
    private final String description;
    private final Instant createdAt;
    private Instant updatedAt;

    private Route(Builder builder) {
        this.routeId = builder.routeId;
        this.path = builder.path;
        this.serviceId = builder.serviceId;
        this.uri = builder.uri;
        this.order = builder.order;
        this.predicates = new HashSet<>(builder.predicates);
        this.filters = new HashSet<>(builder.filters);
        this.enabled = builder.enabled;
        this.description = builder.description;
        this.createdAt = builder.createdAt != null ? builder.createdAt : Instant.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : Instant.now();
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

    public int getOrder() {
        return order;
    }

    public Set<String> getPredicates() {
        return new HashSet<>(predicates);
    }

    public Set<String> getFilters() {
        return new HashSet<>(filters);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Check if this route matches the given path.
     */
    public boolean matches(String requestPath) {
        if (path.endsWith("/**")) {
            String prefix = path.substring(0, path.length() - 3);
            return requestPath.startsWith(prefix);
        }
        return path.equals(requestPath);
    }

    /**
     * Check if this route is for a specific service.
     */
    public boolean isForService(String serviceId) {
        return this.serviceId != null && this.serviceId.equals(serviceId);
    }

    public Builder toBuilder() {
        return new Builder()
                .routeId(this.routeId)
                .path(this.path)
                .serviceId(this.serviceId)
                .uri(this.uri)
                .order(this.order)
                .predicates(this.predicates)
                .filters(this.filters)
                .enabled(this.enabled)
                .description(this.description)
                .createdAt(this.createdAt)
                .updatedAt(Instant.now());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String routeId;
        private String path;
        private String serviceId;
        private String uri;
        private int order = 0;
        private Set<String> predicates = new HashSet<>();
        private Set<String> filters = new HashSet<>();
        private boolean enabled = true;
        private String description;
        private Instant createdAt;
        private Instant updatedAt;

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

        public Builder order(int order) {
            this.order = order;
            return this;
        }

        public Builder predicates(Set<String> predicates) {
            this.predicates = predicates != null ? predicates : new HashSet<>();
            return this;
        }

        public Builder addPredicate(String predicate) {
            this.predicates.add(predicate);
            return this;
        }

        public Builder filters(Set<String> filters) {
            this.filters = filters != null ? filters : new HashSet<>();
            return this;
        }

        public Builder addFilter(String filter) {
            this.filters.add(filter);
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Route build() {
            if (routeId == null) {
                throw new IllegalArgumentException("routeId is required");
            }
            if (path == null) {
                throw new IllegalArgumentException("path is required");
            }
            if (uri == null) {
                throw new IllegalArgumentException("uri is required");
            }
            return new Route(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;
        Route route = (Route) o;
        return routeId.equals(route.routeId);
    }

    @Override
    public int hashCode() {
        return routeId.hashCode();
    }

    @Override
    public String toString() {
        return "Route{" +
                "routeId='" + routeId + '\'' +
                ", path='" + path + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", uri='" + uri + '\'' +
                ", order=" + order +
                ", enabled=" + enabled +
                '}';
    }
}
