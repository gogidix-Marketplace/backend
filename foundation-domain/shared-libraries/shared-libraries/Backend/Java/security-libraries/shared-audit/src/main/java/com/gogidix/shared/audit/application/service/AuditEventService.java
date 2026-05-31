package com.gogidix.shared.audit.application.service;

import com.gogidix.shared.audit.application.port.in.AuditEventUseCase;
import com.gogidix.shared.audit.application.port.out.AuditEventRepositoryPort;
import com.gogidix.shared.audit.api.dto.*;
import com.gogidix.shared.audit.api.mapper.AuditEventMapper;
import com.gogidix.shared.audit.domain.AuditEvent;
import com.gogidix.shared.audit.domain.AuditSearchCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuditEventService implements AuditEventUseCase {

    private final AuditEventRepositoryPort auditEventRepository;
    private final AuditEventMapper auditEventMapper;

    @Override
    public AuditEventDTO createAuditEvent(CreateAuditEventDTO request) {
        log.debug("Creating audit event for action: {}", request.getAction());

        AuditEvent auditEvent = AuditEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .userId(request.getUserId())
                .sessionId(request.getSessionId())
                .action(request.getAction())
                .resource(request.getResourceType() != null ? request.getResourceType() : "UNKNOWN")
                .resourceId(request.getResourceId())
                .ipAddress(request.getIpAddress())
                .userAgent(request.getUserAgent())
                .timestamp(LocalDateTime.now())
                .eventType(com.gogidix.shared.audit.domain.AuditEventType.SYSTEM_EVENT)
                .domain(com.gogidix.shared.audit.domain.BusinessDomain.SHARED_INFRASTRUCTURE)
                .result(com.gogidix.shared.audit.domain.AuditResult.SUCCESS)
                .build();

        AuditEvent savedEvent = auditEventRepository.save(auditEvent);
        return auditEventMapper.toDTO(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditEventDTO> searchAuditEvents(AuditSearchDTO searchDTO) {
        log.debug("Searching audit events with criteria: {}", searchDTO);

        AuditSearchCriteria criteria = auditEventMapper.toSearchCriteria(searchDTO);
        List<AuditEvent> events = auditEventRepository.findByCriteria(criteria);

        return events.stream()
                .map(auditEventMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AuditEventDTO getAuditEvent(String eventId) {
        log.debug("Retrieving audit event: {}", eventId);

        AuditEvent event = auditEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Audit event not found: " + eventId));

        return auditEventMapper.toDTO(event);
    }

    @Override
    @Transactional(readOnly = true)
    public AuditStatisticsDTO getAuditStatistics() {
        log.debug("Generating audit statistics");

        long totalEvents = auditEventRepository.countAll();

        return AuditStatisticsDTO.builder()
                .totalEvents(totalEvents)
                .generatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ComplianceReportDTO generateComplianceReport() {
        log.debug("Generating compliance report");

        return ComplianceReportDTO.builder()
                .reportId(UUID.randomUUID().toString())
                .generatedAt(LocalDateTime.now())
                .status("COMPLIANT")
                .build();
    }

    @Override
    public HealthCheckDTO performHealthCheck() {
        log.debug("Performing health check");

        return HealthCheckDTO.builder()
                .status("UP")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
