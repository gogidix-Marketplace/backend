package com.gogidix.aiservices.aidocumentprocessingservice.application.service;
import org.junit.jupiter.api.*;
import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.request.*;
import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.response.*;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.aggregate.*;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.*;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.port.out.*;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.policy.DocumentProcessingPolicy;
import com.gogidix.aiservices.aidocumentprocessingservice.shared.exception.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.*;

class DocumentProcessingServiceTest {
    private DocumentProcessingRepository repo; private OcrEnginePort ocr; private EventPublisherPort events; private DocumentProcessingPolicy policy; private DocumentProcessingService svc;
    @BeforeEach void setup() { repo = mock(DocumentProcessingRepository.class); ocr = mock(OcrEnginePort.class); events = mock(EventPublisherPort.class); policy = new DocumentProcessingPolicy(); svc = new DocumentProcessingService(repo, ocr, events, policy); }

    @Test void processDocument() {
        when(ocr.processDocument(anyString(), any())).thenReturn(List.of());
        when(repo.save(any())).thenAnswer(a -> a.getArgument(0));
        var req = new ProcessDocumentRequest("http://doc.pdf", DocumentType.INVOICE, null);
        var r = svc.processDocument(req, "user1");
        assertThat(r).isNotNull();
    }

    @Test void getProcessingStatus() {
        var job = DocumentProcessingJob.create("http://doc.pdf", DocumentType.INVOICE, "u1");
        when(repo.findById(job.getJobId().toString())).thenReturn(Optional.of(job));
        var r = svc.getProcessingStatus(job.getJobId().toString());
        assertThat(r).isNotNull();
    }

    @Test void getProcessingStatusNotFound() {
        when(repo.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> svc.getProcessingStatus("x")).isInstanceOf(Exception.class);
    }

    @Test void validateJob() {
        var job = DocumentProcessingJob.create("http://doc.pdf", DocumentType.INVOICE, "u1");
        var r = svc.validateJob(job);
        assertThat(r).isNotNull();
    }
}
