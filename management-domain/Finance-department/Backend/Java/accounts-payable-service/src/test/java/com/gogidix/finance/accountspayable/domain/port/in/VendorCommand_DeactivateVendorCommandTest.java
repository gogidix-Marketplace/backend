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
class VendorCommand_DeactivateVendorCommandTest {

        @Test
    void testSettersAndGetters() {
        VendorCommand.DeactivateVendorCommand dto = new VendorCommand.DeactivateVendorCommand();
        dto.setTenantId("val-tenantId");
        dto.setVendorId("val-vendorId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorId", dto.getVendorId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        VendorCommand.DeactivateVendorCommand dto1 = new VendorCommand.DeactivateVendorCommand();
        VendorCommand.DeactivateVendorCommand dto2 = new VendorCommand.DeactivateVendorCommand();
        dto1.setTenantId("test");
        dto1.setVendorId("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setVendorId("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        VendorCommand.DeactivateVendorCommand dto = new VendorCommand.DeactivateVendorCommand();
        dto.setTenantId("test");
        dto.setVendorId("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        VendorCommand.DeactivateVendorCommand dto = new VendorCommand.DeactivateVendorCommand();
        dto.setTenantId("test");
        dto.setVendorId("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}