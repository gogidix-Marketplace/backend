package com.gogidix.dashboard.aggregation.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeWindowTest {

    private LocalDateTime now = LocalDateTime.now();

    private TimeWindow createWindow(LocalDateTime start, LocalDateTime end, TimeGranularity g) {
        return new TimeWindow(start, end, g);
    }

    @Nested
    @DisplayName("Factory method tests")
    class FactoryTests {
        @Test
        void lastHour_createsHourlyWindow() {
            TimeWindow tw = TimeWindow.lastHour();
            assertEquals(TimeGranularity.HOURLY, tw.getGranularity());
            assertTrue(tw.getDuration().toHours() <= 1);
        }

        @Test
        void lastDay_createsDailyWindow() {
            TimeWindow tw = TimeWindow.lastDay();
            assertEquals(TimeGranularity.DAILY, tw.getGranularity());
        }

        @Test
        void lastWeek_createsWeeklyWindow() {
            TimeWindow tw = TimeWindow.lastWeek();
            assertEquals(TimeGranularity.WEEKLY, tw.getGranularity());
        }

        @Test
        void currentMonth_createsMonthlyWindow() {
            TimeWindow tw = TimeWindow.currentMonth();
            assertEquals(TimeGranularity.MONTHLY, tw.getGranularity());
        }

        @Test
        void currentQuarter_createsQuarterlyWindow() {
            TimeWindow tw = TimeWindow.currentQuarter();
            assertEquals(TimeGranularity.QUARTERLY, tw.getGranularity());
        }

        @Test
        void of_createsCustomWindow() {
            TimeWindow tw = TimeWindow.of(now.minusHours(1), now, TimeGranularity.HOURLY);
            assertEquals(TimeGranularity.HOURLY, tw.getGranularity());
        }
    }

    @Nested
    @DisplayName("Validation tests")
    class ValidationTests {
        @Test
        void constructor_startAfterEnd_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                new TimeWindow(now, now.minusHours(1), TimeGranularity.HOURLY));
        }

        @Test
        void constructor_nullStart_throws() {
            assertThrows(NullPointerException.class, () ->
                new TimeWindow(null, now, TimeGranularity.HOURLY));
        }

        @Test
        void constructor_nullEnd_throws() {
            assertThrows(NullPointerException.class, () ->
                new TimeWindow(now, null, TimeGranularity.HOURLY));
        }

        @Test
        void updateEndTime_beforeStart_throws() {
            TimeWindow tw = TimeWindow.of(now.minusHours(1), now, TimeGranularity.HOURLY);
            assertThrows(IllegalArgumentException.class, () -> tw.updateEndTime(now.minusHours(2)));
        }
    }

    @Nested
    @DisplayName("Relationship tests")
    class RelationshipTests {
        @Test
        void overlapsWith_overlappingWindows_returnsTrue() {
            TimeWindow tw1 = TimeWindow.of(now.minusHours(2), now, TimeGranularity.HOURLY);
            TimeWindow tw2 = TimeWindow.of(now.minusHours(1), now.plusHours(1), TimeGranularity.HOURLY);
            assertTrue(tw1.overlapsWith(tw2));
        }

        @Test
        void overlapsWith_nonOverlappingWindows_returnsFalse() {
            TimeWindow tw1 = TimeWindow.of(now.minusHours(3), now.minusHours(2), TimeGranularity.HOURLY);
            TimeWindow tw2 = TimeWindow.of(now.minusHours(1), now, TimeGranularity.HOURLY);
            assertFalse(tw1.overlapsWith(tw2));
        }

        @Test
        void isCompatibleWith_sameGranularityAndDuration() {
            TimeWindow tw1 = TimeWindow.of(now.minusHours(1), now, TimeGranularity.HOURLY);
            TimeWindow tw2 = TimeWindow.of(now, now.plusHours(1), TimeGranularity.HOURLY);
            assertTrue(tw1.isCompatibleWith(tw2));
        }

        @Test
        void contains_timestampInRange() {
            TimeWindow tw = TimeWindow.of(now.minusHours(1), now, TimeGranularity.HOURLY);
            assertTrue(tw.contains(now.minusMinutes(30)));
        }

        @Test
        void contains_timestampOutOfRange() {
            TimeWindow tw = TimeWindow.of(now.minusHours(1), now, TimeGranularity.HOURLY);
            assertFalse(tw.contains(now.plusHours(1)));
        }

        @Test
        void isComplete_endedInPast() {
            TimeWindow tw = TimeWindow.of(now.minusHours(2), now.minusHours(1), TimeGranularity.HOURLY);
            assertTrue(tw.isComplete());
        }

        @Test
        void isCurrent_includesNow() {
            TimeWindow tw = TimeWindow.of(now.minusHours(1), now.plusHours(1), TimeGranularity.HOURLY);
            assertTrue(tw.isCurrent());
        }
    }

    @Nested
    @DisplayName("Navigation tests")
    class NavigationTests {
        @Test
        void getNext_returnsNextWindow() {
            TimeWindow tw = TimeWindow.of(now, now.plusHours(1), TimeGranularity.HOURLY);
            TimeWindow next = tw.getNext();
            assertEquals(tw.getEndTime(), next.getStartTime());
        }

        @Test
        void getPrevious_returnsPreviousWindow() {
            TimeWindow tw = TimeWindow.of(now, now.plusHours(1), TimeGranularity.HOURLY);
            TimeWindow prev = tw.getPrevious();
            assertEquals(tw.getStartTime(), prev.getEndTime());
        }
    }

    @Nested
    @DisplayName("Split tests")
    class SplitTests {
        @Test
        void splitInto_validSplit() {
            TimeWindow tw = TimeWindow.of(now, now.plusDays(1), TimeGranularity.DAILY);
            var windows = tw.splitInto(TimeGranularity.HOURLY);
            assertEquals(24, windows.size());
        }

        @Test
        void splitInto_invalidSplit_throws() {
            TimeWindow tw = TimeWindow.of(now, now.plusHours(1), TimeGranularity.HOURLY);
            assertThrows(IllegalArgumentException.class, () -> tw.splitInto(TimeGranularity.DAILY));
        }
    }

    @Test
    void getOverlapDuration_overlapping_returnsDuration() {
        TimeWindow tw1 = TimeWindow.of(now.minusHours(2), now, TimeGranularity.HOURLY);
        TimeWindow tw2 = TimeWindow.of(now.minusHours(1), now.plusHours(1), TimeGranularity.HOURLY);
        Duration overlap = tw1.getOverlapDuration(tw2);
        assertEquals(Duration.ofHours(1).toMinutes(), overlap.toMinutes(), 1);
    }

    @Test
    void getOverlapDuration_nonOverlapping_returnsZero() {
        TimeWindow tw1 = TimeWindow.of(now.minusHours(3), now.minusHours(2), TimeGranularity.HOURLY);
        TimeWindow tw2 = TimeWindow.of(now.minusHours(1), now, TimeGranularity.HOURLY);
        assertEquals(Duration.ZERO, tw1.getOverlapDuration(tw2));
    }

    @Test
    void getPositionAt_withinRange() {
        TimeWindow tw = TimeWindow.of(now, now.plusHours(1), TimeGranularity.HOURLY);
        double pos = tw.getPositionAt(now.plusMinutes(30));
        assertEquals(0.5, pos, 0.01);
    }

    @Test
    void formatForDisplay_containsBothTimestamps() {
        TimeWindow tw = TimeWindow.of(now.minusHours(1), now, TimeGranularity.HOURLY);
        String formatted = tw.formatForDisplay();
        assertNotNull(formatted);
        assertTrue(formatted.contains("Hourly"));
    }

    @Test
    void equalsAndHashCode_sameValues_equal() {
        TimeWindow tw1 = TimeWindow.of(now, now.plusHours(1), TimeGranularity.HOURLY);
        TimeWindow tw2 = TimeWindow.of(now, now.plusHours(1), TimeGranularity.HOURLY);
        assertEquals(tw1, tw2);
        assertEquals(tw1.hashCode(), tw2.hashCode());
    }

    @Test
    void toString_returnsFormattedDisplay() {
        TimeWindow tw = TimeWindow.of(now, now.plusHours(1), TimeGranularity.HOURLY);
        assertEquals(tw.formatForDisplay(), tw.toString());
    }
}
