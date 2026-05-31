package com.gogidix.ecommerce.email.domain.policy;

import com.gogidix.ecommerce.email.domain.model.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("EmailValidationPolicy Tests")
class EmailValidationPolicyTest {

    private EmailValidationPolicy policy;

    @BeforeEach
    void setUp() { policy = new EmailValidationPolicy(); }

    @Test
    @DisplayName("Should pass for valid entity")
    void shouldPassValid() {
        Email entity = new Email("tenant-1");
        entity.setName("valid-name");
        assertThatCode(() -> policy.validate(entity)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should reject null entity")
    void shouldRejectNull() {
        assertThatThrownBy(() -> policy.validate(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be null");
    }

    @Test
    @DisplayName("Should reject blank name")
    void shouldRejectBlankName() {
        Email entity = new Email("tenant-1");
        entity.setName("");
        assertThatThrownBy(() -> policy.validate(entity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name is required");
    }

    @Test
    @DisplayName("Should reject null name")
    void shouldRejectNullName() {
        Email entity = new Email("tenant-1");
        assertThatThrownBy(() -> policy.validate(entity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name is required");
    }
}
