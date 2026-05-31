package com.gogidix.ecommerce.oceanshipping.domain.policy;

import com.gogidix.ecommerce.oceanshipping.domain.model.OceanShipping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("OceanShippingValidationPolicy Tests")
class OceanShippingValidationPolicyTest {

    private OceanShippingValidationPolicy policy;

    @BeforeEach
    void setUp() { policy = new OceanShippingValidationPolicy(); }

    @Test
    @DisplayName("Should pass for valid entity")
    void shouldPassValid() {
        OceanShipping entity = new OceanShipping("tenant-1");
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
        OceanShipping entity = new OceanShipping("tenant-1");
        entity.setName("");
        assertThatThrownBy(() -> policy.validate(entity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name is required");
    }

    @Test
    @DisplayName("Should reject null name")
    void shouldRejectNullName() {
        OceanShipping entity = new OceanShipping("tenant-1");
        assertThatThrownBy(() -> policy.validate(entity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name is required");
    }
}
