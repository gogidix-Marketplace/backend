package com.gogidix.courier.routingservice.infrastructure.persistence.adapter;

import com.gogidix.courier.routingservice.domain.entity.OptimizedRoute;
import com.gogidix.courier.routingservice.domain.entity.Route;
import com.gogidix.courier.routingservice.domain.repository.RouteRepository;
import com.gogidix.courier.routingservice.infrastructure.persistence.repository.MongoRouteRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of the RouteRepository interface.
 */
@Component
public class RouteRepositoryAdapter implements RouteRepository {

    private final MongoRouteRepository mongoRepository;

    public RouteRepositoryAdapter(MongoRouteRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Route save(Route route) {
        return mongoRepository.save(route);
    }

    @Override
    public Optional<Route> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<Route> findByTenantIdAndRouteId(String tenantId, String routeId) {
        return mongoRepository.findByTenantIdAndRouteId(tenantId, routeId);
    }

    @Override
    public List<Route> findByTenantId(String tenantId) {
        return mongoRepository.findByTenantId(tenantId);
    }

    @Override
    public List<Route> findByTenantIdAndDriverId(String tenantId, String driverId) {
        return mongoRepository.findByTenantIdAndDriverId(tenantId, driverId);
    }

    @Override
    public List<Route> findActiveByTenantIdAndDriverId(String tenantId, String driverId) {
        return mongoRepository.findByTenantIdAndDriverIdAndStatusIn(
                tenantId,
                driverId,
                List.of(Route.RouteStatus.IN_PROGRESS, Route.RouteStatus.PAUSED)
        );
    }

    @Override
    public List<Route> findByTenantIdAndStatus(String tenantId, Route.RouteStatus status) {
        return mongoRepository.findByTenantIdAndStatus(tenantId, status);
    }

    @Override
    public List<Route> findByTenantIdAndPriority(String tenantId, Route.RoutePriority priority) {
        return mongoRepository.findByTenantIdAndPriority(tenantId, priority);
    }

    @Override
    public List<Route> findByTenantIdAndVehicleType(String tenantId, Route.VehicleType vehicleType) {
        return mongoRepository.findByTenantIdAndVehicleType(tenantId, vehicleType);
    }

    @Override
    public List<Route> findByTenantIdAndStartDateBetween(String tenantId, Instant startDate, Instant endDate) {
        return mongoRepository.findByTenantIdAndStartDateBetween(tenantId, startDate, endDate);
    }

    @Override
    public List<Route> findByTenantIdAndOrderIdsContaining(String tenantId, String orderId) {
        return mongoRepository.findByTenantIdAndOrderIdsContaining(tenantId, orderId);
    }

    @Override
    public List<Route> findPendingByTenantId(String tenantId) {
        return mongoRepository.findByTenantIdAndStatus(tenantId, Route.RouteStatus.PENDING);
    }

    @Override
    public List<Route> findInProgressByTenantId(String tenantId) {
        return mongoRepository.findByTenantIdAndStatus(tenantId, Route.RouteStatus.IN_PROGRESS);
    }

    @Override
    public List<Route> findCompletedByTenantIdAndCompletedAtBetween(String tenantId, Instant startDate, Instant endDate) {
        return mongoRepository.findByTenantIdAndStatusAndCompletedAtBetween(
                tenantId,
                Route.RouteStatus.COMPLETED,
                startDate,
                endDate
        );
    }

    @Override
    public List<Route> findByCriteria(String tenantId, String driverId, Route.RouteStatus status,
                                      Route.RoutePriority priority, Instant startDate, Instant endDate,
                                      int page, int size) {
        // Simplified implementation - in production, use MongoDB query DSL
        List<Route> routes = mongoRepository.findByTenantId(tenantId);
        return routes.stream()
                .skip((long) page * size)
                .limit(size)
                .toList();
    }

    @Override
    public long countByTenantId(String tenantId) {
        return mongoRepository.countByTenantId(tenantId);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, Route.RouteStatus status) {
        return mongoRepository.countByTenantIdAndStatus(tenantId, status);
    }

    @Override
    public long countByTenantIdAndDriverIdAndStatus(String tenantId, String driverId, Route.RouteStatus status) {
        return mongoRepository.countByTenantIdAndDriverIdAndStatus(tenantId, driverId, status);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public boolean existsByTenantIdAndRouteId(String tenantId, String routeId) {
        return mongoRepository.existsByTenantIdAndRouteId(tenantId, routeId);
    }

    @Override
    public List<Route> findAll() {
        return mongoRepository.findAll();
    }

    @Override
    public OptimizedRoute saveOptimizedRoute(OptimizedRoute optimizedRoute) {
        // OptimizedRoute operations require separate collection handling
        throw new UnsupportedOperationException("OptimizedRoute operations not yet implemented");
    }

    @Override
    public Optional<OptimizedRoute> findOptimizedRouteById(String id) {
        return Optional.empty();
    }

    @Override
    public List<OptimizedRoute> findOptimizedRoutesByTenantIdAndOriginalRouteId(String tenantId, String originalRouteId) {
        return List.of();
    }

    @Override
    public List<OptimizedRoute> findOptimizedRoutesByOriginalRouteId(String originalRouteId) {
        return List.of();
    }

    @Override
    public Optional<OptimizedRoute> findLatestOptimizedRouteByTenantIdAndOriginalRouteId(String tenantId, String originalRouteId) {
        return Optional.empty();
    }

    @Override
    public List<OptimizedRoute> findOptimizedRoutesByTenantIdAndStatus(String tenantId, OptimizedRoute.OptimizationStatus status) {
        return List.of();
    }

    @Override
    public void deleteOptimizedRouteById(String id) {
        // No-op for now
    }

    @Override
    public long countOptimizedRoutesByTenantId(String tenantId) {
        return 0;
    }
}
