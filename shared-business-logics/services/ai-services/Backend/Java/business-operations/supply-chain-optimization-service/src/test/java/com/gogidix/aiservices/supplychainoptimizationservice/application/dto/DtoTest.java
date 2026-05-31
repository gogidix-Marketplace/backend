package com.gogidix.aiservices.supplychainoptimizationservice.application.dto;
import org.junit.jupiter.api.*;
import com.gogidix.aiservices.supplychainoptimizationservice.application.dto.request.*;
import com.gogidix.aiservices.supplychainoptimizationservice.application.dto.response.*;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class DtoTest {
    @Test void createReq() {
        var r = new CreateOptimizationRequest("t1", OptimizationType.ROUTE_OPTIMIZATION, Map.of("k","v"), 5);
        assertThat(r.tenantId()).isEqualTo("t1");
    }
    @Test void defPriority() {
        var r = new CreateOptimizationRequest("t1", OptimizationType.ROUTE_OPTIMIZATION, null, 0);
        assertThat(r.priority()).isEqualTo(5);
    }
    @Test void resp() {
        var r = new OptimizationRequestResponse("r1","t1", OptimizationType.ROUTE_OPTIMIZATION, OptimizationStatus.COMPLETED, null, 5, null, null, null, null);
        assertThat(r.requestId()).isEqualTo("r1");
    }
    @Test void resultResp() {
        var r = OptimizationResultResponse.builder().requestId("r1").build();
        assertThat(r.requestId()).isEqualTo("r1");
    }
}
