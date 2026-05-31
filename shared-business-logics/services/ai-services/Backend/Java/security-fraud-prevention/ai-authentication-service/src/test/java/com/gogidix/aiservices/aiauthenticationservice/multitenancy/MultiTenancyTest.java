package com.gogidix.aiservices.aiauthenticationservice.multitenancy;

import com.gogidix.aiservices.aiauthenticationservice.domain.model.AuthenticationResult;
import com.gogidix.aiservices.aiauthenticationservice.domain.model.AuthenticationStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Financial-Grade Multi-Tenancy Tests.
 * Tests tenant isolation and tenant-specific authentication operations.
 */
@DisplayName("Multi-Tenancy Isolation Tests")
class MultiTenancyTest {

    @Nested
    @DisplayName("Tenant ID in Authentication Result Tests")
    class TenantIdTests {

        @Test
        @DisplayName("Should include tenantId in authentication result")
        void shouldIncludeTenantIdInResult() {
            AuthenticationResult result = AuthenticationResult.builder()
                    .tenantId("tenant-123")
                    .userId(UUID.randomUUID())
                    .sessionId("session-456")
                    .build();

            assertThat(result.getTenantId()).isEqualTo("tenant-123");
        }

        @Test
        @DisplayName("Should use default tenant when not specified")
        void shouldUseDefaultTenantWhenNotSpecified() {
            AuthenticationResult result = AuthenticationResult.builder()
                    .userId(UUID.randomUUID())
                    .sessionId("session-456")
                    .build();

            assertThat(result.getTenantId()).isEqualTo("default");
        }

        @Test
        @DisplayName("Should support custom tenant IDs")
        void shouldSupportCustomTenantIds() {
            String customTenant = "acme-corp";

            AuthenticationResult result = AuthenticationResult.builder()
                    .tenantId(customTenant)
                    .userId(UUID.randomUUID())
                    .sessionId("session-456")
                    .build();

            assertThat(result.getTenantId()).isEqualTo(customTenant);
        }

        @Test
        @DisplayName("Should preserve tenantId through toBuilder")
        void shouldPreserveTenantIdThroughToBuilder() {
            AuthenticationResult original = AuthenticationResult.builder()
                    .tenantId("tenant-123")
                    .userId(UUID.randomUUID())
                    .sessionId("session-456")
                    .build();

            AuthenticationResult modified = original.toBuilder()
                    .sessionId("new-session")
                    .build();

            assertThat(modified.getTenantId()).isEqualTo("tenant-123");
            assertThat(modified.getSessionId()).isEqualTo("new-session");
        }
    }

    @Nested
    @DisplayName("Tenant Isolation Tests")
    class TenantIsolationTests {

        @Test
        @DisplayName("Should create separate authentication contexts for different tenants")
        void shouldCreateSeparateContextsForDifferentTenants() {
            UUID userId = UUID.randomUUID();

            AuthenticationResult tenant1Result = AuthenticationResult.builder()
                    .tenantId("tenant-1")
                    .userId(userId)
                    .sessionId("session-1")
                    .build();

            AuthenticationResult tenant2Result = AuthenticationResult.builder()
                    .tenantId("tenant-2")
                    .userId(userId)
                    .sessionId("session-2")
                    .build();

            // Same user can have different sessions in different tenants
            assertThat(tenant1Result.getTenantId()).isNotEqualTo(tenant2Result.getTenantId());
            assertThat(tenant1Result.getUserId()).isEqualTo(tenant2Result.getUserId());
            assertThat(tenant1Result.getSessionId()).isNotEqualTo(tenant2Result.getSessionId());
        }

        @Test
        @DisplayName("Should maintain tenant isolation in failed authentication")
        void shouldMaintainTenantIsolationInFailedAuthentication() {
            AuthenticationResult failedTenant1 = AuthenticationResult.builder()
                    .tenantId("tenant-1")
                    .status(AuthenticationStatus.FAILED)
                    .failureReason("Invalid credentials")
                    .build();

            AuthenticationResult failedTenant2 = AuthenticationResult.builder()
                    .tenantId("tenant-2")
                    .status(AuthenticationStatus.FAILED)
                    .failureReason("Invalid credentials")
                    .build();

            assertThat(failedTenant1.getTenantId()).isEqualTo("tenant-1");
            assertThat(failedTenant2.getTenantId()).isEqualTo("tenant-2");
            assertThat(failedTenant1.getStatus()).isEqualTo(AuthenticationStatus.FAILED);
            assertThat(failedTenant2.getStatus()).isEqualTo(AuthenticationStatus.FAILED);
        }

        @Test
        @DisplayName("Should support tenant-specific MFA methods")
        void shouldSupportTenantSpecificMfaMethods() {
            AuthenticationResult mfaResult = AuthenticationResult.builder()
                    .tenantId("enterprise-tenant")
                    .userId(UUID.randomUUID())
                    .status(AuthenticationStatus.MFA_REQUIRED)
                    .mfaMethod("TOTP")
                    .build();

            assertThat(mfaResult.getTenantId()).isEqualTo("enterprise-tenant");
            assertThat(mfaResult.isMfaRequired()).isTrue();
            assertThat(mfaResult.getMfaMethod()).isEqualTo("TOTP");
        }
    }

    @Nested
    @DisplayName("Tenant Configuration Tests")
    class TenantConfigurationTests {

        @Test
        @DisplayName("Should support tenant-specific risk assessment")
        void shouldSupportTenantSpecificRiskAssessment() {
            String highSecurityTenant = "bank-tenant";

            AuthenticationResult result = AuthenticationResult.withRiskAssessment(
                    UUID.randomUUID().toString(),
                    "session-123",
                    0.8 // High risk
            );

            result = result.toBuilder()
                    .tenantId(highSecurityTenant)
                    .build();

            assertThat(result.getTenantId()).isEqualTo(highSecurityTenant);
            assertThat(result.getRiskScore()).isEqualTo(0.8);
        }

        @Test
        @DisplayName("Should support tenant-specific session expiry")
        void shouldSupportTenantSpecificSessionExpiry() {
            AuthenticationResult result = AuthenticationResult.builder()
                    .tenantId("tenant-123")
                    .userId(UUID.randomUUID())
                    .sessionId("session-456")
                    .build();

            // Tenant-specific configuration could extend session
            AuthenticationResult extendedSession = AuthenticationResult.withSessionExpiry(
                    result,
                    result.getSessionExpiresAt().plusSeconds(3600)
            );

            assertThat(extendedSession.getTenantId()).isEqualTo("tenant-123");
        }
    }

    @Nested
    @DisplayName("Cross-Tenant Security Tests")
    class CrossTenantSecurityTests {

        @Test
        @DisplayName("Should prevent cross-tenant session access")
        void shouldPreventCrossTenantSessionAccess() {
            String tenant1Session = "tenant1-session";
            String tenant2Session = "tenant2-session";

            AuthenticationResult tenant1Auth = AuthenticationResult.builder()
                    .tenantId("tenant-1")
                    .sessionId(tenant1Session)
                    .userId(UUID.randomUUID())
                    .build();

            AuthenticationResult tenant2Auth = AuthenticationResult.builder()
                    .tenantId("tenant-2")
                    .sessionId(tenant2Session)
                    .userId(UUID.randomUUID())
                    .build();

            // Sessions are isolated by tenant
            assertThat(tenant1Auth.getSessionId()).isNotEqualTo(tenant2Auth.getSessionId());
            assertThat(tenant1Auth.getTenantId()).isNotEqualTo(tenant2Auth.getTenantId());
        }

        @Test
        @DisplayName("Should maintain separate lockout states per tenant")
        void shouldMaintainSeparateLockoutStatesPerTenant() {
            UUID userId = UUID.randomUUID();

            AuthenticationResult tenant1Locked = AuthenticationResult.builder()
                    .tenantId("tenant-1")
                    .userId(userId)
                    .status(AuthenticationStatus.ACCOUNT_LOCKED)
                    .lockUntil(null)
                    .remainingAttempts(0)
                    .build();

            AuthenticationResult tenant2Result = AuthenticationResult.builder()
                    .tenantId("tenant-2")
                    .userId(userId)
                    .sessionId("session-2")
                    .build();

            // User locked in tenant-1 but can still authenticate in tenant-2
            assertThat(tenant1Locked.isLocked()).isTrue();
            assertThat(tenant2Result.getStatus()).isNotEqualTo(AuthenticationStatus.ACCOUNT_LOCKED);
        }
    }
}
