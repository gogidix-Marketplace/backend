package com.gogidix.sales.crm.interfaces.rest;

import com.gogidix.sales.crm.domain.model.Customer;
import com.gogidix.sales.crm.interfaces.rest.CustomerController;
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
class CustomerController_AdvanceLifecycleStageRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CustomerController.AdvanceLifecycleStageRequestDto dto = new CustomerController.AdvanceLifecycleStageRequestDto();


    }

    @Test
    void testEqualsAndHashCode() {
        CustomerController.AdvanceLifecycleStageRequestDto dto1 = new CustomerController.AdvanceLifecycleStageRequestDto();
        CustomerController.AdvanceLifecycleStageRequestDto dto2 = new CustomerController.AdvanceLifecycleStageRequestDto();
        dto1.setNewStage(Customer.CustomerLifecycleStage.LEAD);
        dto2.setNewStage(Customer.CustomerLifecycleStage.LEAD);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setNewStage(Customer.CustomerLifecycleStage.PROSPECT);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerController.AdvanceLifecycleStageRequestDto dto = new CustomerController.AdvanceLifecycleStageRequestDto();
        dto.setNewStage(Customer.CustomerLifecycleStage.LEAD);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerController.AdvanceLifecycleStageRequestDto dto = new CustomerController.AdvanceLifecycleStageRequestDto();
        dto.setNewStage(Customer.CustomerLifecycleStage.LEAD);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}