package com.gogidix.aiservices.predictiveanalytics.domain;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class TrendAnalysisServiceTest {
    private TrendAnalysisService s;

    @BeforeEach
    void setup() { s = new TrendAnalysisService(); }

    private Map<String, Object> makeData(int count) {
        var records = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < count; i++) {
            records.add(Map.of("value", (Number)(i * 10.0)));
        }
        return Map.of("data", records);
    }

    @Test
    void analyzeTrends() {
        var result = s.analyzeTrends(makeData(55), "value");
        assertThat(result).isNotNull();
        assertThat(result.getField()).isEqualTo("value");
        assertThat(result.getDirection()).isNotNull();
    }

    @Test
    void analyzeTrendsWithInsufficientDataThrows() {
        assertThatThrownBy(() -> s.analyzeTrends(makeData(5), "value"))
            .isInstanceOf(InsufficientDataException.class);
    }

    @Test
    void trendDirectionEnum() {
        assertThat(TrendAnalysisService.TrendDirection.values()).hasSize(3);
    }

    @Test
    void trendResult() {
        var r = new TrendAnalysisService.TrendResult("f",
            TrendAnalysisService.TrendDirection.INCREASING, 0.8, 0.1, 2.5, 0.95);
        assertThat(r.getField()).isEqualTo("f");
        assertThat(r.getDirection()).isEqualTo(TrendAnalysisService.TrendDirection.INCREASING);
    }
}
