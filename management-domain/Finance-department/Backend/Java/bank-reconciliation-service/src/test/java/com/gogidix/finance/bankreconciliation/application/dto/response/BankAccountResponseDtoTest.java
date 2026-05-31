package com.gogidix.finance.bankreconciliation.application.dto.response;

import com.gogidix.finance.bankreconciliation.application.dto.response.BankAccountResponseDto;
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
class BankAccountResponseDtoTest {

        @Test
    void testBuilder() {
        BankAccountResponseDto dto = BankAccountResponseDto.builder()
                        .id("test-id")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(BankAccountResponseDto.AccountTypeDto.CHECKING)
            .bankName("test-bankName")
            .bankCode("test-bankCode")
            .currency("test-currency")
            .balance(BigDecimal.TEN)
            .balanceDate(LocalDate.of(2025,1,15))
            .status(BankAccountResponseDto.AccountStatusDto.ACTIVE)
            .isPrimary(true)
            .lastReconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastStatementDate(LocalDate.of(2025,1,15))
            .openingBalance(BigDecimal.TEN)
            .iban("test-iban")
            .swiftCode("test-swiftCode")
            .routingNumber("test-routingNumber")
            .description("test-description")
            .tags(Collections.emptyList())
            .statementFrequency(BankAccountResponseDto.StatementFrequencyDto.DAILY)
            .reconciliationTolerance(BigDecimal.TEN)
            .autoReconcile(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-accountNumber", dto.getAccountNumber());
        assertEquals("test-accountName", dto.getAccountName());
        assertEquals(BankAccountResponseDto.AccountTypeDto.CHECKING, dto.getAccountType());
        assertEquals("test-bankName", dto.getBankName());
        assertEquals("test-bankCode", dto.getBankCode());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getBalance());
        assertEquals(LocalDate.of(2025,1,15), dto.getBalanceDate());
        assertEquals(BankAccountResponseDto.AccountStatusDto.ACTIVE, dto.getStatus());
        assertTrue(dto.getIsPrimary());
        assertEquals(LocalDate.of(2025,1,15), dto.getLastStatementDate());
        assertEquals(BigDecimal.TEN, dto.getOpeningBalance());
        assertEquals("test-iban", dto.getIban());
        assertEquals("test-swiftCode", dto.getSwiftCode());
        assertEquals("test-routingNumber", dto.getRoutingNumber());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BankAccountResponseDto.StatementFrequencyDto.DAILY, dto.getStatementFrequency());
        assertEquals(BigDecimal.TEN, dto.getReconciliationTolerance());
        assertTrue(dto.getAutoReconcile());
    }

    @Test
    void testSettersAndGetters() {
        BankAccountResponseDto dto = new BankAccountResponseDto();
        dto.setId("val-id");
        dto.setAccountNumber("val-accountNumber");
        dto.setAccountName("val-accountName");
        dto.setAccountType(BankAccountResponseDto.AccountTypeDto.CHECKING);
        dto.setBankName("val-bankName");
        dto.setBankCode("val-bankCode");
        dto.setCurrency("val-currency");
        dto.setBalance(BigDecimal.ONE);
        dto.setBalanceDate(LocalDate.of(2025,6,1));
        dto.setStatus(BankAccountResponseDto.AccountStatusDto.ACTIVE);
        dto.setIsPrimary(true);
        dto.setLastStatementDate(LocalDate.of(2025,6,1));
        dto.setOpeningBalance(BigDecimal.ONE);
        dto.setIban("val-iban");
        dto.setSwiftCode("val-swiftCode");
        dto.setRoutingNumber("val-routingNumber");
        dto.setDescription("val-description");
        dto.setStatementFrequency(BankAccountResponseDto.StatementFrequencyDto.DAILY);
        dto.setReconciliationTolerance(BigDecimal.ONE);
        dto.setAutoReconcile(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals(BankAccountResponseDto.AccountTypeDto.CHECKING, dto.getAccountType());
        assertEquals("val-bankName", dto.getBankName());
        assertEquals("val-bankCode", dto.getBankCode());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getBalance());
        assertEquals(LocalDate.of(2025,6,1), dto.getBalanceDate());
        assertEquals(BankAccountResponseDto.AccountStatusDto.ACTIVE, dto.getStatus());
        assertTrue(dto.getIsPrimary());
        assertEquals(LocalDate.of(2025,6,1), dto.getLastStatementDate());
        assertEquals(BigDecimal.ONE, dto.getOpeningBalance());
        assertEquals("val-iban", dto.getIban());
        assertEquals("val-swiftCode", dto.getSwiftCode());
        assertEquals("val-routingNumber", dto.getRoutingNumber());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BankAccountResponseDto.StatementFrequencyDto.DAILY, dto.getStatementFrequency());
        assertEquals(BigDecimal.ONE, dto.getReconciliationTolerance());
        assertTrue(dto.getAutoReconcile());
    }

    @Test
    void testEqualsAndHashCode() {
        BankAccountResponseDto dto1 = BankAccountResponseDto.builder()
                        .id("test-id")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(BankAccountResponseDto.AccountTypeDto.CHECKING)
            .bankName("test-bankName")
            .bankCode("test-bankCode")
            .currency("test-currency")
            .balance(BigDecimal.TEN)
            .balanceDate(LocalDate.of(2025,1,15))
            .status(BankAccountResponseDto.AccountStatusDto.ACTIVE)
            .isPrimary(true)
            .lastReconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastStatementDate(LocalDate.of(2025,1,15))
            .openingBalance(BigDecimal.TEN)
            .iban("test-iban")
            .swiftCode("test-swiftCode")
            .routingNumber("test-routingNumber")
            .description("test-description")
            .tags(Collections.emptyList())
            .statementFrequency(BankAccountResponseDto.StatementFrequencyDto.DAILY)
            .reconciliationTolerance(BigDecimal.TEN)
            .autoReconcile(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        BankAccountResponseDto dto2 = BankAccountResponseDto.builder()
                        .id("test-id")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(BankAccountResponseDto.AccountTypeDto.CHECKING)
            .bankName("test-bankName")
            .bankCode("test-bankCode")
            .currency("test-currency")
            .balance(BigDecimal.TEN)
            .balanceDate(LocalDate.of(2025,1,15))
            .status(BankAccountResponseDto.AccountStatusDto.ACTIVE)
            .isPrimary(true)
            .lastReconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastStatementDate(LocalDate.of(2025,1,15))
            .openingBalance(BigDecimal.TEN)
            .iban("test-iban")
            .swiftCode("test-swiftCode")
            .routingNumber("test-routingNumber")
            .description("test-description")
            .tags(Collections.emptyList())
            .statementFrequency(BankAccountResponseDto.StatementFrequencyDto.DAILY)
            .reconciliationTolerance(BigDecimal.TEN)
            .autoReconcile(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BankAccountResponseDto dto = BankAccountResponseDto.builder()
                        .id("test-id")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(BankAccountResponseDto.AccountTypeDto.CHECKING)
            .bankName("test-bankName")
            .bankCode("test-bankCode")
            .currency("test-currency")
            .balance(BigDecimal.TEN)
            .balanceDate(LocalDate.of(2025,1,15))
            .status(BankAccountResponseDto.AccountStatusDto.ACTIVE)
            .isPrimary(true)
            .lastReconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastStatementDate(LocalDate.of(2025,1,15))
            .openingBalance(BigDecimal.TEN)
            .iban("test-iban")
            .swiftCode("test-swiftCode")
            .routingNumber("test-routingNumber")
            .description("test-description")
            .tags(Collections.emptyList())
            .statementFrequency(BankAccountResponseDto.StatementFrequencyDto.DAILY)
            .reconciliationTolerance(BigDecimal.TEN)
            .autoReconcile(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}