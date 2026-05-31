package com.gogidix.ecommerce.wishlist.domain.policy;

import com.gogidix.ecommerce.wishlist.domain.model.Wishlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("WishlistValidationPolicy Tests")
class WishlistValidationPolicyTest {

    private WishlistValidationPolicy policy;

    @BeforeEach
    void setUp() { policy = new WishlistValidationPolicy(); }

    @Test
    @DisplayName("Should pass for valid entity")
    void shouldPassValid() {
        Wishlist entity = new Wishlist("tenant-1");
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
        Wishlist entity = new Wishlist("tenant-1");
        entity.setName("");
        assertThatThrownBy(() -> policy.validate(entity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name is required");
    }

    @Test
    @DisplayName("Should reject null name")
    void shouldRejectNullName() {
        Wishlist entity = new Wishlist("tenant-1");
        assertThatThrownBy(() -> policy.validate(entity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name is required");
    }
}
