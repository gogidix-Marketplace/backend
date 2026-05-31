package com.gogidix.digitalmarketing.brandmanagement.domain.model;

import com.gogidix.digitalmarketing.brandmanagement.domain.model.BrandAsset;
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
class BrandAsset_BrandAssetUsageTest {

        @Test
    void testSettersAndGetters() {
        BrandAsset.BrandAssetUsage dto = new BrandAsset.BrandAssetUsage();
        dto.setContext("val-context");
        dto.setReference("val-reference");
        assertEquals("val-context", dto.getContext());
        assertEquals("val-reference", dto.getReference());
    }

    @Test
    void testEqualsAndHashCode() {
        BrandAsset.BrandAssetUsage dto1 = new BrandAsset.BrandAssetUsage();
        BrandAsset.BrandAssetUsage dto2 = new BrandAsset.BrandAssetUsage();
        dto1.setContext("test");
        dto1.setReference("test");
        dto1.setUsedAt(null);
        dto2.setContext("test");
        dto2.setReference("test");
        dto2.setUsedAt(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setContext(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BrandAsset.BrandAssetUsage dto = new BrandAsset.BrandAssetUsage();
        dto.setContext("test");
        dto.setReference("test");
        dto.setUsedAt(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BrandAsset.BrandAssetUsage dto = new BrandAsset.BrandAssetUsage();
        dto.setContext("test");
        dto.setReference("test");
        dto.setUsedAt(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}