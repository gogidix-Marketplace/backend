package com.gogidix.sales.countrydashboard.shared.requestcontext;

import com.gogidix.sales.countrydashboard.shared.requestcontext.RequestContext;
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
            .countryCode("test-countryCode")
            .correlationId("test-correlationId")
            .region("test-region")
            .requestTime(Instant.parse("2025-01-15T10:00:00Z"))
            .metadata(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-userId", dto.getUserId());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-correlationId", dto.getCorrelationId());
        assertEquals("test-region", dto.getRegion());
    }

    @Test
    void testSettersAndGetters() {
        RequestContext dto = new RequestContext();
        dto.setTenantId("val-tenantId");
        dto.setUserId("val-userId");
        dto.setCountryCode("val-countryCode");
        dto.setCorrelationId("val-correlationId");
        dto.setRegion("val-region");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-userId", dto.getUserId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-correlationId", dto.getCorrelationId());
        assertEquals("val-region", dto.getRegion());
    }

    @Test
    void testEqualsAndHashCode() {
        RequestContext dto1 = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .countryCode("test-countryCode")
            .correlationId("test-correlationId")
            .region("test-region")
            .requestTime(Instant.parse("2025-01-15T10:00:00Z"))
            .metadata(Collections.emptyMap())
            .build();
        RequestContext dto2 = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .countryCode("test-countryCode")
            .correlationId("test-correlationId")
            .region("test-region")
            .requestTime(Instant.parse("2025-01-15T10:00:00Z"))
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
            .countryCode("test-countryCode")
            .correlationId("test-correlationId")
            .region("test-region")
            .requestTime(Instant.parse("2025-01-15T10:00:00Z"))
            .metadata(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}