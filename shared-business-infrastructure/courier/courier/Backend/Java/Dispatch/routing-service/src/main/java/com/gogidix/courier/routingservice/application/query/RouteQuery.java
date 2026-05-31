package com.gogidix.courier.routingservice.application.query;

import com.gogidix.courier.routingservice.domain.entity.Route;

import java.time.Instant;

/**
 * Query object for route searches.
 * Provides filtering and pagination capabilities.
 */
public class RouteQuery {

    private String tenantId;
    private String driverId;
    private Route.RouteStatus status;
    private Route.RoutePriority priority;
    private Route.VehicleType vehicleType;
    private Instant startDateFrom;
    private Instant startDateTo;
    private Instant endDateFrom;
    private Instant endDateTo;
    private String orderId;
    private Boolean active;
    private Integer page = 0;
    private Integer size = 20;
    private String sortBy = "createdAt";
    private String sortDirection = "DESC";

    public RouteQuery() {
    }

    public RouteQuery(String tenantId) {
        this.tenantId = tenantId;
    }

    // Builder-style methods
    public RouteQuery withTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public RouteQuery withDriverId(String driverId) {
        this.driverId = driverId;
        return this;
    }

    public RouteQuery withStatus(Route.RouteStatus status) {
        this.status = status;
        return this;
    }

    public RouteQuery withPriority(Route.RoutePriority priority) {
        this.priority = priority;
        return this;
    }

    public RouteQuery withVehicleType(Route.VehicleType vehicleType) {
        this.vehicleType = vehicleType;
        return this;
    }

    public RouteQuery withStartDateFrom(Instant startDateFrom) {
        this.startDateFrom = startDateFrom;
        return this;
    }

    public RouteQuery withStartDateTo(Instant startDateTo) {
        this.startDateTo = startDateTo;
        return this;
    }

    public RouteQuery withEndDateFrom(Instant endDateFrom) {
        this.endDateFrom = endDateFrom;
        return this;
    }

    public RouteQuery withEndDateTo(Instant endDateTo) {
        this.endDateTo = endDateTo;
        return this;
    }

    public RouteQuery withOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public RouteQuery withActive(Boolean active) {
        this.active = active;
        return this;
    }

    public RouteQuery withPage(Integer page) {
        this.page = page != null ? page : 0;
        return this;
    }

    public RouteQuery withSize(Integer size) {
        this.size = size != null ? size : 20;
        return this;
    }

    public RouteQuery withSortBy(String sortBy) {
        this.sortBy = sortBy != null ? sortBy : "createdAt";
        return this;
    }

    public RouteQuery withSortDirection(String sortDirection) {
        this.sortDirection = sortDirection != null ? sortDirection : "DESC";
        return this;
    }

    // Getters
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public Route.RouteStatus getStatus() {
        return status;
    }

    public void setStatus(Route.RouteStatus status) {
        this.status = status;
    }

    public Route.RoutePriority getPriority() {
        return priority;
    }

    public void setPriority(Route.RoutePriority priority) {
        this.priority = priority;
    }

    public Route.VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Route.VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Instant getStartDateFrom() {
        return startDateFrom;
    }

    public void setStartDateFrom(Instant startDateFrom) {
        this.startDateFrom = startDateFrom;
    }

    public Instant getStartDateTo() {
        return startDateTo;
    }

    public void setStartDateTo(Instant startDateTo) {
        this.startDateTo = startDateTo;
    }

    public Instant getEndDateFrom() {
        return endDateFrom;
    }

    public void setEndDateFrom(Instant endDateFrom) {
        this.endDateFrom = endDateFrom;
    }

    public Instant getEndDateTo() {
        return endDateTo;
    }

    public void setEndDateTo(Instant endDateTo) {
        this.endDateTo = endDateTo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page != null ? page : 0;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size != null ? size : 20;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy != null ? sortBy : "createdAt";
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection != null ? sortDirection : "DESC";
    }

    /**
     * Create a query for active routes by driver.
     */
    public static RouteQuery activeRoutesForDriver(String tenantId, String driverId) {
        return new RouteQuery(tenantId)
                .withDriverId(driverId)
                .withActive(true);
    }

    /**
     * Create a query for pending routes.
     */
    public static RouteQuery pendingRoutes(String tenantId) {
        return new RouteQuery(tenantId)
                .withStatus(Route.RouteStatus.PENDING);
    }

    /**
     * Create a query for routes by date range.
     */
    public static RouteQuery routesByDateRange(String tenantId, Instant startDate, Instant endDate) {
        return new RouteQuery(tenantId)
                .withStartDateFrom(startDate)
                .withStartDateTo(endDate);
    }
}
