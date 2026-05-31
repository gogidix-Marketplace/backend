package com.gogidix.aiservices.aifeaturestoreservice.application.service;

import com.gogidix.aiservices.aifeaturestoreservice.application.dto.*;
import com.gogidix.aiservices.aifeaturestoreservice.application.mapper.FeatureDefinitionMapper;
import com.gogidix.aiservices.aifeaturestoreservice.domain.model.*;
import com.gogidix.aiservices.aifeaturestoreservice.domain.port.in.FeatureStoreServicePort;
import com.gogidix.aiservices.aifeaturestoreservice.domain.port.out.FeatureDefinitionRepositoryPort;
import com.gogidix.aiservices.aifeaturestoreservice.domain.port.out.FeatureValueRepositoryPort;
import com.gogidix.aiservices.aifeaturestoreservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Application service for feature store operations.
 */
@Service
@Transactional
public class FeatureStoreApplicationService implements FeatureStoreServicePort {

    private static final Logger log = LoggerFactory.getLogger(FeatureStoreApplicationService.class);

    private final FeatureDefinitionRepositoryPort definitionRepository;
    private final FeatureValueRepositoryPort valueRepository;
    private final FeatureDefinitionMapper mapper;

    public FeatureStoreApplicationService(
            FeatureDefinitionRepositoryPort definitionRepository,
            FeatureValueRepositoryPort valueRepository,
            FeatureDefinitionMapper mapper) {
        this.definitionRepository = definitionRepository;
        this.valueRepository = valueRepository;
        this.mapper = mapper;
    }

    @Override
    public StoreFeaturesResponseDto storeFeatures(StoreFeaturesRequestDto request) {
        log.info("Storing features for feature: {}", request.featureName());

        String tenantId = "tenant-" + java.util.UUID.randomUUID().toString().substring(0, 8);

        FeatureDefinition definition = definitionRepository
                .findByFeatureNameAndTenantId(request.featureName(), tenantId)
                .orElseGet(() -> {
                    FeatureDefinition def = new FeatureDefinition(tenantId, request.featureName(), request.featureType());
                    if (request.description() != null) {
                        def.setDescription(request.description());
                    }
                    return definitionRepository.save(def);
                });

        if (!definition.getFeatureType().equals(request.featureType())) {
            throw new IllegalArgumentException("Feature type mismatch for existing feature");
        }

        List<FeatureValue> featureValues = request.values().stream()
                .map(fv -> new FeatureValue(tenantId, request.featureName(), fv.entityId(), fv.value()))
                .toList();

        valueRepository.saveAll(featureValues);
        definition.incrementVersion();
        definitionRepository.save(definition);

        log.info("Stored {} features for feature: {}", featureValues.size(), request.featureName());

        return new StoreFeaturesResponseDto(definition.getVersion(), featureValues.size(), java.time.Instant.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntityFeatureDto> getFeatures(String featureName, String entityId, String tenantId) {
        if (entityId != null && !entityId.isBlank()) {
            return valueRepository.findByFeatureNameAndEntityIdAndTenantId(featureName, entityId, tenantId)
                    .map(fv -> new EntityFeatureDto(fv.getEntityId(), fv.getValue()))
                    .map(List::of)
                    .orElse(List.of());
        }

        return valueRepository.findByFeatureNameAndTenantId(featureName, tenantId)
                .stream()
                .map(fv -> new EntityFeatureDto(fv.getEntityId(), fv.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FeatureDefinitionResponseDto getFeatureDefinition(String featureName, String tenantId) {
        FeatureDefinition definition = definitionRepository
                .findByFeatureNameAndTenantId(featureName, tenantId)
                .orElseThrow(() -> new ValidationException("Feature definition not found"));

        return mapper.toResponseDto(definition);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeatureDefinitionResponseDto> listFeatureDefinitions(String tenantId, int page, int size) {
        return definitionRepository.findByTenantId(tenantId, page, size)
                .stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFeatureDefinition(String featureName, String tenantId) {
        definitionRepository.deleteByFeatureNameAndTenantId(featureName, tenantId);
        valueRepository.deleteByFeatureNameAndTenantId(featureName, tenantId);
        log.info("Deleted feature definition: {}", featureName);
    }
}
