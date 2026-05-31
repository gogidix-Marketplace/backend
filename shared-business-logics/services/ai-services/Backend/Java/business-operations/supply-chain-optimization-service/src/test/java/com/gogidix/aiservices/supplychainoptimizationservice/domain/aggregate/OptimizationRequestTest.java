package com.gogidix.aiservices.supplychainoptimizationservice.domain.aggregate;

import com.gogidix.aiservices.supplychainoptimizationservice.domain.event.OptimizationEvent;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.event.OptimizationEventType;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("OptimizationRequest Aggregate Tests")
class OptimizationRequestTest {

    private static final String TENANT_ID = "tenant-123";

    @Nested
    @DisplayName("Request Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create request with valid parameters")
        void shouldCreateRequest() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId(TENANT_ID)
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            assertThat(request).isNotNull();
            assertThat(request.getTenantId()).isEqualTo(TENANT_ID);
            assertThat(request.getType()).isEqualTo(OptimizationType.INVENTORY_LEVELS);
            assertThat(request.getStatus()).isEqualTo(OptimizationStatus.PENDING);
            assertThat(request.getRequestId()).isNotNull();
        }

        @Test
        @DisplayName("Should reject request without tenant ID")
        void shouldRejectWithoutTenantId() {
            assertThatThrownBy(() -> OptimizationRequest.builder()
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Tenant ID");
        }

        @Test
        @DisplayName("Should reject request without type")
        void shouldRejectWithoutType() {
            assertThatThrownBy(() -> OptimizationRequest.builder()
                    .tenantId(TENANT_ID)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Optimization type");
        }

        @Test
        @DisplayName("Should generate unique request ID")
        void shouldGenerateUniqueId() {
            OptimizationRequest request1 = OptimizationRequest.builder()
                    .tenantId(TENANT_ID)
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationRequest request2 = OptimizationRequest.builder()
                    .tenantId(TENANT_ID)
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            assertThat(request1.getRequestId()).isNotEqualTo(request2.getRequestId());
        }
    }

    @Nested
    @DisplayName("Status Transition Tests")
    class StatusTransitionTests {

        @Test
        @DisplayName("Should start processing")
        void shouldStartProcessing() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId(TENANT_ID)
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            request.startProcessing();

            assertThat(request.getStatus()).isEqualTo(OptimizationStatus.IN_PROGRESS);
            assertThat(request.getEvents()).hasSize(1);
        }

        @Test
        @DisplayName("Should complete with result")
        void shouldCompleteWithResult() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId(TENANT_ID)
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            request.startProcessing();

            OptimizationResult result = OptimizationResult.builder()
                    .requestId(request.getRequestId())
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            request.completeWithResult(result);

            assertThat(request.getStatus()).isEqualTo(OptimizationStatus.COMPLETED);
            assertThat(request.getResult()).isNotNull();
            assertThat(request.getCompletedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should fail with error")
        void shouldFailWithError() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId(TENANT_ID)
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            request.failWithError("Processing failed");

            assertThat(request.getStatus()).isEqualTo(OptimizationStatus.FAILED);
            assertThat(request.getErrorMessage()).isEqualTo("Processing failed");
        }

        @Test
        @DisplayName("Should cancel request")
        void shouldCancelRequest() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId(TENANT_ID)
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            request.cancel();

            assertThat(request.getStatus()).isEqualTo(OptimizationStatus.CANCELLED);
        }
    }

    @Nested
    @DisplayName("Parameter Management Tests")
    class ParameterTests {

        @Test
        @DisplayName("Should add parameter")
        void shouldAddParameter() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId(TENANT_ID)
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            request.addParameter("warehouseId", "WH-001");

            assertThat(request.getParameters()).containsEntry("warehouseId", "WH-001");
        }

        @Test
        @DisplayName("Should get typed parameter")
        void shouldGetTypedParameter() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId(TENANT_ID)
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .parameters(Map.of("targetDays", 30))
                    .build();

            Integer targetDays = request.getParameter("targetDays", Integer.class);

            assertThat(targetDays).isEqualTo(30);
        }
    }

    @Nested
    @DisplayName("Event Management Tests")
    class EventTests {

        @Test
        @DisplayName("Should add event")
        void shouldAddEvent() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId(TENANT_ID)
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationEvent event = OptimizationEvent.builder()
                    .eventType(OptimizationEventType.REQUEST_CREATED)
                    .description("Request created")
                    .build();

            request.addEvent(event);

            assertThat(request.getEvents()).hasSize(1);
        }

        @Test
        @DisplayName("Should reject null event")
        void shouldRejectNullEvent() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId(TENANT_ID)
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            assertThatThrownBy(() -> request.addEvent(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Duration Tests")
    class DurationTests {

        @Test
        @DisplayName("Should calculate duration after completion")
        void shouldCalculateDuration() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId(TENANT_ID)
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .priority(5)
                    .build();

            request.startProcessing();

            OptimizationResult result = OptimizationResult.builder()
                    .requestId(request.getRequestId())
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            request.completeWithResult(result);

            assertThat(request.getDurationMs()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal with same request ID")
        void shouldBeEqualWithSameId() {
            UUID requestId = UUID.randomUUID();
            OptimizationRequest request1 = OptimizationRequest.builder()
                    .requestId(requestId)
                    .tenantId(TENANT_ID)
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationRequest request2 = OptimizationRequest.builder()
                    .requestId(requestId)
                    .tenantId(TENANT_ID)
                    .type(OptimizationType.DEMAND_FORECASTING)
                    .build();

            assertThat(request1).isEqualTo(request2);
            assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        }
    }
}
