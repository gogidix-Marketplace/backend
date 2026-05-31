package com.gogidix.aiservices.aiuserprofilingservice.shared.exception;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class ExceptionCoverageTest {
    @Test
    void validationExceptionWithList() {
        var ex = new ValidationException(List.of("err1", "err2"));
        assertThat(ex.getValidationErrors()).hasSize(2);
    }
    @Test
    void validationExceptionWithMessageAndList() {
        var ex = new ValidationException("msg", List.of("e1"));
        assertThat(ex.getMessage()).isEqualTo("msg");
    }
    @Test
    void validationExceptionWithCause() {
        var cause = new RuntimeException("c");
        var ex = new ValidationException("msg", cause);
        assertThat(ex.getCause()).isEqualTo(cause);
    }
    @Test
    void businessExceptionWithCause() {
        var ex = new BusinessException("msg", new RuntimeException());
        assertThat(ex.getMessage()).isEqualTo("msg");
    }
    @Test
    void businessExceptionWithCodeAndCause() {
        var ex = new BusinessException("msg", "CODE", new RuntimeException());
        assertThat(ex.getErrorCode()).isEqualTo("CODE");
    }
    @Test
    void notFoundExceptionWithResource() {
        var ex = new NotFoundException("User", "123");
        assertThat(ex.getResourceType()).isEqualTo("User");
        assertThat(ex.getResourceId()).isEqualTo("123");
    }
    @Test
    void notFoundExceptionWithMessage() {
        var ex = new NotFoundException("not found");
        assertThat(ex.getResourceType()).isEqualTo("RESOURCE");
    }
    @Test
    void notFoundExceptionWithCause() {
        var ex = new NotFoundException("msg", new RuntimeException());
        assertThat(ex.getResourceId()).isEqualTo("UNKNOWN");
    }
    @Test
    void conflictExceptionWithResource() {
        var ex = new ConflictException("User", "email@test.com");
        assertThat(ex.getResourceType()).isEqualTo("User");
        assertThat(ex.getConflictingValue()).isEqualTo("email@test.com");
    }
    @Test
    void conflictExceptionWithMessage() {
        var ex = new ConflictException("conflict");
        assertThat(ex.getConflictingValue()).isEqualTo("UNKNOWN");
    }
    @Test
    void conflictExceptionWithCause() {
        var ex = new ConflictException("msg", new RuntimeException());
        assertThat(ex.getResourceType()).isEqualTo("RESOURCE");
    }
    @Test
    void conflictExceptionWithCode() {
        var ex = new ConflictException("User", "val", "DUPLICATE");
        assertThat(ex.getErrorCode()).isEqualTo("DUPLICATE");
    }
}
