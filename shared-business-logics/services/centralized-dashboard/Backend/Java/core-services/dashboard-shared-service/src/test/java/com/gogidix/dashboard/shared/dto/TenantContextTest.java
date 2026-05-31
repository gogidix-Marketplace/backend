package com.gogidix.dashboard.shared.dto;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TenantContextTest {

    @BeforeEach
    void setUp() {
        TenantContext.clearContext();
    }

    @AfterEach
    void tearDown() {
        TenantContext.clearContext();
    }

    @Nested
    @DisplayName("Builder and Constructor tests")
    class BuilderTests {

        @Test
        void builder_withTenantId_createsContextWithTenantId() {
            TenantContext context = TenantContext.builder("tenant-123")
                    .build();
            assertEquals("tenant-123", context.getTenantId());
        }

        @Test
        void builder_withAllFields_setsAllFields() {
            LocalDateTime now = LocalDateTime.now();
            Map<String, Object> metadata = Map.of("key", "value");
            TenantContext context = TenantContext.builder("t1")
                    .tenantName("Tenant One")
                    .userId("user-1")
                    .username("john")
                    .userRole("ADMIN")
                    .requestTimestamp(now)
                    .correlationId("corr-123")
                    .metadata(metadata)
                    .build();

            assertEquals("t1", context.getTenantId());
            assertEquals("Tenant One", context.getTenantName());
            assertEquals("user-1", context.getUserId());
            assertEquals("john", context.getUsername());
            assertEquals("ADMIN", context.getUserRole());
            assertEquals(now, context.getRequestTimestamp());
            assertEquals("corr-123", context.getCorrelationId());
            assertEquals(metadata, context.getMetadata());
        }

        @Test
        void builder_defaults_correlationIdIsGenerated() {
            TenantContext context = TenantContext.builder("t1").build();
            assertNotNull(context.getCorrelationId());
            assertFalse(context.getCorrelationId().isEmpty());
        }

        @Test
        void noArgsConstructor_createsInstance() {
            TenantContext context = new TenantContext();
            assertNotNull(context);
        }

        @Test
        void allArgsConstructor_setsAllFields() {
            LocalDateTime now = LocalDateTime.now();
            TenantContext context = new TenantContext("t1", "name", "uid", "uname", "role", now, "corr", null);
            assertEquals("t1", context.getTenantId());
            assertEquals("name", context.getTenantName());
            assertEquals("uid", context.getUserId());
        }

        @Test
        void setters_workCorrectly() {
            TenantContext context = new TenantContext();
            context.setTenantId("new-tenant");
            context.setTenantName("New Name");
            context.setUserId("new-user");
            context.setUsername("newuser");
            context.setUserRole("USER");
            context.setRequestTimestamp(LocalDateTime.now());
            context.setCorrelationId("new-corr");

            assertEquals("new-tenant", context.getTenantId());
            assertEquals("New Name", context.getTenantName());
            assertEquals("new-user", context.getUserId());
            assertEquals("newuser", context.getUsername());
            assertEquals("USER", context.getUserRole());
            assertEquals("new-corr", context.getCorrelationId());
        }
    }

    @Nested
    @DisplayName("ThreadLocal context tests")
    class ThreadLocalTests {

        @Test
        void setContext_andGetContext_returnsSameContext() {
            TenantContext context = TenantContext.builder("t1").build();
            TenantContext.setContext(context);

            assertEquals(context, TenantContext.getContext());
        }

        @Test
        void getContext_whenNotSet_returnsNull() {
            assertNull(TenantContext.getContext());
        }

        @Test
        void getTenantId_whenContextSet_returnsTenantId() {
            TenantContext context = TenantContext.builder("t1").build();
            TenantContext.setContext(context);
            assertEquals("t1", TenantContext.getCurrentTenantId());
        }

        @Test
        void getTenantId_whenContextNotSet_returnsNull() {
            assertNull(TenantContext.getCurrentTenantId());
        }

        @Test
        void getUserId_whenContextSet_returnsUserId() {
            TenantContext context = TenantContext.builder("t1").userId("user-1").build();
            TenantContext.setContext(context);
            assertEquals("user-1", TenantContext.getCurrentUserId());
        }

        @Test
        void getUserId_whenContextNotSet_returnsNull() {
            assertNull(TenantContext.getCurrentUserId());
        }

        @Test
        void clearContext_removesContext() {
            TenantContext context = TenantContext.builder("t1").build();
            TenantContext.setContext(context);
            TenantContext.clearContext();
            assertNull(TenantContext.getContext());
        }
    }

    @Nested
    @DisplayName("equals, hashCode, toString tests")
    class EqualsHashCodeToStringTests {

        @Test
        void equals_sameInstance_returnsTrue() {
            TenantContext ctx = new TenantContext();
            assertEquals(ctx, ctx);
        }

        @Test
        void equals_null_returnsFalse() {
            assertNotEquals(null, new TenantContext());
        }

        @Test
        void equals_differentType_returnsFalse() {
            assertNotEquals(new Object(), new TenantContext());
        }

        @Test
        void equals_equalObjects_returnsTrue() {
            LocalDateTime ts = LocalDateTime.now();
            Map<String, Object> meta = Map.of("k", "v");
            TenantContext c1 = new TenantContext("t1", "n", "u", "un", "r", ts, "c", meta);
            TenantContext c2 = new TenantContext("t1", "n", "u", "un", "r", ts, "c", meta);
            assertEquals(c1, c2);
            assertEquals(c1.hashCode(), c2.hashCode());
        }

        @Test
        void equals_differentTenantId_returnsFalse() {
            TenantContext c1 = new TenantContext("a", null, null, null, null, null, null, null);
            TenantContext c2 = new TenantContext("b", null, null, null, null, null, null, null);
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_nullTenantId_vs_nonNullTenantId_returnsFalse() {
            TenantContext c1 = new TenantContext(null, null, null, null, null, null, null, null);
            TenantContext c2 = new TenantContext("t", null, null, null, null, null, null, null);
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_differentTenantName_returnsFalse() {
            TenantContext c1 = new TenantContext(null, "a", null, null, null, null, null, null);
            TenantContext c2 = new TenantContext(null, "b", null, null, null, null, null, null);
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_nullTenantName_vs_nonNullTenantName_returnsFalse() {
            TenantContext c1 = new TenantContext(null, null, null, null, null, null, null, null);
            TenantContext c2 = new TenantContext(null, "n", null, null, null, null, null, null);
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_differentUserId_returnsFalse() {
            TenantContext c1 = new TenantContext(null, null, "a", null, null, null, null, null);
            TenantContext c2 = new TenantContext(null, null, "b", null, null, null, null, null);
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_nullUserId_vs_nonNullUserId_returnsFalse() {
            TenantContext c1 = new TenantContext(null, null, null, null, null, null, null, null);
            TenantContext c2 = new TenantContext(null, null, "u", null, null, null, null, null);
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_differentUsername_returnsFalse() {
            TenantContext c1 = new TenantContext(null, null, null, "a", null, null, null, null);
            TenantContext c2 = new TenantContext(null, null, null, "b", null, null, null, null);
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_nullUsername_vs_nonNullUsername_returnsFalse() {
            TenantContext c1 = new TenantContext(null, null, null, null, null, null, null, null);
            TenantContext c2 = new TenantContext(null, null, null, "un", null, null, null, null);
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_differentUserRole_returnsFalse() {
            TenantContext c1 = new TenantContext(null, null, null, null, "a", null, null, null);
            TenantContext c2 = new TenantContext(null, null, null, null, "b", null, null, null);
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_nullUserRole_vs_nonNullUserRole_returnsFalse() {
            TenantContext c1 = new TenantContext(null, null, null, null, null, null, null, null);
            TenantContext c2 = new TenantContext(null, null, null, null, "r", null, null, null);
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_differentRequestTimestamp_returnsFalse() {
            TenantContext c1 = new TenantContext(null, null, null, null, null, LocalDateTime.now(), null, null);
            TenantContext c2 = new TenantContext(null, null, null, null, null, LocalDateTime.now().plusDays(1), null, null);
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_nullRequestTimestamp_vs_nonNullRequestTimestamp_returnsFalse() {
            TenantContext c1 = new TenantContext(null, null, null, null, null, null, null, null);
            TenantContext c2 = new TenantContext(null, null, null, null, null, LocalDateTime.now(), null, null);
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_differentCorrelationId_returnsFalse() {
            TenantContext c1 = new TenantContext(null, null, null, null, null, null, "a", null);
            TenantContext c2 = new TenantContext(null, null, null, null, null, null, "b", null);
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_nullCorrelationId_vs_nonNullCorrelationId_returnsFalse() {
            TenantContext c1 = new TenantContext(null, null, null, null, null, null, null, null);
            TenantContext c2 = new TenantContext(null, null, null, null, null, null, "c", null);
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_differentMetadata_returnsFalse() {
            TenantContext c1 = new TenantContext(null, null, null, null, null, null, null, Map.of("a", 1));
            TenantContext c2 = new TenantContext(null, null, null, null, null, null, null, Map.of("b", 2));
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_nullMetadata_vs_nonNullMetadata_returnsFalse() {
            TenantContext c1 = new TenantContext(null, null, null, null, null, null, null, null);
            TenantContext c2 = new TenantContext(null, null, null, null, null, null, null, Map.of());
            assertNotEquals(c1, c2);
        }

        @Test
        void equals_allNullFields_returnsTrue() {
            TenantContext c1 = new TenantContext(null, null, null, null, null, null, null, null);
            TenantContext c2 = new TenantContext(null, null, null, null, null, null, null, null);
            assertEquals(c1, c2);
        }

        @Test
        void hashCode_allNullFields_doesNotThrow() {
            assertDoesNotThrow(() -> new TenantContext().hashCode());
        }

        @Test
        void hashCode_nonNullFields_doesNotThrow() {
            TenantContext c = new TenantContext("t", "n", "u", "un", "r", LocalDateTime.now(), "c", Map.of("k", "v"));
            assertDoesNotThrow(() -> c.hashCode());
        }

        @Test
        void toString_containsClassName() {
            String str = new TenantContext().toString();
            assertTrue(str.startsWith("TenantContext"));
        }

        @Test
        void toString_containsFieldValues() {
            TenantContext c = new TenantContext("tenant-1", "MyTenant", "user-1", "john", "ADMIN", null, "corr-1", null);
            String str = c.toString();
            assertTrue(str.contains("tenant-1"));
            assertTrue(str.contains("MyTenant"));
            assertTrue(str.contains("user-1"));
            assertTrue(str.contains("john"));
            assertTrue(str.contains("ADMIN"));
            assertTrue(str.contains("corr-1"));
        }
    }

    @Nested
    @DisplayName("Builder default branch tests")
    class BuilderDefaultTests {

        @Test
        void builder_withExplicitCorrelationId_usesProvidedValue() {
            TenantContext c = TenantContext.builder("t1")
                    .correlationId("explicit-corr")
                    .build();
            assertEquals("explicit-corr", c.getCorrelationId());
        }

        @Test
        void builder_withoutCorrelationId_generatesDefault() {
            TenantContext c = TenantContext.builder("t1").build();
            assertNotNull(c.getCorrelationId());
        }
    }
}
