package com.gogidix.courier.etaservice.application.mapper;

import com.gogidix.courier.etaservice.application.dto.*;
import com.gogidix.courier.etaservice.domain.entity.EtaCalculation;
import com.gogidix.courier.etaservice.domain.entity.EtaHistory;
import com.gogidix.courier.etaservice.domain.entity.TrafficFactor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for ETA-related entities and DTOs.
 */
@Component
public class EtaMapper {

    /**
     * Convert EtaRequest to EtaCalculation entity.
     */
    public EtaCalculation toEntity(EtaRequest request) {
        EtaCalculation.Location pickupLocation = toLocationEntity(request.pickupLocation());
        EtaCalculation.Location dropoffLocation = toLocationEntity(request.dropoffLocation());

        return new EtaCalculation(
                request.dispatchId(),
                request.tenantId(),
                pickupLocation,
                dropoffLocation,
                request.vehicleType()
        );
    }

    /**
     * Convert EtaCalculation entity to EtaResponse DTO.
     */
    public EtaResponse toResponseDto(EtaCalculation entity) {
        return new EtaResponse(
                entity.getId(),
                entity.getDispatchId(),
                entity.getTenantId(),
                toLocationDto(entity.getPickupLocation()),
                toLocationDto(entity.getDropoffLocation()),
                toLocationDto(entity.getCurrentLocation()),
                entity.getVehicleType(),
                entity.getDistanceKm(),
                entity.getEtaMinutes(),
                entity.getEstimatedArrival(),
                entity.getTrafficLevel(),
                entity.getTrafficMultiplier(),
                entity.getConfidenceScore(),
                entity.getCalculationMethod(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getRecalculatedAt(),
                entity.getRecalculationCount()
        );
    }

    /**
     * Convert list of EtaCalculation entities to EtaResponse DTOs.
     */
    public List<EtaResponse> toResponseDtoList(List<EtaCalculation> entities) {
        return entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert EtaHistory entity to EtaHistoryResponse DTO.
     */
    public EtaHistoryResponse toHistoryResponseDto(EtaHistory entity) {
        return new EtaHistoryResponse(
                entity.getId(),
                entity.getDispatchId(),
                entity.getTimestamp(),
                entity.getEtaMinutes(),
                entity.getPreviousEtaMinutes(),
                entity.getEtaChangeMinutes(),
                entity.getDistanceKm(),
                toHistoryLocationDto(entity.getCurrentLocation()),
                entity.getTrafficLevel(),
                entity.getTrafficMultiplier(),
                entity.getVehicleType(),
                entity.getConfidenceScore(),
                entity.getChangeReason(),
                entity.getChangeType(),
                entity.getCalculatedBy(),
                entity.getSource()
        );
    }

    /**
     * Convert list of EtaHistory entities to EtaHistoryResponse DTOs.
     */
    public List<EtaHistoryResponse> toHistoryResponseDtoList(List<EtaHistory> entities) {
        return entities.stream()
                .map(this::toHistoryResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert TrafficFactor entity to TrafficFactorDTO.
     */
    public TrafficFactorDTO toTrafficFactorDto(TrafficFactor entity) {
        return new TrafficFactorDTO(
                entity.getId(),
                entity.getTenantId(),
                entity.getAreaCode(),
                entity.getAreaName(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getRadiusKm(),
                entity.getDayOfWeek(),
                entity.getHourOfDay(),
                entity.getTrafficLevel(),
                entity.getMultiplier(),
                entity.getEffectiveMultiplier(),
                entity.getAverageSpeedKmh(),
                entity.getSampleCount(),
                entity.getConfidence(),
                entity.getIsPeakHour(),
                entity.getSeasonalFactor(),
                entity.getWeatherFactor(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getLastObservedAt()
        );
    }

    /**
     * Convert list of TrafficFactor entities to TrafficFactorDTOs.
     */
    public List<TrafficFactorDTO> toTrafficFactorDtoList(List<TrafficFactor> entities) {
        return entities.stream()
                .map(this::toTrafficFactorDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert Location DTO to entity Location.
     */
    public EtaCalculation.Location toLocationEntity(EtaRequest.LocationDto dto) {
        if (dto == null) {
            return null;
        }
        return new EtaCalculation.Location(
                dto.latitude(),
                dto.longitude(),
                dto.address(),
                dto.city(),
                dto.country()
        );
    }

    /**
     * Convert Location DTO to entity Location.
     */
    public EtaCalculation.Location toLocationEntity(RecalculateEtaRequest.LocationDto dto) {
        if (dto == null) {
            return null;
        }
        return new EtaCalculation.Location(
                dto.latitude(),
                dto.longitude(),
                dto.address(),
                dto.city(),
                dto.country()
        );
    }

    /**
     * Convert entity Location to DTO.
     */
    public EtaResponse.LocationDto toLocationDto(EtaCalculation.Location location) {
        if (location == null) {
            return null;
        }
        return new EtaResponse.LocationDto(
                location.getLatitude(),
                location.getLongitude(),
                location.getAddress(),
                location.getCity(),
                location.getCountry()
        );
    }

    /**
     * Convert entity Location to history DTO.
     */
    public EtaHistoryResponse.LocationDto toHistoryLocationDto(EtaCalculation.Location location) {
        if (location == null) {
            return null;
        }
        return new EtaHistoryResponse.LocationDto(
                location.getLatitude(),
                location.getLongitude(),
                location.getAddress(),
                location.getCity(),
                location.getCountry()
        );
    }

    /**
     * Create batch result item for successful calculation.
     */
    public BatchEtaResponse.ResultItem toSuccessResultItem(String dispatchId, EtaResponse eta) {
        return new BatchEtaResponse.ResultItem(dispatchId, true, eta, null);
    }

    /**
     * Create batch result item for failed calculation.
     */
    public BatchEtaResponse.ResultItem toFailureResultItem(String dispatchId, String errorMessage) {
        return new BatchEtaResponse.ResultItem(dispatchId, false, null, errorMessage);
    }
}
