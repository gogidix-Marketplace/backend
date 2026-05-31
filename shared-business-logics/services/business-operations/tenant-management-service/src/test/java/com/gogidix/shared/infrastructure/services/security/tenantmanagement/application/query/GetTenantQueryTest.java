package com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetTenantQuery Tests")
class GetTenantQueryTest {

    @Test
    void shouldBuildWithAllFields() {
        GetTenantQuery q = GetTenantQuery.builder().id("1").tenantId("t1").domain("test.com").build();
        assertTrue(q.hasId());
        assertTrue(q.hasTenantId());
        assertTrue(q.hasDomain());
        assertTrue(q.hasSearchCriteria());
    }

    @Test
    void shouldHandleNullFields() {
        GetTenantQuery q = GetTenantQuery.builder().build();
        assertFalse(q.hasId());
        assertFalse(q.hasTenantId());
        assertFalse(q.hasDomain());
        assertFalse(q.hasSearchCriteria());
    }

    @Test
    void shouldHandleBlankFields() {
        GetTenantQuery q = GetTenantQuery.builder().id("  ").tenantId("").build();
        assertFalse(q.hasId());
        assertFalse(q.hasTenantId());
        assertFalse(q.hasSearchCriteria());
    }

    @Test
    void shouldBeEqualWithSameFields() {
        GetTenantQuery q1 = GetTenantQuery.builder().tenantId("t1").build();
        GetTenantQuery q2 = GetTenantQuery.builder().tenantId("t1").build();
        assertEquals(q1, q2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        assertNotEquals(null, GetTenantQuery.builder().build());
    }

    @Test
    void shouldHaveToString() {
        assertNotNull(GetTenantQuery.builder().tenantId("t1").build().toString());
    }
}
