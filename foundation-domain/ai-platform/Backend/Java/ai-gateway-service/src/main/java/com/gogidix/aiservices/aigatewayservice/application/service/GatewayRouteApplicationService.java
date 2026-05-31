package com.gogidix.aiservices.aigatewayservice.application.service;

import com.gogidix.aiservices.aigatewayservice.application.command.CreateGatewayRouteCommand;
import com.gogidix.aiservices.aigatewayservice.application.command.DeleteGatewayRouteCommand;
import com.gogidix.aiservices.aigatewayservice.application.command.UpdateGatewayRouteCommand;
import com.gogidix.aiservices.aigatewayservice.application.dto.GatewayRouteResponseDto;
import com.gogidix.aiservices.aigatewayservice.domain.model.GatewayRoute;
import com.gogidix.aiservices.aigatewayservice.domain.model.LoadBalancingStrategy;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteFilter;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteStatus;
import com.gogidix.aiservices.aigatewayservice.domain.port.in.CreateRouteCommand;
import com.gogidix.aiservices.aigatewayservice.domain.port.in.DeleteRouteCommand;
import com.gogidix.aiservices.aigatewayservice.domain.port.in.RouteManagementCommand;
import com.gogidix.aiservices.aigatewayservice.domain.port.in.UpdateRouteCommand;
import com.gogidix.aiservices.aigatewayservice.domain.port.out.RouteEventPublisherPort;
import com.gogidix.aiservices.aigatewayservice.domain.repository.GatewayRouteRepository;
import com.gogidix.aiservices.aigatewayservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aigatewayservice.shared.exception.ValidationException;

import java.util.List;
import java.util.Objects;

/**
 * Application service for managing gateway routes.
 */
public class GatewayRouteApplicationService implements RouteManagementCommand {

    private final GatewayRouteRepository repository;
    private final RouteEventPublisherPort eventPublisher;

    public GatewayRouteApplicationService(
            GatewayRouteRepository repository,
            RouteEventPublisherPort eventPublisher
    ) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    // Create operations
    public GatewayRouteResponseDto createRoute(
            String tenantId,
            String path,
            String targetService,
            List<String> targetUrls,
            List<RouteFilter> filters,
            Integer rateLimit,
            LoadBalancingStrategy loadBalancingStrategy,
            Integer requestTimeout
    ) {
        CreateGatewayRouteCommand command = new CreateGatewayRouteCommand(
                tenantId, path, targetService, targetUrls, filters,
                rateLimit, loadBalancingStrategy, requestTimeout
        );
        GatewayRoute route = createRoute(command);
        return GatewayRouteResponseDto.from(route);
    }

    public GatewayRoute createRoute(CreateGatewayRouteCommand command) {
        GatewayRoute route = new GatewayRoute(
                command.tenantId(),
                command.path(),
                command.targetService(),
                command.targetUrls()
        );

        if (command.filters() != null && !command.filters().isEmpty()) {
            command.filters().forEach(route::addFilter);
        }

        if (command.rateLimit() != null) {
            route.updateRateLimit(command.rateLimit());
        }

        if (command.loadBalancingStrategy() != null) {
            route.setLoadBalancingStrategy(command.loadBalancingStrategy());
        }

        if (command.requestTimeout() != null) {
            route.updateRequestTimeout(command.requestTimeout());
        }

        route.validate();
        GatewayRoute saved = repository.save(route);

        eventPublisher.publish(new com.gogidix.aiservices.aigatewayservice.domain.event.RouteCreatedEvent(
                saved.getRouteId(), saved.getTenantId(), saved.getPath(), saved.getTargetService()
        ));

        return saved;
    }

    // Update operations
    public GatewayRouteResponseDto updateRoute(
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
        UpdateGatewayRouteCommand command = new UpdateGatewayRouteCommand(
                routeId, tenantId, path, targetService, targetUrls, filters,
                rateLimit, loadBalancingStrategy, requestTimeout
        );
        GatewayRoute route = updateRoute(command);
        return GatewayRouteResponseDto.from(route);
    }

    public GatewayRoute updateRoute(UpdateGatewayRouteCommand command) {
        GatewayRoute route = repository.findByRouteIdAndTenantId(command.routeId(), command.tenantId())
                .orElseThrow(() -> new NotFoundException("GatewayRoute", command.routeId()));

        if (command.path() != null) {
            route.setPath(command.path());
        }
        if (command.targetService() != null) {
            route.setTargetService(command.targetService());
        }
        if (command.targetUrls() != null && !command.targetUrls().isEmpty()) {
            route.setTargetUrls(command.targetUrls());
        }
        if (command.filters() != null) {
            route.setFilters(command.filters());
        }
        if (command.rateLimit() != null) {
            route.updateRateLimit(command.rateLimit());
        }
        if (command.loadBalancingStrategy() != null) {
            route.setLoadBalancingStrategy(command.loadBalancingStrategy());
        }
        if (command.requestTimeout() != null) {
            route.updateRequestTimeout(command.requestTimeout());
        }

        route.validate();
        GatewayRoute updated = repository.save(route);

        eventPublisher.publish(new com.gogidix.aiservices.aigatewayservice.domain.event.RouteUpdatedEvent(
                updated.getRouteId(), updated.getTenantId()
        ));

        return updated;
    }

    // Delete operations
    public void deleteRoute(String routeId, String tenantId, String userId) {
        DeleteGatewayRouteCommand command = new DeleteGatewayRouteCommand(routeId, tenantId, userId);
        deleteRoute(command);
    }

    public void deleteRoute(DeleteGatewayRouteCommand command) {
        GatewayRoute route = repository.findByRouteIdAndTenantId(command.routeId(), command.tenantId())
                .orElseThrow(() -> new NotFoundException("GatewayRoute", command.routeId()));

        repository.deleteByRouteIdAndTenantId(command.routeId(), command.tenantId());

        eventPublisher.publish(new com.gogidix.aiservices.aigatewayservice.domain.event.RouteDeletedEvent(
                command.routeId(), command.tenantId()
        ));
    }

    // Query operations
    public GatewayRouteResponseDto getRouteById(String routeId, String tenantId) {
        GatewayRoute route = repository.findByRouteIdAndTenantId(routeId, tenantId)
                .orElseThrow(() -> new NotFoundException("GatewayRoute", routeId));
        return GatewayRouteResponseDto.from(route);
    }

    public List<GatewayRouteResponseDto> getRoutesByTenant(String tenantId) {
        return repository.findByTenantId(tenantId).stream()
                .map(GatewayRouteResponseDto::from)
                .toList();
    }

    public List<GatewayRouteResponseDto> getRoutesByTenantAndStatus(String tenantId, RouteStatus status) {
        return repository.findByTenantIdAndStatus(tenantId, status).stream()
                .map(GatewayRouteResponseDto::from)
                .toList();
    }

    public List<GatewayRouteResponseDto> getActiveRoutes() {
        return repository.findByStatus(RouteStatus.ACTIVE).stream()
                .map(GatewayRouteResponseDto::from)
                .toList();
    }

    // Interface implementation methods - these delegate to the command-specific methods
    @Override
    public GatewayRoute createRoute(CreateRouteCommand command) {
        GatewayRoute route = new GatewayRoute(
                command.tenantId(),
                command.path(),
                command.targetService(),
                command.targetUrls()
        );

        if (command.filters() != null && !command.filters().isEmpty()) {
            command.filters().forEach(route::addFilter);
        }

        if (command.rateLimit() != null) {
            route.updateRateLimit(command.rateLimit());
        }

        if (command.loadBalancingStrategy() != null) {
            route.setLoadBalancingStrategy(command.loadBalancingStrategy());
        }

        if (command.requestTimeout() != null) {
            route.updateRequestTimeout(command.requestTimeout());
        }

        route.validate();
        GatewayRoute saved = repository.save(route);

        eventPublisher.publish(new com.gogidix.aiservices.aigatewayservice.domain.event.RouteCreatedEvent(
                saved.getRouteId(), saved.getTenantId(), saved.getPath(), saved.getTargetService()
        ));

        return saved;
    }

    @Override
    public GatewayRoute updateRoute(UpdateRouteCommand command) {
        GatewayRoute route = repository.findByRouteIdAndTenantId(command.routeId(), command.tenantId())
                .orElseThrow(() -> new NotFoundException("GatewayRoute", command.routeId()));

        if (command.path() != null) {
            route.setPath(command.path());
        }
        if (command.targetService() != null) {
            route.setTargetService(command.targetService());
        }
        if (command.targetUrls() != null && !command.targetUrls().isEmpty()) {
            route.setTargetUrls(command.targetUrls());
        }
        if (command.filters() != null) {
            route.setFilters(command.filters());
        }
        if (command.rateLimit() != null) {
            route.updateRateLimit(command.rateLimit());
        }
        if (command.loadBalancingStrategy() != null) {
            route.setLoadBalancingStrategy(command.loadBalancingStrategy());
        }
        if (command.requestTimeout() != null) {
            route.updateRequestTimeout(command.requestTimeout());
        }

        route.validate();
        GatewayRoute updated = repository.save(route);

        eventPublisher.publish(new com.gogidix.aiservices.aigatewayservice.domain.event.RouteUpdatedEvent(
                updated.getRouteId(), updated.getTenantId()
        ));

        return updated;
    }

    @Override
    public void deleteRoute(DeleteRouteCommand command) {
        GatewayRoute route = repository.findByRouteIdAndTenantId(command.routeId(), command.tenantId())
                .orElseThrow(() -> new NotFoundException("GatewayRoute", command.routeId()));

        repository.deleteByRouteIdAndTenantId(command.routeId(), command.tenantId());

        eventPublisher.publish(new com.gogidix.aiservices.aigatewayservice.domain.event.RouteDeletedEvent(
                command.routeId(), command.tenantId()
        ));
    }

    // Activation/Deactivation
    @Override
    public void activateRoute(String routeId, String tenantId) {
        GatewayRoute route = repository.findByRouteIdAndTenantId(routeId, tenantId)
                .orElseThrow(() -> new NotFoundException("GatewayRoute", routeId));
        route.activate();
        repository.save(route);
    }

    @Override
    public void deactivateRoute(String routeId, String tenantId) {
        GatewayRoute route = repository.findByRouteIdAndTenantId(routeId, tenantId)
                .orElseThrow(() -> new NotFoundException("GatewayRoute", routeId));
        route.deactivate();
        repository.save(route);
    }

    // Circuit breaker operations
    @Override
    public void resetCircuitBreaker(String routeId, String tenantId) {
        GatewayRoute route = repository.findByRouteIdAndTenantId(routeId, tenantId)
                .orElseThrow(() -> new NotFoundException("GatewayRoute", routeId));
        route.resetCircuitBreaker();
        repository.save(route);
    }

    public void recordFailure(String routeId, String tenantId) {
        GatewayRoute route = repository.findByRouteIdAndTenantId(routeId, tenantId)
                .orElseThrow(() -> new NotFoundException("GatewayRoute", routeId));

        boolean wasClosed = !route.isCircuitBreakerOpen();
        route.recordFailure();

        if (wasClosed && route.isCircuitBreakerOpen()) {
            eventPublisher.publish(new com.gogidix.aiservices.aigatewayservice.domain.event.CircuitBreakerTrippedEvent(
                    routeId, tenantId, route.getCircuitBreakerFailureCount()
            ));
        }

        repository.save(route);
    }

    public void recordSuccess(String routeId, String tenantId) {
        GatewayRoute route = repository.findByRouteIdAndTenantId(routeId, tenantId)
                .orElseThrow(() -> new NotFoundException("GatewayRoute", routeId));
        route.recordSuccess();
        repository.save(route);
    }
}
