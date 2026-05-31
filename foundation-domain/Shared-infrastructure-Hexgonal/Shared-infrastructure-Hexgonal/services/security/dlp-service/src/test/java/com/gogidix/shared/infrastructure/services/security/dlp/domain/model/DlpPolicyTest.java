package com.gogidix.shared.infrastructure.services.security.dlp.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DlpPolicy domain model.
 */
@DisplayName("DlpPolicy Domain Model Tests")
class DlpPolicyTest {

    @Test
    @DisplayName("Should create DlpPolicy with default values")
    void shouldCreateDlpPolicyWithDefaults() {
        DlpPolicy policy = new DlpPolicy();

        assertNull(policy.getId());
        assertNull(policy.getTenantId());
        assertNull(policy.getPolicyId());
        assertNull(policy.getPolicyName());
        assertNull(policy.getDescription());
        assertEquals(DlpPolicy.PolicyStatus.ACTIVE, policy.getStatus()); // Status defaults to ACTIVE
        assertNull(policy.getSensitiveDataPatterns());
        assertNull(policy.getAction());
        assertNull(policy.getCreatedAt());
        assertNull(policy.getUpdatedAt());
    }

    @Test
    @DisplayName("Should create DlpPolicy with constructor")
    void shouldCreateDlpPolicyWithConstructor() {
        TenantId tenantId = TenantId.of("tenant-123");
        DlpPolicy policy = new DlpPolicy(tenantId, "Test Policy");

        assertEquals(tenantId, policy.getTenantId());
        assertEquals("Test Policy", policy.getPolicyName());
        assertNotNull(policy.getPolicyId());
        assertEquals(DlpPolicy.PolicyStatus.ACTIVE, policy.getStatus());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllProperties() {
        DlpPolicy policy = new DlpPolicy();
        TenantId tenantId = TenantId.of("tenant-123");
        LocalDateTime now = LocalDateTime.now();

        policy.setId("policy-123");
        policy.setTenantId(tenantId);
        policy.setPolicyId("policy-id-456");
        policy.setPolicyName("SSN Detection Policy");
        policy.setDescription("Detects Social Security Numbers");
        policy.setStatus(DlpPolicy.PolicyStatus.ACTIVE);
        policy.setSensitiveDataPatterns(List.of("\\d{3}-\\d{2}-\\d{4}", "\\d{9}"));
        policy.setAction("BLOCK");
        policy.setCreatedAt(now);
        policy.setUpdatedAt(now);

        assertEquals("policy-123", policy.getId());
        assertEquals(tenantId, policy.getTenantId());
        assertEquals("policy-id-456", policy.getPolicyId());
        assertEquals("SSN Detection Policy", policy.getPolicyName());
        assertEquals("Detects Social Security Numbers", policy.getDescription());
        assertEquals(DlpPolicy.PolicyStatus.ACTIVE, policy.getStatus());
        assertEquals(2, policy.getSensitiveDataPatterns().size());
        assertEquals("BLOCK", policy.getAction());
        assertEquals(now, policy.getCreatedAt());
        assertEquals(now, policy.getUpdatedAt());
    }

    @Test
    @DisplayName("Should generate unique policy IDs")
    void shouldGenerateUniquePolicyIds() {
        TenantId tenantId = TenantId.of("tenant-123");

        DlpPolicy policy1 = new DlpPolicy(tenantId, "Policy 1");
        DlpPolicy policy2 = new DlpPolicy(tenantId, "Policy 2");

        assertNotEquals(policy1.getPolicyId(), policy2.getPolicyId());
    }

    @Test
    @DisplayName("Should handle all PolicyStatus enum values")
    void shouldHandleAllPolicyStatusEnums() {
        assertEquals(3, DlpPolicy.PolicyStatus.values().length);
        assertEquals(DlpPolicy.PolicyStatus.ACTIVE, DlpPolicy.PolicyStatus.valueOf("ACTIVE"));
        assertEquals(DlpPolicy.PolicyStatus.INACTIVE, DlpPolicy.PolicyStatus.valueOf("INACTIVE"));
        assertEquals(DlpPolicy.PolicyStatus.DRAFT, DlpPolicy.PolicyStatus.valueOf("DRAFT"));
    }

    @Test
    @DisplayName("Should set status to ACTIVE by default")
    void shouldSetStatusToActiveByDefault() {
        TenantId tenantId = TenantId.of("tenant-123");
        DlpPolicy policy = new DlpPolicy(tenantId, "Test Policy");

        assertEquals(DlpPolicy.PolicyStatus.ACTIVE, policy.getStatus());
    }

    @Test
    @DisplayName("Should allow setting different statuses")
    void shouldAllowSettingDifferentStatuses() {
        DlpPolicy policy = new DlpPolicy();

        policy.setStatus(DlpPolicy.PolicyStatus.ACTIVE);
        assertEquals(DlpPolicy.PolicyStatus.ACTIVE, policy.getStatus());

        policy.setStatus(DlpPolicy.PolicyStatus.INACTIVE);
        assertEquals(DlpPolicy.PolicyStatus.INACTIVE, policy.getStatus());

        policy.setStatus(DlpPolicy.PolicyStatus.DRAFT);
        assertEquals(DlpPolicy.PolicyStatus.DRAFT, policy.getStatus());
    }

    @Test
    @DisplayName("Should handle empty sensitive data patterns")
    void shouldHandleEmptySensitiveDataPatterns() {
        DlpPolicy policy = new DlpPolicy();
        policy.setSensitiveDataPatterns(List.of());

        assertNotNull(policy.getSensitiveDataPatterns());
        assertTrue(policy.getSensitiveDataPatterns().isEmpty());
    }

    @Test
    @DisplayName("Should handle multiple sensitive data patterns")
    void shouldHandleMultipleSensitiveDataPatterns() {
        DlpPolicy policy = new DlpPolicy();
        List<String> patterns = List.of(
            "\\d{3}-\\d{2}-\\d{4}",
            "\\d{9}",
            "\\b\\d{3}[-.]?\\d{2}[-.]?\\d{4}\\b"
        );
        policy.setSensitiveDataPatterns(patterns);

        assertEquals(3, policy.getSensitiveDataPatterns().size());
        assertTrue(policy.getSensitiveDataPatterns().contains("\\d{3}-\\d{2}-\\d{4}"));
    }

    @Test
    @DisplayName("Should handle different action types")
    void shouldHandleDifferentActionTypes() {
        DlpPolicy policy = new DlpPolicy();

        policy.setAction("BLOCK");
        assertEquals("BLOCK", policy.getAction());

        policy.setAction("ALERT");
        assertEquals("ALERT", policy.getAction());

        policy.setAction("QUARANTINE");
        assertEquals("QUARANTINE", policy.getAction());

        policy.setAction("AUDIT");
        assertEquals("AUDIT", policy.getAction());
    }

    @Test
    @DisplayName("Should handle null action")
    void shouldHandleNullAction() {
        DlpPolicy policy = new DlpPolicy();
        policy.setAction(null);

        assertNull(policy.getAction());
    }

    @Test
    @DisplayName("Should handle null description")
    void shouldHandleNullDescription() {
        DlpPolicy policy = new DlpPolicy();
        policy.setDescription(null);

        assertNull(policy.getDescription());
    }

    @Test
    @DisplayName("Should handle non-null description")
    void shouldHandleNonNullDescription() {
        DlpPolicy policy = new DlpPolicy();
        policy.setDescription("Test description");

        assertEquals("Test description", policy.getDescription());
    }
}
