package com.gogidix.ecommerce.tracking.interfaces.rest;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class HealthControllerTest {
    @Test void health_returnsUp() {
        HealthController ctrl = new HealthController();
        var result = ctrl.health();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().get("status")).isEqualTo("UP");
    }
}