package com.gogidix.digitalmarketing.brandmanagement.domain.model;

import com.gogidix.digitalmarketing.brandmanagement.domain.model.BrandGuideline;
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
class BrandGuideline_GuidelineRuleTest {

        @Test
    void testSettersAndGetters() {
        BrandGuideline.GuidelineRule dto = new BrandGuideline.GuidelineRule();
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        BrandGuideline.GuidelineRule dto1 = new BrandGuideline.GuidelineRule();
        BrandGuideline.GuidelineRule dto2 = new BrandGuideline.GuidelineRule();
        dto1.setTitle("test");
        dto1.setDescription("test");
        dto2.setTitle("test");
        dto2.setDescription("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTitle(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BrandGuideline.GuidelineRule dto = new BrandGuideline.GuidelineRule();
        dto.setTitle("test");
        dto.setDescription("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BrandGuideline.GuidelineRule dto = new BrandGuideline.GuidelineRule();
        dto.setTitle("test");
        dto.setDescription("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}