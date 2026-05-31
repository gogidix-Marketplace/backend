package com.gogidix.aiservices.aiuserprofilingservice.shared.util;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ValidationUtilTest {
    @Test
    void isNotEmptyTest() {
        assertThat(ValidationUtil.isNotEmpty("hello")).isTrue();
        assertThat(ValidationUtil.isNotEmpty("  ")).isFalse();
        assertThat(ValidationUtil.isNotEmpty(null)).isFalse();
    }
    @Test
    void isEmptyTest() {
        assertThat(ValidationUtil.isEmpty(null)).isTrue();
        assertThat(ValidationUtil.isEmpty("text")).isFalse();
    }
    @Test
    void isValidEmailTest() {
        assertThat(ValidationUtil.isValidEmail(null)).isFalse();
    }
}
