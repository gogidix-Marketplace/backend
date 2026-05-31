package com.gogidix.shared.warehousing.publicapi.booking.interfaces.rest;

import com.gogidix.shared.warehousing.publicapi.booking.application.service.WarehousingBookingService;
import com.gogidix.shared.warehousing.publicapi.booking.domain.entity.WarehousingBookingRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Public Warehousing Bookings
 */
@RestController
@RequestMapping("/api/v1/public/warehousing/bookings")
@RequiredArgsConstructor
@Tag(name = "Warehousing Bookings", description = "Public APIs for warehousing booking requests")
public class WarehousingBookingController {

    private final WarehousingBookingService bookingService;

    @PostMapping
    @Operation(summary = "Create booking request", description = "Submit a new warehousing booking request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking request created successfully",
                    content = @Content(schema = @Schema(implementation = WarehousingBookingRequest.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<WarehousingBookingRequest> createBooking(
            @Parameter(description = "Booking request details", required = true)
            @Valid @RequestBody WarehousingBookingRequest request) {
        WarehousingBookingRequest created = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{bookingNumber}")
    @Operation(summary = "Get booking by number", description = "Retrieve a booking by its booking number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking found"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    public ResponseEntity<WarehousingBookingRequest> getByBookingNumber(
            @Parameter(description = "Booking number", required = true, example = "WBK-2024-001")
            @PathVariable String bookingNumber,
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId) {
        WarehousingBookingRequest booking = bookingService.getByBookingNumber(tenantId, bookingNumber);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/customer/{customerEmail}")
    @Operation(summary = "Get bookings by customer", description = "Retrieve all bookings for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully")
    })
    public ResponseEntity<List<WarehousingBookingRequest>> getByCustomer(
            @Parameter(description = "Customer email", required = true, example = "customer@example.com")
            @PathVariable String customerEmail,
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId) {
        List<WarehousingBookingRequest> bookings = bookingService.getByCustomer(tenantId, customerEmail);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get bookings by status", description = "Retrieve all bookings with a specific status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully")
    })
    public ResponseEntity<List<WarehousingBookingRequest>> getByStatus(
            @Parameter(description = "Booking status", required = true, example = "PENDING")
            @PathVariable String status,
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId) {
        List<WarehousingBookingRequest> bookings = bookingService.getByStatus(tenantId, status);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping("/{bookingId}/confirm")
    @Operation(summary = "Confirm booking", description = "Confirm a pending booking request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking confirmed successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    public ResponseEntity<WarehousingBookingRequest> confirmBooking(
            @Parameter(description = "Booking ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String bookingId) {
        WarehousingBookingRequest booking = bookingService.confirmBooking(bookingId);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/{bookingId}/activate")
    @Operation(summary = "Activate booking", description = "Activate a confirmed booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking activated successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    public ResponseEntity<WarehousingBookingRequest> activateBooking(
            @Parameter(description = "Booking ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String bookingId) {
        WarehousingBookingRequest booking = bookingService.activateBooking(bookingId);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/{bookingId}/complete")
    @Operation(summary = "Complete booking", description = "Mark a booking as completed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking completed successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    public ResponseEntity<WarehousingBookingRequest> completeBooking(
            @Parameter(description = "Booking ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String bookingId) {
        WarehousingBookingRequest booking = bookingService.completeBooking(bookingId);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/{bookingId}/cancel")
    @Operation(summary = "Cancel booking", description = "Cancel a booking request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    public ResponseEntity<WarehousingBookingRequest> cancelBooking(
            @Parameter(description = "Booking ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String bookingId,
            @Parameter(description = "Cancellation reason", required = true, example = "Customer requested")
            @RequestParam String reason) {
        WarehousingBookingRequest booking = bookingService.cancelBooking(bookingId, reason);
        return ResponseEntity.ok(booking);
    }
}
