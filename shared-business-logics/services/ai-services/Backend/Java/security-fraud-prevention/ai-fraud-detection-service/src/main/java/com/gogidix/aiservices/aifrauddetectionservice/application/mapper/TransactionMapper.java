package com.gogidix.aiservices.aifrauddetectionservice.application.mapper;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.TransactionDTO;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Mapper for Transaction related entities and DTOs.
 * Handles conversion between domain model and DTO.
 */
@Component
public class TransactionMapper {

    /**
     * Converts domain model to DTO.
     */
    public TransactionDTO toDto(Transaction domain) {
        if (domain == null) {
            return null;
        }

        return TransactionDTO.builder()
                .id(domain.getTransactionId())
                .transactionId(domain.getTransactionId())
                .userId(domain.getUserId())
                .tenantId(domain.getTenantId())
                .amount(domain.getAmount())
                .merchant(domain.getMerchant())
                .timestamp(domain.getTimestamp())
                .currency(domain.getCurrency())
                .metadata(domain.getMetadata())
                .build();
    }

    /**
     * Converts DTO to domain model.
     */
    public Transaction toEntity(TransactionDTO dto) {
        if (dto == null) {
            return null;
        }

        return Transaction.builder()
                .transactionId(dto.getTransactionId())
                .userId(dto.getUserId())
                .tenantId(dto.getTenantId())
                .amount(dto.getAmount())
                .merchant(dto.getMerchant())
                .timestamp(dto.getTimestamp())
                .currency(dto.getCurrency())
                .metadata(dto.getMetadata())
                .build();
    }

    /**
     * Converts list of domain models to DTOs.
     */
    public List<TransactionDTO> toDtoList(List<Transaction> domains) {
        if (domains == null) {
            return List.of();
        }
        return domains.stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Converts list of DTOs to domain models.
     */
    public List<Transaction> toEntityList(List<TransactionDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream()
                .map(this::toEntity)
                .toList();
    }

    /**
     * Converts AnalyzeTransactionRequest to domain model.
     */
    public Transaction fromRequest(com.gogidix.aiservices.aifrauddetectionservice.application.dto.request.AnalyzeTransactionRequest request) {
        if (request == null) {
            return null;
        }

        return Transaction.builder()
                .transactionId(request.getTransactionId())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .merchant(request.getMerchant())
                .timestamp(request.getTimestamp())
                .currency(request.getCurrency())
                .metadata(request.getMetadata())
                .build();
    }

    /**
     * Creates a TransactionDTO from analysis result.
     */
    public TransactionDTO fromAnalysisResult(com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult analysisResult,
                                              Transaction transaction) {
        if (analysisResult == null || transaction == null) {
            return null;
        }

        return TransactionDTO.builder()
                .id(transaction.getTransactionId())
                .transactionId(transaction.getTransactionId())
                .userId(transaction.getUserId())
                .tenantId(transaction.getTenantId())
                .amount(transaction.getAmount())
                .merchant(transaction.getMerchant())
                .timestamp(transaction.getTimestamp())
                .currency(transaction.getCurrency())
                .riskLevel(analysisResult.getRiskLevel())
                .fraudAnalysisId(analysisResult.getAnalysisId())
                .metadata(transaction.getMetadata())
                .build();
    }
}
