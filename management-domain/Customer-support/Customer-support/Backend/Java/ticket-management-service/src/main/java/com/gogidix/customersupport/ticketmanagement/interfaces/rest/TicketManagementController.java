package com.gogidix.customersupport.ticketmanagement.interfaces.rest;

import com.gogidix.customersupport.ticketmanagement.application.dto.TicketRequestDto;
import com.gogidix.customersupport.ticketmanagement.application.dto.TicketResponseDto;
import com.gogidix.customersupport.ticketmanagement.application.dto.TicketUpdateRequestDto;
import com.gogidix.customersupport.ticketmanagement.application.service.TicketManagementService;
import com.gogidix.customersupport.ticketmanagement.domain.model.Ticket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket Management", description = "APIs for managing support tickets")
public class TicketManagementController {

    private final TicketManagementService ticketManagementService;

    @GetMapping
    @Operation(summary = "Get all tickets", description = "Retrieve all tickets with pagination")
    public ResponseEntity<Page<TicketResponseDto>> getAllTickets(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ticketManagementService.getAllTickets(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by ID", description = "Retrieve a specific ticket by ID")
    public ResponseEntity<TicketResponseDto> getTicketById(
            @Parameter(description = "Ticket ID") @PathVariable String id) {
        return ResponseEntity.ok(ticketManagementService.getTicketById(id));
    }

    @GetMapping("/number/{ticketNumber}")
    @Operation(summary = "Get ticket by number", description = "Retrieve a specific ticket by ticket number")
    public ResponseEntity<TicketResponseDto> getTicketByNumber(
            @Parameter(description = "Ticket number") @PathVariable String ticketNumber) {
        return ResponseEntity.ok(ticketManagementService.getTicketByNumber(ticketNumber));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get tickets by customer", description = "Retrieve all tickets for a customer")
    public ResponseEntity<List<TicketResponseDto>> getTicketsByCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {
        return ResponseEntity.ok(ticketManagementService.getTicketsByCustomerId(customerId));
    }

    @GetMapping("/agent/{agentId}")
    @Operation(summary = "Get tickets by agent", description = "Retrieve all tickets assigned to an agent")
    public ResponseEntity<List<TicketResponseDto>> getTicketsByAgent(
            @Parameter(description = "Agent ID") @PathVariable String agentId) {
        return ResponseEntity.ok(ticketManagementService.getTicketsByAgent(agentId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get tickets by status", description = "Retrieve tickets filtered by status")
    public ResponseEntity<List<TicketResponseDto>> getTicketsByStatus(
            @Parameter(description = "Ticket status") @PathVariable Ticket.TicketStatus status) {
        return ResponseEntity.ok(ticketManagementService.getTicketsByStatus(status));
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "Get tickets by priority", description = "Retrieve tickets filtered by priority")
    public ResponseEntity<List<TicketResponseDto>> getTicketsByPriority(
            @Parameter(description = "Ticket priority") @PathVariable Ticket.TicketPriority priority) {
        return ResponseEntity.ok(ticketManagementService.getTicketsByPriority(priority));
    }

    @GetMapping("/sla-breached")
    @Operation(summary = "Get SLA breached tickets", description = "Retrieve all tickets that have breached SLA")
    public ResponseEntity<List<TicketResponseDto>> getSlaBreachedTickets() {
        return ResponseEntity.ok(ticketManagementService.getSlaBreachedTickets());
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue tickets", description = "Retrieve all overdue tickets")
    public ResponseEntity<List<TicketResponseDto>> getOverdueTickets() {
        return ResponseEntity.ok(ticketManagementService.getOverdueTickets());
    }

    @GetMapping("/search")
    @Operation(summary = "Search tickets", description = "Search tickets by keyword in title, description or ticket number")
    public ResponseEntity<Page<TicketResponseDto>> searchTickets(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ticketManagementService.searchTickets(keyword, pageable));
    }

    @PostMapping
    @Operation(summary = "Create a ticket", description = "Create a new support ticket")
    public ResponseEntity<TicketResponseDto> createTicket(
            @Valid @RequestBody TicketRequestDto request) {
        TicketResponseDto created = ticketManagementService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a ticket", description = "Update an existing ticket")
    public ResponseEntity<TicketResponseDto> updateTicket(
            @Parameter(description = "Ticket ID") @PathVariable String id,
            @Valid @RequestBody TicketUpdateRequestDto request) {
        return ResponseEntity.ok(ticketManagementService.updateTicket(id, request));
    }

    @PutMapping("/{id}/assign")
    @Operation(summary = "Assign ticket to agent", description = "Assign a ticket to an agent")
    public ResponseEntity<TicketResponseDto> assignTicket(
            @Parameter(description = "Ticket ID") @PathVariable String id,
            @Parameter(description = "Agent ID") @RequestParam String agentId,
            @Parameter(description = "Agent name") @RequestParam String agentName) {
        return ResponseEntity.ok(ticketManagementService.assignTicket(id, agentId, agentName));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update ticket status", description = "Update the status of a ticket")
    public ResponseEntity<TicketResponseDto> updateTicketStatus(
            @Parameter(description = "Ticket ID") @PathVariable String id,
            @Parameter(description = "New status") @RequestParam Ticket.TicketStatus status) {
        return ResponseEntity.ok(ticketManagementService.updateTicketStatus(id, status));
    }

    @PutMapping("/{id}/escalate")
    @Operation(summary = "Escalate ticket", description = "Escalate a ticket with a reason")
    public ResponseEntity<TicketResponseDto> escalateTicket(
            @Parameter(description = "Ticket ID") @PathVariable String id,
            @Parameter(description = "Escalation reason") @RequestParam String reason) {
        return ResponseEntity.ok(ticketManagementService.escalateTicket(id, reason));
    }

    @PutMapping("/{id}/reopen")
    @Operation(summary = "Reopen ticket", description = "Reopen a closed or resolved ticket")
    public ResponseEntity<TicketResponseDto> reopenTicket(
            @Parameter(description = "Ticket ID") @PathVariable String id) {
        return ResponseEntity.ok(ticketManagementService.reopenTicket(id));
    }

    @PutMapping("/{id}/resolution-notes")
    @Operation(summary = "Add resolution notes", description = "Add resolution notes to a ticket")
    public ResponseEntity<TicketResponseDto> addResolutionNotes(
            @Parameter(description = "Ticket ID") @PathVariable String id,
            @Parameter(description = "Resolution notes") @RequestParam String notes) {
        return ResponseEntity.ok(ticketManagementService.addResolutionNotes(id, notes));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a ticket", description = "Delete a ticket")
    public ResponseEntity<Void> deleteTicket(
            @Parameter(description = "Ticket ID") @PathVariable String id) {
        ticketManagementService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/count/status/{status}")
    @Operation(summary = "Count tickets by status", description = "Get the count of tickets by status")
    public ResponseEntity<Long> getTicketCountByStatus(
            @Parameter(description = "Ticket status") @PathVariable Ticket.TicketStatus status) {
        return ResponseEntity.ok(ticketManagementService.getTicketCountByStatus(status));
    }

    @GetMapping("/stats/count/agent/{agentId}")
    @Operation(summary = "Count tickets by agent", description = "Get the count of tickets assigned to an agent")
    public ResponseEntity<Long> getTicketCountByAgent(
            @Parameter(description = "Agent ID") @PathVariable String agentId) {
        return ResponseEntity.ok(ticketManagementService.getTicketCountByAgent(agentId));
    }
}
