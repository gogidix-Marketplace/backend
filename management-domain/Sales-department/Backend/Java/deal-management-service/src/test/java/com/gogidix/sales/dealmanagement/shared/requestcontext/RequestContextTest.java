package com.gogidix.sales.dealmanagement.shared.requestcontext;

import com.gogidix.sales.dealmanagement.shared.requestcontext.RequestContext;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RequestContextTest {

        @Test
    void testBuilder() {
        RequestContext dto = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .username("test-username")
            .correlationId("test-correlationId")
            .userRole("test-userRole")
            .organizationId("test-organizationId")
            .metadata(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-userId", dto.getUserId());
        assertEquals("test-username", dto.getUsername());
        assertEquals("test-correlationId", dto.getCorrelationId());
        assertEquals("test-userRole", dto.getUserRole());
        assertEquals("test-organizationId", dto.getOrganizationId());
    }

    @Test
    void testSettersAndGetters() {
        RequestContext dto = new RequestContext();
        dto.setTenantId("val-tenantId");
        dto.setUserId("val-userId");
        dto.setUsername("val-username");
        dto.setCorrelationId("val-correlationId");
        dto.setUserRole("val-userRole");
        dto.setOrganizationId("val-organizationId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-userId", dto.getUserId());
        assertEquals("val-username", dto.getUsername());
        assertEquals("val-correlationId", dto.getCorrelationId());
        assertEquals("val-userRole", dto.getUserRole());
        assertEquals("val-organizationId", dto.getOrganizationId());
    }

    @Test
    void testEqualsAndHashCode() {
        RequestContext dto1 = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .username("test-username")
            .correlationId("test-correlationId")
            .userRole("test-userRole")
            .organizationId("test-organizationId")
            .metadata(Collections.emptyMap())
            .build();
        RequestContext dto2 = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .username("test-username")
            .correlationId("test-correlationId")
            .userRole("test-userRole")
            .organizationId("test-organizationId")
            .metadata(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RequestContext dto = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .username("test-username")
            .correlationId("test-correlationId")
            .userRole("test-userRole")
            .organizationId("test-organizationId")
            .metadata(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}