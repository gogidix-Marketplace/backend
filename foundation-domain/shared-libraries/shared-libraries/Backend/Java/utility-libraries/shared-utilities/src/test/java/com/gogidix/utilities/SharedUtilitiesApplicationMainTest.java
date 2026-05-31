package com.gogidix.utilities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

class SharedUtilitiesApplicationMainTest {

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

    @Test
    void mainMethodReturnsVoid() throws NoSuchMethodException {
        Method main = SharedUtilitiesApplication.class.getMethod("main", String[].class);
        assertEquals(void.class, main.getReturnType());
    }

    @Test
    void constructorExists() {
        assertDoesNotThrow(() -> {
            var ctor = SharedUtilitiesApplication.class.getDeclaredConstructor();
            assertNotNull(ctor);
        });
    }
}
