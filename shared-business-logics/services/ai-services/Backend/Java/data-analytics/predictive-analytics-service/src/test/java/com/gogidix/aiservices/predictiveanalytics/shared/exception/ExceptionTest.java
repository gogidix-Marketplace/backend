package com.gogidix.aiservices.predictiveanalytics.shared.exception;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ExceptionTest {
    @Test
    void forecastNotFoundExceptionMessage() { assertThat(new ForecastNotFoundException("id").getMessage()).contains("id"); }
}
