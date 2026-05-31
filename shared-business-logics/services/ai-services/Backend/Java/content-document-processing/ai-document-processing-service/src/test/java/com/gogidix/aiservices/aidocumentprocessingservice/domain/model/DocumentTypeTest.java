package com.gogidix.aiservices.aidocumentprocessingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DocumentType Domain Model Tests")
class DocumentTypeTest {

    @Nested
    @DisplayName("Document Type Validation Tests")
    class DocumentTypeValidationTests {

        @ParameterizedTest
        @EnumSource(DocumentType.class)
        @DisplayName("Should have valid document types")
        void shouldHaveValidDocumentTypes(DocumentType documentType) {
            assertThat(documentType).isNotNull();
            assertThat(documentType.name()).isIn("INVOICE", "CONTRACT", "REPORT", "FORM", "RECEIPT", "CERTIFICATE", "UNKNOWN");
        }

        @Test
        @DisplayName("Should parse document type from string")
        void shouldParseFromString() {
            assertThat(DocumentType.fromString("INVOICE")).isEqualTo(DocumentType.INVOICE);
            assertThat(DocumentType.fromString("CONTRACT")).isEqualTo(DocumentType.CONTRACT);
            assertThat(DocumentType.fromString("REPORT")).isEqualTo(DocumentType.REPORT);
            assertThat(DocumentType.fromString("FORM")).isEqualTo(DocumentType.FORM);
        }

        @Test
        @DisplayName("Should return UNKNOWN for invalid string")
        void shouldReturnUnknownForInvalidString() {
            assertThat(DocumentType.fromString("INVALID_TYPE")).isEqualTo(DocumentType.UNKNOWN);
            assertThat(DocumentType.fromString(null)).isEqualTo(DocumentType.UNKNOWN);
            assertThat(DocumentType.fromString("")).isEqualTo(DocumentType.UNKNOWN);
        }

        @Test
        @DisplayName("Should check if type requires OCR")
        void shouldCheckIfRequiresOcr() {
            assertThat(DocumentType.INVOICE.requiresOcr()).isTrue();
            assertThat(DocumentType.CONTRACT.requiresOcr()).isTrue();
            assertThat(DocumentType.REPORT.requiresOcr()).isTrue();
            assertThat(DocumentType.FORM.requiresOcr()).isTrue();
            assertThat(DocumentType.RECEIPT.requiresOcr()).isTrue();
            assertThat(DocumentType.CERTIFICATE.requiresOcr()).isTrue();
        }

        @Test
        @DisplayName("Should get max file size for document type")
        void shouldGetMaxFileSize() {
            assertThat(DocumentType.INVOICE.getMaxFileSizeMB()).isEqualTo(50);
            assertThat(DocumentType.CONTRACT.getMaxFileSizeMB()).isEqualTo(50);
            assertThat(DocumentType.REPORT.getMaxFileSizeMB()).isEqualTo(50);
        }

        @Test
        @DisplayName("Should get supported formats")
        void shouldGetSupportedFormats() {
            assertThat(DocumentType.INVOICE.getSupportedFormats())
                    .contains("PDF", "DOCX", "TXT", "PNG", "JPG", "JPEG");
        }
    }

    @Nested
    @DisplayName("Document Processing Priority Tests")
    class PriorityTests {

        @Test
        @DisplayName("Should have priority levels")
        void shouldHavePriorityLevels() {
            assertThat(DocumentType.INVOICE.getPriority()).isEqualTo(1);
            assertThat(DocumentType.CONTRACT.getPriority()).isEqualTo(2);
            assertThat(DocumentType.REPORT.getPriority()).isEqualTo(3);
            assertThat(DocumentType.FORM.getPriority()).isEqualTo(4);
        }

        @Test
        @DisplayName("Should compare document types by priority")
        void shouldCompareByPriority() {
            assertThat(DocumentType.INVOICE.getPriority())
                    .isLessThan(DocumentType.CONTRACT.getPriority());
            assertThat(DocumentType.CONTRACT.getPriority())
                    .isLessThan(DocumentType.REPORT.getPriority());
        }
    }
}
