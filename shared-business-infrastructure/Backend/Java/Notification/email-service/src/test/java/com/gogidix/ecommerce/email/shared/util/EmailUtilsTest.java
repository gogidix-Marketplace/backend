package com.gogidix.ecommerce.email.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("EmailUtils Tests")
class EmailUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(EmailUtils.generateId()).isNotNull().isNotEmpty();
    }
}
