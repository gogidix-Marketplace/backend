package com.gogidix.shared.utilities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SharedUtilitiesAppTest {

    @Test
    void mainClassExists() {
        assertNotNull(SharedUtilitiesApplication.class);
    }

    @Test
    void mainMethodExists() throws NoSuchMethodException {
        assertNotNull(SharedUtilitiesApplication.class.getMethod("main", String[].class));
    }

    @Test
    void hasSpringBootApplicationAnnotation() {
        assertNotNull(SharedUtilitiesApplication.class.getAnnotation(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }
}
