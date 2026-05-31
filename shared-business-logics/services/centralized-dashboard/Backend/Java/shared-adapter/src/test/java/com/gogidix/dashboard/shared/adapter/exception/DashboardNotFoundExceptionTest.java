package com.gogidix.dashboard.shared.adapter.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DashboardNotFoundException Tests")
class DashboardNotFoundExceptionTest {

    @Test
    @DisplayName("Should create with message only")
    void shouldCreateWithMessageOnly() {
        DashboardNotFoundException ex = new DashboardNotFoundException("not found");
        assertEquals("not found", ex.getMessage());
        assertNull(ex.getResourceType());
        assertNull(ex.getResourceId());
    }

    @Test
    @DisplayName("Should create with resource type and ID")
    void shouldCreateWithResourceTypeAndId() {
        DashboardNotFoundException ex = new DashboardNotFoundException("Widget", "w-123");
        assertTrue(ex.getMessage().contains("Widget"));
        assertTrue(ex.getMessage().contains("w-123"));
        assertEquals("Widget", ex.getResourceType());
        assertEquals("w-123", ex.getResourceId());
    }

    @Test
    @DisplayName("Should create with custom message")
    void shouldCreateWithCustomMessage() {
        DashboardNotFoundException ex = new DashboardNotFoundException("Widget", "w-123", "custom msg");
        assertEquals("custom msg", ex.getMessage());
        assertEquals("Widget", ex.getResourceType());
        assertEquals("w-123", ex.getResourceId());
    }
}
