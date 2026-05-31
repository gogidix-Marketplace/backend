import { Injectable, NotFoundException, BadRequestException } from '@nestjs/common';
import { ScoreModel } from '../../domain/entities/score-model.entity';
import { ScoreModelRepositoryPort } from '../../domain/ports/in/score-model.repository.port';
import { EventPublisherPort } from '../../domain/ports/out/event-publisher.port';
import {
  CreateScoreModelRequestDto,
  UpdateScoreModelRequestDto,
  ActivateScoreModelRequestDto,
  AddVariantRequestDto,
  EnableABTestingRequestDto,
} from '../dto/request/score-model.request.dto';
import {
  ScoreModelResponseDto,
  ScoreModelListItemDto,
} from '../dto/response/score-model.response.dto';

@Injectable()
export class ScoreModelService {
  constructor(
    private readonly scoreModelRepository: ScoreModelRepositoryPort,
    private readonly eventPublisher: EventPublisherPort,
  ) {}

  async createModel(request: CreateScoreModelRequestDto): Promise<ScoreModelResponseDto> {
    // Check if this is the first model (make it default)
    const existingModels = await this.scoreModelRepository.findByTenantId(request.tenantId);
    const isDefault = existingModels.length === 0 || request.isDefault;

    // If setting as default, unset other defaults
    if (isDefault) {
      for (const model of existingModels) {
        if (model.isDefault) {
          model.unsetAsDefault();
          await this.scoreModelRepository.save(model);
        }
      }
    }

    const scoreModel = new ScoreModel({
      tenantId: request.tenantId,
      name: request.name,
      description: request.description || '',
      modelType: request.modelType as any,
      scoringConfig: request.scoringConfig as any,
      ruleIds: request.ruleIds || [],
      attributeIds: request.attributeIds || [],
      isDefault,
      createdBy: request.createdBy,
    });

    const savedModel = await this.scoreModelRepository.save(scoreModel);

    return this.toScoreModelResponseDto(savedModel);
  }

  async updateModel(id: string, request: UpdateScoreModelRequestDto, userId: string): Promise<ScoreModelResponseDto> {
    const scoreModel = await this.scoreModelRepository.findById(id);

    if (!scoreModel) {
      throw new NotFoundException('Score model not found');
    }

    scoreModel.update(request, userId);
    const updatedModel = await this.scoreModelRepository.save(scoreModel);

    // Publish events
    for (const event of scoreModel.domainEvents) {
      await this.eventPublisher.publish(event);
    }
    scoreModel.clearDomainEvents();

    return this.toScoreModelResponseDto(updatedModel);
  }

  async activateModel(id: string, request: ActivateScoreModelRequestDto): Promise<ScoreModelResponseDto> {
    const scoreModel = await this.scoreModelRepository.findById(id);

    if (!scoreModel) {
      throw new NotFoundException('Score model not found');
    }

    scoreModel.activate(request.userId);
    const activatedModel = await this.scoreModelRepository.save(scoreModel);

    // Publish events
    for (const event of scoreModel.domainEvents) {
      await this.eventPublisher.publish(event);
    }
    scoreModel.clearDomainEvents();

    return this.toScoreModelResponseDto(activatedModel);
  }

  async deactivateModel(id: string, userId: string): Promise<ScoreModelResponseDto> {
    const scoreModel = await this.scoreModelRepository.findById(id);

    if (!scoreModel) {
      throw new NotFoundException('Score model not found');
    }

    scoreModel.deactivate(userId);
    const deactivatedModel = await this.scoreModelRepository.save(scoreModel);

    return this.toScoreModelResponseDto(deactivatedModel);
  }

  async archiveModel(id: string, userId: string): Promise<ScoreModelResponseDto> {
    const scoreModel = await this.scoreModelRepository.findById(id);

    if (!scoreModel) {
      throw new NotFoundException('Score model not found');
    }

    scoreModel.archive(userId);
    const archivedModel = await this.scoreModelRepository.save(scoreModel);

    return this.toScoreModelResponseDto(archivedModel);
  }

  async addVariant(id: string, request: AddVariantRequestDto): Promise<ScoreModelResponseDto> {
    const scoreModel = await this.scoreModelRepository.findById(id);

    if (!scoreModel) {
      throw new NotFoundException('Score model not found');
    }

    scoreModel.addVariant({
      name: request.name,
      description: request.description || '',
      percentage: request.percentage,
      isActive: true,
    });

    const updatedModel = await this.scoreModelRepository.save(scoreModel);

    return this.toScoreModelResponseDto(updatedModel);
  }

  async removeVariant(modelId: string, variantId: string): Promise<ScoreModelResponseDto> {
    const scoreModel = await this.scoreModelRepository.findById(modelId);

    if (!scoreModel) {
      throw new NotFoundException('Score model not found');
    }

    scoreModel.removeVariant(variantId);
    const updatedModel = await this.scoreModelRepository.save(scoreModel);

    return this.toScoreModelResponseDto(updatedModel);
  }

  async enableABTesting(id: string, request: EnableABTestingRequestDto): Promise<ScoreModelResponseDto> {
    const scoreModel = await this.scoreModelRepository.findById(id);

    if (!scoreModel) {
      throw new NotFoundException('Score model not found');
    }

    if (scoreModel.variants.length < 2) {
      throw new BadRequestException('At least 2 variants required for A/B testing');
    }

    scoreModel.enableABTesting(
      new Date(request.startDate),
      new Date(request.endDate)
    );

    const updatedModel = await this.scoreModelRepository.save(scoreModel);

    return this.toScoreModelResponseDto(updatedModel);
  }

  async disableABTesting(id: string): Promise<ScoreModelResponseDto> {
    const scoreModel = await this.scoreModelRepository.findById(id);

    if (!scoreModel) {
      throw new NotFoundException('Score model not found');
    }

    scoreModel.disableABTesting();
    const updatedModel = await this.scoreModelRepository.save(scoreModel);

    return this.toScoreModelResponseDto(updatedModel);
  }

  async setAsDefault(id: string): Promise<ScoreModelResponseDto> {
    const scoreModel = await this.scoreModelRepository.findById(id);

    if (!scoreModel) {
      throw new NotFoundException('Score model not found');
    }

    // Unset other defaults for this tenant
    const existingModels = await this.scoreModelRepository.findByTenantId(scoreModel.tenantId);
    for (const model of existingModels) {
      if (model.id !== id && model.isDefault) {
        model.unsetAsDefault();
        await this.scoreModelRepository.save(model);
      }
    }

    scoreModel.setAsDefault();
    const updatedModel = await this.scoreModelRepository.save(scoreModel);

    return this.toScoreModelResponseDto(updatedModel);
  }

  async createNewVersion(id: string, userId: string): Promise<ScoreModelResponseDto> {
    const scoreModel = await this.scoreModelRepository.findById(id);

    if (!scoreModel) {
      throw new NotFoundException('Score model not found');
    }

    const newVersion = scoreModel.createNewVersion(userId);
    const savedVersion = await this.scoreModelRepository.save(newVersion);

    return this.toScoreModelResponseDto(savedVersion);
  }

  async getModel(id: string): Promise<ScoreModelResponseDto> {
    const scoreModel = await this.scoreModelRepository.findById(id);

    if (!scoreModel) {
      throw new NotFoundException('Score model not found');
    }

    return this.toScoreModelResponseDto(scoreModel);
  }

  async getModelsByTenant(tenantId: string): Promise<ScoreModelListItemDto[]> {
    const scoreModels = await this.scoreModelRepository.findByTenantId(tenantId);
    return scoreModels.map(model => this.toScoreModelListItemDto(model));
  }

  async getActiveModelsByTenant(tenantId: string): Promise<ScoreModelListItemDto[]> {
    const scoreModels = await this.scoreModelRepository.findActiveByTenantId(tenantId);
    return scoreModels.map(model => this.toScoreModelListItemDto(model));
  }

  async getDefaultModel(tenantId: string): Promise<ScoreModelResponseDto> {
    const scoreModel = await this.scoreModelRepository.findDefaultByTenantId(tenantId);

    if (!scoreModel) {
      throw new NotFoundException('No default score model found');
    }

    return this.toScoreModelResponseDto(scoreModel);
  }

  async deleteModel(id: string): Promise<void> {
    const scoreModel = await this.scoreModelRepository.findById(id);

    if (!scoreModel) {
      throw new NotFoundException('Score model not found');
    }

    if (scoreModel.status === 'active') {
      throw new BadRequestException('Cannot delete active model');
    }

    await this.scoreModelRepository.delete(id);
  }

  private toScoreModelResponseDto(scoreModel: ScoreModel): ScoreModelResponseDto {
    const data = scoreModel.toPrimitives();
    return {
      id: data.id,
      tenantId: data.tenantId,
      name: data.name,
      description: data.description,
      modelType: data.modelType,
      status: data.status,
      version: data.version,
      scoringConfig: data.scoringConfig,
      ruleIds: data.ruleIds,
      attributeIds: data.attributeIds,
      variants: data.variants,
      isDefault: data.isDefault,
      isABTestEnabled: data.isABTestEnabled,
      testStartDate: data.testStartDate,
      testEndDate: data.testEndDate,
      metadata: data.metadata,
      createdBy: data.createdBy,
      updatedBy: data.updatedBy,
      createdAt: data.createdAt,
      updatedAt: data.updatedAt,
    };
  }

  private toScoreModelListItemDto(scoreModel: ScoreModel): ScoreModelListItemDto {
    const data = scoreModel.toPrimitives();
    return {
      id: data.id,
      name: data.name,
      description: data.description,
      modelType: data.modelType,
      status: data.status,
      version: data.version,
      isDefault: data.isDefault,
      isABTestEnabled: data.isABTestEnabled,
      createdAt: data.createdAt,
      updatedAt: data.updatedAt,
    };
  }
}
