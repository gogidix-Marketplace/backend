package com.gogidix.shared.infrastructure.services.security.tenantmanagement.shared.requestcontext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Request Context Tests")
class RequestContextTest {

    @AfterEach
    void clear() {
        TenantContextHolder.clearContext();
    }

    @Nested
    @DisplayName("TenantContextHolder")
    class HolderTests {

        @Test
        void shouldSetAndGetContext() {
            TenantContext ctx = TenantContext.builder().tenantId("t1").userId("u1").correlationId("c1").build();
            TenantContextHolder.setContext(ctx);
            assertEquals("t1", TenantContextHolder.getTenantId());
            assertEquals("u1", TenantContextHolder.getUserId());
            assertEquals("c1", TenantContextHolder.getCorrelationId());
            assertTrue(TenantContextHolder.hasContext());
        }

        @Test
        void shouldReturnNullWhenNotSet() {
            assertNull(TenantContextHolder.getTenantId());
            assertNull(TenantContextHolder.getUserId());
            assertNull(TenantContextHolder.getCorrelationId());
            assertFalse(TenantContextHolder.hasContext());
        }

        @Test
        void shouldReturnOptional() {
            assertFalse(TenantContextHolder.getOptionalContext().isPresent());
            TenantContextHolder.setContext(TenantContext.builder().tenantId("t1").build());
            assertTrue(TenantContextHolder.getOptionalContext().isPresent());
        }

        @Test
        void shouldRequireContext() {
            assertThrows(IllegalStateException.class, TenantContextHolder::requireContext);
            TenantContextHolder.setContext(TenantContext.builder().tenantId("t1").build());
            assertNotNull(TenantContextHolder.requireContext());
        }

        @Test
        void shouldThrowWhenSettingNull() {
            assertThrows(Exception.class, () -> TenantContextHolder.setContext(null));
        }

        @Test
        void shouldClearContext() {
            TenantContextHolder.setContext(TenantContext.builder().tenantId("t1").build());
            TenantContextHolder.clearContext();
            assertNull(TenantContextHolder.getTenantId());
            assertFalse(TenantContextHolder.hasContext());
        }
    }

    @Nested
    @DisplayName("TenantContext")
    class ContextTests {

        @Test
        void shouldCheckRoles() {
            TenantContext ctx = TenantContext.builder()
                .roles(Set.of("ADMIN", "USER")).build();
            assertTrue(ctx.hasRole("ADMIN"));
            assertFalse(ctx.hasRole("SUPERADMIN"));
        }

        @Test
        void shouldCheckPermissions() {
            TenantContext ctx = TenantContext.builder()
                .permissions(List.of("READ", "WRITE")).build();
            assertTrue(ctx.hasPermission("READ"));
            assertFalse(ctx.hasPermission("DELETE"));
        }

        @Test
        void shouldCheckAdmin() {
            TenantContext ctx = TenantContext.builder().roles(Set.of("ADMIN")).build();
            assertTrue(ctx.isAdmin());
            TenantContext ctx2 = TenantContext.builder().roles(Set.of("SYSTEM_ADMIN")).build();
            assertTrue(ctx2.isAdmin());
            TenantContext ctx3 = TenantContext.builder().roles(Set.of("USER")).build();
            assertFalse(ctx3.isAdmin());
        }

        @Test
        void shouldHandleNullRolesAndPermissions() {
            TenantContext ctx = TenantContext.builder().build();
            assertFalse(ctx.hasRole("ADMIN"));
            assertFalse(ctx.hasPermission("READ"));
            assertFalse(ctx.isAdmin());
        }

        @Test
        void shouldAccessAllFields() {
            TenantContext ctx = TenantContext.builder()
                .tenantId("t1").userId("u1").correlationId("c1")
                .roles(Set.of("ADMIN")).permissions(List.of("READ"))
                .userName("John").userEmail("john@t.com")
                .build();
            assertEquals("t1", ctx.getTenantId());
            assertEquals("u1", ctx.getUserId());
            assertEquals("c1", ctx.getCorrelationId());
            assertEquals("John", ctx.getUserName());
            assertEquals("john@t.com", ctx.getUserEmail());
        }
    }
}
