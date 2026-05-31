package com.gogidix.shared.infrastructure.core.tenancy.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TenantId Tests")
class TenantIdTest {

    @Test
    @DisplayName("Should create TenantId with valid value")
    void shouldCreateTenantIdWithValidValue() {
        TenantId tenantId = TenantId.of("tenant-123");
        assertEquals("tenant-123", tenantId.getValue());
    }

    @Test
    @DisplayName("Should create TenantId with constructor")
    void shouldCreateTenantIdWithConstructor() {
        TenantId tenantId = new TenantId("tenant-456");
        assertEquals("tenant-456", tenantId.getValue());
    }

    @Test
    @DisplayName("Should throw exception when value is null")
    void shouldThrowExceptionWhenValueIsNull() {
        assertThrows(IllegalArgumentException.class, () -> TenantId.of(null));
    }

    @Test
    @DisplayName("Should throw exception when value is empty")
    void shouldThrowExceptionWhenValueIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> TenantId.of(""));
    }

    @Test
    @DisplayName("Should throw exception when value is blank")
    void shouldThrowExceptionWhenValueIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> TenantId.of("   "));
    }

    @Test
    @DisplayName("Should toString return value")
    void shouldToStringReturnValue() {
        TenantId tenantId = TenantId.of("tenant-789");
        assertEquals("tenant-789", tenantId.toString());
    }

    @Test
    @DisplayName("Should support no-args constructor")
    void shouldSupportNoArgsConstructor() {
        TenantId tenantId = new TenantId();
        assertNull(tenantId.getValue());
    }

    @Test
    @DisplayName("Should set value")
    void shouldSetValue() {
        TenantId tenantId = new TenantId();
        tenantId.setValue("new-value");
        assertEquals("new-value", tenantId.getValue());
    }

    @Test
    @DisplayName("Should be equal when values are same")
    void shouldBeEqualWhenValuesAreSame() {
        TenantId t1 = TenantId.of("same");
        TenantId t2 = TenantId.of("same");
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when values differ")
    void shouldNotBeEqualWhenValuesDiffer() {
        TenantId t1 = TenantId.of("a");
        TenantId t2 = TenantId.of("b");
        assertNotEquals(t1, t2);
    }
}
