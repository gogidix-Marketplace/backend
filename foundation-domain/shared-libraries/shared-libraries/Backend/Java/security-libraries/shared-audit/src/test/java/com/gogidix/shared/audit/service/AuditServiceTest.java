package com.gogidix.shared.audit.service;

import com.gogidix.shared.audit.application.port.in.AuditEventUseCase;
import com.gogidix.shared.audit.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock
    private AuditEventUseCase auditEventUseCase;

    private AuditService createService() {
        return new AuditService.SimpleAuditService(auditEventUseCase);
    }

    @Test
    void logEvent_simple_delegatesToOverload() {
        AuditService service = createService();
        assertDoesNotThrow(() -> service.logEvent("user1", "CREATE", "order", "ord-1"));
    }

    @Test
    void logEvent_withResultAndDetails_delegatesToOverload() {
        AuditService service = createService();
        assertDoesNotThrow(() -> service.logEvent("user1", "UPDATE", "order", "ord-1",
                AuditResult.FAILURE, Map.of("reason", "validation error")));
    }

    @Test
    void logEvent_withResultAndNullDetails() {
        AuditService service = createService();
        assertDoesNotThrow(() -> service.logEvent("user1", "DELETE", "order", "ord-1",
                AuditResult.SUCCESS, null));
    }

    @Test
    void logEvent_withDomainAndEventType() {
        AuditService service = createService();
        assertDoesNotThrow(() -> service.logEvent("user1", "CREATE", "order", "ord-1",
                BusinessDomain.SHARED_INFRASTRUCTURE, AuditEventType.SYSTEM_EVENT));
    }

    @Test
    void logEvent_withFinancialDomainAndType() {
        AuditService service = createService();
        assertDoesNotThrow(() -> service.logEvent("user1", "PAYMENT", "invoice", "inv-1",
                BusinessDomain.PAYMENTS, AuditEventType.FINANCIAL_TRANSACTION));
    }

    @Test
    void logEvent_withSecurityEventType() {
        AuditService service = createService();
        assertDoesNotThrow(() -> service.logEvent("user1", "INTRUSION", "system", "sys-1",
                BusinessDomain.SECURITY, AuditEventType.SECURITY_EVENT));
    }

    @Test
    void auditEventBuilder_fullBuildAndLog() {
        AuditService service = createService();
        assertDoesNotThrow(() -> service.auditEvent()
                .userId("user1")
                .action("CREATE")
                .resource("order")
                .resourceId("ord-1")
                .result(AuditResult.SUCCESS)
                .domain(BusinessDomain.SHARED_INFRASTRUCTURE)
                .eventType(AuditEventType.SYSTEM_EVENT)
                .timestamp(LocalDateTime.now())
                .detail("key1", "value1")
                .detail("key2", 42)
                .log());
    }

    @Test
    void auditEventBuilder_minimalBuildAndLog() {
        AuditService service = createService();
        assertDoesNotThrow(() -> service.auditEvent()
                .userId("user1")
                .action("READ")
                .log());
    }

    @Test
    void auditEventBuilder_chainedSetters() {
        AuditService service = createService();
        AuditService.AuditEventBuilder builder = service.auditEvent()
                .userId("user1")
                .action("UPDATE")
                .resource("resource")
                .resourceId("res-1")
                .result(AuditResult.FAILURE)
                .domain(BusinessDomain.PAYMENTS)
                .eventType(AuditEventType.FINANCIAL_TRANSACTION)
                .timestamp(LocalDateTime.now());

        assertNotNull(builder);
        assertDoesNotThrow(builder::log);
    }

    @Test
    void auditEventBuilder_defaultValues() {
        AuditService service = createService();
        assertDoesNotThrow(() -> service.auditEvent()
                .userId("user1")
                .action("READ")
                .resource("test")
                .log());
    }

    @Test
    void simpleAuditService_constructor() {
        AuditService service = new AuditService.SimpleAuditService(auditEventUseCase);
        assertNotNull(service);
    }
}