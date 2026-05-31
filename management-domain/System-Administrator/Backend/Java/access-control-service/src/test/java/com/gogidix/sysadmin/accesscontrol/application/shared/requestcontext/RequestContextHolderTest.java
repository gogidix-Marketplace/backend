package com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RequestContextHolderTest {

    private com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContext context;

    @BeforeEach
    void setUp() {
        com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.clear();
        context = com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContext.builder()
                .tenantId("t1").userId("u1").correlationId("c1").build();
    }

    @Test
    void set_and_get() {
        com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.set(context);
        var result = com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.get();
        assertTrue(result.isPresent());
        assertEquals("t1", result.get().tenantId());
    }

    @Test
    void get_whenNotSet_returnsEmpty() {
        var result = com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.get();
        assertFalse(result.isPresent());
    }

    @Test
    void require_whenSet_returnsContext() {
        com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.set(context);
        var result = com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.require();
        assertNotNull(result);
        assertEquals("t1", result.tenantId());
    }

    @Test
    void require_whenNotSet_throws() {
        try {
            com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.require();
            fail("Should have thrown");
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    void clear_removesContext() {
        com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.set(context);
        com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.clear();
        var result = com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.get();
        assertFalse(result.isPresent());
    }

    @Test
    void getTenantId_returnsValue() {
        com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.set(context);
        assertEquals("t1", com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.getTenantId());
    }

    @Test
    void getUserId_returnsValue() {
        com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.set(context);
        var result = com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.getUserId();
        assertTrue(result.isPresent());
        assertEquals("u1", result.get());
    }

    @Test
    void getCorrelationId_returnsValue() {
        com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.set(context);
        String corrId = com.gogidix.sysadmin.accesscontrol.application.shared.requestcontext.RequestContextHolder.getCorrelationId();
        assertNotNull(corrId);
        assertFalse(corrId.isEmpty());
    }
}
