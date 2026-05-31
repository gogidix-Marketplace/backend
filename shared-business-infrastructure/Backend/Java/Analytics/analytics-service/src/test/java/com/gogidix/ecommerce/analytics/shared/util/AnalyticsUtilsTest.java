package com.gogidix.ecommerce.analytics.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("AnalyticsUtils Tests")
class AnalyticsUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(AnalyticsUtils.generateId()).isNotNull().isNotEmpty();
    }
}
