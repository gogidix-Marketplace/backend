package com.gogidix.customersupport.customerportal.interfaces.rest;

import com.gogidix.customersupport.customerportal.application.dto.CustomerProfileRequestDto;
import com.gogidix.customersupport.customerportal.application.dto.CustomerProfileResponseDto;
import com.gogidix.customersupport.customerportal.application.dto.TicketHistoryResponseDto;
import com.gogidix.customersupport.customerportal.application.service.CustomerPortalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/customer-portal")
@RequiredArgsConstructor
@Tag(name = "Customer Portal", description = "APIs for customer self-service portal and ticket history")
public class CustomerPortalController {

    private final CustomerPortalService customerPortalService;

    @GetMapping("/profiles")
    @Operation(summary = "Get all customer profiles", description = "Retrieve all customer profiles")
    public ResponseEntity<List<CustomerProfileResponseDto>> getAllProfiles() {
        return ResponseEntity.ok(customerPortalService.getAllProfiles());
    }

    @GetMapping("/profiles/{id}")
    @Operation(summary = "Get profile by ID", description = "Retrieve a specific customer profile by ID")
    public ResponseEntity<CustomerProfileResponseDto> getProfileById(
            @Parameter(description = "Profile ID") @PathVariable String id) {
        return ResponseEntity.ok(customerPortalService.getProfileById(id));
    }

    @GetMapping("/profiles/customer/{customerId}")
    @Operation(summary = "Get profile by customer ID", description = "Retrieve customer profile by customer ID")
    public ResponseEntity<CustomerProfileResponseDto> getProfileByCustomerId(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {
        return ResponseEntity.ok(customerPortalService.getProfileByCustomerId(customerId));
    }

    @PostMapping("/profiles")
    @Operation(summary = "Create customer profile", description = "Create a new customer profile")
    public ResponseEntity<CustomerProfileResponseDto> createProfile(
            @Valid @RequestBody CustomerProfileRequestDto request) {
        CustomerProfileResponseDto created = customerPortalService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/profiles/{id}")
    @Operation(summary = "Update customer profile", description = "Update an existing customer profile")
    public ResponseEntity<CustomerProfileResponseDto> updateProfile(
            @Parameter(description = "Profile ID") @PathVariable String id,
            @Valid @RequestBody CustomerProfileRequestDto request) {
        return ResponseEntity.ok(customerPortalService.updateProfile(id, request));
    }

    @DeleteMapping("/profiles/{id}")
    @Operation(summary = "Delete customer profile", description = "Delete a customer profile")
    public ResponseEntity<Void> deleteProfile(
            @Parameter(description = "Profile ID") @PathVariable String id) {
        customerPortalService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customers/{customerId}/tickets")
    @Operation(summary = "Get customer ticket history", description = "Retrieve ticket history for a customer")
    public ResponseEntity<List<TicketHistoryResponseDto>> getTicketHistory(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {
        return ResponseEntity.ok(customerPortalService.getTicketHistoryByCustomerId(customerId));
    }

    @PostMapping("/customers/{customerId}/tickets")
    @Operation(summary = "Record ticket history", description = "Record a new ticket history entry")
    public ResponseEntity<TicketHistoryResponseDto> recordTicketHistory(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @Parameter(description = "Ticket ID") @RequestParam String ticketId,
            @Parameter(description = "Ticket number") @RequestParam String ticketNumber,
            @Parameter(description = "Title") @RequestParam String title,
            @Parameter(description = "Description") @RequestParam String description) {
        TicketHistoryResponseDto history = customerPortalService.recordTicketHistory(
                customerId, ticketId, ticketNumber, title, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(history);
    }

    @GetMapping("/customers/{customerId}/tickets/count")
    @Operation(summary = "Count customer tickets", description = "Get total ticket count for a customer")
    public ResponseEntity<Long> getTicketCount(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {
        return ResponseEntity.ok(customerPortalService.getTicketCountByCustomerId(customerId));
    }

    @GetMapping("/customers/{customerId}/tickets/count/status/{status}")
    @Operation(summary = "Count tickets by status", description = "Get ticket count for a customer filtered by status")
    public ResponseEntity<Long> getTicketCountByStatus(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @Parameter(description = "Ticket status") @PathVariable String status) {
        return ResponseEntity.ok(customerPortalService.getTicketCountByCustomerIdAndStatus(customerId, status));
    }
}
