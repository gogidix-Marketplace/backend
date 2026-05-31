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
class CustomerCommand_UpdateCustomerCommandTest {

        @Test
    void testSettersAndGetters() {
        CustomerCommand.UpdateCustomerCommand dto = new CustomerCommand.UpdateCustomerCommand();
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setCompanyName("val-companyName");
        dto.setIndustry("val-industry");
        dto.setDescription("val-description");
        dto.setWebsite("val-website");
        dto.setEmployeeCount(99);
        dto.setPhoneNumber("val-phoneNumber");
        dto.setEmail("val-email");
        dto.setTerritory("val-territory");
        dto.setSatisfactionScore(99);
        dto.setPaymentTerms("val-paymentTerms");
        dto.setCurrency("val-currency");
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-companyName", dto.getCompanyName());
        assertEquals("val-industry", dto.getIndustry());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-website", dto.getWebsite());
        assertEquals(99, dto.getEmployeeCount());
        assertEquals("val-phoneNumber", dto.getPhoneNumber());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-territory", dto.getTerritory());
        assertEquals(99, dto.getSatisfactionScore());
        assertEquals("val-paymentTerms", dto.getPaymentTerms());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerCommand.UpdateCustomerCommand dto1 = new CustomerCommand.UpdateCustomerCommand();
        CustomerCommand.UpdateCustomerCommand dto2 = new CustomerCommand.UpdateCustomerCommand();
        dto1.setTenantId("test");
        dto1.setCustomerId("test");
        dto1.setCompanyName("test");
        dto1.setIndustry("test");
        dto1.setSegment(Customer.CustomerSegment.ENTERPRISE);
        dto1.setDescription("test");
        dto1.setWebsite("test");
        dto1.setEmployeeCount(42);
        dto1.setAnnualRevenue(null);
        dto1.setPhoneNumber("test");
        dto1.setEmail("test");
        dto1.setTerritory("test");
        dto1.setSatisfactionScore(42);
        dto1.setPaymentTerms("test");
        dto1.setCurrency("test");
        dto1.setCreditLimit(null);
        dto1.setTags(Collections.emptyList());
        dto1.setNotes("test");
        dto2.setTenantId("test");
        dto2.setCustomerId("test");
        dto2.setCompanyName("test");
        dto2.setIndustry("test");
        dto2.setSegment(Customer.CustomerSegment.ENTERPRISE);
        dto2.setDescription("test");
        dto2.setWebsite("test");
        dto2.setEmployeeCount(42);
        dto2.setAnnualRevenue(null);
        dto2.setPhoneNumber("test");
        dto2.setEmail("test");
        dto2.setTerritory("test");
        dto2.setSatisfactionScore(42);
        dto2.setPaymentTerms("test");
        dto2.setCurrency("test");
        dto2.setCreditLimit(null);
        dto2.setTags(Collections.emptyList());
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerCommand.UpdateCustomerCommand dto = new CustomerCommand.UpdateCustomerCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setCompanyName("test");
        dto.setIndustry("test");
        dto.setSegment(Customer.CustomerSegment.ENTERPRISE);
        dto.setDescription("test");
        dto.setWebsite("test");
        dto.setEmployeeCount(42);
        dto.setAnnualRevenue(null);
        dto.setPhoneNumber("test");
        dto.setEmail("test");
        dto.setTerritory("test");
        dto.setSatisfactionScore(42);
        dto.setPaymentTerms("test");
        dto.setCurrency("test");
        dto.setCreditLimit(null);
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerCommand.UpdateCustomerCommand dto = new CustomerCommand.UpdateCustomerCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setCompanyName("test");
        dto.setIndustry("test");
        dto.setSegment(Customer.CustomerSegment.ENTERPRISE);
        dto.setDescription("test");
        dto.setWebsite("test");
        dto.setEmployeeCount(42);
        dto.setAnnualRevenue(null);
        dto.setPhoneNumber("test");
        dto.setEmail("test");
        dto.setTerritory("test");
        dto.setSatisfactionScore(42);
        dto.setPaymentTerms("test");
        dto.setCurrency("test");
        dto.setCreditLimit(null);
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}