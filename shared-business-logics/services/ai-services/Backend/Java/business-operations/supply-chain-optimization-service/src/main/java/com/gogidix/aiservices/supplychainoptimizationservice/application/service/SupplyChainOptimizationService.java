package com.gogidix.aiservices.supplychainoptimizationservice.application.service;

import com.gogidix.aiservices.supplychainoptimizationservice.application.dto.request.CreateOptimizationRequest;
import com.gogidix.aiservices.supplychainoptimizationservice.application.dto.response.OptimizationRequestResponse;
import com.gogidix.aiservices.supplychainoptimizationservice.application.dto.response.OptimizationResultResponse;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.aggregate.OptimizationRequest;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.*;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.port.out.OptimizationEnginePort;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.port.out.OptimizationRequestRepository;
import com.gogidix.aiservices.supplychainoptimizationservice.shared.exception.OptimizationRequestNotFoundException;
import com.gogidix.aiservices.supplychainoptimizationservice.shared.exception.SupplyChainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplyChainOptimizationService {

    private final OptimizationRequestRepository requestRepository;
    private final OptimizationEnginePort optimizationEngine;

    public OptimizationRequestResponse createRequest(CreateOptimizationRequest request) {
        OptimizationRequest optRequest = OptimizationRequest.builder()
                .tenantId(request.tenantId())
                .type(request.type())
                .parameters(request.parameters())
                .priority(request.priority())
                .build();

        // Validate parameters
        if (!optimizationEngine.validateParameters(request.type(), request.parameters())) {
            throw new SupplyChainException("Invalid parameters for optimization type: " + request.type());
        }

        OptimizationRequest saved = requestRepository.save(optRequest);
        return toResponse(saved);
    }

    public OptimizationRequestResponse getRequest(String requestId) {
        OptimizationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new OptimizationRequestNotFoundException(requestId));
        return toResponse(request);
    }

    public List<OptimizationRequestResponse> getRequestsByTenant(String tenantId) {
        return requestRepository.findByTenantId(tenantId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<OptimizationRequestResponse> getRequestsByStatus(OptimizationStatus status) {
        return requestRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public OptimizationRequestResponse processRequest(String requestId) {
        OptimizationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new OptimizationRequestNotFoundException(requestId));

        request.startProcessing();
        requestRepository.save(request);

        try {
            OptimizationResult result = optimizationEngine.generateOptimization(request);
            request.completeWithResult(result);
        } catch (Exception e) {
            request.failWithError(e.getMessage());
        }

        OptimizationRequest saved = requestRepository.save(request);
        return toResponse(saved);
    }

    public OptimizationResultResponse getResult(String requestId) {
        OptimizationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new OptimizationRequestNotFoundException(requestId));

        if (request.getResult() == null) {
            throw new SupplyChainException("No result available for request: " + requestId);
        }

        return toResultResponse(request.getResult());
    }

    public void cancelRequest(String requestId) {
        OptimizationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new OptimizationRequestNotFoundException(requestId));

        request.cancel();
        requestRepository.save(request);
    }

    public void deleteRequest(String requestId) {
        OptimizationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new OptimizationRequestNotFoundException(requestId));

        requestRepository.delete(requestId);
    }

    private OptimizationRequestResponse toResponse(OptimizationRequest request) {
        return OptimizationRequestResponse.builder()
                .requestId(request.getRequestId().toString())
                .tenantId(request.getTenantId())
                .type(request.getType())
                .status(request.getStatus())
                .parameters(request.getParameters())
                .priority(request.getPriority())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .completedAt(request.getCompletedAt())
                .errorMessage(request.getErrorMessage())
                .build();
    }

    private OptimizationResultResponse toResultResponse(OptimizationResult result) {
        return OptimizationResultResponse.builder()
                .resultId(result.getResultId().toString())
                .requestId(result.getRequestId().toString())
                .type(result.getType())
                .metrics(result.getMetrics())
                .recommendations(result.getRecommendations())
                .data(result.getData())
                .confidenceScore(result.getConfidenceScore())
                .generatedAt(result.getGeneratedAt())
                .summary(result.getSummary())
                .build();
    }
}
