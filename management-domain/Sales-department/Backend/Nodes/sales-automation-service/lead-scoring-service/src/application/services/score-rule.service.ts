import { Injectable, NotFoundException, BadRequestException } from '@nestjs/common';
import { ScoreRule } from '../../domain/entities/score-rule.entity';
import { ScoreRuleRepositoryPort } from '../../domain/ports/in/score-rule.repository.port';
import {
  CreateScoreRuleRequestDto,
  UpdateScoreRuleRequestDto,
  UpdateRuleScoreRequestDto,
} from '../dto/request/score-rule.request.dto';
import {
  ScoreRuleResponseDto,
  ScoreRuleListItemDto,
} from '../dto/response/score-rule.response.dto';

@Injectable()
export class ScoreRuleService {
  constructor(
    private readonly scoreRuleRepository: ScoreRuleRepositoryPort,
  ) {}

  async createRule(request: CreateScoreRuleRequestDto): Promise<ScoreRuleResponseDto> {
    const scoreRule = new ScoreRule({
      tenantId: request.tenantId,
      scoreModelId: request.scoreModelId,
      name: request.name,
      description: request.description || '',
      ruleType: request.ruleType,
      conditions: request.conditions as any,
      formula: request.formula as any || null,
      baseScore: request.baseScore,
      maxScore: request.maxScore,
      priority: request.priority,
      isActive: request.isActive ?? true,
      category: request.category || 'general',
      tags: request.tags || [],
    });

    const savedRule = await this.scoreRuleRepository.save(scoreRule);

    return this.toScoreRuleResponseDto(savedRule);
  }

  async updateRule(id: string, request: UpdateScoreRuleRequestDto): Promise<ScoreRuleResponseDto> {
    const scoreRule = await this.scoreRuleRepository.findById(id);

    if (!scoreRule) {
      throw new NotFoundException('Score rule not found');
    }

    if (request.name !== undefined) {
      // Update name through reflection (in a real app, use proper setters)
      (scoreRule as any)._name = request.name;
    }
    if (request.description !== undefined) {
      (scoreRule as any)._description = request.description;
    }
    if (request.conditions !== undefined) {
      (scoreRule as any)._conditions = request.conditions;
    }
    if (request.baseScore !== undefined && request.maxScore !== undefined) {
      scoreRule.updateScore(request.baseScore, request.maxScore);
    }
    if (request.priority !== undefined) {
      scoreRule.setPriority(request.priority);
    }
    if (request.isActive !== undefined) {
      if (request.isActive) {
        scoreRule.activate();
      } else {
        scoreRule.deactivate();
      }
    }

    const updatedRule = await this.scoreRuleRepository.save(scoreRule);

    return this.toScoreRuleResponseDto(updatedRule);
  }

  async updateRuleScore(id: string, request: UpdateRuleScoreRequestDto): Promise<ScoreRuleResponseDto> {
    const scoreRule = await this.scoreRuleRepository.findById(id);

    if (!scoreRule) {
      throw new NotFoundException('Score rule not found');
    }

    scoreRule.updateScore(request.baseScore, request.maxScore);
    const updatedRule = await this.scoreRuleRepository.save(scoreRule);

    return this.toScoreRuleResponseDto(updatedRule);
  }

  async activateRule(id: string): Promise<ScoreRuleResponseDto> {
    const scoreRule = await this.scoreRuleRepository.findById(id);

    if (!scoreRule) {
      throw new NotFoundException('Score rule not found');
    }

    scoreRule.activate();
    const updatedRule = await this.scoreRuleRepository.save(scoreRule);

    return this.toScoreRuleResponseDto(updatedRule);
  }

  async deactivateRule(id: string): Promise<ScoreRuleResponseDto> {
    const scoreRule = await this.scoreRuleRepository.findById(id);

    if (!scoreRule) {
      throw new NotFoundException('Score rule not found');
    }

    scoreRule.deactivate();
    const updatedRule = await this.scoreRuleRepository.save(scoreRule);

    return this.toScoreRuleResponseDto(updatedRule);
  }

  async addCondition(id: string, condition: any): Promise<ScoreRuleResponseDto> {
    const scoreRule = await this.scoreRuleRepository.findById(id);

    if (!scoreRule) {
      throw new NotFoundException('Score rule not found');
    }

    scoreRule.addCondition(condition);
    const updatedRule = await this.scoreRuleRepository.save(scoreRule);

    return this.toScoreRuleResponseDto(updatedRule);
  }

  async removeCondition(id: string, conditionIndex: number): Promise<ScoreRuleResponseDto> {
    const scoreRule = await this.scoreRuleRepository.findById(id);

    if (!scoreRule) {
      throw new NotFoundException('Score rule not found');
    }

    scoreRule.removeCondition(conditionIndex);
    const updatedRule = await this.scoreRuleRepository.save(scoreRule);

    return this.toScoreRuleResponseDto(updatedRule);
  }

  async addTag(id: string, tag: string): Promise<ScoreRuleResponseDto> {
    const scoreRule = await this.scoreRuleRepository.findById(id);

    if (!scoreRule) {
      throw new NotFoundException('Score rule not found');
    }

    scoreRule.addTag(tag);
    const updatedRule = await this.scoreRuleRepository.save(scoreRule);

    return this.toScoreRuleResponseDto(updatedRule);
  }

  async removeTag(id: string, tag: string): Promise<ScoreRuleResponseDto> {
    const scoreRule = await this.scoreRuleRepository.findById(id);

    if (!scoreRule) {
      throw new NotFoundException('Score rule not found');
    }

    scoreRule.removeTag(tag);
    const updatedRule = await this.scoreRuleRepository.save(scoreRule);

    return this.toScoreRuleResponseDto(updatedRule);
  }

  async getRule(id: string): Promise<ScoreRuleResponseDto> {
    const scoreRule = await this.scoreRuleRepository.findById(id);

    if (!scoreRule) {
      throw new NotFoundException('Score rule not found');
    }

    return this.toScoreRuleResponseDto(scoreRule);
  }

  async getRulesByTenant(tenantId: string): Promise<ScoreRuleListItemDto[]> {
    const scoreRules = await this.scoreRuleRepository.findByTenantId(tenantId);
    return scoreRules.map(rule => this.toScoreRuleListItemDto(rule));
  }

  async getRulesByModel(scoreModelId: string): Promise<ScoreRuleResponseDto[]> {
    const scoreRules = await this.scoreRuleRepository.findByScoreModelId(scoreModelId);
    return scoreRules.map(rule => this.toScoreRuleResponseDto(rule));
  }

  async getActiveRulesByModel(scoreModelId: string): Promise<ScoreRuleResponseDto[]> {
    const scoreRules = await this.scoreRuleRepository.findActiveByScoreModelId(scoreModelId);
    return scoreRules.map(rule => this.toScoreRuleResponseDto(rule));
  }

  async deleteRule(id: string): Promise<void> {
    const scoreRule = await this.scoreRuleRepository.findById(id);

    if (!scoreRule) {
      throw new NotFoundException('Score rule not found');
    }

    await this.scoreRuleRepository.delete(id);
  }

  private toScoreRuleResponseDto(scoreRule: ScoreRule): ScoreRuleResponseDto {
    const data = scoreRule.toPrimitives();
    return {
      id: data.id,
      tenantId: data.tenantId,
      scoreModelId: data.scoreModelId,
      name: data.name,
      description: data.description,
      ruleType: data.ruleType,
      conditions: data.conditions,
      formula: data.formula,
      baseScore: data.baseScore,
      maxScore: data.maxScore,
      priority: data.priority,
      isActive: data.isActive,
      category: data.category,
      tags: data.tags,
      metadata: data.metadata,
      createdAt: data.createdAt,
      updatedAt: data.updatedAt,
    };
  }

  private toScoreRuleListItemDto(scoreRule: ScoreRule): ScoreRuleListItemDto {
    const data = scoreRule.toPrimitives();
    return {
      id: data.id,
      scoreModelId: data.scoreModelId,
      name: data.name,
      description: data.description,
      ruleType: data.ruleType,
      baseScore: data.baseScore,
      maxScore: data.maxScore,
      priority: data.priority,
      isActive: data.isActive,
      category: data.category,
      createdAt: data.createdAt,
    };
  }
}
