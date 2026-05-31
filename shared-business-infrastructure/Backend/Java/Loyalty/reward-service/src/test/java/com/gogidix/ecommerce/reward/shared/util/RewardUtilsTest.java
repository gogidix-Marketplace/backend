package com.gogidix.ecommerce.reward.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("RewardUtils Tests")
class RewardUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(RewardUtils.generateId()).isNotNull().isNotEmpty();
    }
}
