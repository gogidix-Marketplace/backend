package com.gogidix.finance.bankreconciliation.domain.event;

import com.gogidix.finance.bankreconciliation.domain.event.ReconciliationCompletedEvent;
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
class ReconciliationCompletedEventCoverageTest {

    private ReconciliationCompletedEvent testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new ReconciliationCompletedEvent();
    }

    @Test
    void eventId_setterTest() {
        testEntity.setEventId("test");
    }

    @Test
    void reconciliationId_setterTest() {
        testEntity.setReconciliationId("test");
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
    void statementId_setterTest() {
        testEntity.setStatementId("test");
    }

    @Test
    void reconciliationDate_setterTest() {
        testEntity.setReconciliationDate(LocalDate.of(2025,1,15));
    }

    @Test
    void periodStart_setterTest() {
        testEntity.setPeriodStart(LocalDate.of(2025,1,15));
    }

    @Test
    void periodEnd_setterTest() {
        testEntity.setPeriodEnd(LocalDate.of(2025,1,15));
    }

    @Test
    void startingBalance_setterTest() {
        testEntity.setStartingBalance(BigDecimal.TEN);
    }

    @Test
    void endingBalance_setterTest() {
        testEntity.setEndingBalance(BigDecimal.TEN);
    }

    @Test
    void bookBalance_setterTest() {
        testEntity.setBookBalance(BigDecimal.TEN);
    }

    @Test
    void bankBalance_setterTest() {
        testEntity.setBankBalance(BigDecimal.TEN);
    }

    @Test
    void difference_setterTest() {
        testEntity.setDifference(BigDecimal.TEN);
    }

    @Test
    void isBalanced_setterTest() {
        testEntity.setIsBalanced(true);
    }

    @Test
    void eventId_getterSetterTest() {
        testEntity.setEventId("test");
        var result = testEntity.getEventId();
        assertNotNull(result);
    }

    @Test
    void reconciliationId_getterSetterTest() {
        testEntity.setReconciliationId("test");
        var result = testEntity.getReconciliationId();
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
    void statementId_getterSetterTest() {
        testEntity.setStatementId("test");
        var result = testEntity.getStatementId();
        assertNotNull(result);
    }

    @Test
    void reconciliationDate_getterSetterTest() {
        testEntity.setReconciliationDate(LocalDate.of(2025,1,15));
        var result = testEntity.getReconciliationDate();
        assertNotNull(result);
    }

    @Test
    void periodStart_getterSetterTest() {
        testEntity.setPeriodStart(LocalDate.of(2025,1,15));
        var result = testEntity.getPeriodStart();
        assertNotNull(result);
    }

    @Test
    void periodEnd_getterSetterTest() {
        testEntity.setPeriodEnd(LocalDate.of(2025,1,15));
        var result = testEntity.getPeriodEnd();
        assertNotNull(result);
    }

    @Test
    void startingBalance_getterSetterTest() {
        testEntity.setStartingBalance(BigDecimal.TEN);
        var result = testEntity.getStartingBalance();
        assertNotNull(result);
    }

    @Test
    void endingBalance_getterSetterTest() {
        testEntity.setEndingBalance(BigDecimal.TEN);
        var result = testEntity.getEndingBalance();
        assertNotNull(result);
    }

    @Test
    void bookBalance_getterSetterTest() {
        testEntity.setBookBalance(BigDecimal.TEN);
        var result = testEntity.getBookBalance();
        assertNotNull(result);
    }

    @Test
    void bankBalance_getterSetterTest() {
        testEntity.setBankBalance(BigDecimal.TEN);
        var result = testEntity.getBankBalance();
        assertNotNull(result);
    }

    @Test
    void difference_getterSetterTest() {
        testEntity.setDifference(BigDecimal.TEN);
        var result = testEntity.getDifference();
        assertNotNull(result);
    }

    @Test
    void isBalanced_getterSetterTest() {
        testEntity.setIsBalanced(true);
        var result = testEntity.getIsBalanced();
        assertNotNull(result);
    }
}
