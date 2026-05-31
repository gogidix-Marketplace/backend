package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.port.in.VendorCommand;
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
class VendorCommand_SetPreferredVendorCommandTest {

        @Test
    void testSettersAndGetters() {
        VendorCommand.SetPreferredVendorCommand dto = new VendorCommand.SetPreferredVendorCommand();
        dto.setTenantId("val-tenantId");
        dto.setVendorId("val-vendorId");
        dto.setPreferred(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorId", dto.getVendorId());
        assertTrue(dto.getPreferred());
    }

    @Test
    void testEqualsAndHashCode() {
        VendorCommand.SetPreferredVendorCommand dto1 = new VendorCommand.SetPreferredVendorCommand();
        VendorCommand.SetPreferredVendorCommand dto2 = new VendorCommand.SetPreferredVendorCommand();
        dto1.setTenantId("test");
        dto1.setVendorId("test");
        dto1.setPreferred(true);
        dto2.setTenantId("test");
        dto2.setVendorId("test");
        dto2.setPreferred(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        VendorCommand.SetPreferredVendorCommand dto = new VendorCommand.SetPreferredVendorCommand();
        dto.setTenantId("test");
        dto.setVendorId("test");
        dto.setPreferred(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        VendorCommand.SetPreferredVendorCommand dto = new VendorCommand.SetPreferredVendorCommand();
        dto.setTenantId("test");
        dto.setVendorId("test");
        dto.setPreferred(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}