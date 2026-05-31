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
class VendorQuery_GetVendorsByTagQueryTest {

        @Test
    void testSettersAndGetters() {
        VendorQuery.GetVendorsByTagQuery dto = new VendorQuery.GetVendorsByTagQuery();
        dto.setTenantId("val-tenantId");
        dto.setTag("val-tag");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-tag", dto.getTag());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        VendorQuery.GetVendorsByTagQuery dto1 = new VendorQuery.GetVendorsByTagQuery();
        VendorQuery.GetVendorsByTagQuery dto2 = new VendorQuery.GetVendorsByTagQuery();
        dto1.setTenantId("test");
        dto1.setTag("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setTag("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        VendorQuery.GetVendorsByTagQuery dto = new VendorQuery.GetVendorsByTagQuery();
        dto.setTenantId("test");
        dto.setTag("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        VendorQuery.GetVendorsByTagQuery dto = new VendorQuery.GetVendorsByTagQuery();
        dto.setTenantId("test");
        dto.setTag("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}