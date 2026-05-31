package com.gogidix.finance.revenue.domain.port.in;

import com.gogidix.finance.revenue.domain.model.Revenue;
import com.gogidix.finance.revenue.domain.port.in.RevenueCommand;
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
class RevenueCommand_CreateRevenueCommandTest {

        @Test
    void testSettersAndGetters() {
        RevenueCommand.CreateRevenueCommand dto = new RevenueCommand.CreateRevenueCommand();
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setContractId("val-contractId");
        dto.setProjectId("val-projectId");
        dto.setTotalAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setTransactionDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setCategory("val-category");
        dto.setProductCode("val-productCode");
        dto.setProductSku("val-productSku");
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setSalespersonId("val-salespersonId");
        dto.setRegion("val-region");
        dto.setTerritory("val-territory");
        dto.setInvoiceDate(LocalDate.of(2025,6,1));
        dto.setInvoiceNumber("val-invoiceNumber");
        dto.setDueDate(LocalDate.of(2025,6,1));
        dto.setRecognitionPeriods(99);
        dto.setRecognitionStartDate(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        dto.setIsRecurring(true);
        dto.setRecurringSchedule("val-recurringSchedule");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-contractId", dto.getContractId());
        assertEquals("val-projectId", dto.getProjectId());
        assertEquals(BigDecimal.ONE, dto.getTotalAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getTransactionDate());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-productCode", dto.getProductCode());
        assertEquals("val-productSku", dto.getProductSku());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-salespersonId", dto.getSalespersonId());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-territory", dto.getTerritory());
        assertEquals(LocalDate.of(2025,6,1), dto.getInvoiceDate());
        assertEquals("val-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(LocalDate.of(2025,6,1), dto.getDueDate());
        assertEquals(99, dto.getRecognitionPeriods());
        assertEquals(LocalDate.of(2025,6,1), dto.getRecognitionStartDate());
        assertEquals("val-notes", dto.getNotes());
        assertTrue(dto.getIsRecurring());
        assertEquals("val-recurringSchedule", dto.getRecurringSchedule());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueCommand.CreateRevenueCommand dto1 = new RevenueCommand.CreateRevenueCommand();
        RevenueCommand.CreateRevenueCommand dto2 = new RevenueCommand.CreateRevenueCommand();
        dto1.setTenantId("test");
        dto1.setCustomerId("test");
        dto1.setCustomerName("test");
        dto1.setContractId("test");
        dto1.setProjectId("test");
        dto1.setType(Revenue.RevenueType.RECURRING);
        dto1.setTotalAmount(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setRecognitionMethod(Revenue.RecognitionMethod.POINT_IN_TIME);
        dto1.setTransactionDate(LocalDate.of(2025,1,1));
        dto1.setDescription("test");
        dto1.setCategory("test");
        dto1.setProductCode("test");
        dto1.setProductSku("test");
        dto1.setDepartment("test");
        dto1.setCostCenter("test");
        dto1.setSalespersonId("test");
        dto1.setRegion("test");
        dto1.setTerritory("test");
        dto1.setPaymentTerms(Revenue.PaymentTerms.NET_15);
        dto1.setInvoiceDate(LocalDate.of(2025,1,1));
        dto1.setInvoiceNumber("test");
        dto1.setDueDate(LocalDate.of(2025,1,1));
        dto1.setRecognitionPeriods(42);
        dto1.setRecognitionStartDate(LocalDate.of(2025,1,1));
        dto1.setMilestones(Collections.emptyList());
        dto1.setTags(Collections.emptyList());
        dto1.setNotes("test");
        dto1.setIsRecurring(true);
        dto1.setRecurringSchedule("test");
        dto2.setTenantId("test");
        dto2.setCustomerId("test");
        dto2.setCustomerName("test");
        dto2.setContractId("test");
        dto2.setProjectId("test");
        dto2.setType(Revenue.RevenueType.RECURRING);
        dto2.setTotalAmount(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setRecognitionMethod(Revenue.RecognitionMethod.POINT_IN_TIME);
        dto2.setTransactionDate(LocalDate.of(2025,1,1));
        dto2.setDescription("test");
        dto2.setCategory("test");
        dto2.setProductCode("test");
        dto2.setProductSku("test");
        dto2.setDepartment("test");
        dto2.setCostCenter("test");
        dto2.setSalespersonId("test");
        dto2.setRegion("test");
        dto2.setTerritory("test");
        dto2.setPaymentTerms(Revenue.PaymentTerms.NET_15);
        dto2.setInvoiceDate(LocalDate.of(2025,1,1));
        dto2.setInvoiceNumber("test");
        dto2.setDueDate(LocalDate.of(2025,1,1));
        dto2.setRecognitionPeriods(42);
        dto2.setRecognitionStartDate(LocalDate.of(2025,1,1));
        dto2.setMilestones(Collections.emptyList());
        dto2.setTags(Collections.emptyList());
        dto2.setNotes("test");
        dto2.setIsRecurring(true);
        dto2.setRecurringSchedule("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueCommand.CreateRevenueCommand dto = new RevenueCommand.CreateRevenueCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setCustomerName("test");
        dto.setContractId("test");
        dto.setProjectId("test");
        dto.setType(Revenue.RevenueType.RECURRING);
        dto.setTotalAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setRecognitionMethod(Revenue.RecognitionMethod.POINT_IN_TIME);
        dto.setTransactionDate(LocalDate.of(2025,1,1));
        dto.setDescription("test");
        dto.setCategory("test");
        dto.setProductCode("test");
        dto.setProductSku("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        dto.setSalespersonId("test");
        dto.setRegion("test");
        dto.setTerritory("test");
        dto.setPaymentTerms(Revenue.PaymentTerms.NET_15);
        dto.setInvoiceDate(LocalDate.of(2025,1,1));
        dto.setInvoiceNumber("test");
        dto.setDueDate(LocalDate.of(2025,1,1));
        dto.setRecognitionPeriods(42);
        dto.setRecognitionStartDate(LocalDate.of(2025,1,1));
        dto.setMilestones(Collections.emptyList());
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        dto.setIsRecurring(true);
        dto.setRecurringSchedule("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueCommand.CreateRevenueCommand dto = new RevenueCommand.CreateRevenueCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setCustomerName("test");
        dto.setContractId("test");
        dto.setProjectId("test");
        dto.setType(Revenue.RevenueType.RECURRING);
        dto.setTotalAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setRecognitionMethod(Revenue.RecognitionMethod.POINT_IN_TIME);
        dto.setTransactionDate(LocalDate.of(2025,1,1));
        dto.setDescription("test");
        dto.setCategory("test");
        dto.setProductCode("test");
        dto.setProductSku("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        dto.setSalespersonId("test");
        dto.setRegion("test");
        dto.setTerritory("test");
        dto.setPaymentTerms(Revenue.PaymentTerms.NET_15);
        dto.setInvoiceDate(LocalDate.of(2025,1,1));
        dto.setInvoiceNumber("test");
        dto.setDueDate(LocalDate.of(2025,1,1));
        dto.setRecognitionPeriods(42);
        dto.setRecognitionStartDate(LocalDate.of(2025,1,1));
        dto.setMilestones(Collections.emptyList());
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        dto.setIsRecurring(true);
        dto.setRecurringSchedule("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}