package com.gogidix.aiservices.aidocumentprocessingservice.domain.event;

import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.DocumentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ProcessingStartedEvent Domain Event Tests")
class ProcessingStartedEventTest {

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

            ProcessingStartedEvent event = new ProcessingStartedEvent(
                    JOB_ID, DOCUMENT_URL, DocumentType.INVOICE, USER_ID, timestamp
            );

            assertThat(event.jobId()).isEqualTo(JOB_ID);
            assertThat(event.documentUrl()).isEqualTo(DOCUMENT_URL);
            assertThat(event.documentType()).isEqualTo(DocumentType.INVOICE);
            assertThat(event.userId()).isEqualTo(USER_ID);
            assertThat(event.timestamp()).isEqualTo(timestamp);
        }

        @Test
        @DisplayName("Should set current timestamp when null")
        void shouldSetCurrentTimestampWhenNull() {
            Instant before = Instant.now();

            ProcessingStartedEvent event = new ProcessingStartedEvent(
                    JOB_ID, DOCUMENT_URL, DocumentType.INVOICE, USER_ID, null
            );

            Instant after = Instant.now();

            assertThat(event.timestamp()).isNotNull();
            assertThat(event.timestamp()).isBetween(before, after);
        }

        @Test
        @DisplayName("Should create event for contract")
        void shouldCreateEventForContract() {
            ProcessingStartedEvent event = new ProcessingStartedEvent(
                    JOB_ID, DOCUMENT_URL, DocumentType.CONTRACT, USER_ID, null
            );

            assertThat(event.documentType()).isEqualTo(DocumentType.CONTRACT);
        }

        @Test
        @DisplayName("Should create event for report")
        void shouldCreateEventForReport() {
            ProcessingStartedEvent event = new ProcessingStartedEvent(
                    JOB_ID, DOCUMENT_URL, DocumentType.REPORT, USER_ID, null
            );

            assertThat(event.documentType()).isEqualTo(DocumentType.REPORT);
        }

        @Test
        @DisplayName("Should create event for form")
        void shouldCreateEventForForm() {
            ProcessingStartedEvent event = new ProcessingStartedEvent(
                    JOB_ID, DOCUMENT_URL, DocumentType.FORM, USER_ID, null
            );

            assertThat(event.documentType()).isEqualTo(DocumentType.FORM);
        }
    }

    @Nested
    @DisplayName("Event Properties Tests")
    class EventPropertiesTests {

        @Test
        @DisplayName("Should expose job ID")
        void shouldExposeJobId() {
            ProcessingStartedEvent event = new ProcessingStartedEvent(
                    JOB_ID, DOCUMENT_URL, DocumentType.INVOICE, USER_ID, null
            );

            assertThat(event.jobId()).isNotNull();
            assertThat(event.jobId()).isEqualTo(JOB_ID);
        }

        @Test
        @DisplayName("Should expose document URL")
        void shouldExposeDocumentUrl() {
            ProcessingStartedEvent event = new ProcessingStartedEvent(
                    JOB_ID, DOCUMENT_URL, DocumentType.INVOICE, USER_ID, null
            );

            assertThat(event.documentUrl()).isEqualTo(DOCUMENT_URL);
        }

        @Test
        @DisplayName("Should expose user ID")
        void shouldExposeUserId() {
            ProcessingStartedEvent event = new ProcessingStartedEvent(
                    JOB_ID, DOCUMENT_URL, DocumentType.INVOICE, USER_ID, null
            );

            assertThat(event.userId()).isEqualTo(USER_ID);
        }

        @Test
        @DisplayName("Should expose document type")
        void shouldExposeDocumentType() {
            ProcessingStartedEvent event = new ProcessingStartedEvent(
                    JOB_ID, DOCUMENT_URL, DocumentType.RECEIPT, USER_ID, null
            );

            assertThat(event.documentType()).isEqualTo(DocumentType.RECEIPT);
        }
    }

    @Nested
    @DisplayName("Event Equality Tests")
    class EventEqualityTests {

        @Test
        @DisplayName("Should be equal when all properties match")
        void shouldBeEqualWhenAllPropertiesMatch() {
            Instant timestamp = Instant.now();

            ProcessingStartedEvent event1 = new ProcessingStartedEvent(
                    JOB_ID, DOCUMENT_URL, DocumentType.INVOICE, USER_ID, timestamp
            );
            ProcessingStartedEvent event2 = new ProcessingStartedEvent(
                    JOB_ID, DOCUMENT_URL, DocumentType.INVOICE, USER_ID, timestamp
            );

            assertThat(event1).isEqualTo(event2);
        }

        @Test
        @DisplayName("Should not be equal when job IDs differ")
        void shouldNotBeEqualWhenJobIdsDiffer() {
            Instant timestamp = Instant.now();

            ProcessingStartedEvent event1 = new ProcessingStartedEvent(
                    JOB_ID, DOCUMENT_URL, DocumentType.INVOICE, USER_ID, timestamp
            );
            ProcessingStartedEvent event2 = new ProcessingStartedEvent(
                    UUID.randomUUID(), DOCUMENT_URL, DocumentType.INVOICE, USER_ID, timestamp
            );

            assertThat(event1).isNotEqualTo(event2);
        }
    }
}
