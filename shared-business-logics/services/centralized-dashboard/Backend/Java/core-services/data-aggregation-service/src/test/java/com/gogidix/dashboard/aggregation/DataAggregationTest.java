package com.gogidix.dashboard.aggregation;

import com.gogidix.dashboard.aggregation.domain.model.*;
import com.gogidix.dashboard.aggregation.domain.port.in.CreateAggregationCommand;
import com.gogidix.dashboard.aggregation.interfaces.rest.HealthController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Data Aggregation Service Tests")
class DataAggregationTest {

    @Nested
    @DisplayName("AggregationRequest Tests")
    class AggregationRequestTest {

        @Test
        @DisplayName("Should build with defaults")
        void shouldBuildWithDefaults() {
            AggregationRequest req = AggregationRequest.builder().build();
            assertEquals(AggregationStatus.PENDING, req.getStatus());
        }

        @Test
        @DisplayName("Should mark as processing")
        void shouldMarkAsProcessing() {
            AggregationRequest req = AggregationRequest.builder().build();
            req.markAsProcessing();
            assertEquals(AggregationStatus.PROCESSING, req.getStatus());
            assertNotNull(req.getProcessedAt());
        }

        @Test
        @DisplayName("Should mark as completed")
        void shouldMarkAsCompleted() {
            AggregationRequest req = AggregationRequest.builder().build();
            req.markAsCompleted("{\"result\": 42}");
            assertEquals(AggregationStatus.COMPLETED, req.getStatus());
            assertEquals("{\"result\": 42}", req.getResult());
            assertNotNull(req.getCompletedAt());
        }

        @Test
        @DisplayName("Should mark as failed")
        void shouldMarkAsFailed() {
            AggregationRequest req = AggregationRequest.builder().build();
            req.markAsFailed("timeout");
            assertEquals(AggregationStatus.FAILED, req.getStatus());
            assertEquals("timeout", req.getErrorMessage());
        }

        @Test
        @DisplayName("Should detect pending")
        void shouldDetectPending() {
            AggregationRequest req = AggregationRequest.builder().build();
            assertTrue(req.isPending());
            assertFalse(req.isProcessing());
            assertFalse(req.isCompleted());
            assertFalse(req.isFailed());
        }

        @Test
        @DisplayName("Should detect completed")
        void shouldDetectCompleted() {
            AggregationRequest req = AggregationRequest.builder().build();
            req.markAsCompleted("ok");
            assertTrue(req.isCompleted());
            assertFalse(req.isPending());
        }
    }

    @Nested
    @DisplayName("AggregationStatus Tests")
    class AggregationStatusTest {

        @Test
        @DisplayName("Should have all statuses")
        void shouldHaveAllStatuses() {
            assertEquals(6, AggregationStatus.values().length);
            assertEquals("Pending", AggregationStatus.PENDING.getDisplayName());
            assertNotNull(AggregationStatus.COMPLETED.getDescription());
        }
    }

    @Nested
    @DisplayName("CachedAggregationResult Tests")
    class CachedAggregationResultTest {

        @Test
        @DisplayName("Should detect expired result")
        void shouldDetectExpired() {
            CachedAggregationResult cached = CachedAggregationResult.builder()
                    .expiresAt(LocalDateTime.now().minusHours(1)).build();
            assertTrue(cached.isExpired());
        }

        @Test
        @DisplayName("Should detect non-expired result")
        void shouldDetectNonExpired() {
            CachedAggregationResult cached = CachedAggregationResult.builder()
                    .expiresAt(LocalDateTime.now().plusHours(1)).build();
            assertFalse(cached.isExpired());
        }
    }

    @Nested
    @DisplayName("CreateAggregationCommand Tests")
    class CreateAggregationCommandTest {

        @Test
        @DisplayName("Should build command")
        void shouldBuildCommand() {
            CreateAggregationCommand cmd = CreateAggregationCommand.builder()
                    .name("test").tenantId("t1")
                    .sourceDomains(List.of("COURIER", "WAREHOUSE")).build();
            assertEquals("test", cmd.getName());
            assertEquals(2, cmd.getSourceDomains().size());
        }
    }

    @Nested
    @DisplayName("HealthController Tests")
    class HealthControllerTest {

        private final HealthController controller = new HealthController();

        @Test
        @DisplayName("Should return health")
        void shouldReturnHealth() {
            ResponseEntity<Map<String, Object>> resp = controller.health();
            assertEquals("UP", resp.getBody().get("status"));
        }

        @Test
        @DisplayName("Should return info")
        void shouldReturnInfo() {
            ResponseEntity<Map<String, Object>> resp = controller.info();
            assertNotNull(resp.getBody().get("name"));
        }
    }
}
