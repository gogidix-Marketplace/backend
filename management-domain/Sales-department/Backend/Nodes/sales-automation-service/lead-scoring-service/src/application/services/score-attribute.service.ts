import { Injectable, NotFoundException, BadRequestException } from '@nestjs/common';
import { ScoreAttribute } from '../../domain/entities/score-attribute.entity';
import { ScoreAttributeRepositoryPort } from '../../domain/ports/in/score-attribute.repository.port';
import {
  CreateScoreAttributeRequestDto,
  UpdateScoreAttributeRequestDto,
  UpdateAttributeWeightRequestDto,
} from '../dto/request/score-attribute.request.dto';
import {
  ScoreAttributeResponseDto,
  ScoreAttributeListItemDto,
  AttributeValidationDto,
  AttributeContributionDto,
} from '../dto/response/score-attribute.response.dto';

@Injectable()
export class ScoreAttributeService {
  constructor(
    private readonly scoreAttributeRepository: ScoreAttributeRepositoryPort,
  ) {}

  async createAttribute(request: CreateScoreAttributeRequestDto): Promise<ScoreAttributeResponseDto> {
    const scoreAttribute = new ScoreAttribute({
      tenantId: request.tenantId,
      name: request.name,
      description: request.description || '',
      type: request.type,
      dataType: request.dataType,
      weight: request.weight,
      defaultValue: request.defaultValue,
      isRequired: request.isRequired ?? false,
      options: request.options || [],
      validationRule: request.validationRule,
      sourceField: request.sourceField,
      isActive: request.isActive ?? true,
      displayOrder: request.displayOrder ?? 0,
    });

    const savedAttribute = await this.scoreAttributeRepository.save(scoreAttribute);

    return this.toScoreAttributeResponseDto(savedAttribute);
  }

  async updateAttribute(id: string, request: UpdateScoreAttributeRequestDto): Promise<ScoreAttributeResponseDto> {
    const scoreAttribute = await this.scoreAttributeRepository.findById(id);

    if (!scoreAttribute) {
      throw new NotFoundException('Score attribute not found');
    }

    if (request.name !== undefined) {
      (scoreAttribute as any)._name = request.name;
    }
    if (request.description !== undefined) {
      (scoreAttribute as any)._description = request.description;
    }
    if (request.weight !== undefined) {
      scoreAttribute.updateWeight(request.weight);
    }
    if (request.defaultValue !== undefined) {
      scoreAttribute.setDefaultValue(request.defaultValue);
    }
    if (request.isActive !== undefined) {
      if (request.isActive) {
        scoreAttribute.activate();
      } else {
        scoreAttribute.deactivate();
      }
    }
    if (request.options !== undefined) {
      (scoreAttribute as any)._options = request.options;
    }

    const updatedAttribute = await this.scoreAttributeRepository.save(scoreAttribute);

    return this.toScoreAttributeResponseDto(updatedAttribute);
  }

  async updateWeight(id: string, request: UpdateAttributeWeightRequestDto): Promise<ScoreAttributeResponseDto> {
    const scoreAttribute = await this.scoreAttributeRepository.findById(id);

    if (!scoreAttribute) {
      throw new NotFoundException('Score attribute not found');
    }

    scoreAttribute.updateWeight(request.weight);
    const updatedAttribute = await this.scoreAttributeRepository.save(scoreAttribute);

    return this.toScoreAttributeResponseDto(updatedAttribute);
  }

  async activateAttribute(id: string): Promise<ScoreAttributeResponseDto> {
    const scoreAttribute = await this.scoreAttributeRepository.findById(id);

    if (!scoreAttribute) {
      throw new NotFoundException('Score attribute not found');
    }

    scoreAttribute.activate();
    const updatedAttribute = await this.scoreAttributeRepository.save(scoreAttribute);

    return this.toScoreAttributeResponseDto(updatedAttribute);
  }

  async deactivateAttribute(id: string): Promise<ScoreAttributeResponseDto> {
    const scoreAttribute = await this.scoreAttributeRepository.findById(id);

    if (!scoreAttribute) {
      throw new NotFoundException('Score attribute not found');
    }

    scoreAttribute.deactivate();
    const updatedAttribute = await this.scoreAttributeRepository.save(scoreAttribute);

    return this.toScoreAttributeResponseDto(updatedAttribute);
  }

  async addOption(id: string, option: string): Promise<ScoreAttributeResponseDto> {
    const scoreAttribute = await this.scoreAttributeRepository.findById(id);

    if (!scoreAttribute) {
      throw new NotFoundException('Score attribute not found');
    }

    scoreAttribute.addOption(option);
    const updatedAttribute = await this.scoreAttributeRepository.save(scoreAttribute);

    return this.toScoreAttributeResponseDto(updatedAttribute);
  }

  async removeOption(id: string, option: string): Promise<ScoreAttributeResponseDto> {
    const scoreAttribute = await this.scoreAttributeRepository.findById(id);

    if (!scoreAttribute) {
      throw new NotFoundException('Score attribute not found');
    }

    scoreAttribute.removeOption(option);
    const updatedAttribute = await this.scoreAttributeRepository.save(scoreAttribute);

    return this.toScoreAttributeResponseDto(updatedAttribute);
  }

  async validateValue(id: string, value: any): Promise<AttributeValidationDto> {
    const scoreAttribute = await this.scoreAttributeRepository.findById(id);

    if (!scoreAttribute) {
      throw new NotFoundException('Score attribute not found');
    }

    const validation = scoreAttribute.validateValue(value);

    return {
      valid: validation.valid,
      error: validation.error,
    };
  }

  async calculateContribution(id: string, value: any): Promise<AttributeContributionDto> {
    const scoreAttribute = await this.scoreAttributeRepository.findById(id);

    if (!scoreAttribute) {
      throw new NotFoundException('Score attribute not found');
    }

    const contribution = scoreAttribute.calculateContribution(value);

    return {
      attributeName: scoreAttribute.name,
      value,
      contribution,
      weight: scoreAttribute.weight,
    };
  }

  async getAttribute(id: string): Promise<ScoreAttributeResponseDto> {
    const scoreAttribute = await this.scoreAttributeRepository.findById(id);

    if (!scoreAttribute) {
      throw new NotFoundException('Score attribute not found');
    }

    return this.toScoreAttributeResponseDto(scoreAttribute);
  }

  async getAttributesByTenant(tenantId: string): Promise<ScoreAttributeListItemDto[]> {
    const scoreAttributes = await this.scoreAttributeRepository.findByTenantId(tenantId);
    return scoreAttributes.map(attribute => this.toScoreAttributeListItemDto(attribute));
  }

  async getActiveAttributesByTenant(tenantId: string): Promise<ScoreAttributeListItemDto[]> {
    const scoreAttributes = await this.scoreAttributeRepository.findActiveByTenantId(tenantId);
    return scoreAttributes.map(attribute => this.toScoreAttributeListItemDto(attribute));
  }

  async getAttributesByType(tenantId: string, type: string): Promise<ScoreAttributeResponseDto[]> {
    const scoreAttributes = await this.scoreAttributeRepository.findByType(tenantId, type);
    return scoreAttributes.map(attribute => this.toScoreAttributeResponseDto(attribute));
  }

  async deleteAttribute(id: string): Promise<void> {
    const scoreAttribute = await this.scoreAttributeRepository.findById(id);

    if (!scoreAttribute) {
      throw new NotFoundException('Score attribute not found');
    }

    await this.scoreAttributeRepository.delete(id);
  }

  private toScoreAttributeResponseDto(scoreAttribute: ScoreAttribute): ScoreAttributeResponseDto {
    const data = scoreAttribute.toPrimitives();
    return {
      id: data.id,
      tenantId: data.tenantId,
      name: data.name,
      description: data.description,
      type: data.type,
      dataType: data.dataType,
      weight: data.weight,
      defaultValue: data.defaultValue,
      isRequired: data.isRequired,
      options: data.options,
      validationRule: data.validationRule,
      sourceField: data.sourceField,
      isActive: data.isActive,
      displayOrder: data.displayOrder,
      metadata: data.metadata,
      createdAt: data.createdAt,
      updatedAt: data.updatedAt,
    };
  }

  private toScoreAttributeListItemDto(scoreAttribute: ScoreAttribute): ScoreAttributeListItemDto {
    const data = scoreAttribute.toPrimitives();
    return {
      id: data.id,
      name: data.name,
      description: data.description,
      type: data.type,
      weight: data.weight,
      isActive: data.isActive,
      displayOrder: data.displayOrder,
    };
  }
}
