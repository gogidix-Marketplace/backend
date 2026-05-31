package com.gogidix.aiservices.aidatavalidation.domain;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ValidationStatisticsTest {
    @Test
    void validityRate() {
        var s = new ValidationStatistics(100, 80, 15, 5);
        assertThat(s.getValidityRate()).isEqualTo(0.8);
    }

    @Test
    void invalidityRate() {
        var s = new ValidationStatistics(100, 80, 15, 5);
        assertThat(s.getInvalidityRate()).isEqualTo(0.15);
    }

    @Test
    void completenessRate() {
        var s = new ValidationStatistics(100, 80, 15, 5);
        assertThat(s.getCompletenessRate()).isEqualTo(0.95);
    }

    @Test
    void zeroTotalRecords() {
        var s = new ValidationStatistics(0, 0, 0, 0);
        assertThat(s.getValidityRate()).isEqualTo(0.0);
        assertThat(s.getInvalidityRate()).isEqualTo(0.0);
        assertThat(s.getCompletenessRate()).isEqualTo(0.0);
    }

    @Test
    void builder() {
        var s = ValidationStatistics.builder()
            .totalRecords(50)
            .validRecords(40)
            .invalidRecords(8)
            .skippedRecords(2)
            .build();
        assertThat(s.getTotalRecords()).isEqualTo(50);
    }
}
