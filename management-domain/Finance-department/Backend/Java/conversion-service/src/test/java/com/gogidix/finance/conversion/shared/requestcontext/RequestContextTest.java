package com.gogidix.finance.conversion.shared.requestcontext;

import com.gogidix.finance.conversion.shared.requestcontext.RequestContext;
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
            .requestId("test-requestId")
            .userAgent("test-userAgent")
            .ipAddress("test-ipAddress")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-userId", dto.getUserId());
        assertEquals("test-correlationId", dto.getCorrelationId());
        assertEquals("test-requestId", dto.getRequestId());
        assertEquals("test-userAgent", dto.getUserAgent());
        assertEquals("test-ipAddress", dto.getIpAddress());
    }

    @Test
    void testSettersAndGetters() {
        RequestContext dto = new RequestContext();
        dto.setTenantId("val-tenantId");
        dto.setUserId("val-userId");
        dto.setCorrelationId("val-correlationId");
        dto.setRequestId("val-requestId");
        dto.setUserAgent("val-userAgent");
        dto.setIpAddress("val-ipAddress");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-userId", dto.getUserId());
        assertEquals("val-correlationId", dto.getCorrelationId());
        assertEquals("val-requestId", dto.getRequestId());
        assertEquals("val-userAgent", dto.getUserAgent());
        assertEquals("val-ipAddress", dto.getIpAddress());
    }

    @Test
    void testEqualsAndHashCode() {
        RequestContext dto1 = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .correlationId("test-correlationId")
            .requestId("test-requestId")
            .userAgent("test-userAgent")
            .ipAddress("test-ipAddress")
            .build();
        RequestContext dto2 = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .correlationId("test-correlationId")
            .requestId("test-requestId")
            .userAgent("test-userAgent")
            .ipAddress("test-ipAddress")
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
            .requestId("test-requestId")
            .userAgent("test-userAgent")
            .ipAddress("test-ipAddress")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}