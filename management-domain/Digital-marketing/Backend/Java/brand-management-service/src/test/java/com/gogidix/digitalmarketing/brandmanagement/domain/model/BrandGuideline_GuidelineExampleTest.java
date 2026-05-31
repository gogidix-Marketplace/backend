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
class BrandGuideline_GuidelineExampleTest {

        @Test
    void testSettersAndGetters() {
        BrandGuideline.GuidelineExample dto = new BrandGuideline.GuidelineExample();
        dto.setTitle("val-title");
        dto.setImageUrl("val-imageUrl");
        dto.setCorrect(true);
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-imageUrl", dto.getImageUrl());
        assertTrue(dto.isCorrect());
    }

    @Test
    void testEqualsAndHashCode() {
        BrandGuideline.GuidelineExample dto1 = new BrandGuideline.GuidelineExample();
        BrandGuideline.GuidelineExample dto2 = new BrandGuideline.GuidelineExample();
        dto1.setTitle("test");
        dto1.setImageUrl("test");
        dto1.setCorrect(true);
        dto2.setTitle("test");
        dto2.setImageUrl("test");
        dto2.setCorrect(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTitle(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BrandGuideline.GuidelineExample dto = new BrandGuideline.GuidelineExample();
        dto.setTitle("test");
        dto.setImageUrl("test");
        dto.setCorrect(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BrandGuideline.GuidelineExample dto = new BrandGuideline.GuidelineExample();
        dto.setTitle("test");
        dto.setImageUrl("test");
        dto.setCorrect(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}