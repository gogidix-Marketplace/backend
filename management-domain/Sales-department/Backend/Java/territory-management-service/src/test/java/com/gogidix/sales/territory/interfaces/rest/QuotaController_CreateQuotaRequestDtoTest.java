package com.gogidix.sales.territory.interfaces.rest;

import com.gogidix.sales.territory.domain.model.Quota;
import com.gogidix.sales.territory.interfaces.rest.QuotaController;
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
class QuotaController_CreateQuotaRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        QuotaController.CreateQuotaRequestDto dto = new QuotaController.CreateQuotaRequestDto();
        dto.setTerritoryId("val-territoryId");
        dto.setSalesRepresentativeId("val-salesRepresentativeId");
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        assertEquals("val-territoryId", dto.getTerritoryId());
        assertEquals("val-salesRepresentativeId", dto.getSalesRepresentativeId());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
    }

    @Test
    void testEqualsAndHashCode() {
        QuotaController.CreateQuotaRequestDto dto1 = new QuotaController.CreateQuotaRequestDto();
        QuotaController.CreateQuotaRequestDto dto2 = new QuotaController.CreateQuotaRequestDto();
        dto1.setTerritoryId("test");
        dto1.setSalesRepresentativeId("test");
        dto1.setType(Quota.QuotaType.REVENUE);
        dto1.setAmount(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setPeriod(Quota.QuotaPeriod.DAILY);
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto2.setTerritoryId("test");
        dto2.setSalesRepresentativeId("test");
        dto2.setType(Quota.QuotaType.REVENUE);
        dto2.setAmount(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setPeriod(Quota.QuotaPeriod.DAILY);
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTerritoryId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        QuotaController.CreateQuotaRequestDto dto = new QuotaController.CreateQuotaRequestDto();
        dto.setTerritoryId("test");
        dto.setSalesRepresentativeId("test");
        dto.setType(Quota.QuotaType.REVENUE);
        dto.setAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setPeriod(Quota.QuotaPeriod.DAILY);
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        QuotaController.CreateQuotaRequestDto dto = new QuotaController.CreateQuotaRequestDto();
        dto.setTerritoryId("test");
        dto.setSalesRepresentativeId("test");
        dto.setType(Quota.QuotaType.REVENUE);
        dto.setAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setPeriod(Quota.QuotaPeriod.DAILY);
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}