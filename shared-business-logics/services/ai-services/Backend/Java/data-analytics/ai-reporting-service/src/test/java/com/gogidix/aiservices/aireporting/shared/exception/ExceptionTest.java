package com.gogidix.aiservices.aireporting.shared.exception;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ExceptionTest {
    @Test
    void reportExceptionMessage() { assertThat(new ReportException("msg").getMessage()).isEqualTo("msg"); }
    @Test
    void reportExceptionCause() { var c = new RuntimeException("c"); assertThat(new ReportException("msg", c).getCause()).isEqualTo(c); }
    @Test
    void reportNotFoundExceptionMessage() { assertThat(new ReportNotFoundException("id").getMessage()).contains("id"); }
}
