package com.gogidix.ecommerce.storecredit.shared.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("StoreCredit Exception Tests")
class StoreCreditExceptionTest {

    @Test
    @DisplayName("Should create not found exception")
    void shouldCreateNotFoundException() {
        StoreCreditNotFoundException ex = new StoreCreditNotFoundException("id-1");
        assertThat(ex.getMessage()).contains("id-1");
        assertThat(ex.getId()).isEqualTo("id-1");
    }

    @Test
    @DisplayName("Should create duplicate exception")
    void shouldCreateDuplicateException() {
        DuplicateStoreCreditException ex = new DuplicateStoreCreditException("name-1");
        assertThat(ex.getMessage()).contains("name-1");
        assertThat(ex.getName()).isEqualTo("name-1");
    }

    @Test
    @DisplayName("Should create invalid exception with message")
    void shouldCreateInvalidException() {
        InvalidStoreCreditException ex = new InvalidStoreCreditException("bad data");
        assertThat(ex.getMessage()).isEqualTo("bad data");
    }

    @Test
    @DisplayName("Should create invalid exception with cause")
    void shouldCreateInvalidExceptionWithCause() {
        RuntimeException cause = new RuntimeException("root cause");
        InvalidStoreCreditException ex = new InvalidStoreCreditException("msg", cause);
        assertThat(ex.getMessage()).isEqualTo("msg");
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("GlobalExceptionHandler should handle IllegalArgumentException")
    void shouldHandleIllegalArgument() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        var response = handler.handleIllegalArgument(new IllegalArgumentException("test"));
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).containsKey("error");
    }

    @Test
    @DisplayName("GlobalExceptionHandler should handle generic exception")
    void shouldHandleGeneric() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        var response = handler.handleGeneric(new RuntimeException("test"));
        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody()).containsKey("error");
    }
}
