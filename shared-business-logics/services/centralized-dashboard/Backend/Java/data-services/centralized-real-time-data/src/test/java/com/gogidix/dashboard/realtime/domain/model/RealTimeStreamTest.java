package com.gogidix.dashboard.realtime.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class RealTimeStreamTest {

    private StreamId streamId;
    private StreamConfiguration config;

    @BeforeEach
    void setUp() {
        streamId = StreamId.generate();
        config = StreamConfiguration.defaultDashboardConfig();
    }

    private RealTimeStream createActiveStream() {
        RealTimeStream stream = new RealTimeStream.Builder()
                .withId(streamId)
                .withStreamName("test-stream")
                .withStreamType(StreamType.DASHBOARD_KPI)
                .withConfiguration(config)
                .build();
        stream.start();
        return stream;
    }

    @Nested
    @DisplayName("Builder tests")
    class BuilderTests {
        @Test
        void build_createsStream() {
            RealTimeStream stream = new RealTimeStream.Builder()
                    .withId(streamId)
                    .withStreamName("test")
                    .withStreamType(StreamType.DASHBOARD_KPI)
                    .withConfiguration(config)
                    .build();
            assertNotNull(stream);
            assertEquals(streamId, stream.getId());
            assertEquals("test", stream.getStreamName());
        }

        @Test
        void build_nullId_throws() {
            assertThrows(NullPointerException.class, () ->
                new RealTimeStream.Builder()
                    .withId(null)
                    .withStreamName("test")
                    .withStreamType(StreamType.DASHBOARD_KPI)
                    .withConfiguration(config)
                    .build());
        }
    }

    @Nested
    @DisplayName("Lifecycle tests")
    class LifecycleTests {
        @Test
        void start_initializesToActive() {
            RealTimeStream stream = new RealTimeStream.Builder()
                    .withId(streamId)
                    .withStreamName("test")
                    .withStreamType(StreamType.DASHBOARD_KPI)
                    .withConfiguration(config)
                    .build();
            assertEquals(StreamStatus.INITIALIZING, stream.getStatus());
            stream.start();
            assertEquals(StreamStatus.ACTIVE, stream.getStatus());
        }

        @Test
        void stop_changesStatusToStopped() {
            RealTimeStream stream = createActiveStream();
            stream.stop();
            assertEquals(StreamStatus.STOPPED, stream.getStatus());
        }

        @Test
        void pause_changesStatusToPaused() {
            RealTimeStream stream = createActiveStream();
            stream.pause();
            assertEquals(StreamStatus.PAUSED, stream.getStatus());
        }

        @Test
        void resume_changesStatusToActive() {
            RealTimeStream stream = createActiveStream();
            stream.pause();
            stream.resume();
            assertEquals(StreamStatus.ACTIVE, stream.getStatus());
        }

        @Test
        void start_fromActive_throws() {
            RealTimeStream stream = createActiveStream();
            assertThrows(IllegalStateException.class, stream::start);
        }

        @Test
        void pause_inactiveStream_throws() {
            RealTimeStream stream = new RealTimeStream.Builder()
                    .withId(streamId)
                    .withStreamName("test")
                    .withStreamType(StreamType.DASHBOARD_KPI)
                    .withConfiguration(config)
                    .build();
            assertThrows(IllegalStateException.class, stream::pause);
        }

        @Test
        void resume_activeStream_throws() {
            RealTimeStream stream = createActiveStream();
            assertThrows(IllegalStateException.class, stream::resume);
        }
    }

    @Nested
    @DisplayName("Message processing tests")
    class MessageProcessingTests {
        @Test
        void processMessage_returnsStreamMessage() {
            RealTimeStream stream = createActiveStream();
            StreamMessage msg = stream.processMessage(java.util.Map.of("key", "value"));
            assertNotNull(msg);
            assertEquals(1, stream.getMessagesProcessed());
        }

        @Test
        void processMessage_inactiveStream_throws() {
            RealTimeStream stream = new RealTimeStream.Builder()
                    .withId(streamId)
                    .withStreamName("test")
                    .withStreamType(StreamType.DASHBOARD_KPI)
                    .withConfiguration(config)
                    .build();
            assertThrows(IllegalStateException.class, () ->
                stream.processMessage(java.util.Map.of("key", "value")));
        }

        @Test
        void processMessage_incrementsBytesTransferred() {
            RealTimeStream stream = createActiveStream();
            long before = stream.getBytesTransferred();
            stream.processMessage(java.util.Map.of("key", "value"));
            assertTrue(stream.getBytesTransferred() > before);
        }
    }

    @Nested
    @DisplayName("Subscriber tests")
    class SubscriberTests {
        @Test
        void addSubscriber_increasesCount() {
            RealTimeStream stream = createActiveStream();
            StreamSubscriber sub = new StreamSubscriber("sub1", "Test Subscriber");
            stream.addSubscriber(sub);
            assertEquals(1, stream.getSubscriberCount());
            assertTrue(sub.isActive());
        }

        @Test
        void addSubscriber_null_throws() {
            RealTimeStream stream = createActiveStream();
            assertThrows(NullPointerException.class, () -> stream.addSubscriber(null));
        }

        @Test
        void removeSubscriber_decreasesCount() {
            RealTimeStream stream = createActiveStream();
            StreamSubscriber sub = new StreamSubscriber("sub1", "Test");
            stream.addSubscriber(sub);
            stream.removeSubscriber("sub1");
            assertEquals(0, stream.getSubscriberCount());
        }

        @Test
        void stop_clearsSubscribers() {
            RealTimeStream stream = createActiveStream();
            stream.addSubscriber(new StreamSubscriber("sub1", "Test"));
            stream.stop();
            assertEquals(0, stream.getSubscriberCount());
        }
    }

    @Nested
    @DisplayName("Metrics tests")
    class MetricsTests {
        @Test
        void getMetrics_returnsNonNull() {
            RealTimeStream stream = createActiveStream();
            StreamMetrics m = stream.getMetrics();
            assertNotNull(m);
            assertEquals(0, m.getMessagesProcessed());
        }

        @Test
        void getHealth_returnsStreamHealth() {
            RealTimeStream stream = createActiveStream();
            assertNotNull(stream.getHealth());
        }
    }
}
