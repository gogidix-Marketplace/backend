package com.gogidix.dashboard.realtime.adapter.in.web;

import com.gogidix.dashboard.realtime.domain.model.*;
import com.gogidix.dashboard.realtime.domain.port.in.RealTimeDataManagementUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RealTimeDataControllerTest {

    @Mock
    private RealTimeDataManagementUseCase realTimeService;

    private RealTimeDataController controller;

    private RealTimeDataStream createTestStream() {
        return RealTimeDataStream.builder()
                .withStreamId(StreamId.generate())
                .withStreamName("test-stream")
                .withStreamType(StreamType.DASHBOARD_KPI)
                .withDataSource(new DataSourceConnection("c1", "ws://localhost"))
                .withConfiguration(StreamConfiguration.defaultDashboardConfig())
                .withStatus(StreamStatus.ACTIVE)
                .withCreatedAt(LocalDateTime.now())
                .withMetrics(StreamMetrics.builder().withHealth(StreamHealth.HEALTHY).withUptime(Duration.ofHours(1)).build())
                .withBackpressureStrategy(BackpressureStrategy.BUFFER)
                .withQualityOfService(QualityOfService.AT_LEAST_ONCE)
                .withMetadata(Map.of("domain", "test"))
                .build();
    }

    @BeforeEach
    void setUp() {
        controller = new RealTimeDataController(realTimeService);
    }

    @Nested
    @DisplayName("createStream tests")
    class CreateStreamTests {
        @Test
        void createStream_returnsOk() {
            RealTimeDataStream stream = createTestStream();
            RealTimeDataController.CreateStreamRequestDTO request = new RealTimeDataController.CreateStreamRequestDTO();
            request.setStreamName("test-stream");
            request.setDomain("test");
            request.setConfiguration(StreamConfiguration.defaultDashboardConfig());

            when(realTimeService.createStream(any())).thenReturn(stream);

            ResponseEntity<RealTimeDataStream> response = controller.createStream(request);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }
    }

    @Nested
    @DisplayName("startStream tests")
    class StartStreamTests {
        @Test
        void startStream_returnsOk() {
            ResponseEntity<Void> response = controller.startStream("stream-1");
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(realTimeService).startStream(any(StreamId.class));
        }
    }

    @Nested
    @DisplayName("stopStream tests")
    class StopStreamTests {
        @Test
        void stopStream_returnsOk() {
            ResponseEntity<Void> response = controller.stopStream("stream-1");
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(realTimeService).stopStream(any(StreamId.class));
        }
    }

    @Nested
    @DisplayName("getStream tests")
    class GetStreamTests {
        @Test
        void getStream_found_returnsOk() {
            RealTimeDataStream stream = createTestStream();
            when(realTimeService.getStream(any())).thenReturn(Optional.of(stream));

            ResponseEntity<RealTimeDataStream> response = controller.getStream("stream-1");
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        void getStream_notFound_returns404() {
            when(realTimeService.getStream(any())).thenReturn(Optional.empty());

            ResponseEntity<RealTimeDataStream> response = controller.getStream("nonexistent");
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("getActiveStreams tests")
    class GetActiveStreamsTests {
        @Test
        void getActiveStreams_returnsList() {
            when(realTimeService.getActiveStreams()).thenReturn(List.of(createTestStream()));

            ResponseEntity<List<RealTimeDataStream>> response = controller.getActiveStreams();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
        }
    }

    @Nested
    @DisplayName("getStreamsByDomain tests")
    class GetStreamsByDomainTests {
        @Test
        void getStreamsByDomain_returnsList() {
            when(realTimeService.getStreamsByDomain("test")).thenReturn(List.of());

            ResponseEntity<List<RealTimeDataStream>> response = controller.getStreamsByDomain("test");
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("pushData tests")
    class PushDataTests {
        @Test
        void handleStreamData_returnsOk() {
            ResponseEntity<Void> response = controller.handleStreamData("s1", Map.of("key", "val"));
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(realTimeService).pushData(any(StreamId.class), any());
        }

        @Test
        void pushData_returnsOk() {
            ResponseEntity<Void> response = controller.pushData("s1", Map.of("key", "val"));
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("deleteStream tests")
    class DeleteStreamTests {
        @Test
        void deleteStream_returnsOk() {
            ResponseEntity<Void> response = controller.deleteStream("s1");
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(realTimeService).deleteStream(any(StreamId.class));
        }
    }

    @Nested
    @DisplayName("health tests")
    class HealthTests {
        @Test
        void health_returnsOk() {
            ResponseEntity<String> response = controller.health();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().contains("running"));
        }
    }

    @Nested
    @DisplayName("CreateStreamRequestDTO tests")
    class RequestDTOTests {
        @Test
        void settersAndGetters_work() {
            RealTimeDataController.CreateStreamRequestDTO dto = new RealTimeDataController.CreateStreamRequestDTO();
            dto.setStreamName("name");
            dto.setDomain("domain");
            dto.setConfiguration(StreamConfiguration.defaultDashboardConfig());
            assertEquals("name", dto.getStreamName());
            assertEquals("domain", dto.getDomain());
            assertNotNull(dto.getConfiguration());
        }
    }
}
