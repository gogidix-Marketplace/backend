package com.gogidix.customersupport.ticketmanagement.application.service;

import com.gogidix.customersupport.ticketmanagement.application.dto.TicketRequestDto;
import com.gogidix.customersupport.ticketmanagement.application.dto.TicketResponseDto;
import com.gogidix.customersupport.ticketmanagement.application.dto.TicketUpdateRequestDto;
import com.gogidix.customersupport.ticketmanagement.application.mapper.TicketMapper;
import com.gogidix.customersupport.ticketmanagement.domain.model.Ticket;
import com.gogidix.customersupport.ticketmanagement.domain.model.TicketAuditLog;
import com.gogidix.customersupport.ticketmanagement.domain.repository.TicketAuditLogRepository;
import com.gogidix.customersupport.ticketmanagement.domain.repository.TicketRepository;
import com.gogidix.customersupport.ticketmanagement.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketManagementService {

    private final TicketRepository ticketRepository;
    private final TicketAuditLogRepository auditLogRepository;
    private final TicketMapper ticketMapper;

    public Page<TicketResponseDto> getAllTickets(Pageable pageable) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all tickets for tenant: {}", tenantId);
        return ticketRepository.findByTenantId(tenantId, pageable)
                .map(ticketMapper::toResponseDto);
    }

    public TicketResponseDto getTicketById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching ticket with id: {} for tenant: {}", id, tenantId);
        Ticket ticket = ticketRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));
        return ticketMapper.toResponseDto(ticket);
    }

    public TicketResponseDto getTicketByNumber(String ticketNumber) {
        log.debug("Fetching ticket with number: {}", ticketNumber);
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with number: " + ticketNumber));
        return ticketMapper.toResponseDto(ticket);
    }

    public List<TicketResponseDto> getTicketsByCustomerId(String customerId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching tickets for customer: {} and tenant: {}", customerId, tenantId);
        return ticketRepository.findByTenantIdAndCustomerId(tenantId, customerId).stream()
                .map(ticketMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<TicketResponseDto> getTicketsByAgent(String agentId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching tickets for agent: {} and tenant: {}", agentId, tenantId);
        return ticketRepository.findByTenantIdAndAssignedAgentId(tenantId, agentId).stream()
                .map(ticketMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<TicketResponseDto> getTicketsByStatus(Ticket.TicketStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching tickets with status: {} for tenant: {}", status, tenantId);
        return ticketRepository.findByTenantIdAndStatus(tenantId, status).stream()
                .map(ticketMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<TicketResponseDto> getTicketsByPriority(Ticket.TicketPriority priority) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching tickets with priority: {} for tenant: {}", priority, tenantId);
        return ticketRepository.findByTenantIdAndPriority(tenantId, priority).stream()
                .map(ticketMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<TicketResponseDto> getSlaBreachedTickets() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching SLA breached tickets for tenant: {}", tenantId);
        return ticketRepository.findSlaBreachedTickets(tenantId).stream()
                .map(ticketMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<TicketResponseDto> getOverdueTickets() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching overdue tickets for tenant: {}", tenantId);
        return ticketRepository.findOverdueTickets(
                tenantId,
                Instant.now(),
                Ticket.TicketStatus.OPEN,
                Ticket.TicketStatus.IN_PROGRESS,
                Ticket.TicketStatus.ESCALATED
        ).stream()
                .map(ticketMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Page<TicketResponseDto> searchTickets(String keyword, Pageable pageable) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Searching tickets with keyword: {} for tenant: {}", keyword, tenantId);
        return ticketRepository.searchTickets(tenantId, keyword, pageable)
                .map(ticketMapper::toResponseDto);
    }

    @Transactional
    public TicketResponseDto createTicket(TicketRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Creating new ticket for tenant: {}", tenantId);

        Ticket ticket = ticketMapper.toEntity(request, tenantId);

        ticket.setCustomerName(request.getCustomerName());
        ticket.setCustomerPhone(request.getCustomerPhone());
        ticket.setSource(request.getSource());
        ticket.setSubCategory(request.getSubCategory());
        ticket.setTags(request.getTags());
        ticket.setWatchers(request.getWatchers());
        ticket.setRelatedTicketIds(request.getRelatedTicketIds());
        ticket.setParentTicketId(request.getParentTicketId());
        ticket.setSlaPolicyId(request.getSlaPolicyId());

        if (request.getAssignedAgentId() != null) {
            ticket.assignToAgent(request.getAssignedAgentId(), request.getAssignedAgentName());
        }
        if (request.getAssignedTeam() != null) {
            ticket.assignToTeam(request.getAssignedTeam());
        }
        if (request.getDueDate() != null) {
            ticket.setDueDate(request.getDueDate());
        }

        Ticket saved = ticketRepository.save(ticket);

        createAuditLog(saved.getId(), saved.getTicketNumber(), TicketAuditLog.ActionType.TICKET_CREATED,
                "system", "system");

        log.info("Created ticket with number: {} for tenant: {}", saved.getTicketNumber(), tenantId);
        return ticketMapper.toResponseDto(saved);
    }

    @Transactional
    public TicketResponseDto updateTicket(String id, TicketUpdateRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Updating ticket with id: {} for tenant: {}", id, tenantId);

        Ticket ticket = ticketRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));

        ticketMapper.updateEntityFromDto(request, ticket);

        if (request.getAssignedAgentId() != null) {
            if (ticket.getAssignedAgentId() != null && !ticket.getAssignedAgentId().equals(request.getAssignedAgentId())) {
                createAuditLog(ticket.getId(), ticket.getTicketNumber(), TicketAuditLog.ActionType.TICKET_REASSIGNED,
                        request.getAssignedAgentId(), "agent");
            }
            ticket.assignToAgent(request.getAssignedAgentId(), request.getAssignedAgentName());
        }

        if (request.getAssignedTeam() != null) {
            ticket.assignToTeam(request.getAssignedTeam());
        }

        if (request.getStatus() != null) {
            Ticket.TicketStatus newStatus = request.toEntityStatus();
            if (!ticket.getStatus().equals(newStatus)) {
                createAuditLog(ticket.getId(), ticket.getTicketNumber(), TicketAuditLog.ActionType.STATUS_CHANGED,
                        "system", "system");
            }
            ticket.updateStatus(newStatus, "system");
        }

        Ticket updated = ticketRepository.save(ticket);
        log.info("Updated ticket with number: {} for tenant: {}", updated.getTicketNumber(), tenantId);
        return ticketMapper.toResponseDto(updated);
    }

    @Transactional
    public TicketResponseDto assignTicket(String id, String agentId, String agentName) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Assigning ticket: {} to agent: {} for tenant: {}", id, agentId, tenantId);

        Ticket ticket = ticketRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));

        String previousAgent = ticket.getAssignedAgentId();
        ticket.assignToAgent(agentId, agentName);

        TicketAuditLog.ActionType actionType = previousAgent == null
                ? TicketAuditLog.ActionType.TICKET_ASSIGNED
                : TicketAuditLog.ActionType.TICKET_REASSIGNED;

        createAuditLog(ticket.getId(), ticket.getTicketNumber(), actionType, agentId, "agent");

        Ticket updated = ticketRepository.save(ticket);
        log.info("Assigned ticket: {} to agent: {}", updated.getTicketNumber(), agentId);
        return ticketMapper.toResponseDto(updated);
    }

    @Transactional
    public TicketResponseDto updateTicketStatus(String id, Ticket.TicketStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Updating status of ticket: {} to {} for tenant: {}", id, status, tenantId);

        Ticket ticket = ticketRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));

        ticket.updateStatus(status, "system");

        createAuditLog(ticket.getId(), ticket.getTicketNumber(), TicketAuditLog.ActionType.STATUS_CHANGED,
                "system", "system");

        Ticket updated = ticketRepository.save(ticket);
        log.info("Updated ticket: {} status to {}", updated.getTicketNumber(), status);
        return ticketMapper.toResponseDto(updated);
    }

    @Transactional
    public TicketResponseDto escalateTicket(String id, String reason) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Escalating ticket: {} for tenant: {}", id, tenantId);

        Ticket ticket = ticketRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));

        ticket.escalate(reason);

        createAuditLog(ticket.getId(), ticket.getTicketNumber(), TicketAuditLog.ActionType.TICKET_ESCALATED,
                "system", "system");

        Ticket updated = ticketRepository.save(ticket);
        log.info("Escalated ticket: {}", updated.getTicketNumber());
        return ticketMapper.toResponseDto(updated);
    }

    @Transactional
    public TicketResponseDto reopenTicket(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Reopening ticket: {} for tenant: {}", id, tenantId);

        Ticket ticket = ticketRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));

        ticket.reopen();

        createAuditLog(ticket.getId(), ticket.getTicketNumber(), TicketAuditLog.ActionType.TICKET_REOPENED,
                "system", "system");

        Ticket updated = ticketRepository.save(ticket);
        log.info("Reopened ticket: {}", updated.getTicketNumber());
        return ticketMapper.toResponseDto(updated);
    }

    @Transactional
    public TicketResponseDto addResolutionNotes(String id, String notes) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Adding resolution notes to ticket: {} for tenant: {}", id, tenantId);

        Ticket ticket = ticketRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));

        ticket.addResolutionNotes(notes);

        Ticket updated = ticketRepository.save(ticket);
        log.info("Added resolution notes to ticket: {}", updated.getTicketNumber());
        return ticketMapper.toResponseDto(updated);
    }

    @Transactional
    public void deleteTicket(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Deleting ticket with id: {} for tenant: {}", id, tenantId);

        Ticket ticket = ticketRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));

        auditLogRepository.deleteByTicketId(ticket.getId());
        ticketRepository.deleteByTenantIdAndId(tenantId, id);

        log.info("Deleted ticket: {} for tenant: {}", ticket.getTicketNumber(), tenantId);
    }

    public long getTicketCountByStatus(Ticket.TicketStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return ticketRepository.countByTenantIdAndStatus(tenantId, status);
    }

    public long getTicketCountByAgent(String agentId) {
        String tenantId = RequestContextHolder.getTenantId();
        return ticketRepository.countByTenantIdAndAssignedAgentId(tenantId, agentId);
    }

    private void createAuditLog(String ticketId, String ticketNumber, TicketAuditLog.ActionType actionType,
                                 String performedBy, String performedByRole) {
        String tenantId = RequestContextHolder.getTenantId();
        TicketAuditLog log = TicketAuditLog.create(tenantId, ticketId, ticketNumber, actionType, performedBy, performedByRole);
        auditLogRepository.save(log);
    }
}
