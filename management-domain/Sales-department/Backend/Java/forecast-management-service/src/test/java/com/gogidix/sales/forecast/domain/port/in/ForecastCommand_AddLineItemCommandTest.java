package com.gogidix.sales.forecast.domain.port.in;

import com.gogidix.sales.forecast.domain.model.ForecastLineItem;
import com.gogidix.sales.forecast.domain.port.in.ForecastCommand;
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
class ForecastCommand_AddLineItemCommandTest {

        @Test
    void testSettersAndGetters() {
        ForecastCommand.AddLineItemCommand dto = new ForecastCommand.AddLineItemCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setBestCase(BigDecimal.ONE);
        dto.setLikely(BigDecimal.ONE);
        dto.setWorstCase(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setProductId("val-productId");
        dto.setProductName("val-productName");
        dto.setTerritoryId("val-territoryId");
        dto.setTerritoryName("val-territoryName");
        dto.setCustomerSegmentId("val-customerSegmentId");
        dto.setCustomerSegmentName("val-customerSegmentName");
        dto.setSalesChannel("val-salesChannel");
        dto.setNotes("val-notes");
        dto.setOwner("val-owner");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getBestCase());
        assertEquals(BigDecimal.ONE, dto.getLikely());
        assertEquals(BigDecimal.ONE, dto.getWorstCase());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-productId", dto.getProductId());
        assertEquals("val-productName", dto.getProductName());
        assertEquals("val-territoryId", dto.getTerritoryId());
        assertEquals("val-territoryName", dto.getTerritoryName());
        assertEquals("val-customerSegmentId", dto.getCustomerSegmentId());
        assertEquals("val-customerSegmentName", dto.getCustomerSegmentName());
        assertEquals("val-salesChannel", dto.getSalesChannel());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-owner", dto.getOwner());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastCommand.AddLineItemCommand dto1 = new ForecastCommand.AddLineItemCommand();
        ForecastCommand.AddLineItemCommand dto2 = new ForecastCommand.AddLineItemCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setCategory(ForecastLineItem.ForecastCategory.BEST_CASE);
        dto1.setType(ForecastLineItem.LineItemType.PRODUCT);
        dto1.setBestCase(BigDecimal.TEN);
        dto1.setLikely(BigDecimal.TEN);
        dto1.setWorstCase(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setProductId("test");
        dto1.setProductName("test");
        dto1.setTerritoryId("test");
        dto1.setTerritoryName("test");
        dto1.setCustomerSegmentId("test");
        dto1.setCustomerSegmentName("test");
        dto1.setSalesChannel("test");
        dto1.setNotes("test");
        dto1.setOwner("test");
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setCategory(ForecastLineItem.ForecastCategory.BEST_CASE);
        dto2.setType(ForecastLineItem.LineItemType.PRODUCT);
        dto2.setBestCase(BigDecimal.TEN);
        dto2.setLikely(BigDecimal.TEN);
        dto2.setWorstCase(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setProductId("test");
        dto2.setProductName("test");
        dto2.setTerritoryId("test");
        dto2.setTerritoryName("test");
        dto2.setCustomerSegmentId("test");
        dto2.setCustomerSegmentName("test");
        dto2.setSalesChannel("test");
        dto2.setNotes("test");
        dto2.setOwner("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastCommand.AddLineItemCommand dto = new ForecastCommand.AddLineItemCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setCategory(ForecastLineItem.ForecastCategory.BEST_CASE);
        dto.setType(ForecastLineItem.LineItemType.PRODUCT);
        dto.setBestCase(BigDecimal.TEN);
        dto.setLikely(BigDecimal.TEN);
        dto.setWorstCase(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setProductId("test");
        dto.setProductName("test");
        dto.setTerritoryId("test");
        dto.setTerritoryName("test");
        dto.setCustomerSegmentId("test");
        dto.setCustomerSegmentName("test");
        dto.setSalesChannel("test");
        dto.setNotes("test");
        dto.setOwner("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastCommand.AddLineItemCommand dto = new ForecastCommand.AddLineItemCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setCategory(ForecastLineItem.ForecastCategory.BEST_CASE);
        dto.setType(ForecastLineItem.LineItemType.PRODUCT);
        dto.setBestCase(BigDecimal.TEN);
        dto.setLikely(BigDecimal.TEN);
        dto.setWorstCase(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setProductId("test");
        dto.setProductName("test");
        dto.setTerritoryId("test");
        dto.setTerritoryName("test");
        dto.setCustomerSegmentId("test");
        dto.setCustomerSegmentName("test");
        dto.setSalesChannel("test");
        dto.setNotes("test");
        dto.setOwner("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}