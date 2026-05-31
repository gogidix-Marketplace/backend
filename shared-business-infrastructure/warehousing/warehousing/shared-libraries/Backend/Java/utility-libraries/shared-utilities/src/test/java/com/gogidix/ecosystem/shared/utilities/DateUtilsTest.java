package com.gogidix.shared.utilities;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Date;
import static org.assertj.core.api.Assertions.*;

class DateUtilsTest {

    @Test
    void shouldFormatToIso() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
        String result = DateUtils.formatToIso(dateTime);
        assertThat(result).isEqualTo("2023-01-01T12:00:00");
    }

    @Test
    void shouldParseFromIso() {
        String isoString = "2023-01-01T12:00:00";
        LocalDateTime result = DateUtils.parseFromIso(isoString);
        assertThat(result).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    }

    @Test
    void shouldConvertToDate() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
        Date result = DateUtils.toDate(dateTime);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldConvertFromDate() {
        Date date = new Date();
        LocalDateTime result = DateUtils.fromDate(date);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldCheckIfExpired() {
        LocalDateTime pastTime = LocalDateTime.now().minusDays(1);
        LocalDateTime futureTime = LocalDateTime.now().plusDays(1);
        
        assertThat(DateUtils.isExpired(pastTime)).isTrue();
        assertThat(DateUtils.isExpired(futureTime)).isFalse();
    }
}