package com.gogidix.aiservices.aidocumentprocessingservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.request.ProcessDocumentRequest;
import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.response.DocumentProcessingResponse;
import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.response.ProcessingStatusResponse;
import com.gogidix.aiservices.aidocumentprocessingservice.application.service.DocumentProcessingService;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.*;
import com.gogidix.aiservices.aidocumentprocessingservice.shared.exception.DocumentNotFoundException;
import com.gogidix.aiservices.aidocumentprocessingservice.shared.exception.DocumentProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DocumentProcessingController.class)
@DisplayName("Document Processing Controller Interface Tests")
class DocumentProcessingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DocumentProcessingService documentProcessingService;

    private static final String DOCUMENT_URL = "https://example.com/invoice.pdf";
    private static final String JOB_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String USER_ID = "user-123";

    @Nested
    @DisplayName("Document Processing Endpoints")
    class ProcessingEndpoints {

        @Test
        @DisplayName("POST /api/v1/documents/process - Should process document")
        void shouldProcessDocument() throws Exception {
            ProcessDocumentRequest request = ProcessDocumentRequest.builder()
                    .documentUrl(DOCUMENT_URL)
                    .documentType(DocumentType.INVOICE)
                    .extractionConfig(ExtractionConfig.builder()
                            .fields(Arrays.asList("invoice_number", "amount"))
                            .extractTables(true)
                            .extractImages(false)
                            .build())
                    .build();

            DocumentProcessingResponse response = DocumentProcessingResponse.builder()
                    .processingId(JOB_ID)
                    .status(ProcessingStatus.PROCESSING)
                    .extractedData(Map.of("invoice_number", "INV-001"))
                    .confidence(0.0)
                    .pagesProcessed(0)
                    .build();

            when(documentProcessingService.processDocument(any(ProcessDocumentRequest.class), eq(USER_ID)))
                    .thenReturn(response);

            mockMvc.perform(post("/api/v1/documents/process")
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.processingId").value(JOB_ID))
                    .andExpect(jsonPath("$.status").value("PROCESSING"));

            verify(documentProcessingService).processDocument(any(ProcessDocumentRequest.class), eq(USER_ID));
        }

        @Test
        @DisplayName("POST /api/v1/documents/process - Should reject missing document URL")
        void shouldRejectMissingDocumentUrl() throws Exception {
            Map<String, Object> request = new HashMap<>();
            request.put("documentType", "INVOICE");

            mockMvc.perform(post("/api/v1/documents/process")
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verify(documentProcessingService, never()).processDocument(any(), any());
        }

        @Test
        @DisplayName("POST /api/v1/documents/process - Should reject invalid document type")
        void shouldRejectInvalidDocumentType() throws Exception {
            Map<String, Object> request = new HashMap<>();
            request.put("documentUrl", DOCUMENT_URL);
            request.put("documentType", "INVALID_TYPE");

            mockMvc.perform(post("/api/v1/documents/process")
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /api/v1/documents/process - Should require user ID header")
        void shouldRequireUserIdHeader() throws Exception {
            ProcessDocumentRequest request = ProcessDocumentRequest.builder()
                    .documentUrl(DOCUMENT_URL)
                    .documentType(DocumentType.INVOICE)
                    .build();

            mockMvc.perform(post("/api/v1/documents/process")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Status Query Endpoints")
    class StatusEndpoints {

        @Test
        @DisplayName("GET /api/v1/documents/processing/{processingId} - Should get status")
        void shouldGetProcessingStatus() throws Exception {
            ProcessingStatusResponse response = ProcessingStatusResponse.builder()
                    .processingId(JOB_ID)
                    .status(ProcessingStatus.COMPLETED)
                    .progress(100)
                    .pagesProcessed(2)
                    .confidence(0.92)
                    .extractedData(Map.of("invoice_number", "INV-001", "amount", "100.00"))
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            when(documentProcessingService.getProcessingStatus(JOB_ID)).thenReturn(response);

            mockMvc.perform(get("/api/v1/documents/processing/{processingId}", JOB_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.processingId").value(JOB_ID))
                    .andExpect(jsonPath("$.status").value("COMPLETED"))
                    .andExpect(jsonPath("$.pagesProcessed").value(2))
                    .andExpect(jsonPath("$.confidence").value(0.92))
                    .andExpect(jsonPath("$.extractedData.invoice_number").value("INV-001"));

            verify(documentProcessingService).getProcessingStatus(JOB_ID);
        }

        @Test
        @DisplayName("GET /api/v1/documents/processing/{processingId} - Should return 404 when not found")
        void shouldReturn404WhenJobNotFound() throws Exception {
            when(documentProcessingService.getProcessingStatus(JOB_ID))
                    .thenThrow(new DocumentNotFoundException("Processing job not found"));

            mockMvc.perform(get("/api/v1/documents/processing/{processingId}", JOB_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").exists());

            verify(documentProcessingService).getProcessingStatus(JOB_ID);
        }

        @Test
        @DisplayName("GET /api/v1/documents/processing/{processingId} - Should reject invalid UUID")
        void shouldRejectInvalidUuid() throws Exception {
            mockMvc.perform(get("/api/v1/documents/processing/{processingId}", "not-a-uuid"))
                    .andExpect(status().isBadRequest());

            verify(documentProcessingService, never()).getProcessingStatus(any());
        }
    }

    @Nested
    @DisplayName("Job Management Endpoints")
    class JobManagementEndpoints {

        @Test
        @DisplayName("DELETE /api/v1/documents/processing/{processingId} - Should cancel job")
        void shouldCancelJob() throws Exception {
            doNothing().when(documentProcessingService).cancelProcessing(JOB_ID);

            mockMvc.perform(delete("/api/v1/documents/processing/{processingId}", JOB_ID))
                    .andExpect(status().isNoContent());

            verify(documentProcessingService).cancelProcessing(JOB_ID);
        }

        @Test
        @DisplayName("POST /api/v1/documents/processing/{processingId}/retry - Should retry job")
        void shouldRetryJob() throws Exception {
            doNothing().when(documentProcessingService).retryProcessing(JOB_ID);

            mockMvc.perform(post("/api/v1/documents/processing/{processingId}/retry", JOB_ID))
                    .andExpect(status().isAccepted());

            verify(documentProcessingService).retryProcessing(JOB_ID);
        }
    }

    @Nested
    @DisplayName("Batch Processing Endpoints")
    class BatchEndpoints {

        @Test
        @DisplayName("POST /api/v1/documents/batch - Should process batch")
        void shouldProcessBatch() throws Exception {
            List<Map<String, Object>> documents = Arrays.asList(
                    Map.of("documentUrl", "https://example.com/doc1.pdf", "documentType", "INVOICE"),
                    Map.of("documentUrl", "https://example.com/doc2.pdf", "documentType", "INVOICE")
            );

            List<DocumentProcessingResponse> responses = Arrays.asList(
                    DocumentProcessingResponse.builder()
                            .processingId(UUID.randomUUID().toString())
                            .status(ProcessingStatus.PROCESSING)
                            .build(),
                    DocumentProcessingResponse.builder()
                            .processingId(UUID.randomUUID().toString())
                            .status(ProcessingStatus.PROCESSING)
                            .build()
            );

            when(documentProcessingService.processBatch(any(), eq(USER_ID))).thenReturn(responses);

            mockMvc.perform(post("/api/v1/documents/batch")
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(Map.of("documents", documents))))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.jobs").isArray())
                    .andExpect(jsonPath("$.jobs.length()").value(2));

            verify(documentProcessingService).processBatch(any(), eq(USER_ID));
        }

        @Test
        @DisplayName("POST /api/v1/documents/batch - Should reject oversized batch")
        void shouldRejectOversizedBatch() throws Exception {
            List<Map<String, Object>> documents = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                documents.add(Map.of("documentUrl", "https://example.com/doc" + i + ".pdf", "documentType", "INVOICE"));
            }

            when(documentProcessingService.processBatch(any(), eq(USER_ID)))
                    .thenThrow(new IllegalArgumentException("Batch size exceeds maximum"));

            mockMvc.perform(post("/api/v1/documents/batch")
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(Map.of("documents", documents))))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle document processing exception")
        void shouldHandleProcessingException() throws Exception {
            ProcessDocumentRequest request = ProcessDocumentRequest.builder()
                    .documentUrl(DOCUMENT_URL)
                    .documentType(DocumentType.INVOICE)
                    .build();

            when(documentProcessingService.processDocument(any(), eq(USER_ID)))
                    .thenThrow(new DocumentProcessingException("Unsupported format"));

            mockMvc.perform(post("/api/v1/documents/process")
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("Should handle generic exception")
        void shouldHandleGenericException() throws Exception {
            when(documentProcessingService.getProcessingStatus(JOB_ID))
                    .thenThrow(new RuntimeException("Unexpected error"));

            mockMvc.perform(get("/api/v1/documents/processing/{processingId}", JOB_ID))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error").exists());
        }

        @Test
        @DisplayName("Should return standard error format")
        void shouldReturnStandardErrorFormat() throws Exception {
            when(documentProcessingService.getProcessingStatus(JOB_ID))
                    .thenThrow(new DocumentProcessingException("Processing failed"));

            mockMvc.perform(get("/api/v1/documents/processing/{processingId}", JOB_ID))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(jsonPath("$.timestamp").exists());
        }
    }

    @Nested
    @DisplayName("Request Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should validate document URL format")
        void shouldValidateDocumentUrlFormat() throws Exception {
            Map<String, Object> request = new HashMap<>();
            request.put("documentUrl", "not-a-valid-url");
            request.put("documentType", "INVOICE");

            mockMvc.perform(post("/api/v1/documents/process")
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should validate extraction config field count")
        void shouldValidateExtractionConfig() throws Exception {
            List<String> tooManyFields = Arrays.asList("field1", "field2", "field3", "field4", "field5", "field6");

            Map<String, Object> config = new HashMap<>();
            config.put("fields", tooManyFields);
            config.put("tables", true);
            config.put("images", false);

            Map<String, Object> request = new HashMap<>();
            request.put("documentUrl", DOCUMENT_URL);
            request.put("documentType", "INVOICE");
            request.put("extractionConfig", config);

            mockMvc.perform(post("/api/v1/documents/process")
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Response Mapping Tests")
    class ResponseMappingTests {

        @Test
        @DisplayName("Should map processing response correctly")
        void shouldMapProcessingResponse() throws Exception {
            Map<String, Object> extractedData = new HashMap<>();
            extractedData.put("invoice_number", "INV-001");
            extractedData.put("amount", "100.00");
            extractedData.put("date", "2024-01-15");

            DocumentProcessingResponse response = DocumentProcessingResponse.builder()
                    .processingId(JOB_ID)
                    .status(ProcessingStatus.COMPLETED)
                    .extractedData(extractedData)
                    .confidence(0.92)
                    .pagesProcessed(2)
                    .build();

            when(documentProcessingService.processDocument(any(), eq(USER_ID))).thenReturn(response);

            ProcessDocumentRequest request = ProcessDocumentRequest.builder()
                    .documentUrl(DOCUMENT_URL)
                    .documentType(DocumentType.INVOICE)
                    .build();

            mockMvc.perform(post("/api/v1/documents/process")
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.processingId").value(JOB_ID))
                    .andExpect(jsonPath("$.status").value("COMPLETED"))
                    .andExpect(jsonPath("$.confidence").value(0.92))
                    .andExpect(jsonPath("$.pagesProcessed").value(2))
                    .andExpect(jsonPath("$.extractedData.invoice_number").value("INV-001"))
                    .andExpect(jsonPath("$.extractedData.amount").value("100.00"));
        }

        @Test
        @DisplayName("Should map status response correctly")
        void shouldMapStatusResponse() throws Exception {
            ProcessingStatusResponse response = ProcessingStatusResponse.builder()
                    .processingId(JOB_ID)
                    .status(ProcessingStatus.PROCESSING)
                    .progress(50)
                    .pagesProcessed(1)
                    .confidence(0.85)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            when(documentProcessingService.getProcessingStatus(JOB_ID)).thenReturn(response);

            mockMvc.perform(get("/api/v1/documents/processing/{processingId}", JOB_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.processingId").value(JOB_ID))
                    .andExpect(jsonPath("$.status").value("PROCESSING"))
                    .andExpect(jsonPath("$.progress").value(50))
                    .andExpect(jsonPath("$.pagesProcessed").value(1))
                    .andExpect(jsonPath("$.confidence").value(0.85));
        }
    }
}
