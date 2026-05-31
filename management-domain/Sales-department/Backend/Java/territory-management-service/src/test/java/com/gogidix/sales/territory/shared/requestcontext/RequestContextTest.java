package com.gogidix.sales.territory.shared.requestcontext;

import com.gogidix.sales.territory.shared.requestcontext.RequestContext;
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
            .correlationId("test-correlationId")
            .organizationId("test-organizationId")
            .userAgent("test-userAgent")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-userId", dto.getUserId());
        assertEquals("test-correlationId", dto.getCorrelationId());
        assertEquals("test-organizationId", dto.getOrganizationId());
        assertEquals("test-userAgent", dto.getUserAgent());
    }

    @Test
    void testSettersAndGetters() {
        RequestContext dto = new RequestContext();
        dto.setTenantId("val-tenantId");
        dto.setUserId("val-userId");
        dto.setCorrelationId("val-correlationId");
        dto.setOrganizationId("val-organizationId");
        dto.setUserAgent("val-userAgent");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-userId", dto.getUserId());
        assertEquals("val-correlationId", dto.getCorrelationId());
        assertEquals("val-organizationId", dto.getOrganizationId());
        assertEquals("val-userAgent", dto.getUserAgent());
    }

    @Test
    void testEqualsAndHashCode() {
        RequestContext dto1 = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .correlationId("test-correlationId")
            .organizationId("test-organizationId")
            .userAgent("test-userAgent")
            .build();
        RequestContext dto2 = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .correlationId("test-correlationId")
            .organizationId("test-organizationId")
            .userAgent("test-userAgent")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RequestContext dto = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .correlationId("test-correlationId")
            .organizationId("test-organizationId")
            .userAgent("test-userAgent")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}