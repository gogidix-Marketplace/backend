package com.gogidix.aiservices.aidocumentprocessingservice.interfaces.rest;

import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.response.DocumentProcessingResponse;

import java.util.List;

public record BatchProcessingResponse(
        String message,
        int totalJobs,
        List<DocumentProcessingResponse> jobs
) {
}
