package com.gogidix.dashboard.shared.adapter.audit;

import com.gogidix.shared.audit.service.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("DashboardAuditService Tests")
class DashboardAuditServiceTest {

    @Mock
    private AuditService delegate;

    private DashboardAuditService service;

    @BeforeEach
    void setUp() {
        service = new DashboardAuditService(delegate);
    }

    @Test
    @DisplayName("Should audit create")
    void shouldAuditCreate() {
        service.auditCreate("Dashboard", "d-1", "created");
        verify(delegate).logEvent("system", "CREATE", "Dashboard", "d-1");
    }

    @Test
    @DisplayName("Should audit update")
    void shouldAuditUpdate() {
        service.auditUpdate("Widget", "w-1", "updated");
        verify(delegate).logEvent("system", "UPDATE", "Widget", "w-1");
    }

    @Test
    @DisplayName("Should audit delete")
    void shouldAuditDelete() {
        service.auditDelete("Report", "r-1", "deleted");
        verify(delegate).logEvent("system", "DELETE", "Report", "r-1");
    }

    @Test
    @DisplayName("Should audit read")
    void shouldAuditRead() {
        service.auditRead("Chart", "c-1", "read");
        verify(delegate).logEvent("system", "READ", "Chart", "c-1");
    }

    @Test
    @DisplayName("Should audit export")
    void shouldAuditExport() {
        service.auditExport("Data", "export-1", "exported");
        verify(delegate).logEvent("system", "EXPORT", "Data", "export-1");
    }

    @Test
    @DisplayName("Should audit with custom action")
    void shouldAuditWithCustomAction() {
        service.audit("CUSTOM", "Entity", "e-1", "custom action");
        verify(delegate).logEvent("system", "CUSTOM", "Entity", "e-1");
    }

    @Test
    @DisplayName("Should audit with metadata")
    void shouldAuditWithMetadata() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key", "value");
        service.auditWithMetadata("ACTION", "Entity", "e-1", "desc", metadata);
        verify(delegate).logEvent("system", "ACTION", "Entity", "e-1");
    }

    @Test
    @DisplayName("Should audit security event")
    void shouldAuditSecurityEvent() {
        service.auditSecurity("LOGIN", "user-1", "user logged in");
        verify(delegate).logEvent("system", "LOGIN", "User", "user-1");
    }

    @Test
    @DisplayName("Should audit query")
    void shouldAuditQuery() {
        service.auditQuery("SQL", "q-1", "executed");
        verify(delegate).logEvent("system", "QUERY", "SQL", "q-1");
    }
}
