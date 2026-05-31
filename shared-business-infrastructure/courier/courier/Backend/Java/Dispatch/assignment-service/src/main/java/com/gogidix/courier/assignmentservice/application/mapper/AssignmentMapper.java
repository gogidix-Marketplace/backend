package com.gogidix.courier.assignmentservice.application.mapper;

import com.gogidix.courier.assignmentservice.application.dto.*;
import com.gogidix.courier.assignmentservice.domain.entity.AssignmentHistory;
import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between domain entities and DTOs.
 */
@Component
public class AssignmentMapper {

    /**
     * Convert AssignmentRequest to DriverAssignment domain entity.
     */
    public DriverAssignment toEntity(AssignmentRequest request) {
        if (request == null) {
            return null;
        }
        DriverAssignment assignment = new DriverAssignment(
                request.tenantId(),
                request.dispatchId(),
                request.driverId(),
                toLocationEntity(request.pickupLocation()),
                toLocationEntity(request.deliveryLocation())
        );

        if (request.priority() != null) {
            assignment.setPriority(request.priority());
        }
        if (request.assignmentScore() != null) {
            assignment.updateScore(request.assignmentScore());
        }
        if (request.assignmentReason() != null) {
            assignment.setAssignmentReason(request.assignmentReason());
        }
        if (request.estimatedDistanceKm() != null) {
            assignment.setEstimatedDistanceKm(request.estimatedDistanceKm());
        }
        if (request.estimatedDurationMinutes() != null) {
            assignment.setEstimatedDurationMinutes(request.estimatedDurationMinutes());
        }
        if (request.notes() != null) {
            assignment.setNotes(request.notes());
        }

        return assignment;
    }

    /**
     * Convert DriverAssignment domain entity to AssignmentResponse DTO.
     */
    public AssignmentResponse toResponseDto(DriverAssignment assignment) {
        if (assignment == null) {
            return null;
        }
        return new AssignmentResponse(
                assignment.getId(),
                assignment.getTenantId(),
                assignment.getDispatchId(),
                assignment.getDriverId(),
                assignment.getStatus(),
                assignment.getPriority(),
                toLocationDto(assignment.getPickupLocation()),
                toLocationDto(assignment.getDeliveryLocation()),
                assignment.getEstimatedDistanceKm(),
                assignment.getEstimatedDurationMinutes(),
                assignment.getActualDistanceKm(),
                assignment.getActualDurationMinutes(),
                assignment.getAssignmentScore(),
                assignment.getAssignmentReason(),
                assignment.getAssignedAt(),
                assignment.getAcceptedAt(),
                assignment.getStartedAt(),
                assignment.getCompletedAt(),
                assignment.getCancelledAt(),
                assignment.getCancellationReason(),
                assignment.getNotes(),
                toMetadataDto(assignment.getMetadata()),
                assignment.getCreatedAt(),
                assignment.getUpdatedAt(),
                assignment.getVersion()
        );
    }

    /**
     * Convert Location DTO to Location entity.
     */
    private DriverAssignment.Location toLocationEntity(AssignmentRequest.LocationDto dto) {
        if (dto == null) {
            return null;
        }
        DriverAssignment.Location location = new DriverAssignment.Location();
        location.setLatitude(dto.latitude());
        location.setLongitude(dto.longitude());
        location.setAddress(dto.address());
        location.setCity(dto.city());
        location.setPostalCode(dto.postalCode());
        location.setCountry(dto.country());
        return location;
    }

    /**
     * Convert Location entity to Location DTO.
     */
    private AssignmentResponse.LocationDto toLocationDto(DriverAssignment.Location location) {
        if (location == null) {
            return null;
        }
        return new AssignmentResponse.LocationDto(
                location.getLatitude(),
                location.getLongitude(),
                location.getAddress(),
                location.getCity(),
                location.getPostalCode(),
                location.getCountry()
        );
    }

    /**
     * Convert AssignmentMetadata entity to AssignmentMetadataDto.
     */
    private AssignmentResponse.AssignmentMetadataDto toMetadataDto(DriverAssignment.AssignmentMetadata metadata) {
        if (metadata == null) {
            return null;
        }
        List<AssignmentResponse.ReassignmentRecordDto> reassignmentHistory = null;
        if (metadata.getReassignmentHistory() != null) {
            reassignmentHistory = metadata.getReassignmentHistory().stream()
                    .map(this::toReassignmentRecordDto)
                    .collect(Collectors.toList());
        }
        return new AssignmentResponse.AssignmentMetadataDto(
                metadata.getAssignedBy(),
                metadata.getAssignmentAlgorithm(),
                reassignmentHistory,
                metadata.getAttemptCount(),
                metadata.getPreviousDriverId()
        );
    }

    /**
     * Convert ReassignmentRecord entity to ReassignmentRecordDto.
     */
    private AssignmentResponse.ReassignmentRecordDto toReassignmentRecordDto(
            DriverAssignment.AssignmentMetadata.ReassignmentRecord record) {
        if (record == null) {
            return null;
        }
        return new AssignmentResponse.ReassignmentRecordDto(
                record.getFromDriverId(),
                record.getToDriverId(),
                record.getTimestamp(),
                record.getReason()
        );
    }

    /**
     * Convert list of assignments to response DTOs.
     */
    public List<AssignmentResponse> toResponseDtoList(List<DriverAssignment> assignments) {
        if (assignments == null) {
            return List.of();
        }
        return assignments.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Update assignment entity from request DTO.
     */
    public void updateEntityFromRequest(DriverAssignment assignment, AssignDriverRequest request) {
        if (assignment == null || request == null) {
            return;
        }
        if (request.assignmentScore() != null) {
            assignment.updateScore(request.assignmentScore());
        }
        if (request.reason() != null) {
            assignment.setAssignmentReason(request.reason());
        }
    }
}
