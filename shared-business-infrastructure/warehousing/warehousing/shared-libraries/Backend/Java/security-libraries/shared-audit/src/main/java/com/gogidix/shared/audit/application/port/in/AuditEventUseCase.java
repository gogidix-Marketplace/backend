package com.gogidix.shared.audit.application.port.in;

import com.gogidix.shared.audit.api.dto.*;

import java.util.List;

public interface AuditEventUseCase {
    
    AuditEventDTO createAuditEvent(CreateAuditEventDTO request);
    
    List<AuditEventDTO> searchAuditEvents(AuditSearchDTO searchCriteria);
    
    AuditEventDTO getAuditEvent(String eventId);
    
    AuditStatisticsDTO getAuditStatistics();
    
    ComplianceReportDTO generateComplianceReport();
    
    HealthCheckDTO performHealthCheck();
}