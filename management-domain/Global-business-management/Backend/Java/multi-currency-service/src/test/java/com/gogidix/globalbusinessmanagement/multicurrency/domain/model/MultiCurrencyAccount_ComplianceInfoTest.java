package com.gogidix.globalbusinessmanagement.multicurrency.domain.model;

import com.gogidix.globalbusinessmanagement.multicurrency.domain.model.MultiCurrencyAccount;
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
class MultiCurrencyAccount_ComplianceInfoTest {

        @Test
    void testBuilder() {
        MultiCurrencyAccount.ComplianceInfo dto = MultiCurrencyAccount.ComplianceInfo.builder()
                        .taxId("test-taxId")
            .regulatoryJurisdiction("test-regulatoryJurisdiction")
            .kycVerifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .amlCheckedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .riskLevel("test-riskLevel")
            .requiresEnhancedDiligence(true)
            .additionalInfo(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-taxId", dto.getTaxId());
        assertEquals("test-regulatoryJurisdiction", dto.getRegulatoryJurisdiction());
        assertEquals("test-riskLevel", dto.getRiskLevel());
        assertTrue(dto.getRequiresEnhancedDiligence());
    }

    @Test
    void testSettersAndGetters() {
        MultiCurrencyAccount.ComplianceInfo dto = new MultiCurrencyAccount.ComplianceInfo();
        dto.setTaxId("val-taxId");
        dto.setRegulatoryJurisdiction("val-regulatoryJurisdiction");
        dto.setRiskLevel("val-riskLevel");
        dto.setRequiresEnhancedDiligence(true);
        assertEquals("val-taxId", dto.getTaxId());
        assertEquals("val-regulatoryJurisdiction", dto.getRegulatoryJurisdiction());
        assertEquals("val-riskLevel", dto.getRiskLevel());
        assertTrue(dto.getRequiresEnhancedDiligence());
    }

    @Test
    void testEqualsAndHashCode() {
        MultiCurrencyAccount.ComplianceInfo dto1 = MultiCurrencyAccount.ComplianceInfo.builder()
                        .taxId("test-taxId")
            .regulatoryJurisdiction("test-regulatoryJurisdiction")
            .kycVerifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .amlCheckedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .riskLevel("test-riskLevel")
            .requiresEnhancedDiligence(true)
            .additionalInfo(Collections.emptyMap())
            .build();
        MultiCurrencyAccount.ComplianceInfo dto2 = MultiCurrencyAccount.ComplianceInfo.builder()
                        .taxId("test-taxId")
            .regulatoryJurisdiction("test-regulatoryJurisdiction")
            .kycVerifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .amlCheckedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .riskLevel("test-riskLevel")
            .requiresEnhancedDiligence(true)
            .additionalInfo(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MultiCurrencyAccount.ComplianceInfo dto = MultiCurrencyAccount.ComplianceInfo.builder()
                        .taxId("test-taxId")
            .regulatoryJurisdiction("test-regulatoryJurisdiction")
            .kycVerifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .amlCheckedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .riskLevel("test-riskLevel")
            .requiresEnhancedDiligence(true)
            .additionalInfo(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}