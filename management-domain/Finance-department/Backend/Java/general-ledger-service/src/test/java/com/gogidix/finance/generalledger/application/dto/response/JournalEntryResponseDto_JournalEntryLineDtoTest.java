package com.gogidix.finance.generalledger.application.dto.response;

import com.gogidix.finance.generalledger.application.dto.response.JournalEntryResponseDto;
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
class JournalEntryResponseDto_JournalEntryLineDtoTest {

        @Test
    void testBuilder() {
        JournalEntryResponseDto.JournalEntryLineDto dto = JournalEntryResponseDto.JournalEntryLineDto.builder()
                        .lineId("test-lineId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .debitAmount(BigDecimal.TEN)
            .creditAmount(BigDecimal.TEN)
            .description("test-description")
            .costCenter("test-costCenter")
            .department("test-department")
            .projectId("test-projectId")
            .taskId("test-taskId")
            .reference("test-reference")
            .taxCode("test-taxCode")
            .taxRate(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .isTaxInclusive(true)
            .tags(Collections.emptyList())
            .sequenceNumber(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-lineId", dto.getLineId());
        assertEquals("test-accountId", dto.getAccountId());
        assertEquals("test-accountNumber", dto.getAccountNumber());
        assertEquals("test-accountName", dto.getAccountName());
        assertEquals(BigDecimal.TEN, dto.getDebitAmount());
        assertEquals(BigDecimal.TEN, dto.getCreditAmount());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-costCenter", dto.getCostCenter());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-projectId", dto.getProjectId());
        assertEquals("test-taskId", dto.getTaskId());
        assertEquals("test-reference", dto.getReference());
        assertEquals("test-taxCode", dto.getTaxCode());
        assertEquals(BigDecimal.TEN, dto.getTaxRate());
        assertEquals(BigDecimal.TEN, dto.getTaxAmount());
        assertTrue(dto.getIsTaxInclusive());
        assertEquals(42, dto.getSequenceNumber());
    }

    @Test
    void testSettersAndGetters() {
        JournalEntryResponseDto.JournalEntryLineDto dto = new JournalEntryResponseDto.JournalEntryLineDto();
        dto.setLineId("val-lineId");
        dto.setAccountId("val-accountId");
        dto.setAccountNumber("val-accountNumber");
        dto.setAccountName("val-accountName");
        dto.setDebitAmount(BigDecimal.ONE);
        dto.setCreditAmount(BigDecimal.ONE);
        dto.setDescription("val-description");
        dto.setCostCenter("val-costCenter");
        dto.setDepartment("val-department");
        dto.setProjectId("val-projectId");
        dto.setTaskId("val-taskId");
        dto.setReference("val-reference");
        dto.setTaxCode("val-taxCode");
        dto.setTaxRate(BigDecimal.ONE);
        dto.setTaxAmount(BigDecimal.ONE);
        dto.setIsTaxInclusive(true);
        dto.setSequenceNumber(99);
        assertEquals("val-lineId", dto.getLineId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals(BigDecimal.ONE, dto.getDebitAmount());
        assertEquals(BigDecimal.ONE, dto.getCreditAmount());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-projectId", dto.getProjectId());
        assertEquals("val-taskId", dto.getTaskId());
        assertEquals("val-reference", dto.getReference());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals(BigDecimal.ONE, dto.getTaxRate());
        assertEquals(BigDecimal.ONE, dto.getTaxAmount());
        assertTrue(dto.getIsTaxInclusive());
        assertEquals(99, dto.getSequenceNumber());
    }

    @Test
    void testEqualsAndHashCode() {
        JournalEntryResponseDto.JournalEntryLineDto dto1 = JournalEntryResponseDto.JournalEntryLineDto.builder()
                        .lineId("test-lineId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .debitAmount(BigDecimal.TEN)
            .creditAmount(BigDecimal.TEN)
            .description("test-description")
            .costCenter("test-costCenter")
            .department("test-department")
            .projectId("test-projectId")
            .taskId("test-taskId")
            .reference("test-reference")
            .taxCode("test-taxCode")
            .taxRate(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .isTaxInclusive(true)
            .tags(Collections.emptyList())
            .sequenceNumber(42)
            .build();
        JournalEntryResponseDto.JournalEntryLineDto dto2 = JournalEntryResponseDto.JournalEntryLineDto.builder()
                        .lineId("test-lineId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .debitAmount(BigDecimal.TEN)
            .creditAmount(BigDecimal.TEN)
            .description("test-description")
            .costCenter("test-costCenter")
            .department("test-department")
            .projectId("test-projectId")
            .taskId("test-taskId")
            .reference("test-reference")
            .taxCode("test-taxCode")
            .taxRate(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .isTaxInclusive(true)
            .tags(Collections.emptyList())
            .sequenceNumber(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        JournalEntryResponseDto.JournalEntryLineDto dto = JournalEntryResponseDto.JournalEntryLineDto.builder()
                        .lineId("test-lineId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .debitAmount(BigDecimal.TEN)
            .creditAmount(BigDecimal.TEN)
            .description("test-description")
            .costCenter("test-costCenter")
            .department("test-department")
            .projectId("test-projectId")
            .taskId("test-taskId")
            .reference("test-reference")
            .taxCode("test-taxCode")
            .taxRate(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .isTaxInclusive(true)
            .tags(Collections.emptyList())
            .sequenceNumber(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}