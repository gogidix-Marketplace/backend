package com.gogidix.aiservices.aidocumentprocessingservice.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ProcessingFailedEvent Domain Event Tests")
class ProcessingFailedEventTest {

    private static final UUID JOB_ID = UUID.randomUUID();
    private static final String DOCUMENT_URL = "https://example.com/invoice.pdf";
    private static final String USER_ID = "user-123";
    private static final String ERROR_MESSAGE = "OCR processing failed: unable to read document";

    @Nested
    @DisplayName("Event Creation Tests")
    class EventCreationTests {

        @Test
        @DisplayName("Should create event with all parameters")
        void shouldCreateEventWithAllParameters() {
            Instant timestamp = Instant.now();

            ProcessingFailedEvent event = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, ERROR_MESSAGE, USER_ID, timestamp
            );

            assertThat(event.jobId()).isEqualTo(JOB_ID);
            assertThat(event.documentUrl()).isEqualTo(DOCUMENT_URL);
            assertThat(event.errorMessage()).isEqualTo(ERROR_MESSAGE);
            assertThat(event.userId()).isEqualTo(USER_ID);
            assertThat(event.timestamp()).isEqualTo(timestamp);
        }

        @Test
        @DisplayName("Should set current timestamp when null")
        void shouldSetCurrentTimestampWhenNull() {
            Instant before = Instant.now();

            ProcessingFailedEvent event = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, ERROR_MESSAGE, USER_ID, null
            );

            Instant after = Instant.now();

            assertThat(event.timestamp()).isNotNull();
            assertThat(event.timestamp()).isBetween(before, after);
        }

        @Test
        @DisplayName("Should handle empty error message")
        void shouldHandleEmptyErrorMessage() {
            ProcessingFailedEvent event = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, "", USER_ID, null
            );

            assertThat(event.errorMessage()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null error message")
        void shouldHandleNullErrorMessage() {
            ProcessingFailedEvent event = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, null, USER_ID, null
            );

            assertThat(event.errorMessage()).isNull();
        }

        @Test
        @DisplayName("Should handle long error message")
        void shouldHandleLongErrorMessage() {
            String longMessage = "Error: ".repeat(100);

            ProcessingFailedEvent event = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, longMessage, USER_ID, null
            );

            assertThat(event.errorMessage()).hasSizeGreaterThan(500);
        }
    }

    @Nested
    @DisplayName("Event Properties Tests")
    class EventPropertiesTests {

        @Test
        @DisplayName("Should expose job ID")
        void shouldExposeJobId() {
            ProcessingFailedEvent event = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, ERROR_MESSAGE, USER_ID, null
            );

            assertThat(event.jobId()).isEqualTo(JOB_ID);
        }

        @Test
        @DisplayName("Should expose document URL")
        void shouldExposeDocumentUrl() {
            ProcessingFailedEvent event = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, ERROR_MESSAGE, USER_ID, null
            );

            assertThat(event.documentUrl()).isEqualTo(DOCUMENT_URL);
        }

        @Test
        @DisplayName("Should expose user ID")
        void shouldExposeUserId() {
            ProcessingFailedEvent event = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, ERROR_MESSAGE, USER_ID, null
            );

            assertThat(event.userId()).isEqualTo(USER_ID);
        }

        @Test
        @DisplayName("Should expose error message")
        void shouldExposeErrorMessage() {
            ProcessingFailedEvent event = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, ERROR_MESSAGE, USER_ID, null
            );

            assertThat(event.errorMessage()).isEqualTo(ERROR_MESSAGE);
        }

        @Test
        @DisplayName("Should expose timestamp")
        void shouldExposeTimestamp() {
            Instant timestamp = Instant.now();

            ProcessingFailedEvent event = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, ERROR_MESSAGE, USER_ID, timestamp
            );

            assertThat(event.timestamp()).isEqualTo(timestamp);
        }
    }

    @Nested
    @DisplayName("Error Message Tests")
    class ErrorMessageTests {

        @Test
        @DisplayName("Should contain timeout error")
        void shouldContainTimeoutError() {
            ProcessingFailedEvent event = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, "Processing timeout after 5 minutes", USER_ID, null
            );

            assertThat(event.errorMessage()).contains("timeout");
        }

        @Test
        @DisplayName("Should contain format error")
        void shouldContainFormatError() {
            ProcessingFailedEvent event = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, "Unsupported document format", USER_ID, null
            );

            assertThat(event.errorMessage()).contains("Unsupported");
        }

        @Test
        @DisplayName("Should contain size error")
        void shouldContainSizeError() {
            ProcessingFailedEvent event = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, "Document exceeds maximum size", USER_ID, null
            );

            assertThat(event.errorMessage()).contains("exceeds maximum size");
        }
    }

    @Nested
    @DisplayName("Event Equality Tests")
    class EventEqualityTests {

        @Test
        @DisplayName("Should be equal when all properties match")
        void shouldBeEqualWhenAllPropertiesMatch() {
            Instant timestamp = Instant.now();

            ProcessingFailedEvent event1 = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, ERROR_MESSAGE, USER_ID, timestamp
            );
            ProcessingFailedEvent event2 = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, ERROR_MESSAGE, USER_ID, timestamp
            );

            assertThat(event1).isEqualTo(event2);
        }

        @Test
        @DisplayName("Should not be equal when error messages differ")
        void shouldNotBeEqualWhenErrorMessagesDiffer() {
            Instant timestamp = Instant.now();

            ProcessingFailedEvent event1 = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, "Error 1", USER_ID, timestamp
            );
            ProcessingFailedEvent event2 = new ProcessingFailedEvent(
                    JOB_ID, DOCUMENT_URL, "Error 2", USER_ID, timestamp
            );

            assertThat(event1).isNotEqualTo(event2);
        }
    }
}
