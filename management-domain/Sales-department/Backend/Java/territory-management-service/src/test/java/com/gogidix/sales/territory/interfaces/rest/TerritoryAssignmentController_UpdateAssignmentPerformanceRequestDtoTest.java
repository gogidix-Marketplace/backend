package com.gogidix.sales.territory.interfaces.rest;

import com.gogidix.sales.territory.interfaces.rest.TerritoryAssignmentController;
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
class TerritoryAssignmentController_UpdateAssignmentPerformanceRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TerritoryAssignmentController.UpdateAssignmentPerformanceRequestDto dto = new TerritoryAssignmentController.UpdateAssignmentPerformanceRequestDto();
        dto.setSalesGenerated(BigDecimal.ONE);
        dto.setAccountsManaged(99);
        dto.setDealsClosed(99);
        assertEquals(BigDecimal.ONE, dto.getSalesGenerated());
        assertEquals(99, dto.getAccountsManaged());
        assertEquals(99, dto.getDealsClosed());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryAssignmentController.UpdateAssignmentPerformanceRequestDto dto1 = new TerritoryAssignmentController.UpdateAssignmentPerformanceRequestDto();
        TerritoryAssignmentController.UpdateAssignmentPerformanceRequestDto dto2 = new TerritoryAssignmentController.UpdateAssignmentPerformanceRequestDto();
        dto1.setSalesGenerated(BigDecimal.TEN);
        dto1.setAccountsManaged(42);
        dto1.setDealsClosed(42);
        dto2.setSalesGenerated(BigDecimal.TEN);
        dto2.setAccountsManaged(42);
        dto2.setDealsClosed(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setSalesGenerated(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TerritoryAssignmentController.UpdateAssignmentPerformanceRequestDto dto = new TerritoryAssignmentController.UpdateAssignmentPerformanceRequestDto();
        dto.setSalesGenerated(BigDecimal.TEN);
        dto.setAccountsManaged(42);
        dto.setDealsClosed(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TerritoryAssignmentController.UpdateAssignmentPerformanceRequestDto dto = new TerritoryAssignmentController.UpdateAssignmentPerformanceRequestDto();
        dto.setSalesGenerated(BigDecimal.TEN);
        dto.setAccountsManaged(42);
        dto.setDealsClosed(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}