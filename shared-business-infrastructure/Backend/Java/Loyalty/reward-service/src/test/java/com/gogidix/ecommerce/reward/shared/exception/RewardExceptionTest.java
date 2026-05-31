package com.gogidix.ecommerce.reward.shared.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Reward Exception Tests")
class RewardExceptionTest {

    @Test
    @DisplayName("Should create not found exception")
    void shouldCreateNotFoundException() {
        RewardNotFoundException ex = new RewardNotFoundException("id-1");
        assertThat(ex.getMessage()).contains("id-1");
        assertThat(ex.getId()).isEqualTo("id-1");
    }

    @Test
    @DisplayName("Should create duplicate exception")
    void shouldCreateDuplicateException() {
        DuplicateRewardException ex = new DuplicateRewardException("name-1");
        assertThat(ex.getMessage()).contains("name-1");
        assertThat(ex.getName()).isEqualTo("name-1");
    }

    @Test
    @DisplayName("Should create invalid exception with message")
    void shouldCreateInvalidException() {
        InvalidRewardException ex = new InvalidRewardException("bad data");
        assertThat(ex.getMessage()).isEqualTo("bad data");
    }

    @Test
    @DisplayName("Should create invalid exception with cause")
    void shouldCreateInvalidExceptionWithCause() {
        RuntimeException cause = new RuntimeException("root cause");
        InvalidRewardException ex = new InvalidRewardException("msg", cause);
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
