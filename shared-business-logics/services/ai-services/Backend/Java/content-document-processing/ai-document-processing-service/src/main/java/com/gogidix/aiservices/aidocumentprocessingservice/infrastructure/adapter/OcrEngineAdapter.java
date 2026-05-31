package com.gogidix.aiservices.aidocumentprocessingservice.infrastructure.adapter;

import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.ExtractedField;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.ExtractionConfig;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.port.out.OcrEnginePort;
import com.gogidix.aiservices.aidocumentprocessingservice.infrastructure.config.OcrServiceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OcrEngineAdapter implements OcrEnginePort {
    private final RestTemplate restTemplate;
    private final OcrServiceProperties properties;

    @Override
    public List<ExtractedField> processDocument(String documentUrl, ExtractionConfig config) {
        String serviceUrl = properties.getServiceUrl() + "/extract";

        Map<String, Object> request = buildRequest(documentUrl, config);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(serviceUrl, request, Map.class);

            if (response == null) {
                return List.of();
            }

            return mapResponseToFields(response);
        } catch (Exception e) {
            log.error("OCR processing failed for document: {}", documentUrl, e);
            throw new RuntimeException("OCR processing failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isHealthy() {
        try {
            String healthUrl = properties.getServiceUrl() + properties.getHealthEndpoint();
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(healthUrl, Map.class);
            return response != null && "UP".equals(response.get("status"));
        } catch (Exception e) {
            log.warn("OCR service health check failed", e);
            return false;
        }
    }

    private Map<String, Object> buildRequest(String documentUrl, ExtractionConfig config) {
        Map<String, Object> request = new HashMap<>();
        request.put("documentUrl", documentUrl);
        request.put("timeoutMs", properties.getTimeoutMs());

        if (config != null) {
            request.put("fields", config.getFields() != null ? config.getFields() : List.of());
            request.put("extractTables", config.isExtractTables());
            request.put("extractImages", config.isExtractImages());
        }

        return request;
    }

    @SuppressWarnings("unchecked")
    private List<ExtractedField> mapResponseToFields(Map<String, Object> response) {
        List<Map<String, Object>> fields = (List<Map<String, Object>>) response.get("fields");

        if (fields == null) {
            return List.of();
        }

        return fields.stream()
                .map(this::mapToField)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private ExtractedField mapToField(Map<String, Object> fieldData) {
        try {
            String name = (String) fieldData.get("name");
            String value = (String) fieldData.get("value");
            Number confidenceNum = (Number) fieldData.get("confidence");
            double confidence = confidenceNum != null ? confidenceNum.doubleValue() : 0.0;

            Map<String, Object> metadata = new HashMap<>();

            Map<String, Object> boundingBox = (Map<String, Object>) fieldData.get("boundingBox");
            if (boundingBox != null) {
                metadata.put("position", boundingBox);
            }

            Object page = fieldData.get("page");
            if (page != null) {
                metadata.put("page", page);
            }

            return new ExtractedField(name, value, confidence, metadata);
        } catch (Exception e) {
            log.warn("Failed to map field data: {}", fieldData, e);
            return null;
        }
    }
}
