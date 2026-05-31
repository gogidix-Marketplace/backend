package com.gogidix.sales.crm.domain.port.in;

import com.gogidix.sales.crm.domain.model.Customer;
import com.gogidix.sales.crm.domain.port.in.CustomerCommand;
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
class CustomerCommand_AdvanceLifecycleStageCommandTest {

        @Test
    void testSettersAndGetters() {
        CustomerCommand.AdvanceLifecycleStageCommand dto = new CustomerCommand.AdvanceLifecycleStageCommand();
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerCommand.AdvanceLifecycleStageCommand dto1 = new CustomerCommand.AdvanceLifecycleStageCommand();
        CustomerCommand.AdvanceLifecycleStageCommand dto2 = new CustomerCommand.AdvanceLifecycleStageCommand();
        dto1.setTenantId("test");
        dto1.setCustomerId("test");
        dto1.setNewStage(Customer.CustomerLifecycleStage.LEAD);
        dto1.setUpdatedBy("test");
        dto2.setTenantId("test");
        dto2.setCustomerId("test");
        dto2.setNewStage(Customer.CustomerLifecycleStage.LEAD);
        dto2.setUpdatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerCommand.AdvanceLifecycleStageCommand dto = new CustomerCommand.AdvanceLifecycleStageCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setNewStage(Customer.CustomerLifecycleStage.LEAD);
        dto.setUpdatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerCommand.AdvanceLifecycleStageCommand dto = new CustomerCommand.AdvanceLifecycleStageCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setNewStage(Customer.CustomerLifecycleStage.LEAD);
        dto.setUpdatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}