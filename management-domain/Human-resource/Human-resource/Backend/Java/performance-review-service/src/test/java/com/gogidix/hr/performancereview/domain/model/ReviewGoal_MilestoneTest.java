package com.gogidix.hr.performancereview.domain.model;

import com.gogidix.hr.performancereview.domain.model.ReviewGoal;
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
class ReviewGoal_MilestoneTest {

        @Test
    void testSettersAndGetters() {
        ReviewGoal.Milestone dto = new ReviewGoal.Milestone();
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setDueDate(LocalDate.of(2025,6,1));
        dto.setIsCompleted(true);
        dto.setCompletedDate(LocalDate.of(2025,6,1));
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals(LocalDate.of(2025,6,1), dto.getDueDate());
        assertTrue(dto.getIsCompleted());
        assertEquals(LocalDate.of(2025,6,1), dto.getCompletedDate());
    }

    @Test
    void testEqualsAndHashCode() {
        ReviewGoal.Milestone dto1 = new ReviewGoal.Milestone();
        ReviewGoal.Milestone dto2 = new ReviewGoal.Milestone();
        dto1.setTitle("test");
        dto1.setDescription("test");
        dto1.setDueDate(LocalDate.of(2025,1,1));
        dto1.setIsCompleted(true);
        dto1.setCompletedDate(LocalDate.of(2025,1,1));
        dto2.setTitle("test");
        dto2.setDescription("test");
        dto2.setDueDate(LocalDate.of(2025,1,1));
        dto2.setIsCompleted(true);
        dto2.setCompletedDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTitle(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReviewGoal.Milestone dto = new ReviewGoal.Milestone();
        dto.setTitle("test");
        dto.setDescription("test");
        dto.setDueDate(LocalDate.of(2025,1,1));
        dto.setIsCompleted(true);
        dto.setCompletedDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReviewGoal.Milestone dto = new ReviewGoal.Milestone();
        dto.setTitle("test");
        dto.setDescription("test");
        dto.setDueDate(LocalDate.of(2025,1,1));
        dto.setIsCompleted(true);
        dto.setCompletedDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}