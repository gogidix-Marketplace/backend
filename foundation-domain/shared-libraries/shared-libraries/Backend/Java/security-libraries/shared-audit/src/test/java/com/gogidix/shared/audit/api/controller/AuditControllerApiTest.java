package com.gogidix.shared.audit.api.controller;

import com.gogidix.shared.audit.api.dto.*;
import com.gogidix.shared.audit.api.mapper.AuditEventMapper;
import com.gogidix.shared.audit.application.SharedAuditService;
import com.gogidix.shared.audit.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditControllerApiTest {

    @Mock
    private SharedAuditService sharedAuditService;

    @Mock
    private AuditEventMapper auditEventMapper;

    private AuditController controller;

    @BeforeEach
    void setUp() {
        controller = new AuditController(sharedAuditService, auditEventMapper);
    }

    private AuditEvent buildTestEvent(String eventId) {
        return AuditEvent.builder()
                .eventId(eventId)
                .userId("user1")
                .action("CREATE")
                .resource("/api/test")
                .result(AuditResult.SUCCESS)
                .eventType(AuditEventType.SYSTEM_EVENT)
                .domain(BusinessDomain.SHARED_INFRASTRUCTURE)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private AuditStatistics buildTestStatistics() {
        return AuditStatistics.builder()
                .periodStart(LocalDateTime.now().minusHours(1))
                .periodEnd(LocalDateTime.now())
                .totalEvents(100)
                .successfulEvents(95)
                .failedEvents(5)
                .securityEvents(2)
                .complianceEvents(10)
                .uniqueUsers(20)
                .uniqueSessions(30)
                .mostActiveUser("user1")
                .mostActiveUserEvents(15)
                .averageEventsPerUser(5.0)
                .averageEventsPerHour(10.0)
                .compliancePercentage(95.0)
                .securityEventPercentage(2.0)
                .criticalSeverityEvents(0)
                .highSeverityEvents(1)
                .mediumSeverityEvents(3)
                .lowSeverityEvents(96)
                .suspiciousPatterns(0)
                .escalationRequiredEvents(0)
                .financialTransactions(5)
                .totalFinancialAmount(10000.0)
                .build();
    }

    @Test
    void recordAuditEvent_success() {
        CreateAuditEventDTO request = CreateAuditEventDTO.builder()
                .userId("user1").sessionId("s1").action("CREATE").build();
        AuditEventCreationRequest creationRequest = AuditEventCreationRequest.builder()
                .userId("user1").action("CREATE")
                .eventType(AuditEventType.SYSTEM_EVENT)
                .domain(BusinessDomain.SHARED_INFRASTRUCTURE)
                .result(AuditResult.SUCCESS)
                .resource("/api/test")
                .build();
        AuditEvent event = buildTestEvent("evt-1");
        AuditEventDTO dto = AuditEventDTO.builder().eventId("evt-1").userId("user1").build();

        when(auditEventMapper.toCreationRequest(request)).thenReturn(creationRequest);
        when(sharedAuditService.recordAuditEvent(creationRequest))
                .thenReturn(CompletableFuture.completedFuture(event));
        when(auditEventMapper.toDTO(event)).thenReturn(dto);

        CompletableFuture<ResponseEntity<AuditEventDTO>> future = controller.recordAuditEvent(request);
        ResponseEntity<AuditEventDTO> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("evt-1", response.getBody().getEventId());
    }

    @Test
    void recordAuditEvent_failure() {
        CreateAuditEventDTO request = CreateAuditEventDTO.builder()
                .userId("user1").sessionId("s1").action("CREATE").build();
        AuditEventCreationRequest creationRequest = AuditEventCreationRequest.builder()
                .userId("user1").action("CREATE").build();

        when(auditEventMapper.toCreationRequest(request)).thenReturn(creationRequest);
        when(sharedAuditService.recordAuditEvent(creationRequest))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("test error")));

        CompletableFuture<ResponseEntity<AuditEventDTO>> future = controller.recordAuditEvent(request);
        ResponseEntity<AuditEventDTO> response = future.join();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void recordBatchAuditEvents_success() {
        CreateAuditEventDTO request = CreateAuditEventDTO.builder()
                .userId("user1").sessionId("s1").action("CREATE").build();
        AuditEventCreationRequest creationRequest = AuditEventCreationRequest.builder()
                .userId("user1").action("CREATE").build();
        AuditEvent event = buildTestEvent("evt-1");
        AuditEventDTO dto = AuditEventDTO.builder().eventId("evt-1").userId("user1").build();

        when(auditEventMapper.toCreationRequest(request)).thenReturn(creationRequest);
        when(sharedAuditService.recordBatchAuditEvents(anyList()))
                .thenReturn(CompletableFuture.completedFuture(List.of(event)));
        when(auditEventMapper.toDTO(event)).thenReturn(dto);

        CompletableFuture<ResponseEntity<List<AuditEventDTO>>> future =
                controller.recordBatchAuditEvents(List.of(request));
        ResponseEntity<List<AuditEventDTO>> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void recordBatchAuditEvents_failure() {
        CreateAuditEventDTO request = CreateAuditEventDTO.builder()
                .userId("user1").sessionId("s1").action("CREATE").build();
        AuditEventCreationRequest creationRequest = AuditEventCreationRequest.builder()
                .userId("user1").action("CREATE").build();

        when(auditEventMapper.toCreationRequest(request)).thenReturn(creationRequest);
        when(sharedAuditService.recordBatchAuditEvents(anyList()))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("batch error")));

        CompletableFuture<ResponseEntity<List<AuditEventDTO>>> future =
                controller.recordBatchAuditEvents(List.of(request));
        ResponseEntity<List<AuditEventDTO>> response = future.join();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void searchAuditEvents_success() {
        AuditSearchDTO searchRequest = AuditSearchDTO.builder().userId("user1").build();
        AuditSearchCriteria criteria = AuditSearchCriteria.builder().userId("user1").build();
        AuditEvent event = buildTestEvent("evt-1");
        AuditEventDTO dto = AuditEventDTO.builder().eventId("evt-1").build();

        when(auditEventMapper.toSearchCriteria(searchRequest)).thenReturn(criteria);
        when(sharedAuditService.searchAuditEvents(criteria))
                .thenReturn(CompletableFuture.completedFuture(List.of(event)));
        when(auditEventMapper.toDTO(event)).thenReturn(dto);

        CompletableFuture<ResponseEntity<List<AuditEventDTO>>> future =
                controller.searchAuditEvents(searchRequest);
        ResponseEntity<List<AuditEventDTO>> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void searchAuditEvents_failure() {
        AuditSearchDTO searchRequest = AuditSearchDTO.builder().build();
        AuditSearchCriteria criteria = AuditSearchCriteria.builder().build();

        when(auditEventMapper.toSearchCriteria(searchRequest)).thenReturn(criteria);
        when(sharedAuditService.searchAuditEvents(criteria))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("search error")));

        CompletableFuture<ResponseEntity<List<AuditEventDTO>>> future =
                controller.searchAuditEvents(searchRequest);
        ResponseEntity<List<AuditEventDTO>> response = future.join();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getUserAuditEvents_withDates() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endTime = LocalDateTime.now();
        AuditEvent event = buildTestEvent("evt-1");
        AuditEventDTO dto = AuditEventDTO.builder().eventId("evt-1").build();

        when(sharedAuditService.searchAuditEvents(any(AuditSearchCriteria.class)))
                .thenReturn(CompletableFuture.completedFuture(List.of(event)));
        when(auditEventMapper.toDTO(event)).thenReturn(dto);

        ResponseEntity<List<AuditEventDTO>> response =
                controller.getUserAuditEvents("user1", startTime, endTime);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getUserAuditEvents_withoutDates_usesDefaults() {
        AuditEvent event = buildTestEvent("evt-1");
        AuditEventDTO dto = AuditEventDTO.builder().eventId("evt-1").build();

        when(sharedAuditService.searchAuditEvents(any(AuditSearchCriteria.class)))
                .thenReturn(CompletableFuture.completedFuture(List.of(event)));
        when(auditEventMapper.toDTO(event)).thenReturn(dto);

        ResponseEntity<List<AuditEventDTO>> response =
                controller.getUserAuditEvents("user1", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getUserAuditEvents_exception_returnsBadRequest() {
        when(sharedAuditService.searchAuditEvents(any(AuditSearchCriteria.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("error")));

        ResponseEntity<List<AuditEventDTO>> response =
                controller.getUserAuditEvents("user1", null, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getAuditStatistics_success() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();
        AuditStatistics stats = buildTestStatistics();
        AuditStatisticsDTO statsDTO = AuditStatisticsDTO.builder()
                .totalEvents(100L).generatedAt(LocalDateTime.now()).build();

        when(sharedAuditService.generateAuditStatistics(startTime, endTime))
                .thenReturn(CompletableFuture.completedFuture(stats));
        when(auditEventMapper.toStatisticsDTO(stats)).thenReturn(statsDTO);

        CompletableFuture<ResponseEntity<AuditStatisticsDTO>> future =
                controller.getAuditStatistics(startTime, endTime);
        ResponseEntity<AuditStatisticsDTO> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(100L, response.getBody().getTotalEvents());
    }

    @Test
    void getAuditStatistics_failure() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();

        when(sharedAuditService.generateAuditStatistics(startTime, endTime))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("stats error")));

        CompletableFuture<ResponseEntity<AuditStatisticsDTO>> future =
                controller.getAuditStatistics(startTime, endTime);
        ResponseEntity<AuditStatisticsDTO> response = future.join();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void generateComplianceReport_success() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(30);
        LocalDateTime endTime = LocalDateTime.now();
        ComplianceReport report = ComplianceReport.builder()
                .reportId("rpt-1").generatedAt(LocalDateTime.now())
                .complianceType(ComplianceType.PCI_DSS).build();
        ComplianceReportDTO reportDTO = ComplianceReportDTO.builder()
                .reportId("rpt-1").status("COMPLIANT").build();

        when(sharedAuditService.generateComplianceReport(ComplianceType.PCI_DSS, startTime, endTime))
                .thenReturn(CompletableFuture.completedFuture(report));
        when(auditEventMapper.toComplianceReportDTO(report)).thenReturn(reportDTO);

        CompletableFuture<ResponseEntity<ComplianceReportDTO>> future =
                controller.generateComplianceReport(ComplianceType.PCI_DSS, startTime, endTime);
        ResponseEntity<ComplianceReportDTO> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("rpt-1", response.getBody().getReportId());
    }

    @Test
    void generateComplianceReport_failure() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(30);
        LocalDateTime endTime = LocalDateTime.now();

        when(sharedAuditService.generateComplianceReport(ComplianceType.GDPR, startTime, endTime))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("report error")));

        CompletableFuture<ResponseEntity<ComplianceReportDTO>> future =
                controller.generateComplianceReport(ComplianceType.GDPR, startTime, endTime);
        ResponseEntity<ComplianceReportDTO> response = future.join();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void detectSuspiciousPatterns_success() {
        AuditEvent event = buildTestEvent("evt-1");
        AuditEventDTO dto = AuditEventDTO.builder().eventId("evt-1").build();

        when(sharedAuditService.detectSuspiciousPatterns())
                .thenReturn(CompletableFuture.completedFuture(List.of(event)));
        when(auditEventMapper.toDTO(event)).thenReturn(dto);

        CompletableFuture<ResponseEntity<List<AuditEventDTO>>> future =
                controller.detectSuspiciousPatterns();
        ResponseEntity<List<AuditEventDTO>> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void detectSuspiciousPatterns_failure() {
        when(sharedAuditService.detectSuspiciousPatterns())
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("detection error")));

        CompletableFuture<ResponseEntity<List<AuditEventDTO>>> future =
                controller.detectSuspiciousPatterns();
        ResponseEntity<List<AuditEventDTO>> response = future.join();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void validateAuditTrailCompleteness_success() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endTime = LocalDateTime.now();

        when(sharedAuditService.validateAuditTrailCompleteness(startTime, endTime))
                .thenReturn(CompletableFuture.completedFuture(true));

        CompletableFuture<ResponseEntity<AuditTrailCompletenessDTO>> future =
                controller.validateAuditTrailCompleteness(startTime, endTime);
        ResponseEntity<AuditTrailCompletenessDTO> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getIsComplete());
    }

    @Test
    void validateAuditTrailCompleteness_incomplete() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endTime = LocalDateTime.now();

        when(sharedAuditService.validateAuditTrailCompleteness(startTime, endTime))
                .thenReturn(CompletableFuture.completedFuture(false));

        CompletableFuture<ResponseEntity<AuditTrailCompletenessDTO>> future =
                controller.validateAuditTrailCompleteness(startTime, endTime);
        ResponseEntity<AuditTrailCompletenessDTO> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getIsComplete());
    }

    @Test
    void validateAuditTrailCompleteness_failure() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endTime = LocalDateTime.now();

        when(sharedAuditService.validateAuditTrailCompleteness(startTime, endTime))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("validation error")));

        CompletableFuture<ResponseEntity<AuditTrailCompletenessDTO>> future =
                controller.validateAuditTrailCompleteness(startTime, endTime);
        ResponseEntity<AuditTrailCompletenessDTO> response = future.join();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void healthCheck_success() {
        AuditStatistics stats = buildTestStatistics();

        when(sharedAuditService.generateAuditStatistics(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(CompletableFuture.completedFuture(stats));

        ResponseEntity<HealthCheckDTO> response = controller.healthCheck();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().getStatus());
        assertEquals("Audit service is operational", response.getBody().getDetails());
        assertEquals(100L, response.getBody().getEventsLastHour());
    }

    @Test
    void healthCheck_failure_returnsServiceUnavailable() {
        when(sharedAuditService.generateAuditStatistics(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("service down")));

        ResponseEntity<HealthCheckDTO> response = controller.healthCheck();

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("DOWN", response.getBody().getStatus());
        assertNotNull(response.getBody().getDetails());
    }
}