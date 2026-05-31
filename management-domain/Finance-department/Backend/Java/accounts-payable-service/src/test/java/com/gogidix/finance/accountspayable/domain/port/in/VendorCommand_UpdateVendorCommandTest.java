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
class VendorCommand_UpdateVendorCommandTest {

        @Test
    void testBuilder() {
        VendorCommand.UpdateVendorCommand dto = VendorCommand.UpdateVendorCommand.builder()
                        .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .contactPerson("test-contactPerson")
            .email("test-email")
            .phone("test-phone")
            .billingAddress(null)
            .shippingAddress(null)
            .notes("test-notes")
            .tags(Collections.emptyList())
            .website("test-website")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-vendorId", dto.getVendorId());
        assertEquals("test-vendorName", dto.getVendorName());
        assertEquals("test-contactPerson", dto.getContactPerson());
        assertEquals("test-email", dto.getEmail());
        assertEquals("test-phone", dto.getPhone());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-website", dto.getWebsite());
    }

    @Test
    void testSettersAndGetters() {
        VendorCommand.UpdateVendorCommand dto = new VendorCommand.UpdateVendorCommand();
        dto.setTenantId("val-tenantId");
        dto.setVendorId("val-vendorId");
        dto.setVendorName("val-vendorName");
        dto.setContactPerson("val-contactPerson");
        dto.setEmail("val-email");
        dto.setPhone("val-phone");
        dto.setNotes("val-notes");
        dto.setWebsite("val-website");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorId", dto.getVendorId());
        assertEquals("val-vendorName", dto.getVendorName());
        assertEquals("val-contactPerson", dto.getContactPerson());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-phone", dto.getPhone());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-website", dto.getWebsite());
    }

    @Test
    void testEqualsAndHashCode() {
        VendorCommand.UpdateVendorCommand dto1 = VendorCommand.UpdateVendorCommand.builder()
                        .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .contactPerson("test-contactPerson")
            .email("test-email")
            .phone("test-phone")
            .billingAddress(null)
            .shippingAddress(null)
            .notes("test-notes")
            .tags(Collections.emptyList())
            .website("test-website")
            .build();
        VendorCommand.UpdateVendorCommand dto2 = VendorCommand.UpdateVendorCommand.builder()
                        .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .contactPerson("test-contactPerson")
            .email("test-email")
            .phone("test-phone")
            .billingAddress(null)
            .shippingAddress(null)
            .notes("test-notes")
            .tags(Collections.emptyList())
            .website("test-website")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        VendorCommand.UpdateVendorCommand dto = VendorCommand.UpdateVendorCommand.builder()
                        .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .contactPerson("test-contactPerson")
            .email("test-email")
            .phone("test-phone")
            .billingAddress(null)
            .shippingAddress(null)
            .notes("test-notes")
            .tags(Collections.emptyList())
            .website("test-website")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}