package com.gogidix.sales.dealmanagement.domain.port.in;

import com.gogidix.sales.dealmanagement.domain.model.DealProduct;
import com.gogidix.sales.dealmanagement.domain.port.in.DealCommand;
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
class DealCommand_CreateProductCommandTest {

        @Test
    void testSettersAndGetters() {
        DealCommand.CreateProductCommand dto = new DealCommand.CreateProductCommand();
        dto.setProductCode("val-productCode");
        dto.setProductName("val-productName");
        dto.setProductDescription("val-productDescription");
        dto.setProductCategory("val-productCategory");
        dto.setQuantity(99);
        dto.setUnitPrice(BigDecimal.ONE);
        dto.setDiscountAmount(BigDecimal.ONE);
        dto.setDiscountPercentage(BigDecimal.ONE);
        dto.setServiceType("val-serviceType");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setIsRecurring(true);
        assertEquals("val-productCode", dto.getProductCode());
        assertEquals("val-productName", dto.getProductName());
        assertEquals("val-productDescription", dto.getProductDescription());
        assertEquals("val-productCategory", dto.getProductCategory());
        assertEquals(99, dto.getQuantity());
        assertEquals(BigDecimal.ONE, dto.getUnitPrice());
        assertEquals(BigDecimal.ONE, dto.getDiscountAmount());
        assertEquals(BigDecimal.ONE, dto.getDiscountPercentage());
        assertEquals("val-serviceType", dto.getServiceType());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertTrue(dto.getIsRecurring());
    }

    @Test
    void testEqualsAndHashCode() {
        DealCommand.CreateProductCommand dto1 = new DealCommand.CreateProductCommand();
        DealCommand.CreateProductCommand dto2 = new DealCommand.CreateProductCommand();
        dto1.setProductCode("test");
        dto1.setProductName("test");
        dto1.setProductDescription("test");
        dto1.setProductCategory("test");
        dto1.setQuantity(42);
        dto1.setUnitPrice(BigDecimal.TEN);
        dto1.setDiscountAmount(BigDecimal.TEN);
        dto1.setDiscountPercentage(BigDecimal.TEN);
        dto1.setServiceType("test");
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setIsRecurring(true);
        dto1.setBillingCycle(DealProduct.BillingCycle.MONTHLY);
        dto2.setProductCode("test");
        dto2.setProductName("test");
        dto2.setProductDescription("test");
        dto2.setProductCategory("test");
        dto2.setQuantity(42);
        dto2.setUnitPrice(BigDecimal.TEN);
        dto2.setDiscountAmount(BigDecimal.TEN);
        dto2.setDiscountPercentage(BigDecimal.TEN);
        dto2.setServiceType("test");
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setIsRecurring(true);
        dto2.setBillingCycle(DealProduct.BillingCycle.MONTHLY);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setProductCode(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealCommand.CreateProductCommand dto = new DealCommand.CreateProductCommand();
        dto.setProductCode("test");
        dto.setProductName("test");
        dto.setProductDescription("test");
        dto.setProductCategory("test");
        dto.setQuantity(42);
        dto.setUnitPrice(BigDecimal.TEN);
        dto.setDiscountAmount(BigDecimal.TEN);
        dto.setDiscountPercentage(BigDecimal.TEN);
        dto.setServiceType("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setIsRecurring(true);
        dto.setBillingCycle(DealProduct.BillingCycle.MONTHLY);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealCommand.CreateProductCommand dto = new DealCommand.CreateProductCommand();
        dto.setProductCode("test");
        dto.setProductName("test");
        dto.setProductDescription("test");
        dto.setProductCategory("test");
        dto.setQuantity(42);
        dto.setUnitPrice(BigDecimal.TEN);
        dto.setDiscountAmount(BigDecimal.TEN);
        dto.setDiscountPercentage(BigDecimal.TEN);
        dto.setServiceType("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setIsRecurring(true);
        dto.setBillingCycle(DealProduct.BillingCycle.MONTHLY);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}