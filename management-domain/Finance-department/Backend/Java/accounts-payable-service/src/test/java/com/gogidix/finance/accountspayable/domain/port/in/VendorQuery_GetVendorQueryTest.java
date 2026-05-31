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
class VendorQuery_GetVendorQueryTest {

        @Test
    void testSettersAndGetters() {
        VendorQuery.GetVendorQuery dto = new VendorQuery.GetVendorQuery();
        dto.setTenantId("val-tenantId");
        dto.setVendorId("val-vendorId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorId", dto.getVendorId());
    }

    @Test
    void testEqualsAndHashCode() {
        VendorQuery.GetVendorQuery dto1 = new VendorQuery.GetVendorQuery();
        VendorQuery.GetVendorQuery dto2 = new VendorQuery.GetVendorQuery();
        dto1.setTenantId("test");
        dto1.setVendorId("test");
        dto2.setTenantId("test");
        dto2.setVendorId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        VendorQuery.GetVendorQuery dto = new VendorQuery.GetVendorQuery();
        dto.setTenantId("test");
        dto.setVendorId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        VendorQuery.GetVendorQuery dto = new VendorQuery.GetVendorQuery();
        dto.setTenantId("test");
        dto.setVendorId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}