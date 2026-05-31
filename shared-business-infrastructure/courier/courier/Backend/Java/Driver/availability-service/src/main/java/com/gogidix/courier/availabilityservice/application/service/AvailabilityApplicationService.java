package com.gogidix.courier.availabilityservice.application.service;

import com.gogidix.courier.availabilityservice.application.dto.*;
import com.gogidix.courier.availabilityservice.application.mapper.AvailabilityMapper;
import com.gogidix.courier.availabilityservice.application.query.FindAvailableDriversQuery;
import com.gogidix.courier.availabilityservice.domain.entity.AvailabilitySlot;
import com.gogidix.courier.availabilityservice.domain.entity.DriverAvailability;
import com.gogidix.courier.availabilityservice.domain.entity.UnavailablePeriod;
import com.gogidix.courier.availabilityservice.domain.event.SlotBookedEvent;
import com.gogidix.courier.availabilityservice.domain.repository.AvailabilitySlotRepository;
import com.gogidix.courier.availabilityservice.domain.repository.DriverAvailabilityRepository;
import com.gogidix.courier.availabilityservice.domain.repository.UnavailablePeriodRepository;
import com.gogidix.courier.availabilityservice.shared.exception.NotFoundException;
import com.gogidix.courier.availabilityservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Application service for availability operations.
 */
@Service
@Transactional(readOnly = true)
public class AvailabilityApplicationService {

    private static final Logger log = LoggerFactory.getLogger(AvailabilityApplicationService.class);

    private final DriverAvailabilityRepository availabilityRepository;
    private final AvailabilitySlotRepository slotRepository;
    private final UnavailablePeriodRepository periodRepository;
    private final AvailabilityMapper mapper;
    private final AvailabilityEventPublisher eventPublisher;

    public AvailabilityApplicationService(
            DriverAvailabilityRepository availabilityRepository,
            AvailabilitySlotRepository slotRepository,
            UnavailablePeriodRepository periodRepository,
            AvailabilityMapper mapper,
            AvailabilityEventPublisher eventPublisher) {
        this.availabilityRepository = availabilityRepository;
        this.slotRepository = slotRepository;
        this.periodRepository = periodRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Create driver availability.
     */
    @Transactional
    @CacheEvict(value = "driverAvailability", allEntries = true)
    public DriverAvailabilityResponse createAvailability(String tenantId, DriverAvailabilityRequest request) {
        log.info("Creating availability for driver: {} on date: {}", request.driverId(), request.date());

        if (availabilityRepository.findByDriverIdAndDate(request.driverId(), request.date()).isPresent()) {
            throw new ValidationException("Availability already exists for driver on this date");
        }

        DriverAvailability availability = mapper.toEntity(tenantId, request);
        DriverAvailability saved = availabilityRepository.save(availability);

        log.info("Availability created: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Get driver availability by ID.
     */
    @Cacheable(value = "driverAvailability", key = "#id")
    public DriverAvailabilityResponse getAvailability(String id) {
        DriverAvailability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("DriverAvailability", id));
        return mapper.toResponseDto(availability);
    }

    /**
     * Get driver availability by driver ID and date.
     */
    @Cacheable(value = "driverAvailability", key = "'driver:' + #driverId + ':date:' + #date")
    public DriverAvailabilityResponse getAvailabilityByDriverAndDate(String driverId, LocalDate date) {
        DriverAvailability availability = availabilityRepository.findByDriverIdAndDate(driverId, date)
                .orElseThrow(() -> new NotFoundException("DriverAvailability", driverId + ":" + date));
        return mapper.toResponseDto(availability);
    }

    /**
     * Get driver availability for a date range.
     */
    public List<DriverAvailabilityResponse> getAvailabilityInRange(String driverId, LocalDate startDate, LocalDate endDate) {
        List<DriverAvailability> availabilities = availabilityRepository.findByDriverIdAndDateBetween(
                driverId, startDate, endDate);
        return availabilities.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Find available drivers.
     */
    public List<AvailableDriverResponse> findAvailableDrivers(FindAvailableDriversQuery query) {
        log.debug("Finding available drivers for tenant: {} on date: {}", query.tenantId(), query.date());

        List<String> driverIds;

        if (query.zoneId() != null && !query.zoneId().isBlank()) {
            driverIds = availabilityRepository.findAvailableDriversByZone(
                    query.tenantId(), query.zoneId(), query.date());
        } else {
            driverIds = availabilityRepository.findAvailableDrivers(
                    query.tenantId(), query.date());
        }

        return driverIds.stream()
                .map(driverId -> {
                    var availabilityOpt = availabilityRepository.findByDriverIdAndDate(driverId, query.date());
                    return availabilityOpt.map(availability ->
                            mapper.toAvailableDriverResponse(
                                    driverId,
                                    availability.getStatus(),
                                    availability.getCurrentLoad(),
                                    availability.getMaxCapacity(),
                                    availability.getDate(),
                                    availability.getCurrentLocation()
                            )
                    ).orElse(null);
                })
                .filter(response -> response != null)
                .collect(Collectors.toList());
    }

    /**
     * Add an availability slot.
     */
    @Transactional
    @CacheEvict(value = "driverAvailability", allEntries = true)
    public SlotResponse addSlot(String tenantId, SlotRequest request) {
        log.info("Adding slot for driver: {} on date: {}", request.driverId(), request.date());

        AvailabilitySlot slot = mapper.toSlotEntity(tenantId, request);

        // Check for overlapping slots
        List<AvailabilitySlot> overlapping = slotRepository.findByDriverIdAndDateAndTimeRange(
                request.driverId(),
                request.date(),
                request.startTime(),
                request.endTime()
        );

        if (!overlapping.isEmpty()) {
            throw new ValidationException("Slot overlaps with existing slots");
        }

        AvailabilitySlot saved = slotRepository.save(slot);

        // Update driver availability status
        availabilityRepository.findByDriverIdAndDate(request.driverId(), request.date())
                .ifPresent(availability -> {
                    availability.markAvailable();
                    availabilityRepository.save(availability);
                });

        log.info("Slot created: {}", saved.getId());
        return mapper.toSlotResponseDto(saved);
    }

    /**
     * Book a slot.
     */
    @Transactional
    @CacheEvict(value = "driverAvailability", allEntries = true)
    public void bookSlot(String slotId, String bookingId) {
        log.info("Booking slot: {} for booking: {}", slotId, bookingId);

        AvailabilitySlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new NotFoundException("AvailabilitySlot", slotId));

        if (!slot.isAvailable()) {
            throw new ValidationException("Slot is not available");
        }

        slot.book(bookingId);
        AvailabilitySlot saved = slotRepository.save(slot);

        // Publish event
        eventPublisher.publish(new SlotBookedEvent(
                saved.getId(),
                saved.getTenantId(),
                saved.getDriverId(),
                saved.getDate(),
                saved.getStartTime(),
                saved.getEndTime(),
                bookingId
        ));

        log.info("Slot booked: {}", slotId);
    }

    /**
     * Release a slot.
     */
    @Transactional
    @CacheEvict(value = "driverAvailability", allEntries = true)
    public void releaseSlot(String slotId) {
        log.info("Releasing slot: {}", slotId);

        AvailabilitySlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new NotFoundException("AvailabilitySlot", slotId));

        slot.release();
        slotRepository.save(slot);

        log.info("Slot released: {}", slotId);
    }

    /**
     * Add an unavailable period.
     */
    @Transactional
    @CacheEvict(value = "driverAvailability", allEntries = true)
    public void addUnavailablePeriod(String tenantId, UnavailablePeriodRequest request) {
        log.info("Adding unavailable period for driver: {}", request.driverId());

        UnavailablePeriod period = mapper.toUnavailablePeriodEntity(tenantId, request);
        periodRepository.save(period);

        // Update driver availability status for affected dates
        LocalDate currentDate = request.startDate();
        while (!currentDate.isAfter(request.endDate())) {
            availabilityRepository.findByDriverIdAndDate(request.driverId(), currentDate)
                    .ifPresent(availability -> {
                        availability.addUnavailablePeriod(
                                request.startTime(),
                                request.endTime(),
                                request.reason()
                        );
                        availabilityRepository.save(availability);
                    });
            currentDate = currentDate.plusDays(1);
        }

        log.info("Unavailable period added for driver: {}", request.driverId());
    }

    /**
     * Update driver location.
     */
    @Transactional
    @CacheEvict(value = "driverAvailability", allEntries = true)
    public void updateLocation(String driverId, Double latitude, Double longitude) {
        log.debug("Updating location for driver: {}", driverId);

        availabilityRepository.findByDriverIdAndDate(driverId, LocalDate.now())
                .ifPresent(availability -> {
                    availability.updateLocation(latitude, longitude);
                    availabilityRepository.save(availability);
                });
    }

    /**
     * Delete availability.
     */
    @Transactional
    @CacheEvict(value = "driverAvailability", allEntries = true)
    public void deleteAvailability(String id) {
        log.info("Deleting availability: {}", id);

        if (!availabilityRepository.findById(id).isPresent()) {
            throw new NotFoundException("DriverAvailability", id);
        }

        availabilityRepository.deleteById(id);
        log.info("Availability deleted: {}", id);
    }
}
