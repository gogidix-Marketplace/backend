package com.gogidix.finance.bankreconciliation.domain.event;

import com.gogidix.finance.bankreconciliation.domain.event.BankStatementImportedEvent;
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
class BankStatementImportedEventCoverageTest {

    private BankStatementImportedEvent testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new BankStatementImportedEvent();
    }

    @Test
    void eventId_setterTest() {
        testEntity.setEventId("test");
    }

    @Test
    void statementId_setterTest() {
        testEntity.setStatementId("test");
    }

    @Test
    void tenantId_setterTest() {
        testEntity.setTenantId("test");
    }

    @Test
    void accountId_setterTest() {
        testEntity.setAccountId("test");
    }

    @Test
    void accountNumber_setterTest() {
        testEntity.setAccountNumber("test");
    }

    @Test
    void statementDate_setterTest() {
        testEntity.setStatementDate(LocalDate.of(2025,1,15));
    }

    @Test
    void startDate_setterTest() {
        testEntity.setStartDate(LocalDate.of(2025,1,15));
    }

    @Test
    void endDate_setterTest() {
        testEntity.setEndDate(LocalDate.of(2025,1,15));
    }

    @Test
    void openingBalance_setterTest() {
        testEntity.setOpeningBalance(BigDecimal.TEN);
    }

    @Test
    void closingBalance_setterTest() {
        testEntity.setClosingBalance(BigDecimal.TEN);
    }

    @Test
    void currency_setterTest() {
        testEntity.setCurrency("test");
    }

    @Test
    void fileReference_setterTest() {
        testEntity.setFileReference("test");
    }

    @Test
    void transactionCount_setterTest() {
        testEntity.setTransactionCount(42);
    }

    @Test
    void totalDebits_setterTest() {
        testEntity.setTotalDebits(BigDecimal.TEN);
    }

    @Test
    void totalCredits_setterTest() {
        testEntity.setTotalCredits(BigDecimal.TEN);
    }

    @Test
    void eventId_getterSetterTest() {
        testEntity.setEventId("test");
        var result = testEntity.getEventId();
        assertNotNull(result);
    }

    @Test
    void statementId_getterSetterTest() {
        testEntity.setStatementId("test");
        var result = testEntity.getStatementId();
        assertNotNull(result);
    }

    @Test
    void tenantId_getterSetterTest() {
        testEntity.setTenantId("test");
        var result = testEntity.getTenantId();
        assertNotNull(result);
    }

    @Test
    void accountId_getterSetterTest() {
        testEntity.setAccountId("test");
        var result = testEntity.getAccountId();
        assertNotNull(result);
    }

    @Test
    void accountNumber_getterSetterTest() {
        testEntity.setAccountNumber("test");
        var result = testEntity.getAccountNumber();
        assertNotNull(result);
    }

    @Test
    void statementDate_getterSetterTest() {
        testEntity.setStatementDate(LocalDate.of(2025,1,15));
        var result = testEntity.getStatementDate();
        assertNotNull(result);
    }

    @Test
    void startDate_getterSetterTest() {
        testEntity.setStartDate(LocalDate.of(2025,1,15));
        var result = testEntity.getStartDate();
        assertNotNull(result);
    }

    @Test
    void endDate_getterSetterTest() {
        testEntity.setEndDate(LocalDate.of(2025,1,15));
        var result = testEntity.getEndDate();
        assertNotNull(result);
    }

    @Test
    void openingBalance_getterSetterTest() {
        testEntity.setOpeningBalance(BigDecimal.TEN);
        var result = testEntity.getOpeningBalance();
        assertNotNull(result);
    }

    @Test
    void closingBalance_getterSetterTest() {
        testEntity.setClosingBalance(BigDecimal.TEN);
        var result = testEntity.getClosingBalance();
        assertNotNull(result);
    }

    @Test
    void currency_getterSetterTest() {
        testEntity.setCurrency("test");
        var result = testEntity.getCurrency();
        assertNotNull(result);
    }

    @Test
    void fileReference_getterSetterTest() {
        testEntity.setFileReference("test");
        var result = testEntity.getFileReference();
        assertNotNull(result);
    }

    @Test
    void transactionCount_getterSetterTest() {
        testEntity.setTransactionCount(42);
        var result = testEntity.getTransactionCount();
        assertNotNull(result);
    }

    @Test
    void totalDebits_getterSetterTest() {
        testEntity.setTotalDebits(BigDecimal.TEN);
        var result = testEntity.getTotalDebits();
        assertNotNull(result);
    }

    @Test
    void totalCredits_getterSetterTest() {
        testEntity.setTotalCredits(BigDecimal.TEN);
        var result = testEntity.getTotalCredits();
        assertNotNull(result);
    }
}
