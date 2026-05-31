package com.gogidix.courier.routingservice.infrastructure.persistence.repository;

import com.gogidix.courier.routingservice.domain.entity.Route;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for Route entity.
 */
public interface MongoRouteRepository extends MongoRepository<Route, String> {

    // Route queries
    Optional<Route> findByTenantIdAndRouteId(String tenantId, String routeId);
    List<Route> findByTenantId(String tenantId);
    List<Route> findByTenantIdAndDriverId(String tenantId, String driverId);
    List<Route> findByTenantIdAndDriverIdAndStatusIn(String tenantId, String driverId, List<Route.RouteStatus> statuses);
    List<Route> findByTenantIdAndStatus(String tenantId, Route.RouteStatus status);
    List<Route> findByTenantIdAndPriority(String tenantId, Route.RoutePriority priority);
    List<Route> findByTenantIdAndVehicleType(String tenantId, Route.VehicleType vehicleType);
    List<Route> findByTenantIdAndStartDateBetween(String tenantId, Instant startDate, Instant endDate);
    List<Route> findByTenantIdAndOrderIdsContaining(String tenantId, String orderId);
    List<Route> findByTenantIdAndStatusAndCompletedAtBetween(String tenantId, Route.RouteStatus status, Instant startDate, Instant endDate);

    long countByTenantId(String tenantId);
    long countByTenantIdAndStatus(String tenantId, Route.RouteStatus status);
    long countByTenantIdAndDriverIdAndStatus(String tenantId, String driverId, Route.RouteStatus status);

    boolean existsByTenantIdAndRouteId(String tenantId, String routeId);
}
