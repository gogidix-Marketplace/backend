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
class VendorCommand_ActivateVendorCommandTest {

        @Test
    void testSettersAndGetters() {
        VendorCommand.ActivateVendorCommand dto = new VendorCommand.ActivateVendorCommand();
        dto.setTenantId("val-tenantId");
        dto.setVendorId("val-vendorId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorId", dto.getVendorId());
    }

    @Test
    void testEqualsAndHashCode() {
        VendorCommand.ActivateVendorCommand dto1 = new VendorCommand.ActivateVendorCommand();
        VendorCommand.ActivateVendorCommand dto2 = new VendorCommand.ActivateVendorCommand();
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
        VendorCommand.ActivateVendorCommand dto = new VendorCommand.ActivateVendorCommand();
        dto.setTenantId("test");
        dto.setVendorId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        VendorCommand.ActivateVendorCommand dto = new VendorCommand.ActivateVendorCommand();
        dto.setTenantId("test");
        dto.setVendorId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}