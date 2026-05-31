package com.gogidix.aiservices.aifeatureextractionservice.application.service;

import com.gogidix.aiservices.aifeatureextractionservice.application.dto.ExtractFeaturesRequestDto;
import com.gogidix.aiservices.aifeatureextractionservice.application.dto.FeatureSchemaResponseDto;
import com.gogidix.aiservices.aifeatureextractionservice.application.dto.FeatureSetResponseDto;
import com.gogidix.aiservices.aifeatureextractionservice.application.mapper.FeatureSetMapper;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.ExtractionMethod;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureExtractionStatus;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureSet;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureValue;
import com.gogidix.aiservices.aifeatureextractionservice.domain.port.in.FeatureExtractionServicePort;
import com.gogidix.aiservices.aifeatureextractionservice.domain.port.out.FeatureSetRepositoryPort;
import com.gogidix.aiservices.aifeatureextractionservice.domain.port.out.FeatureStoreClientPort;
import com.gogidix.aiservices.aifeatureextractionservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service for feature extraction operations.
 * Implements the input port and orchestrates domain logic.
 */
@Service
@Transactional
public class FeatureExtractionApplicationService implements FeatureExtractionServicePort {

    private static final Logger log = LoggerFactory.getLogger(FeatureExtractionApplicationService.class);

    private final FeatureSetRepositoryPort featureSetRepository;
    private final FeatureStoreClientPort featureStoreClient;
    private final FeatureSetMapper featureSetMapper;

    public FeatureExtractionApplicationService(
            FeatureSetRepositoryPort featureSetRepository,
            FeatureStoreClientPort featureStoreClient,
            FeatureSetMapper featureSetMapper) {
        this.featureSetRepository = featureSetRepository;
        this.featureStoreClient = featureStoreClient;
        this.featureSetMapper = featureSetMapper;
    }

    @Override
    public FeatureSetResponseDto extractFeatures(ExtractFeaturesRequestDto request) {
        log.info("Starting feature extraction for dataSource: {}", request.dataSource());

        FeatureSet featureSet = new FeatureSet(
                "tenant-" + UUID.randomUUID().toString().substring(0, 8),
                request.dataSource(),
                request.methods()
        );
        featureSet.setNormalized(request.normalize());
        featureSet.validate();

        featureSet = featureSetRepository.save(featureSet);
        featureSet.markAsProcessing();

        try {
            List<FeatureValue> extractedFeatures = performExtraction(request.features(), request.methods());
            featureSet.completeWith(extractedFeatures);

            if (request.normalize()) {
                featureSet.setNormalized(true);
            }

            featureSet = featureSetRepository.save(featureSet);

            if (featureStoreClient != null) {
                featureStoreClient.storeFeatures(
                        featureSet.getId(),
                        featureSet.getTenantId(),
                        extractedFeatures
                );
            }

            log.info("Feature extraction completed successfully for featureSetId: {}", featureSet.getId());
        } catch (Exception e) {
            log.error("Feature extraction failed for featureSetId: {}", featureSet.getId(), e);
            featureSet.failWith(e.getMessage());
            featureSet = featureSetRepository.save(featureSet);
        }

        return featureSetMapper.toResponseDto(featureSet);
    }

    @Override
    @Transactional(readOnly = true)
    public FeatureSchemaResponseDto getFeatureSchema(String featureSetId, String tenantId) {
        FeatureSet featureSet = featureSetRepository.findByIdAndTenantId(featureSetId, tenantId)
                .orElseThrow(() -> new ValidationException("Feature set not found"));

        return featureSetMapper.toSchemaDto(featureSet);
    }

    @Override
    @Transactional(readOnly = true)
    public FeatureSetResponseDto getFeatureSet(String featureSetId, String tenantId) {
        FeatureSet featureSet = featureSetRepository.findByIdAndTenantId(featureSetId, tenantId)
                .orElseThrow(() -> new ValidationException("Feature set not found"));

        return featureSetMapper.toResponseDto(featureSet);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeatureSetResponseDto> listFeatureSets(String tenantId, String dataSource, String status, int page, int size) {
        List<FeatureSet> featureSets = featureSetRepository.findByTenantId(tenantId, page, size);

        return featureSets.stream()
                .filter(fs -> dataSource == null || dataSource.equals(fs.getDataSource()))
                .filter(fs -> status == null || status.equals(fs.getStatus().name()))
                .map(featureSetMapper::toResponseDto)
                .toList();
    }

    @Override
    public void deleteFeatureSet(String featureSetId, String tenantId) {
        FeatureSet featureSet = featureSetRepository.findByIdAndTenantId(featureSetId, tenantId)
                .orElseThrow(() -> new ValidationException("Feature set not found"));

        if (featureSet.getStatus() == FeatureExtractionStatus.PROCESSING) {
            throw new IllegalStateException("Cannot delete feature set that is currently processing");
        }

        featureSetRepository.deleteById(featureSetId);
        log.info("Deleted feature set: {}", featureSetId);
    }

    private List<FeatureValue> performExtraction(List<String> featureNames, List<ExtractionMethod> methods) {
        return featureNames.stream()
                .map(name -> switch (methods.get(0)) {
                    case TFIDF -> FeatureValue.numeric(name, Math.random());
                    case PCA -> FeatureValue.numeric(name + "_pca", Math.random());
                    case AUTOENCODER -> FeatureValue.numeric(name + "_ae", Math.random());
                    default -> FeatureValue.numeric(name, Math.random());
                })
                .toList();
    }
}
