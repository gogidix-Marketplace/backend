package com.gogidix.aiservices.supplychainoptimizationservice.application.service;
import org.junit.jupiter.api.*;
import com.gogidix.aiservices.supplychainoptimizationservice.application.dto.request.*;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.aggregate.*;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.*;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.port.out.*;
import com.gogidix.aiservices.supplychainoptimizationservice.shared.exception.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.*;

class SupplyChainOptimizationServiceTest {
    private OptimizationRequestRepository repo;
    private OptimizationEnginePort engine;
    private SupplyChainOptimizationService svc;

    @BeforeEach
    void setup() {
        repo = mock(OptimizationRequestRepository.class);
        engine = mock(OptimizationEnginePort.class);
        svc = new SupplyChainOptimizationService(repo, engine);
    }

    @Test
    void create() {
        when(engine.validateParameters(any(), any())).thenReturn(true);
        when(engine.generateOptimization(any())).thenReturn(
            OptimizationResult.builder()
                .requestId(java.util.UUID.randomUUID())
                .type(OptimizationType.ROUTE_OPTIMIZATION)
                .confidenceScore(0.9).build());
        when(repo.save(any())).thenAnswer(a -> a.getArgument(0));
        var r = svc.createRequest(CreateOptimizationRequest.of("t1", OptimizationType.ROUTE_OPTIMIZATION, Map.of()));
        assertThat(r).isNotNull();
    }

    @Test
    void notFound() {
        when(repo.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> svc.getRequest(java.util.UUID.randomUUID().toString()))
            .isInstanceOf(OptimizationRequestNotFoundException.class);
    }

    @Test
    void byTenant() {
        when(repo.findByTenantId("t1")).thenReturn(List.of());
        assertThat(svc.getRequestsByTenant("t1")).isEmpty();
    }

    @Test
    void byStatus() {
        when(repo.findByStatus(OptimizationStatus.COMPLETED)).thenReturn(List.of());
        assertThat(svc.getRequestsByStatus(OptimizationStatus.COMPLETED)).isEmpty();
    }
}
