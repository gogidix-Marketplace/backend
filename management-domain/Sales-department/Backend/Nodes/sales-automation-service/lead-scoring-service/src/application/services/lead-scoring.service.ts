import { Injectable, NotFoundException, BadRequestException } from '@nestjs/common';
import { LeadScore } from '../../domain/entities/lead-score.entity';
import { ScoreModel } from '../../domain/entities/score-model.entity';
import { ScoreRule } from '../../domain/entities/score-rule.entity';
import { ScoreAttribute } from '../../domain/entities/score-attribute.entity';
import { LeadScoreRepositoryPort, PaginationOptions } from '../../domain/ports/in/lead-score.repository.port';
import { ScoreModelRepositoryPort } from '../../domain/ports/in/score-model.repository.port';
import { ScoreRuleRepositoryPort } from '../../domain/ports/in/score-rule.repository.port';
import { ScoreAttributeRepositoryPort } from '../../domain/ports/in/score-attribute.repository.port';
import { EventPublisherPort } from '../../domain/ports/out/event-publisher.port';
import { LeadDataProviderPort, LeadData } from '../../domain/ports/out/lead-data-provider.port';
import { ScoreLeadRequestDto, BatchScoreLeadRequestDto, RescoreLeadsRequestDto } from '../dto/request/score-lead.request.dto';
import {
  LeadScoreResponseDto,
  BatchScoreResponseDto,
  ScoreStatisticsDto,
  ScoreTrendDto,
} from '../dto/response/lead-score.response.dto';
import { LeadGrade } from '../../domain/entities/enums/lead-grade.enum';
import { ScoreType } from '../../domain/entities/enums/score-type.enum';

@Injectable()
export class LeadScoringService {
  constructor(
    private readonly leadScoreRepository: LeadScoreRepositoryPort,
    private readonly scoreModelRepository: ScoreModelRepositoryPort,
    private readonly scoreRuleRepository: ScoreRuleRepositoryPort,
    private readonly scoreAttributeRepository: ScoreAttributeRepositoryPort,
    private readonly eventPublisher: EventPublisherPort,
    private readonly leadDataProvider: LeadDataProviderPort,
  ) {}

  async scoreLead(request: ScoreLeadRequestDto): Promise<LeadScoreResponseDto> {
    // Get or determine score model
    let scoreModel: ScoreModel | null = null;

    if (request.scoreModelId) {
      scoreModel = await this.scoreModelRepository.findById(request.scoreModelId);
    } else {
      scoreModel = await this.scoreModelRepository.findDefaultByTenantId(request.tenantId);
    }

    if (!scoreModel) {
      throw new NotFoundException('No scoring model found');
    }

    if (scoreModel.status !== 'active') {
      throw new BadRequestException('Scoring model is not active');
    }

    // Get lead data
    const leadData = request.leadData ||
      await this.leadDataProvider.getLeadData(request.leadId, request.tenantId);

    if (!leadData) {
      throw new NotFoundException('Lead not found');
    }

    // Get rules and attributes for the model
    const rules = await this.scoreRuleRepository.findByScoreModelId(scoreModel.id);
    const activeRules = rules.filter(r => r.isActive);

    // Determine variant if A/B testing is enabled
    const variant = scoreModel.selectVariant();
    const variantId = variant?.id ?? request.variantId ?? null;
    const isControlGroup = variant?.name === 'Control' || !variant;

    // Calculate score
    const scoreResult = await this.calculateScore(
      leadData as LeadData,
      activeRules,
      scoreModel.scoringConfig
    );

    // Check if lead score already exists
    const existingScore = await this.leadScoreRepository.findByLeadIdAndTenantId(
      request.leadId,
      request.tenantId
    );

    let leadScore: LeadScore;

    if (existingScore) {
      // Update existing score
      existingScore.updateScore(
        scoreResult.totalScore,
        scoreResult.breakdown,
        scoreResult.attributes
      );
      existingScore.setVariant(variantId ?? '', isControlGroup);
      leadScore = await this.leadScoreRepository.save(existingScore);
    } else {
      // Create new score
      leadScore = new LeadScore({
        leadId: request.leadId,
        tenantId: request.tenantId,
        scoreModelId: scoreModel.id,
        totalScore: scoreResult.totalScore,
        grade: scoreResult.grade,
        scoreType: scoreModel.modelType as any,
        breakdown: scoreResult.breakdown,
        attributes: scoreResult.attributes,
        variantId,
        isControlGroup,
      });
      leadScore = await this.leadScoreRepository.save(leadScore);
    }

    // Publish domain events
    for (const event of leadScore.domainEvents) {
      await this.eventPublisher.publish(event);
    }
    leadScore.clearDomainEvents();

    return this.toLeadScoreResponseDto(leadScore);
  }

  async batchScoreLeads(request: BatchScoreLeadRequestDto): Promise<BatchScoreResponseDto> {
    const successful: Array<{ leadId: string; scoreId: string; score: number }> = [];
    const failed: Array<{ leadId: string; error: string }> = [];

    // Get score model
    let scoreModel: ScoreModel | null = null;

    if (request.scoreModelId) {
      scoreModel = await this.scoreModelRepository.findById(request.scoreModelId);
    } else {
      scoreModel = await this.scoreModelRepository.findDefaultByTenantId(request.tenantId);
    }

    if (!scoreModel) {
      throw new NotFoundException('No scoring model found');
    }

    // Get rules for the model
    const rules = await this.scoreRuleRepository.findByScoreModelId(scoreModel.id);
    const activeRules = rules.filter(r => r.isActive);

    // Process leads in batches
    for (const leadId of request.leadIds) {
      try {
        const leadData = await this.leadDataProvider.getLeadData(leadId, request.tenantId);

        if (!leadData) {
          failed.push({ leadId, error: 'Lead not found' });
          continue;
        }

        const scoreResult = await this.calculateScore(
          leadData,
          activeRules,
          scoreModel.scoringConfig
        );

        const existingScore = await this.leadScoreRepository.findByLeadIdAndTenantId(
          leadId,
          request.tenantId
        );

        let leadScore: LeadScore;

        if (existingScore) {
          existingScore.updateScore(
            scoreResult.totalScore,
            scoreResult.breakdown,
            scoreResult.attributes
          );
          leadScore = await this.leadScoreRepository.save(existingScore);
        } else {
          leadScore = new LeadScore({
            leadId,
            tenantId: request.tenantId,
            scoreModelId: scoreModel.id,
            totalScore: scoreResult.totalScore,
            grade: scoreResult.grade,
            scoreType: scoreModel.modelType as any,
            breakdown: scoreResult.breakdown,
            attributes: scoreResult.attributes,
          });
          leadScore = await this.leadScoreRepository.save(leadScore);
        }

        // Publish events
        for (const event of leadScore.domainEvents) {
          await this.eventPublisher.publish(event);
        }
        leadScore.clearDomainEvents();

        successful.push({
          leadId,
          scoreId: leadScore.id,
          score: leadScore.totalScore,
        });
      } catch (error) {
        failed.push({
          leadId,
          error: error instanceof Error ? error.message : 'Unknown error',
        });
      }
    }

    return {
      successful,
      failed,
      totalProcessed: request.leadIds.length,
    };
  }

  async applyScoreDecay(tenantId: string): Promise<void> {
    const leadScores = await this.leadScoreRepository.findByTenantId(tenantId);

    for (const leadScore of leadScores) {
      const daysSinceActivity = leadScore.daysSinceLastScored();

      if (daysSinceActivity > 0) {
        leadScore.applyDecay(daysSinceActivity);
        await this.leadScoreRepository.save(leadScore);

        // Publish events
        for (const event of leadScore.domainEvents) {
          await this.eventPublisher.publish(event);
        }
        leadScore.clearDomainEvents();
      }
    }
  }

  async getLeadScore(leadId: string, tenantId: string): Promise<LeadScoreResponseDto> {
    const leadScore = await this.leadScoreRepository.findByLeadIdAndTenantId(leadId, tenantId);

    if (!leadScore) {
      throw new NotFoundException('Lead score not found');
    }

    return this.toLeadScoreResponseDto(leadScore);
  }

  async getLeadScoresByTenant(
    tenantId: string,
    options?: PaginationOptions
  ): Promise<LeadScoreResponseDto[]> {
    const leadScores = await this.leadScoreRepository.findByTenantId(tenantId, options);
    return leadScores.map(score => this.toLeadScoreResponseDto(score));
  }

  async getQualifiedLeads(
    tenantId: string,
    minScore: number = 50,
    options?: PaginationOptions
  ): Promise<LeadScoreResponseDto[]> {
    const leadScores = await this.leadScoreRepository.findQualifiedLeads(tenantId, minScore, options);
    return leadScores.map(score => this.toLeadScoreResponseDto(score));
  }

  async getScoreStatistics(tenantId: string): Promise<ScoreStatisticsDto> {
    const statistics = await this.leadScoreRepository.getScoreStatistics(tenantId);

    return {
      totalLeads: statistics.totalLeads,
      averageScore: statistics.averageScore,
      scoreDistribution: statistics.scoreDistribution,
      gradeDistribution: statistics.gradeDistribution,
      topPerformers: statistics.topPerformers.map(p => ({
        ...p,
        grade: this.getGradeForScore(p.score),
      })),
    };
  }

  private async calculateScore(
    leadData: LeadData,
    rules: ScoreRule[],
    scoringConfig: any
  ): Promise<{
    totalScore: number;
    grade: LeadGrade;
    breakdown: any;
    attributes: any[];
  }> {
    let demographicScore = 0;
    let behavioralScore = 0;
    let engagementScore = 0;
    let firmographicScore = 0;
    const customScores: Record<string, number> = {};

    const calculatedAttributes: Array<{
      name: string;
      value: any;
      weight: number;
      contribution: number;
    }> = [];

    // Evaluate each rule
    for (const rule of rules) {
      const ruleScore = rule.evaluate(leadData as any);

      // Categorize by rule category
      switch (rule.category.toLowerCase()) {
        case 'demographic':
          demographicScore += ruleScore;
          break;
        case 'behavioral':
          behavioralScore += ruleScore;
          break;
        case 'engagement':
          engagementScore += ruleScore;
          break;
        case 'firmographic':
          firmographicScore += ruleScore;
          break;
        default:
          customScores[rule.category] = (customScores[rule.category] || 0) + ruleScore;
      }

      // Track attribute contributions
      for (const condition of rule.conditions) {
        calculatedAttributes.push({
          name: condition.field,
          value: (leadData as any)[condition.field],
          weight: condition.weight ?? rule.baseScore,
          contribution: ruleScore,
        });
      }
    }

    // Calculate weighted total
    const totalScore = Math.min(100, Math.round(
      (demographicScore * 0.25) +
      (behavioralScore * 0.30) +
      (engagementScore * 0.25) +
      (firmographicScore * 0.20)
    ));

    // Determine grade
    let grade: LeadGrade;
    if (totalScore >= 80) grade = LeadGrade.A;
    else if (totalScore >= 60) grade = LeadGrade.B;
    else if (totalScore >= 40) grade = LeadGrade.C;
    else grade = LeadGrade.D;

    return {
      totalScore,
      grade,
      breakdown: {
        demographicScore: Math.min(100, demographicScore),
        behavioralScore: Math.min(100, behavioralScore),
        engagementScore: Math.min(100, engagementScore),
        firmographicScore: Math.min(100, firmographicScore),
        customScores,
      },
      attributes: calculatedAttributes,
    };
  }

  private getGradeForScore(score: number): string {
    if (score >= 80) return 'A';
    if (score >= 60) return 'B';
    if (score >= 40) return 'C';
    return 'D';
  }

  private toLeadScoreResponseDto(leadScore: LeadScore): LeadScoreResponseDto {
    const data = leadScore.toPrimitives();
    return {
      id: data.id,
      leadId: data.leadId,
      tenantId: data.tenantId,
      scoreModelId: data.scoreModelId,
      totalScore: data.totalScore,
      grade: data.grade,
      scoreType: data.scoreType,
      breakdown: data.breakdown,
      attributes: data.attributes,
      lastScoredAt: data.lastScoredAt,
      scoreDecayRate: data.scoreDecayRate,
      decayApplied: data.decayApplied,
      lastDecayAppliedAt: data.lastDecayAppliedAt,
      variantId: data.variantId,
      isControlGroup: data.isControlGroup,
      metadata: data.metadata,
      createdAt: data.createdAt,
      updatedAt: data.updatedAt,
    };
  }
}
