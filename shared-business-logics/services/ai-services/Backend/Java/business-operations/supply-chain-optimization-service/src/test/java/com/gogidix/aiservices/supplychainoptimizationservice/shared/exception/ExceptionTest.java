package com.gogidix.aiservices.supplychainoptimizationservice.shared.exception;
import org.junit.jupiter.api.*; import static org.assertj.core.api.Assertions.*;
class ExceptionTest {
    @Test void supplyChain() { assertThat(new SupplyChainException("m").getMessage()).isEqualTo("m"); }
    @Test void supplyChainCause() { assertThat(new SupplyChainException("m", new RuntimeException()).getCause()).isInstanceOf(RuntimeException.class); }
    @Test void notFound() { assertThat(new OptimizationRequestNotFoundException("x").getMessage()).contains("x"); }
}
