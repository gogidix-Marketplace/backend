package com.gogidix.finance.generalledger.application.dto.response;

import com.gogidix.finance.generalledger.application.dto.response.LedgerAccountResponseDto;
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
class LedgerAccountResponseDtoTest {

        @Test
    void testBuilder() {
        LedgerAccountResponseDto dto = LedgerAccountResponseDto.builder()
                        .id("test-id")
            .accountId("test-accountId")
            .tenantId("test-tenantId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccountResponseDto.AccountTypeDto.ASSET)
            .accountSubType(LedgerAccountResponseDto.AccountSubTypeDto.CURRENT_ASSET)
            .parentAccountId("test-parentAccountId")
            .accountLevel(42)
            .status(LedgerAccountResponseDto.AccountStatusDto.ACTIVE)
            .currency("test-currency")
            .currentBalance(BigDecimal.TEN)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .openingBalanceDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .costCenter("test-costCenter")
            .department("test-department")
            .location("test-location")
            .isCashAccount(true)
            .isReconcilable(true)
            .isTaxAccount(true)
            .taxCode("test-taxCode")
            .allowsManualEntry(true)
            .normalBalanceSide(42)
            .createdByUserId("test-createdByUserId")
            .lastReconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastReconciledBy("test-lastReconciledBy")
            .tags(Collections.emptyList())
            .creditLimit(BigDecimal.TEN)
            .notes("test-notes")
            .archivedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .archivedBy("test-archivedBy")
            .archivedReason("test-archivedReason")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-accountId", dto.getAccountId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-accountNumber", dto.getAccountNumber());
        assertEquals("test-accountName", dto.getAccountName());
        assertEquals(LedgerAccountResponseDto.AccountTypeDto.ASSET, dto.getAccountType());
        assertEquals(LedgerAccountResponseDto.AccountSubTypeDto.CURRENT_ASSET, dto.getAccountSubType());
        assertEquals("test-parentAccountId", dto.getParentAccountId());
        assertEquals(42, dto.getAccountLevel());
        assertEquals(LedgerAccountResponseDto.AccountStatusDto.ACTIVE, dto.getStatus());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getCurrentBalance());
        assertEquals(BigDecimal.TEN, dto.getDebitBalance());
        assertEquals(BigDecimal.TEN, dto.getCreditBalance());
        assertEquals(BigDecimal.TEN, dto.getOpeningBalance());
        assertEquals(LocalDate.of(2025,1,15), dto.getOpeningBalanceDate());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-costCenter", dto.getCostCenter());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-location", dto.getLocation());
        assertTrue(dto.getIsCashAccount());
        assertTrue(dto.getIsReconcilable());
        assertTrue(dto.getIsTaxAccount());
        assertEquals("test-taxCode", dto.getTaxCode());
        assertTrue(dto.getAllowsManualEntry());
        assertEquals(42, dto.getNormalBalanceSide());
        assertEquals("test-createdByUserId", dto.getCreatedByUserId());
        assertEquals("test-lastReconciledBy", dto.getLastReconciledBy());
        assertEquals(BigDecimal.TEN, dto.getCreditLimit());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-archivedBy", dto.getArchivedBy());
        assertEquals("test-archivedReason", dto.getArchivedReason());
    }

    @Test
    void testSettersAndGetters() {
        LedgerAccountResponseDto dto = new LedgerAccountResponseDto();
        dto.setId("val-id");
        dto.setAccountId("val-accountId");
        dto.setTenantId("val-tenantId");
        dto.setAccountNumber("val-accountNumber");
        dto.setAccountName("val-accountName");
        dto.setAccountType(LedgerAccountResponseDto.AccountTypeDto.ASSET);
        dto.setAccountSubType(LedgerAccountResponseDto.AccountSubTypeDto.CURRENT_ASSET);
        dto.setParentAccountId("val-parentAccountId");
        dto.setAccountLevel(99);
        dto.setStatus(LedgerAccountResponseDto.AccountStatusDto.ACTIVE);
        dto.setCurrency("val-currency");
        dto.setCurrentBalance(BigDecimal.ONE);
        dto.setDebitBalance(BigDecimal.ONE);
        dto.setCreditBalance(BigDecimal.ONE);
        dto.setOpeningBalance(BigDecimal.ONE);
        dto.setOpeningBalanceDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setCostCenter("val-costCenter");
        dto.setDepartment("val-department");
        dto.setLocation("val-location");
        dto.setIsCashAccount(true);
        dto.setIsReconcilable(true);
        dto.setIsTaxAccount(true);
        dto.setTaxCode("val-taxCode");
        dto.setAllowsManualEntry(true);
        dto.setNormalBalanceSide(99);
        dto.setCreatedByUserId("val-createdByUserId");
        dto.setLastReconciledBy("val-lastReconciledBy");
        dto.setCreditLimit(BigDecimal.ONE);
        dto.setNotes("val-notes");
        dto.setArchivedBy("val-archivedBy");
        dto.setArchivedReason("val-archivedReason");
        assertEquals("val-id", dto.getId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals(LedgerAccountResponseDto.AccountTypeDto.ASSET, dto.getAccountType());
        assertEquals(LedgerAccountResponseDto.AccountSubTypeDto.CURRENT_ASSET, dto.getAccountSubType());
        assertEquals("val-parentAccountId", dto.getParentAccountId());
        assertEquals(99, dto.getAccountLevel());
        assertEquals(LedgerAccountResponseDto.AccountStatusDto.ACTIVE, dto.getStatus());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getCurrentBalance());
        assertEquals(BigDecimal.ONE, dto.getDebitBalance());
        assertEquals(BigDecimal.ONE, dto.getCreditBalance());
        assertEquals(BigDecimal.ONE, dto.getOpeningBalance());
        assertEquals(LocalDate.of(2025,6,1), dto.getOpeningBalanceDate());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-location", dto.getLocation());
        assertTrue(dto.getIsCashAccount());
        assertTrue(dto.getIsReconcilable());
        assertTrue(dto.getIsTaxAccount());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertTrue(dto.getAllowsManualEntry());
        assertEquals(99, dto.getNormalBalanceSide());
        assertEquals("val-createdByUserId", dto.getCreatedByUserId());
        assertEquals("val-lastReconciledBy", dto.getLastReconciledBy());
        assertEquals(BigDecimal.ONE, dto.getCreditLimit());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-archivedBy", dto.getArchivedBy());
        assertEquals("val-archivedReason", dto.getArchivedReason());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccountResponseDto dto1 = LedgerAccountResponseDto.builder()
                        .id("test-id")
            .accountId("test-accountId")
            .tenantId("test-tenantId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccountResponseDto.AccountTypeDto.ASSET)
            .accountSubType(LedgerAccountResponseDto.AccountSubTypeDto.CURRENT_ASSET)
            .parentAccountId("test-parentAccountId")
            .accountLevel(42)
            .status(LedgerAccountResponseDto.AccountStatusDto.ACTIVE)
            .currency("test-currency")
            .currentBalance(BigDecimal.TEN)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .openingBalanceDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .costCenter("test-costCenter")
            .department("test-department")
            .location("test-location")
            .isCashAccount(true)
            .isReconcilable(true)
            .isTaxAccount(true)
            .taxCode("test-taxCode")
            .allowsManualEntry(true)
            .normalBalanceSide(42)
            .createdByUserId("test-createdByUserId")
            .lastReconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastReconciledBy("test-lastReconciledBy")
            .tags(Collections.emptyList())
            .creditLimit(BigDecimal.TEN)
            .notes("test-notes")
            .archivedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .archivedBy("test-archivedBy")
            .archivedReason("test-archivedReason")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        LedgerAccountResponseDto dto2 = LedgerAccountResponseDto.builder()
                        .id("test-id")
            .accountId("test-accountId")
            .tenantId("test-tenantId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccountResponseDto.AccountTypeDto.ASSET)
            .accountSubType(LedgerAccountResponseDto.AccountSubTypeDto.CURRENT_ASSET)
            .parentAccountId("test-parentAccountId")
            .accountLevel(42)
            .status(LedgerAccountResponseDto.AccountStatusDto.ACTIVE)
            .currency("test-currency")
            .currentBalance(BigDecimal.TEN)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .openingBalanceDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .costCenter("test-costCenter")
            .department("test-department")
            .location("test-location")
            .isCashAccount(true)
            .isReconcilable(true)
            .isTaxAccount(true)
            .taxCode("test-taxCode")
            .allowsManualEntry(true)
            .normalBalanceSide(42)
            .createdByUserId("test-createdByUserId")
            .lastReconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastReconciledBy("test-lastReconciledBy")
            .tags(Collections.emptyList())
            .creditLimit(BigDecimal.TEN)
            .notes("test-notes")
            .archivedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .archivedBy("test-archivedBy")
            .archivedReason("test-archivedReason")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LedgerAccountResponseDto dto = LedgerAccountResponseDto.builder()
                        .id("test-id")
            .accountId("test-accountId")
            .tenantId("test-tenantId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccountResponseDto.AccountTypeDto.ASSET)
            .accountSubType(LedgerAccountResponseDto.AccountSubTypeDto.CURRENT_ASSET)
            .parentAccountId("test-parentAccountId")
            .accountLevel(42)
            .status(LedgerAccountResponseDto.AccountStatusDto.ACTIVE)
            .currency("test-currency")
            .currentBalance(BigDecimal.TEN)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .openingBalanceDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .costCenter("test-costCenter")
            .department("test-department")
            .location("test-location")
            .isCashAccount(true)
            .isReconcilable(true)
            .isTaxAccount(true)
            .taxCode("test-taxCode")
            .allowsManualEntry(true)
            .normalBalanceSide(42)
            .createdByUserId("test-createdByUserId")
            .lastReconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastReconciledBy("test-lastReconciledBy")
            .tags(Collections.emptyList())
            .creditLimit(BigDecimal.TEN)
            .notes("test-notes")
            .archivedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .archivedBy("test-archivedBy")
            .archivedReason("test-archivedReason")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}