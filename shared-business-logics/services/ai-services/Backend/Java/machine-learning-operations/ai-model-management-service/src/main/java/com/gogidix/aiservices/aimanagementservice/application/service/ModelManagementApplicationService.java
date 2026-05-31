package com.gogidix.aiservices.aimanagementservice.application.service;

import com.gogidix.aiservices.aimanagementservice.application.dto.*;
import com.gogidix.aiservices.aimanagementservice.application.mapper.ModelMapper;
import com.gogidix.aiservices.aimanagementservice.domain.model.MLModel;
import com.gogidix.aiservices.aimanagementservice.domain.model.ModelStatus;
import com.gogidix.aiservices.aimanagementservice.domain.port.in.ModelManagementServicePort;
import com.gogidix.aiservices.aimanagementservice.domain.port.out.ModelRepositoryPort;
import com.gogidix.aiservices.aimanagementservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Application service for model management operations.
 */
@Service
@Transactional
public class ModelManagementApplicationService implements ModelManagementServicePort {

    private static final Logger log = LoggerFactory.getLogger(ModelManagementApplicationService.class);

    private final ModelRepositoryPort modelRepository;
    private final ModelMapper modelMapper;

    public ModelManagementApplicationService(
            ModelRepositoryPort modelRepository,
            ModelMapper modelMapper) {
        this.modelRepository = modelRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RegisterModelResponseDto registerModel(RegisterModelRequestDto request) {
        String tenantId = "tenant-" + java.util.UUID.randomUUID().toString().substring(0, 8);

        MLModel model = new MLModel(
                tenantId,
                request.modelName(),
                request.framework(),
                request.version(),
                request.modelArtifactUrl()
        );

        model.validate();
        model = modelRepository.save(model);

        log.info("Registered model: {} with ID: {}", request.modelName(), model.getId());

        return new RegisterModelResponseDto(
                model.getId(),
                model.getStatus().name(),
                model.getRegisteredAt()
        );
    }

    @Override
    public void deployModel(String modelId, String tenantId) {
        MLModel model = modelRepository.findByIdAndTenantId(modelId, tenantId)
                .orElseThrow(() -> new ValidationException("Model not found"));

        model.deploy();
        modelRepository.save(model);

        // Simulate async deployment completion
        model.completeDeployment();
        modelRepository.save(model);

        log.info("Deployed model: {}", modelId);
    }

    @Override
    public void undeployModel(String modelId, String tenantId) {
        MLModel model = modelRepository.findByIdAndTenantId(modelId, tenantId)
                .orElseThrow(() -> new ValidationException("Model not found"));

        model.undeploy();
        modelRepository.save(model);

        model.completeUndeployment();
        modelRepository.save(model);

        log.info("Undeployed model: {}", modelId);
    }

    @Override
    @Transactional(readOnly = true)
    public ModelResponseDto getModel(String modelId, String tenantId) {
        MLModel model = modelRepository.findByIdAndTenantId(modelId, tenantId)
                .orElseThrow(() -> new ValidationException("Model not found"));

        return modelMapper.toResponseDto(model);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModelResponseDto> listModels(String tenantId, int page, int size) {
        return modelRepository.findByTenantId(tenantId, page, size)
                .stream()
                .map(modelMapper::toResponseDto)
                .toList();
    }

    @Override
    public void archiveModel(String modelId, String tenantId) {
        MLModel model = modelRepository.findByIdAndTenantId(modelId, tenantId)
                .orElseThrow(() -> new ValidationException("Model not found"));

        model.archive();
        modelRepository.save(model);

        log.info("Archived model: {}", modelId);
    }
}
