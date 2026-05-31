package com.gogidix.aiservices.aidocumentprocessingservice.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ProcessingCompletedEvent Domain Event Tests")
class ProcessingCompletedEventTest {

    private static final UUID JOB_ID = UUID.randomUUID();
    private static final String DOCUMENT_URL = "https://example.com/invoice.pdf";
    private static final String USER_ID = "user-123";

    @Nested
    @DisplayName("Event Creation Tests")
    class EventCreationTests {

        @Test
        @DisplayName("Should create event with all parameters")
        void shouldCreateEventWithAllParameters() {
            Instant timestamp = Instant.now();

            ProcessingCompletedEvent event = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 5, 2, 0.92, USER_ID, timestamp
            );

            assertThat(event.jobId()).isEqualTo(JOB_ID);
            assertThat(event.documentUrl()).isEqualTo(DOCUMENT_URL);
            assertThat(event.fieldsCount()).isEqualTo(5);
            assertThat(event.pagesProcessed()).isEqualTo(2);
            assertThat(event.confidence()).isEqualTo(0.92);
            assertThat(event.userId()).isEqualTo(USER_ID);
            assertThat(event.timestamp()).isEqualTo(timestamp);
        }

        @Test
        @DisplayName("Should set current timestamp when null")
        void shouldSetCurrentTimestampWhenNull() {
            Instant before = Instant.now();

            ProcessingCompletedEvent event = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 3, 1, 0.88, USER_ID, null
            );

            Instant after = Instant.now();

            assertThat(event.timestamp()).isNotNull();
            assertThat(event.timestamp()).isBetween(before, after);
        }

        @Test
        @DisplayName("Should handle zero fields")
        void shouldHandleZeroFields() {
            ProcessingCompletedEvent event = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 0, 1, 0.5, USER_ID, null
            );

            assertThat(event.fieldsCount()).isZero();
        }

        @Test
        @DisplayName("Should handle single page")
        void shouldHandleSinglePage() {
            ProcessingCompletedEvent event = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 3, 1, 0.9, USER_ID, null
            );

            assertThat(event.pagesProcessed()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should handle low confidence")
        void shouldHandleLowConfidence() {
            ProcessingCompletedEvent event = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 2, 1, 0.65, USER_ID, null
            );

            assertThat(event.confidence()).isEqualTo(0.65);
        }

        @Test
        @DisplayName("Should handle perfect confidence")
        void shouldHandlePerfectConfidence() {
            ProcessingCompletedEvent event = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 4, 2, 1.0, USER_ID, null
            );

            assertThat(event.confidence()).isEqualTo(1.0);
        }
    }

    @Nested
    @DisplayName("Event Properties Tests")
    class EventPropertiesTests {

        @Test
        @DisplayName("Should expose job ID")
        void shouldExposeJobId() {
            ProcessingCompletedEvent event = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 3, 1, 0.9, USER_ID, null
            );

            assertThat(event.jobId()).isEqualTo(JOB_ID);
        }

        @Test
        @DisplayName("Should expose document URL")
        void shouldExposeDocumentUrl() {
            ProcessingCompletedEvent event = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 3, 1, 0.9, USER_ID, null
            );

            assertThat(event.documentUrl()).isEqualTo(DOCUMENT_URL);
        }

        @Test
        @DisplayName("Should expose user ID")
        void shouldExposeUserId() {
            ProcessingCompletedEvent event = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 3, 1, 0.9, USER_ID, null
            );

            assertThat(event.userId()).isEqualTo(USER_ID);
        }

        @Test
        @DisplayName("Should expose fields count")
        void shouldExposeFieldsCount() {
            ProcessingCompletedEvent event = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 7, 2, 0.9, USER_ID, null
            );

            assertThat(event.fieldsCount()).isEqualTo(7);
        }

        @Test
        @DisplayName("Should expose pages processed")
        void shouldExposePagesProcessed() {
            ProcessingCompletedEvent event = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 3, 5, 0.9, USER_ID, null
            );

            assertThat(event.pagesProcessed()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should expose confidence")
        void shouldExposeConfidence() {
            ProcessingCompletedEvent event = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 3, 1, 0.85, USER_ID, null
            );

            assertThat(event.confidence()).isEqualTo(0.85);
        }
    }

    @Nested
    @DisplayName("Event Equality Tests")
    class EventEqualityTests {

        @Test
        @DisplayName("Should be equal when all properties match")
        void shouldBeEqualWhenAllPropertiesMatch() {
            Instant timestamp = Instant.now();

            ProcessingCompletedEvent event1 = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 3, 1, 0.9, USER_ID, timestamp
            );
            ProcessingCompletedEvent event2 = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 3, 1, 0.9, USER_ID, timestamp
            );

            assertThat(event1).isEqualTo(event2);
        }

        @Test
        @DisplayName("Should not be equal when confidence differs")
        void shouldNotBeEqualWhenConfidenceDiffers() {
            Instant timestamp = Instant.now();

            ProcessingCompletedEvent event1 = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 3, 1, 0.9, USER_ID, timestamp
            );
            ProcessingCompletedEvent event2 = new ProcessingCompletedEvent(
                    JOB_ID, DOCUMENT_URL, 3, 1, 0.8, USER_ID, timestamp
            );

            assertThat(event1).isNotEqualTo(event2);
        }
    }
}
