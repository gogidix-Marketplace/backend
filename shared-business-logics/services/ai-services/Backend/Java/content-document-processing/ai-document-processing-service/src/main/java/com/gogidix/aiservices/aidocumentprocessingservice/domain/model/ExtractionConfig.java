package com.gogidix.aiservices.aidocumentprocessingservice.domain.model;

import lombok.Builder;

import java.util.List;

@Builder
public class ExtractionConfig {
    private final List<String> fields;
    private final boolean extractTables;
    private final boolean extractImages;

    public List<String> getFields() {
        return fields != null ? fields : List.of();
    }

    public boolean isExtractTables() {
        return extractTables;
    }

    public boolean isExtractImages() {
        return extractImages;
    }
}
