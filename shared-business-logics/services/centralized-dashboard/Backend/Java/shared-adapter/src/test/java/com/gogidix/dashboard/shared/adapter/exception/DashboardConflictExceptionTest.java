package com.gogidix.dashboard.shared.adapter.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DashboardConflictException Tests")
class DashboardConflictExceptionTest {

    @Test
    @DisplayName("Should create with message only")
    void shouldCreateWithMessageOnly() {
        DashboardConflictException ex = new DashboardConflictException("conflict occurred");
        assertEquals("conflict occurred", ex.getMessage());
        assertNull(ex.getResourceType());
        assertNull(ex.getConflictReason());
    }

    @Test
    @DisplayName("Should create with resource type and reason")
    void shouldCreateWithResourceTypeAndReason() {
        DashboardConflictException ex = new DashboardConflictException("Dashboard", "already exists");
        assertTrue(ex.getMessage().contains("Dashboard"));
        assertTrue(ex.getMessage().contains("already exists"));
        assertEquals("Dashboard", ex.getResourceType());
        assertEquals("already exists", ex.getConflictReason());
    }
}
