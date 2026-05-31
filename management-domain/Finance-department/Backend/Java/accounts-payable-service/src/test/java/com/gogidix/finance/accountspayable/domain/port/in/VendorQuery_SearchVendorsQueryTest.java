package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.model.Vendor;
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
class VendorQuery_SearchVendorsQueryTest {

        @Test
    void testSettersAndGetters() {
        VendorQuery.SearchVendorsQuery dto = new VendorQuery.SearchVendorsQuery();
        dto.setTenantId("val-tenantId");
        dto.setSearchTerm("val-searchTerm");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-searchTerm", dto.getSearchTerm());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        VendorQuery.SearchVendorsQuery dto1 = new VendorQuery.SearchVendorsQuery();
        VendorQuery.SearchVendorsQuery dto2 = new VendorQuery.SearchVendorsQuery();
        dto1.setTenantId("test");
        dto1.setSearchTerm("test");
        dto1.setVendorType(Vendor.VendorType.INDIVIDUAL);
        dto1.setStatus(Vendor.VendorStatus.ACTIVE);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setSearchTerm("test");
        dto2.setVendorType(Vendor.VendorType.INDIVIDUAL);
        dto2.setStatus(Vendor.VendorStatus.ACTIVE);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        VendorQuery.SearchVendorsQuery dto = new VendorQuery.SearchVendorsQuery();
        dto.setTenantId("test");
        dto.setSearchTerm("test");
        dto.setVendorType(Vendor.VendorType.INDIVIDUAL);
        dto.setStatus(Vendor.VendorStatus.ACTIVE);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        VendorQuery.SearchVendorsQuery dto = new VendorQuery.SearchVendorsQuery();
        dto.setTenantId("test");
        dto.setSearchTerm("test");
        dto.setVendorType(Vendor.VendorType.INDIVIDUAL);
        dto.setStatus(Vendor.VendorStatus.ACTIVE);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}