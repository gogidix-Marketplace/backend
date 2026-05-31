package com.gogidix.ecosystem.shared.validation;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for deprecated EmailValidator.
 * This class intentionally tests deprecated API to ensure backward compatibility.
 */
@SuppressWarnings("deprecation")
class EmailValidatorTest {

    @Test
    void shouldValidateCorrectEmails() {
        assertThat(EmailValidator.isValid("test@example.com")).isTrue();
        assertThat(EmailValidator.isValid("user.name@domain.co.uk")).isTrue();
        assertThat(EmailValidator.isValid("123@test.org")).isTrue();
    }

    @Test
    void shouldRejectInvalidEmails() {
        assertThat(EmailValidator.isValid("invalid")).isFalse();
        assertThat(EmailValidator.isValid("@domain.com")).isFalse();
        assertThat(EmailValidator.isValid("user@")).isFalse();
        assertThat(EmailValidator.isValid(null)).isFalse();
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        assertThatThrownBy(() -> EmailValidator.validate("invalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid email format");
    }

    @Test
    void shouldNotThrowExceptionForValidEmail() {
        assertThatCode(() -> EmailValidator.validate("test@example.com"))
                .doesNotThrowAnyException();
    }
}