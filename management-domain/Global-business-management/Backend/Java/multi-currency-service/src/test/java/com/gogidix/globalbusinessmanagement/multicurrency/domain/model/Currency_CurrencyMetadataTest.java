package com.gogidix.globalbusinessmanagement.multicurrency.domain.model;

import com.gogidix.globalbusinessmanagement.multicurrency.domain.model.Currency;
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
class Currency_CurrencyMetadataTest {

        @Test
    void testBuilder() {
        Currency.CurrencyMetadata dto = Currency.CurrencyMetadata.builder()
                        .issuer("test-issuer")
            .issuerWebsite("test-issuerWebsite")
            .localizedNames(Collections.emptyMap())
            .aliases(Collections.emptyList())
            .currencyFamily("test-currencyFamily")
            .isFiat(true)
            .marketCapRank(42)
            .circulatingSupply(BigDecimal.TEN)
            .maxSupply(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals("test-issuer", dto.getIssuer());
        assertEquals("test-issuerWebsite", dto.getIssuerWebsite());
        assertEquals("test-currencyFamily", dto.getCurrencyFamily());
        assertTrue(dto.getIsFiat());
        assertEquals(42, dto.getMarketCapRank());
        assertEquals(BigDecimal.TEN, dto.getCirculatingSupply());
        assertEquals(BigDecimal.TEN, dto.getMaxSupply());
    }

    @Test
    void testSettersAndGetters() {
        Currency.CurrencyMetadata dto = new Currency.CurrencyMetadata();
        dto.setIssuer("val-issuer");
        dto.setIssuerWebsite("val-issuerWebsite");
        dto.setCurrencyFamily("val-currencyFamily");
        dto.setIsFiat(true);
        dto.setMarketCapRank(99);
        dto.setCirculatingSupply(BigDecimal.ONE);
        dto.setMaxSupply(BigDecimal.ONE);
        assertEquals("val-issuer", dto.getIssuer());
        assertEquals("val-issuerWebsite", dto.getIssuerWebsite());
        assertEquals("val-currencyFamily", dto.getCurrencyFamily());
        assertTrue(dto.getIsFiat());
        assertEquals(99, dto.getMarketCapRank());
        assertEquals(BigDecimal.ONE, dto.getCirculatingSupply());
        assertEquals(BigDecimal.ONE, dto.getMaxSupply());
    }

    @Test
    void testEqualsAndHashCode() {
        Currency.CurrencyMetadata dto1 = Currency.CurrencyMetadata.builder()
                        .issuer("test-issuer")
            .issuerWebsite("test-issuerWebsite")
            .localizedNames(Collections.emptyMap())
            .aliases(Collections.emptyList())
            .currencyFamily("test-currencyFamily")
            .isFiat(true)
            .marketCapRank(42)
            .circulatingSupply(BigDecimal.TEN)
            .maxSupply(BigDecimal.TEN)
            .build();
        Currency.CurrencyMetadata dto2 = Currency.CurrencyMetadata.builder()
                        .issuer("test-issuer")
            .issuerWebsite("test-issuerWebsite")
            .localizedNames(Collections.emptyMap())
            .aliases(Collections.emptyList())
            .currencyFamily("test-currencyFamily")
            .isFiat(true)
            .marketCapRank(42)
            .circulatingSupply(BigDecimal.TEN)
            .maxSupply(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Currency.CurrencyMetadata dto = Currency.CurrencyMetadata.builder()
                        .issuer("test-issuer")
            .issuerWebsite("test-issuerWebsite")
            .localizedNames(Collections.emptyMap())
            .aliases(Collections.emptyList())
            .currencyFamily("test-currencyFamily")
            .isFiat(true)
            .marketCapRank(42)
            .circulatingSupply(BigDecimal.TEN)
            .maxSupply(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}