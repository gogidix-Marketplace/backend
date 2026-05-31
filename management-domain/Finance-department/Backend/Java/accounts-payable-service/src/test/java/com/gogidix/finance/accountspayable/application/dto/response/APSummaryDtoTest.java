package com.gogidix.finance.accountspayable.application.dto.response;

import com.gogidix.finance.accountspayable.application.dto.response.APSummaryDto;
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
class APSummaryDtoTest {

        @Test
    void testBuilder() {
        APSummaryDto dto = APSummaryDto.builder()
                        .totalVendors(42L)
            .activeVendors(42L)
            .totalInvoices(42L)
            .pendingInvoices(42L)
            .approvedInvoices(42L)
            .overdueInvoices(42L)
            .pendingAmount(BigDecimal.TEN)
            .approvedAmount(BigDecimal.TEN)
            .overdueAmount(BigDecimal.TEN)
            .totalOutstanding(BigDecimal.TEN)
            .totalPayments(42L)
            .pendingPayments(42L)
            .scheduledPayments(42L)
            .completedPayments(42L)
            .failedPayments(42L)
            .totalPaymentAmount(BigDecimal.TEN)
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .vendorStatus("test-vendorStatus")
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalVendors());
        assertEquals(42L, dto.getActiveVendors());
        assertEquals(42L, dto.getTotalInvoices());
        assertEquals(42L, dto.getPendingInvoices());
        assertEquals(42L, dto.getApprovedInvoices());
        assertEquals(42L, dto.getOverdueInvoices());
        assertEquals(BigDecimal.TEN, dto.getPendingAmount());
        assertEquals(BigDecimal.TEN, dto.getApprovedAmount());
        assertEquals(BigDecimal.TEN, dto.getOverdueAmount());
        assertEquals(BigDecimal.TEN, dto.getTotalOutstanding());
        assertEquals(42L, dto.getTotalPayments());
        assertEquals(42L, dto.getPendingPayments());
        assertEquals(42L, dto.getScheduledPayments());
        assertEquals(42L, dto.getCompletedPayments());
        assertEquals(42L, dto.getFailedPayments());
        assertEquals(BigDecimal.TEN, dto.getTotalPaymentAmount());
        assertEquals("test-vendorId", dto.getVendorId());
        assertEquals("test-vendorName", dto.getVendorName());
        assertEquals("test-vendorStatus", dto.getVendorStatus());
    }

    @Test
    void testSettersAndGetters() {
        APSummaryDto dto = new APSummaryDto();
        dto.setPendingAmount(BigDecimal.ONE);
        dto.setApprovedAmount(BigDecimal.ONE);
        dto.setOverdueAmount(BigDecimal.ONE);
        dto.setTotalOutstanding(BigDecimal.ONE);
        dto.setTotalPaymentAmount(BigDecimal.ONE);
        dto.setVendorId("val-vendorId");
        dto.setVendorName("val-vendorName");
        dto.setVendorStatus("val-vendorStatus");
        assertEquals(BigDecimal.ONE, dto.getPendingAmount());
        assertEquals(BigDecimal.ONE, dto.getApprovedAmount());
        assertEquals(BigDecimal.ONE, dto.getOverdueAmount());
        assertEquals(BigDecimal.ONE, dto.getTotalOutstanding());
        assertEquals(BigDecimal.ONE, dto.getTotalPaymentAmount());
        assertEquals("val-vendorId", dto.getVendorId());
        assertEquals("val-vendorName", dto.getVendorName());
        assertEquals("val-vendorStatus", dto.getVendorStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        APSummaryDto dto1 = APSummaryDto.builder()
                        .totalVendors(42L)
            .activeVendors(42L)
            .totalInvoices(42L)
            .pendingInvoices(42L)
            .approvedInvoices(42L)
            .overdueInvoices(42L)
            .pendingAmount(BigDecimal.TEN)
            .approvedAmount(BigDecimal.TEN)
            .overdueAmount(BigDecimal.TEN)
            .totalOutstanding(BigDecimal.TEN)
            .totalPayments(42L)
            .pendingPayments(42L)
            .scheduledPayments(42L)
            .completedPayments(42L)
            .failedPayments(42L)
            .totalPaymentAmount(BigDecimal.TEN)
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .vendorStatus("test-vendorStatus")
            .build();
        APSummaryDto dto2 = APSummaryDto.builder()
                        .totalVendors(42L)
            .activeVendors(42L)
            .totalInvoices(42L)
            .pendingInvoices(42L)
            .approvedInvoices(42L)
            .overdueInvoices(42L)
            .pendingAmount(BigDecimal.TEN)
            .approvedAmount(BigDecimal.TEN)
            .overdueAmount(BigDecimal.TEN)
            .totalOutstanding(BigDecimal.TEN)
            .totalPayments(42L)
            .pendingPayments(42L)
            .scheduledPayments(42L)
            .completedPayments(42L)
            .failedPayments(42L)
            .totalPaymentAmount(BigDecimal.TEN)
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .vendorStatus("test-vendorStatus")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        APSummaryDto dto = APSummaryDto.builder()
                        .totalVendors(42L)
            .activeVendors(42L)
            .totalInvoices(42L)
            .pendingInvoices(42L)
            .approvedInvoices(42L)
            .overdueInvoices(42L)
            .pendingAmount(BigDecimal.TEN)
            .approvedAmount(BigDecimal.TEN)
            .overdueAmount(BigDecimal.TEN)
            .totalOutstanding(BigDecimal.TEN)
            .totalPayments(42L)
            .pendingPayments(42L)
            .scheduledPayments(42L)
            .completedPayments(42L)
            .failedPayments(42L)
            .totalPaymentAmount(BigDecimal.TEN)
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .vendorStatus("test-vendorStatus")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}