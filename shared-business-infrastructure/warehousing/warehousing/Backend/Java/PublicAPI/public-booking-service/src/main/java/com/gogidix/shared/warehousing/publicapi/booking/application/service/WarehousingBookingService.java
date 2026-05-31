package com.gogidix.shared.warehousing.publicapi.booking.application.service;

import com.gogidix.shared.warehousing.publicapi.booking.domain.entity.WarehousingBookingRequest;
import com.gogidix.shared.warehousing.publicapi.booking.domain.repository.WarehousingBookingRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Application Service for Public Warehousing Bookings
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WarehousingBookingService {

    private final WarehousingBookingRequestRepository bookingRepository;

    public WarehousingBookingRequest createBooking(WarehousingBookingRequest request) {
        log.info("Creating warehousing booking request for customer: {}", request.getCustomerEmail());

        request.setId(UUID.randomUUID().toString());
        request.setBookingNumber(generateBookingNumber());
        request.setStatus(WarehousingBookingRequest.BookingStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());

        return bookingRepository.save(request);
    }

    public WarehousingBookingRequest getByBookingNumber(String tenantId, String bookingNumber) {
        return bookingRepository.findByTenantIdAndBookingNumber(tenantId, bookingNumber)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingNumber));
    }

    public List<WarehousingBookingRequest> getByCustomer(String tenantId, String customerEmail) {
        return bookingRepository.findByTenantIdAndCustomerEmail(tenantId, customerEmail);
    }

    public List<WarehousingBookingRequest> getByStatus(String tenantId, String status) {
        return bookingRepository.findByTenantIdAndStatus(
                tenantId, WarehousingBookingRequest.BookingStatus.valueOf(status));
    }

    public List<WarehousingBookingRequest> getActiveBookings(String tenantId) {
        return bookingRepository.findByTenantIdAndStatus(
                tenantId, WarehousingBookingRequest.BookingStatus.ACTIVE);
    }

    public WarehousingBookingRequest confirmBooking(String bookingId) {
        WarehousingBookingRequest booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));

        booking.confirm();
        return bookingRepository.save(booking);
    }

    public WarehousingBookingRequest activateBooking(String bookingId) {
        WarehousingBookingRequest booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));

        booking.activate();
        return bookingRepository.save(booking);
    }

    public WarehousingBookingRequest completeBooking(String bookingId) {
        WarehousingBookingRequest booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));

        booking.complete();
        return bookingRepository.save(booking);
    }

    public WarehousingBookingRequest cancelBooking(String bookingId, String reason) {
        WarehousingBookingRequest booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));

        booking.cancel(reason);
        return bookingRepository.save(booking);
    }

    private String generateBookingNumber() {
        return "WH-" + System.currentTimeMillis();
    }
}
