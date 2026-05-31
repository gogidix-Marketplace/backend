package com.gogidix.aiservices.aidocumentprocessingservice.domain.policy;

import com.gogidix.aiservices.aidocumentprocessingservice.domain.aggregate.DocumentProcessingJob;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DocumentProcessingPolicy Tests")
class DocumentProcessingPolicyTest {

    private final DocumentProcessingPolicy policy = new DocumentProcessingPolicy();

    @Nested
    @DisplayName("Document Validation Tests")
    class DocumentValidationTests {

        @Test
        @DisplayName("Should validate supported document formats")
        void shouldValidateSupportedFormats() {
            assertThat(policy.isSupportedFormat("pdf")).isTrue();
            assertThat(policy.isSupportedFormat("PDF")).isTrue();
            assertThat(policy.isSupportedFormat("docx")).isTrue();
            assertThat(policy.isSupportedFormat("txt")).isTrue();
            assertThat(policy.isSupportedFormat("png")).isTrue();
            assertThat(policy.isSupportedFormat("jpg")).isTrue();
            assertThat(policy.isSupportedFormat("jpeg")).isTrue();
        }

        @Test
        @DisplayName("Should reject unsupported formats")
        void shouldRejectUnsupportedFormats() {
            assertThat(policy.isSupportedFormat("exe")).isFalse();
            assertThat(policy.isSupportedFormat("zip")).isFalse();
            assertThat(policy.isSupportedFormat("mp4")).isFalse();
        }

        @Test
        @DisplayName("Should validate document size within limits")
        void shouldValidateDocumentSize() {
            assertThat(policy.isValidSize(10 * 1024 * 1024)).isTrue(); // 10MB
            assertThat(policy.isValidSize(50 * 1024 * 1024)).isTrue(); // 50MB - limit
            assertThat(policy.isValidSize(51 * 1024 * 1024)).isFalse(); // 51MB - over limit
        }

        @Test
        @DisplayName("Should validate URL format")
        void shouldValidateUrlFormat() {
            assertThat(policy.isValidUrl("https://example.com/doc.pdf")).isTrue();
            assertThat(policy.isValidUrl("http://example.com/doc.pdf")).isTrue();
            assertThat(policy.isValidUrl("s3://bucket/doc.pdf")).isTrue();
            assertThat(policy.isValidUrl("gs://bucket/doc.pdf")).isTrue();
            assertThat(policy.isValidUrl("invalid-url")).isFalse();
            assertThat(policy.isValidUrl("")).isFalse();
            assertThat(policy.isValidUrl(null)).isFalse();
        }

        @Test
        @DisplayName("Should validate document URL accessibility")
        void shouldValidateUrlAccessibility() {
            // For unit tests, we validate format only
            // Actual accessibility would be checked in integration tests
            assertThat(policy.isAccessibleUrl("https://example.com/doc.pdf")).isTrue();
        }
    }

    @Nested
    @DisplayName("Confidence Threshold Tests")
    class ConfidenceThresholdTests {

        @Test
        @DisplayName("Should check if confidence meets minimum threshold")
        void shouldCheckConfidenceThreshold() {
            assertThat(policy.meetsMinimumThreshold(0.8)).isTrue();
            assertThat(policy.meetsMinimumThreshold(0.7)).isTrue();
            assertThat(policy.meetsMinimumThreshold(0.69)).isFalse();
            assertThat(policy.meetsMinimumThreshold(0.5)).isFalse();
        }

        @Test
        @DisplayName("Should get minimum confidence threshold")
        void shouldGetMinimumThreshold() {
            assertThat(policy.getMinimumConfidenceThreshold()).isEqualTo(0.7);
        }

        @Test
        @DisplayName("Should check if job quality is acceptable")
        void shouldCheckJobQuality() {
            DocumentProcessingJob job = DocumentProcessingJob.create(
                    "https://example.com/doc.pdf", DocumentType.INVOICE, "user-123");
            job.startProcessing();

            List<ExtractedField> fields = Arrays.asList(
                    ExtractedField.builder().name("field1").value("value1").confidence(0.9).build(),
                    ExtractedField.builder().name("field2").value("value2").confidence(0.8).build()
            );

            job.completeProcessing(fields, 1, 0.85);

            assertThat(policy.isJobQualityAcceptable(job)).isTrue();
        }

        @Test
        @DisplayName("Should reject job with low quality")
        void shouldRejectLowQualityJob() {
            DocumentProcessingJob job = DocumentProcessingJob.create(
                    "https://example.com/doc.pdf", DocumentType.INVOICE, "user-123");
            job.startProcessing();

            List<ExtractedField> fields = Arrays.asList(
                    ExtractedField.builder().name("field1").value("value1").confidence(0.5).build()
            );

            job.completeProcessing(fields, 1, 0.5);

            assertThat(policy.isJobQualityAcceptable(job)).isFalse();
        }
    }

    @Nested
    @DisplayName("Field Validation Tests")
    class FieldValidationTests {

        @Test
        @DisplayName("Should validate required fields for invoice")
        void shouldValidateRequiredInvoiceFields() {
            List<ExtractedField> completeFields = Arrays.asList(
                    ExtractedField.builder().name("invoice_number").value("INV-001").confidence(0.9).build(),
                    ExtractedField.builder().name("amount").value("100.00").confidence(0.9).build(),
                    ExtractedField.builder().name("date").value("2024-01-15").confidence(0.9).build(),
                    ExtractedField.builder().name("vendor").value("Acme Corp").confidence(0.9).build()
            );

            assertThat(policy.hasRequiredFields(DocumentType.INVOICE, completeFields)).isTrue();

            List<ExtractedField> incompleteFields = Arrays.asList(
                    ExtractedField.builder().name("invoice_number").value("INV-001").confidence(0.9).build(),
                    ExtractedField.builder().name("amount").value("100.00").confidence(0.9).build()
            );

            assertThat(policy.hasRequiredFields(DocumentType.INVOICE, incompleteFields)).isFalse();
        }

        @Test
        @DisplayName("Should validate required fields for contract")
        void shouldValidateRequiredContractFields() {
            List<ExtractedField> completeFields = Arrays.asList(
                    ExtractedField.builder().name("contract_number").value("CTR-001").confidence(0.9).build(),
                    ExtractedField.builder().name("party_a").value("Company A").confidence(0.9).build(),
                    ExtractedField.builder().name("party_b").value("Company B").confidence(0.9).build(),
                    ExtractedField.builder().name("start_date").value("2024-01-01").confidence(0.9).build(),
                    ExtractedField.builder().name("end_date").value("2024-12-31").confidence(0.9).build()
            );

            assertThat(policy.hasRequiredFields(DocumentType.CONTRACT, completeFields)).isTrue();
        }

        @Test
        @DisplayName("Should validate field confidence levels")
        void shouldValidateFieldConfidence() {
            List<ExtractedField> highConfidenceFields = Arrays.asList(
                    ExtractedField.builder().name("field1").value("value1").confidence(0.9).build(),
                    ExtractedField.builder().name("field2").value("value2").confidence(0.8).build()
            );

            assertThat(policy.allFieldsMeetThreshold(highConfidenceFields, 0.75)).isTrue();

            List<ExtractedField> lowConfidenceFields = Arrays.asList(
                    ExtractedField.builder().name("field1").value("value1").confidence(0.9).build(),
                    ExtractedField.builder().name("field2").value("value2").confidence(0.6).build()
            );

            assertThat(policy.allFieldsMeetThreshold(lowConfidenceFields, 0.75)).isFalse();
        }
    }

    @Nested
    @DisplayName("Processing Time Tests")
    class ProcessingTimeTests {

        @Test
        @DisplayName("Should check if processing timeout exceeded")
        void shouldCheckTimeoutExceeded() {
            assertThat(policy.isProcessingTimeoutExceeded(300000)).isFalse(); // 5 minutes
            assertThat(policy.isProcessingTimeoutExceeded(301000)).isTrue(); // Over 5 minutes
        }

        @Test
        @DisplayName("Should get maximum processing timeout")
        void shouldGetMaxTimeout() {
            assertThat(policy.getMaxProcessingTimeoutMs()).isEqualTo(300000); // 5 minutes
        }

        @Test
        @DisplayName("Should calculate remaining processing time")
        void shouldCalculateRemainingTime() {
            long elapsed = 60000; // 1 minute
            long remaining = policy.getRemainingProcessingTime(elapsed);

            assertThat(remaining).isEqualTo(240000); // 4 minutes remaining
        }

        @Test
        @DisplayName("Should return zero if timeout exceeded")
        void shouldReturnZeroIfExceeded() {
            long elapsed = 400000; // Over 5 minutes
            long remaining = policy.getRemainingProcessingTime(elapsed);

            assertThat(remaining).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Document Type Detection Tests")
    class DocumentTypeDetectionTests {

        @Test
        @DisplayName("Should detect document type from filename")
        void shouldDetectFromFilename() {
            assertThat(policy.detectTypeFromFilename("invoice_2024.pdf")).isEqualTo(DocumentType.INVOICE);
            assertThat(policy.detectTypeFromFilename("contract_v1.pdf")).isEqualTo(DocumentType.CONTRACT);
            assertThat(policy.detectTypeFromFilename("annual_report.pdf")).isEqualTo(DocumentType.REPORT);
            assertThat(policy.detectTypeFromFilename("form_1040.pdf")).isEqualTo(DocumentType.FORM);
            assertThat(policy.detectTypeFromFilename("receipt.pdf")).isEqualTo(DocumentType.RECEIPT);
            assertThat(policy.detectTypeFromFilename("certificate.pdf")).isEqualTo(DocumentType.CERTIFICATE);
            assertThat(policy.detectTypeFromFilename("unknown.pdf")).isEqualTo(DocumentType.UNKNOWN);
        }

        @Test
        @DisplayName("Should return UNKNOWN for null filename")
        void shouldReturnUnknownForNull() {
            assertThat(policy.detectTypeFromFilename(null)).isEqualTo(DocumentType.UNKNOWN);
        }
    }

    @Nested
    @DisplayName("Extraction Configuration Tests")
    class ExtractionConfigTests {

        @Test
        @DisplayName("Should validate extraction configuration")
        void shouldValidateExtractionConfig() {
            ExtractionConfig validConfig = ExtractionConfig.builder()
                    .fields(Arrays.asList("field1", "field2"))
                    .extractTables(true)
                    .extractImages(false)
                    .build();

            assertThat(policy.isValidExtractionConfig(validConfig)).isTrue();
        }

        @Test
        @DisplayName("Should reject configuration with too many fields")
        void shouldRejectTooManyFields() {
            List<String> manyFields = Arrays.asList(
                    "field1", "field2", "field3", "field4", "field5", "field6"
            );

            ExtractionConfig invalidConfig = ExtractionConfig.builder()
                    .fields(manyFields)
                    .extractTables(true)
                    .extractImages(true)
                    .build();

            assertThat(policy.isValidExtractionConfig(invalidConfig)).isFalse();
        }

        @Test
        @DisplayName("Should get default extraction config for document type")
        void shouldGetDefaultConfig() {
            ExtractionConfig invoiceConfig = policy.getDefaultExtractionConfig(DocumentType.INVOICE);

            assertThat(invoiceConfig).isNotNull();
            assertThat(invoiceConfig.getFields()).contains("invoice_number", "amount", "date", "vendor");
            assertThat(invoiceConfig.isExtractTables()).isTrue();
        }
    }
}
