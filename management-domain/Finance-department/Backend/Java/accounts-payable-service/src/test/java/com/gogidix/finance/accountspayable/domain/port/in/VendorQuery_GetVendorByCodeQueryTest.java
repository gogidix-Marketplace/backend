package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.port.in.VendorQuery;
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
class VendorQuery_GetVendorByCodeQueryTest {

        @Test
    void testSettersAndGetters() {
        VendorQuery.GetVendorByCodeQuery dto = new VendorQuery.GetVendorByCodeQuery();
        dto.setTenantId("val-tenantId");
        dto.setVendorCode("val-vendorCode");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorCode", dto.getVendorCode());
    }

    @Test
    void testEqualsAndHashCode() {
        VendorQuery.GetVendorByCodeQuery dto1 = new VendorQuery.GetVendorByCodeQuery();
        VendorQuery.GetVendorByCodeQuery dto2 = new VendorQuery.GetVendorByCodeQuery();
        dto1.setTenantId("test");
        dto1.setVendorCode("test");
        dto2.setTenantId("test");
        dto2.setVendorCode("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        VendorQuery.GetVendorByCodeQuery dto = new VendorQuery.GetVendorByCodeQuery();
        dto.setTenantId("test");
        dto.setVendorCode("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        VendorQuery.GetVendorByCodeQuery dto = new VendorQuery.GetVendorByCodeQuery();
        dto.setTenantId("test");
        dto.setVendorCode("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}