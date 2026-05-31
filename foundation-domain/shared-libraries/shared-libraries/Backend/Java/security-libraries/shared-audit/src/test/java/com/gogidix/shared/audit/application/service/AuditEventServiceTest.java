package com.gogidix.shared.audit.application.service;

import com.gogidix.shared.audit.api.dto.*;
import com.gogidix.shared.audit.api.mapper.AuditEventMapper;
import com.gogidix.shared.audit.application.port.out.AuditEventRepositoryPort;
import com.gogidix.shared.audit.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditEventServiceTest {

    @Mock
    private AuditEventRepositoryPort auditEventRepository;

    @Mock
    private AuditEventMapper auditEventMapper;

    @InjectMocks
    private AuditEventService auditEventService;

    private CreateAuditEventDTO buildCreateRequest(String resourceType) {
        return CreateAuditEventDTO.builder()
                .userId("user1")
                .sessionId("session1")
                .action("CREATE")
                .resourceType(resourceType)
                .resourceId("res-1")
                .ipAddress("127.0.0.1")
                .userAgent("test-agent")
                .build();
    }

    @Test
    void createAuditEvent_withResourceType() {
        CreateAuditEventDTO request = buildCreateRequest("ORDER");
        AuditEvent savedEvent = AuditEvent.builder().eventId("evt-1").userId("user1").resource("ORDER").build();
        AuditEventDTO expectedDTO = AuditEventDTO.builder().eventId("evt-1").userId("user1").build();

        when(auditEventRepository.save(any(AuditEvent.class))).thenReturn(savedEvent);
        when(auditEventMapper.toDTO(any(AuditEvent.class))).thenReturn(expectedDTO);

        AuditEventDTO result = auditEventService.createAuditEvent(request);

        assertNotNull(result);
        assertEquals("evt-1", result.getEventId());
        verify(auditEventRepository).save(argThat(event ->
                "ORDER".equals(event.getResource())
        ));
    }

    @Test
    void createAuditEvent_withoutResourceType_defaultsToUnknown() {
        CreateAuditEventDTO request = buildCreateRequest(null);
        AuditEvent savedEvent = AuditEvent.builder().eventId("evt-2").userId("user1").resource("UNKNOWN").build();
        AuditEventDTO expectedDTO = AuditEventDTO.builder().eventId("evt-2").userId("user1").build();

        when(auditEventRepository.save(any(AuditEvent.class))).thenReturn(savedEvent);
        when(auditEventMapper.toDTO(any(AuditEvent.class))).thenReturn(expectedDTO);

        AuditEventDTO result = auditEventService.createAuditEvent(request);

        assertNotNull(result);
        verify(auditEventRepository).save(argThat(event ->
                "UNKNOWN".equals(event.getResource())
        ));
    }

    @Test
    void searchAuditEvents_returnsMappedResults() {
        AuditSearchDTO searchDTO = AuditSearchDTO.builder().userId("user1").build();
        AuditSearchCriteria criteria = AuditSearchCriteria.builder().userId("user1").build();
        AuditEvent event = AuditEvent.builder().eventId("evt-1").build();
        AuditEventDTO dto = AuditEventDTO.builder().eventId("evt-1").build();

        when(auditEventMapper.toSearchCriteria(searchDTO)).thenReturn(criteria);
        when(auditEventRepository.findByCriteria(criteria)).thenReturn(List.of(event));
        when(auditEventMapper.toDTO(event)).thenReturn(dto);

        List<AuditEventDTO> results = auditEventService.searchAuditEvents(searchDTO);

        assertEquals(1, results.size());
        assertEquals("evt-1", results.get(0).getEventId());
    }

    @Test
    void searchAuditEvents_returnsEmptyList() {
        AuditSearchDTO searchDTO = AuditSearchDTO.builder().build();
        AuditSearchCriteria criteria = AuditSearchCriteria.builder().build();

        when(auditEventMapper.toSearchCriteria(searchDTO)).thenReturn(criteria);
        when(auditEventRepository.findByCriteria(criteria)).thenReturn(Collections.emptyList());

        List<AuditEventDTO> results = auditEventService.searchAuditEvents(searchDTO);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void getAuditEvent_found() {
        AuditEvent event = AuditEvent.builder().eventId("evt-1").userId("user1").build();
        AuditEventDTO dto = AuditEventDTO.builder().eventId("evt-1").userId("user1").build();

        when(auditEventRepository.findById("evt-1")).thenReturn(Optional.of(event));
        when(auditEventMapper.toDTO(event)).thenReturn(dto);

        AuditEventDTO result = auditEventService.getAuditEvent("evt-1");

        assertNotNull(result);
        assertEquals("evt-1", result.getEventId());
        assertEquals("user1", result.getUserId());
    }

    @Test
    void getAuditEvent_notFound_throwsRuntimeException() {
        when(auditEventRepository.findById("missing")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> auditEventService.getAuditEvent("missing"));
        assertTrue(exception.getMessage().contains("missing"));
    }

    @Test
    void getAuditStatistics_returnsStats() {
        when(auditEventRepository.countAll()).thenReturn(42L);

        AuditStatisticsDTO result = auditEventService.getAuditStatistics();

        assertNotNull(result);
        assertEquals(42L, result.getTotalEvents());
        assertNotNull(result.getGeneratedAt());
    }

    @Test
    void getAuditStatistics_zeroEvents() {
        when(auditEventRepository.countAll()).thenReturn(0L);

        AuditStatisticsDTO result = auditEventService.getAuditStatistics();

        assertNotNull(result);
        assertEquals(0L, result.getTotalEvents());
    }

    @Test
    void generateComplianceReport_returnsReport() {
        ComplianceReportDTO result = auditEventService.generateComplianceReport();

        assertNotNull(result);
        assertNotNull(result.getReportId());
        assertNotNull(result.getGeneratedAt());
        assertEquals("COMPLIANT", result.getStatus());
    }

    @Test
    void performHealthCheck_returnsUpStatus() {
        HealthCheckDTO result = auditEventService.performHealthCheck();

        assertNotNull(result);
        assertEquals("UP", result.getStatus());
        assertNotNull(result.getTimestamp());
    }
}