package com.gogidix.courier.availabilityservice.application.mapper;

import com.gogidix.courier.availabilityservice.application.dto.*;
import com.gogidix.courier.availabilityservice.domain.entity.AvailabilitySlot;
import com.gogidix.courier.availabilityservice.domain.entity.DriverAvailability;
import com.gogidix.courier.availabilityservice.domain.entity.UnavailablePeriod;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for availability-related DTOs.
 */
@Component
public class AvailabilityMapper {

    public DriverAvailability toEntity(String tenantId, DriverAvailabilityRequest request) {
        DriverAvailability availability = new DriverAvailability(
                tenantId,
                request.driverId(),
                request.date()
        );
        if (request.maxCapacity() != null) {
            availability.setMaxCapacity(request.maxCapacity());
        }
        if (request.preferredZones() != null) {
            availability.setPreferredZones(request.preferredZones());
        }
        return availability;
    }

    public AvailabilitySlot toSlotEntity(String tenantId, SlotRequest request) {
        return new AvailabilitySlot(
                tenantId,
                request.driverId(),
                request.date(),
                request.startTime(),
                request.endTime()
        );
    }

    public UnavailablePeriod toUnavailablePeriodEntity(String tenantId, UnavailablePeriodRequest request) {
        LocalDateTime startDateTime = LocalDateTime.of(request.startDate(), request.startTime());
        LocalDateTime endDateTime = LocalDateTime.of(request.endDate(), request.endTime());

        UnavailablePeriod period = new UnavailablePeriod(
                tenantId,
                request.driverId(),
                startDateTime,
                endDateTime,
                request.reason(),
                request.reasonType() != null ? request.reasonType() : UnavailablePeriod.UnavailabilityReasonType.OTHER
        );

        if (Boolean.TRUE.equals(request.isRecurring())) {
            period.setRecurring(request.recurrencePattern());
        }

        return period;
    }

    public DriverAvailabilityResponse toResponseDto(DriverAvailability availability) {
        List<DriverAvailabilityResponse.SlotResponse> slotResponses = availability.getSlots().stream()
                .map(slot -> new DriverAvailabilityResponse.SlotResponse(
                        slot.getStartTime().toString(),
                        slot.getEndTime().toString(),
                        slot.getStatus().name()
                ))
                .collect(Collectors.toList());

        DriverAvailabilityResponse.LocationResponse locationResponse = null;
        if (availability.getCurrentLocation() != null) {
            locationResponse = new DriverAvailabilityResponse.LocationResponse(
                    availability.getCurrentLocation().getLatitude(),
                    availability.getCurrentLocation().getLongitude(),
                    availability.getCurrentLocation().getUpdatedAt().toString()
            );
        }

        return new DriverAvailabilityResponse(
                availability.getId(),
                availability.getTenantId(),
                availability.getDriverId(),
                availability.getDate().toString(),
                availability.getStatus(),
                slotResponses,
                availability.getMaxCapacity(),
                availability.getCurrentLoad(),
                availability.getPreferredZones(),
                locationResponse,
                availability.getCreatedAt(),
                availability.getUpdatedAt()
        );
    }

    public SlotResponse toSlotResponseDto(AvailabilitySlot slot) {
        return new SlotResponse(
                slot.getId(),
                slot.getTenantId(),
                slot.getDriverId(),
                slot.getDate(),
                slot.getStartTime(),
                slot.getEndTime(),
                slot.getStatus(),
                slot.getBookingId(),
                slot.getZoneId(),
                slot.getCreatedAt(),
                slot.getUpdatedAt()
        );
    }

    public List<SlotResponse> toSlotResponseDtoList(List<AvailabilitySlot> slots) {
        return slots.stream()
                .map(this::toSlotResponseDto)
                .collect(Collectors.toList());
    }

    public AvailableDriverResponse toAvailableDriverResponse(
            String driverId,
            DriverAvailability.AvailabilityStatus status,
            Integer currentLoad,
            Integer maxCapacity,
            LocalDate date,
            DriverAvailability.LocationInfo location) {

        AvailableDriverResponse.LocationInfo locationInfo = null;
        if (location != null) {
            locationInfo = new AvailableDriverResponse.LocationInfo(
                    location.getLatitude(),
                    location.getLongitude(),
                    location.getUpdatedAt().toString()
            );
        }

        return new AvailableDriverResponse(
                driverId,
                status.name(),
                currentLoad,
                maxCapacity,
                date.toString(),
                locationInfo
        );
    }

    public void updateEntityFromRequest(DriverAvailability availability, DriverAvailabilityRequest request) {
        if (request.maxCapacity() != null) {
            availability.setMaxCapacity(request.maxCapacity());
        }
        if (request.preferredZones() != null) {
            availability.setPreferredZones(request.preferredZones());
        }
    }
}
