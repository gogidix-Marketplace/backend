package com.gogidix.sales.revenue.domain.port.in;

import com.gogidix.sales.revenue.domain.model.Revenue;
import com.gogidix.sales.revenue.domain.port.in.RevenueCommand;
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
        dto.setContractId("val-contractId");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setProductId("val-productId");
        dto.setProductName("val-productName");
        dto.setTotalAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setRecognitionPeriodMonths(99);
        dto.setTerritory("val-territory");
        dto.setRegion("val-region");
        dto.setSalespersonId("val-salespersonId");
        dto.setSalespersonName("val-salespersonName");
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setProjectId("val-projectId");
        dto.setSalesOrderId("val-salesOrderId");
        dto.setOpportunityId("val-opportunityId");
        dto.setDescription("val-description");
        dto.setNotes("val-notes");
        dto.setInvoiceId("val-invoiceId");
        dto.setInvoiceNumber("val-invoiceNumber");
        dto.setInvoiceDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-contractId", dto.getContractId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-productId", dto.getProductId());
        assertEquals("val-productName", dto.getProductName());
        assertEquals(BigDecimal.ONE, dto.getTotalAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(99, dto.getRecognitionPeriodMonths());
        assertEquals("val-territory", dto.getTerritory());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-salespersonId", dto.getSalespersonId());
        assertEquals("val-salespersonName", dto.getSalespersonName());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-projectId", dto.getProjectId());
        assertEquals("val-salesOrderId", dto.getSalesOrderId());
        assertEquals("val-opportunityId", dto.getOpportunityId());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(LocalDate.of(2025,6,1), dto.getInvoiceDate());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueCommand.CreateRevenueCommand dto1 = new RevenueCommand.CreateRevenueCommand();
        RevenueCommand.CreateRevenueCommand dto2 = new RevenueCommand.CreateRevenueCommand();
        dto1.setTenantId("test");
        dto1.setContractId("test");
        dto1.setCustomerId("test");
        dto1.setCustomerName("test");
        dto1.setProductId("test");
        dto1.setProductName("test");
        dto1.setRevenueType(Revenue.RevenueType.NEW_BUSINESS);
        dto1.setTotalAmount(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setRecognitionType(Revenue.RevenueRecognitionType.POINT_IN_TIME);
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setRecognitionPeriodMonths(42);
        dto1.setTerritory("test");
        dto1.setRegion("test");
        dto1.setSalespersonId("test");
        dto1.setSalespersonName("test");
        dto1.setDepartment("test");
        dto1.setCostCenter("test");
        dto1.setProjectId("test");
        dto1.setSalesOrderId("test");
        dto1.setOpportunityId("test");
        dto1.setTags(Collections.emptyList());
        dto1.setDescription("test");
        dto1.setNotes("test");
        dto1.setInvoiceId("test");
        dto1.setInvoiceNumber("test");
        dto1.setInvoiceDate(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setContractId("test");
        dto2.setCustomerId("test");
        dto2.setCustomerName("test");
        dto2.setProductId("test");
        dto2.setProductName("test");
        dto2.setRevenueType(Revenue.RevenueType.NEW_BUSINESS);
        dto2.setTotalAmount(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setRecognitionType(Revenue.RevenueRecognitionType.POINT_IN_TIME);
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setRecognitionPeriodMonths(42);
        dto2.setTerritory("test");
        dto2.setRegion("test");
        dto2.setSalespersonId("test");
        dto2.setSalespersonName("test");
        dto2.setDepartment("test");
        dto2.setCostCenter("test");
        dto2.setProjectId("test");
        dto2.setSalesOrderId("test");
        dto2.setOpportunityId("test");
        dto2.setTags(Collections.emptyList());
        dto2.setDescription("test");
        dto2.setNotes("test");
        dto2.setInvoiceId("test");
        dto2.setInvoiceNumber("test");
        dto2.setInvoiceDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueCommand.CreateRevenueCommand dto = new RevenueCommand.CreateRevenueCommand();
        dto.setTenantId("test");
        dto.setContractId("test");
        dto.setCustomerId("test");
        dto.setCustomerName("test");
        dto.setProductId("test");
        dto.setProductName("test");
        dto.setRevenueType(Revenue.RevenueType.NEW_BUSINESS);
        dto.setTotalAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setRecognitionType(Revenue.RevenueRecognitionType.POINT_IN_TIME);
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setRecognitionPeriodMonths(42);
        dto.setTerritory("test");
        dto.setRegion("test");
        dto.setSalespersonId("test");
        dto.setSalespersonName("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        dto.setProjectId("test");
        dto.setSalesOrderId("test");
        dto.setOpportunityId("test");
        dto.setTags(Collections.emptyList());
        dto.setDescription("test");
        dto.setNotes("test");
        dto.setInvoiceId("test");
        dto.setInvoiceNumber("test");
        dto.setInvoiceDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueCommand.CreateRevenueCommand dto = new RevenueCommand.CreateRevenueCommand();
        dto.setTenantId("test");
        dto.setContractId("test");
        dto.setCustomerId("test");
        dto.setCustomerName("test");
        dto.setProductId("test");
        dto.setProductName("test");
        dto.setRevenueType(Revenue.RevenueType.NEW_BUSINESS);
        dto.setTotalAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setRecognitionType(Revenue.RevenueRecognitionType.POINT_IN_TIME);
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setRecognitionPeriodMonths(42);
        dto.setTerritory("test");
        dto.setRegion("test");
        dto.setSalespersonId("test");
        dto.setSalespersonName("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        dto.setProjectId("test");
        dto.setSalesOrderId("test");
        dto.setOpportunityId("test");
        dto.setTags(Collections.emptyList());
        dto.setDescription("test");
        dto.setNotes("test");
        dto.setInvoiceId("test");
        dto.setInvoiceNumber("test");
        dto.setInvoiceDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}