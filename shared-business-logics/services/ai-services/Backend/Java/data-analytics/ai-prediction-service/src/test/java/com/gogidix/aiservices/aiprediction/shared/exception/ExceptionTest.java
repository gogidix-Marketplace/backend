package com.gogidix.aiservices.aiprediction.shared.exception;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ExceptionTest {
    @Test
    void predictionExceptionMessage() { assertThat(new PredictionException("msg").getMessage()).isEqualTo("msg"); }
    @Test
    void predictionExceptionCause() { var c = new RuntimeException("c"); assertThat(new PredictionException("msg", c).getCause()).isEqualTo(c); }
    @Test
    void predictionNotFoundExceptionMessage() { assertThat(new PredictionNotFoundException("id").getMessage()).contains("id"); }
}
