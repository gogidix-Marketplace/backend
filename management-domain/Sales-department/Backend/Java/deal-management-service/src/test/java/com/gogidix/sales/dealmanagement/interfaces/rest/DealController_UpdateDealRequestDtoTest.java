package com.gogidix.sales.dealmanagement.interfaces.rest;

import com.gogidix.sales.dealmanagement.domain.model.Deal;
import com.gogidix.sales.dealmanagement.interfaces.rest.DealController;
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
class DealController_UpdateDealRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        DealController.UpdateDealRequestDto dto = new DealController.UpdateDealRequestDto();
        dto.setDealName("val-dealName");
        dto.setAmount(BigDecimal.ONE);
        dto.setExpectedCloseDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setNextSteps("val-nextSteps");
        dto.setProbability(99);
        assertEquals("val-dealName", dto.getDealName());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpectedCloseDate());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-nextSteps", dto.getNextSteps());
        assertEquals(99, dto.getProbability());
    }

    @Test
    void testEqualsAndHashCode() {
        DealController.UpdateDealRequestDto dto1 = new DealController.UpdateDealRequestDto();
        DealController.UpdateDealRequestDto dto2 = new DealController.UpdateDealRequestDto();
        dto1.setDealName("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setExpectedCloseDate(LocalDate.of(2025,1,1));
        dto1.setPriority(Deal.DealPriority.LOW);
        dto1.setDescription("test");
        dto1.setNextSteps("test");
        dto1.setProbability(42);
        dto1.setTags(Collections.emptyList());
        dto2.setDealName("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setExpectedCloseDate(LocalDate.of(2025,1,1));
        dto2.setPriority(Deal.DealPriority.LOW);
        dto2.setDescription("test");
        dto2.setNextSteps("test");
        dto2.setProbability(42);
        dto2.setTags(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setDealName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealController.UpdateDealRequestDto dto = new DealController.UpdateDealRequestDto();
        dto.setDealName("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setExpectedCloseDate(LocalDate.of(2025,1,1));
        dto.setPriority(Deal.DealPriority.LOW);
        dto.setDescription("test");
        dto.setNextSteps("test");
        dto.setProbability(42);
        dto.setTags(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealController.UpdateDealRequestDto dto = new DealController.UpdateDealRequestDto();
        dto.setDealName("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setExpectedCloseDate(LocalDate.of(2025,1,1));
        dto.setPriority(Deal.DealPriority.LOW);
        dto.setDescription("test");
        dto.setNextSteps("test");
        dto.setProbability(42);
        dto.setTags(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}