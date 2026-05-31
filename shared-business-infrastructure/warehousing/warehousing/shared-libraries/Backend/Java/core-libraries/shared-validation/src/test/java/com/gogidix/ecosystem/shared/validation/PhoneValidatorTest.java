package com.gogidix.ecosystem.shared.validation;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for deprecated PhoneValidator.
 * This class intentionally tests deprecated API to ensure backward compatibility.
 */
@SuppressWarnings("deprecation")
class PhoneValidatorTest {

    @Test
    void shouldValidateCorrectPhones() {
        assertThat(PhoneValidator.isValid("+1234567890")).isTrue();
        assertThat(PhoneValidator.isValid("1234567890")).isTrue();
        assertThat(PhoneValidator.isValid("+44 20 1234 5678")).isTrue();
    }

    @Test
    void shouldRejectInvalidPhones() {
        assertThat(PhoneValidator.isValid("123")).isFalse();
        assertThat(PhoneValidator.isValid("abcd")).isFalse();
        assertThat(PhoneValidator.isValid("+")).isFalse();
        assertThat(PhoneValidator.isValid(null)).isFalse();
    }

    @Test
    void shouldThrowExceptionForInvalidPhone() {
        assertThatThrownBy(() -> PhoneValidator.validate("123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid phone format");
    }

    @Test
    void shouldNotThrowExceptionForValidPhone() {
        assertThatCode(() -> PhoneValidator.validate("+1234567890"))
                .doesNotThrowAnyException();
    }
}