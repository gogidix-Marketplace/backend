package com.gogidix.aiservices.intelligenceanalysisservice.shared.util;
import org.junit.jupiter.api.*; import static org.assertj.core.api.Assertions.*;
class ValidationUtilTest {
    @Test void notEmpty() { assertThat(ValidationUtil.isNotEmpty("h")).isTrue(); assertThat(ValidationUtil.isNotEmpty(null)).isFalse(); }
    @Test void empty() { assertThat(ValidationUtil.isEmpty(null)).isTrue(); assertThat(ValidationUtil.isEmpty("t")).isFalse(); }
    @Test void email() { assertThat(ValidationUtil.isValidEmail(null)).isFalse(); }
}
