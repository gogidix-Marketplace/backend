package com.gogidix.aiservices.aidocumentprocessingservice.domain.port.out;

import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.ExtractedField;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.ExtractionConfig;

import java.util.List;

public interface OcrEnginePort {
    List<ExtractedField> processDocument(String documentUrl, ExtractionConfig config);
    boolean isHealthy();
}
