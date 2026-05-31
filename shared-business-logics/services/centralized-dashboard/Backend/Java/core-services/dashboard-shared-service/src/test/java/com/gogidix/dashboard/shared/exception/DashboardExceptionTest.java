package com.gogidix.dashboard.shared.exception;

import com.gogidix.dashboard.shared.constants.DashboardConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DashboardExceptionTest {

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        void constructor_withMessage_setsDefaultErrorCode() {
            DashboardException ex = new DashboardException("test message");
            assertEquals("test message", ex.getMessage());
            assertEquals(DashboardConstants.ERROR_INVALID_INPUT, ex.getErrorCode());
            assertNull(ex.getMetadata());
        }

        @Test
        void constructor_withMessageAndErrorCode_setsBoth() {
            DashboardException ex = new DashboardException("msg", "CUSTOM_CODE");
            assertEquals("msg", ex.getMessage());
            assertEquals("CUSTOM_CODE", ex.getErrorCode());
            assertNull(ex.getMetadata());
        }

        @Test
        void constructor_withMessageErrorCodeAndMetadata_setsAll() {
            Map<String, Object> meta = Map.of("key1", "val1");
            DashboardException ex = new DashboardException("msg", "CODE", meta);
            assertEquals("msg", ex.getMessage());
            assertEquals("CODE", ex.getErrorCode());
            assertEquals(meta, ex.getMetadata());
        }

        @Test
        void constructor_withMessageAndCause_setsCause() {
            RuntimeException cause = new RuntimeException("root cause");
            DashboardException ex = new DashboardException("msg", cause);
            assertEquals("msg", ex.getMessage());
            assertEquals(cause, ex.getCause());
            assertEquals(DashboardConstants.ERROR_INVALID_INPUT, ex.getErrorCode());
        }

        @Test
        void constructor_withMessageErrorCodeAndCause_setsAll() {
            RuntimeException cause = new RuntimeException("root");
            DashboardException ex = new DashboardException("msg", "CODE", cause);
            assertEquals("msg", ex.getMessage());
            assertEquals("CODE", ex.getErrorCode());
            assertEquals(cause, ex.getCause());
        }
    }

    @Nested
    @DisplayName("Factory method tests")
    class FactoryMethodTests {

        @Test
        void tenantNotFound_createsCorrectException() {
            DashboardException ex = DashboardException.tenantNotFound("t1");
            assertTrue(ex.getMessage().contains("t1"));
            assertEquals(DashboardConstants.ERROR_TENANT_NOT_FOUND, ex.getErrorCode());
        }

        @Test
        void invalidTenant_createsCorrectException() {
            DashboardException ex = DashboardException.invalidTenant("bad-tenant");
            assertTrue(ex.getMessage().contains("bad-tenant"));
            assertEquals(DashboardConstants.ERROR_TENANT_INVALID, ex.getErrorCode());
        }

        @Test
        void kpiNotFound_createsCorrectException() {
            DashboardException ex = DashboardException.kpiNotFound("kpi-1");
            assertTrue(ex.getMessage().contains("kpi-1"));
            assertEquals(DashboardConstants.ERROR_KPI_NOT_FOUND, ex.getErrorCode());
        }

        @Test
        void kpiExists_createsCorrectException() {
            DashboardException ex = DashboardException.kpiExists("CODE_1");
            assertTrue(ex.getMessage().contains("CODE_1"));
            assertEquals(DashboardConstants.ERROR_KPI_EXISTS, ex.getErrorCode());
        }

        @Test
        void calculationFailed_createsCorrectException() {
            RuntimeException cause = new RuntimeException("math error");
            DashboardException ex = DashboardException.calculationFailed("KPI_X", cause);
            assertTrue(ex.getMessage().contains("KPI_X"));
            assertEquals(DashboardConstants.ERROR_CALCULATION_FAILED, ex.getErrorCode());
            assertEquals(cause, ex.getCause());
        }

        @Test
        void dataSourceError_createsCorrectException() {
            DashboardException ex = DashboardException.dataSourceError("source1", "conn refused");
            assertTrue(ex.getMessage().contains("source1"));
            assertTrue(ex.getMessage().contains("conn refused"));
            assertEquals(DashboardConstants.ERROR_DATA_SOURCE_ERROR, ex.getErrorCode());
        }
    }
}
