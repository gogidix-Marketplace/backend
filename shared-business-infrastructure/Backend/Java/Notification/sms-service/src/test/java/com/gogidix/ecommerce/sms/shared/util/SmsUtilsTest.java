package com.gogidix.ecommerce.sms.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SmsUtils Tests")
class SmsUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(SmsUtils.generateId()).isNotNull().isNotEmpty();
    }
}
