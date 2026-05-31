package com.gogidix.aiservices.aigatewayservice.application.mapper;

import com.gogidix.aiservices.aigatewayservice.application.dto.GatewayRouteResponseDto;
import com.gogidix.aiservices.aigatewayservice.domain.model.GatewayRoute;

/**
 * Mapper for converting between GatewayRoute entities and DTOs.
 */
public final class GatewayRouteMapper {

    private GatewayRouteMapper() {
    }

    /**
     * Convert GatewayRoute entity to response DTO.
     *
     * @param route the entity
     * @return the response DTO
     */
    public static GatewayRouteResponseDto toResponseDto(GatewayRoute route) {
        return GatewayRouteResponseDto.from(route);
    }

    /**
     * Convert list of GatewayRoute entities to response DTOs.
     *
     * @param routes the entities
     * @return the response DTOs
     */
    public static java.util.List<GatewayRouteResponseDto> toResponseDtoList(java.util.List<GatewayRoute> routes) {
        return routes.stream()
                .map(GatewayRouteResponseDto::from)
                .toList();
    }
}
