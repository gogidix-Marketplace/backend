package com.gogidix.ecommerce.cart.interfaces.rest;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class HealthControllerTest {
    @Test void health_returnsUp() {
        HealthController ctrl = new HealthController();
        Map<String, Object> result = ctrl.health();
        assertThat(result.get("status")).isEqualTo("UP");
        assertThat(result.get("service")).isEqualTo("cart-service");
        assertThat(result.get("timestamp")).isNotNull();
    }
}