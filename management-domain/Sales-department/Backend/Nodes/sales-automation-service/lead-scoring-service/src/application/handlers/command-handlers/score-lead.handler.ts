import { Injectable } from '@nestjs/common';
import { ICommandHandler } from './interfaces/command-handler.interface';
import { ScoreLeadCommand } from '../../commands/score-lead.command';
import { LeadScore } from '../../../domain/entities/lead-score.entity';
import { ScoreModel } from '../../../domain/entities/score-model.entity';
import { LeadScoreRepositoryPort } from '../../../domain/ports/in/lead-score.repository.port';
import { ScoreModelRepositoryPort } from '../../../domain/ports/in/score-model.repository.port';
import { ScoreRuleRepositoryPort } from '../../../domain/ports/in/score-rule.repository.port';
import { EventPublisherPort } from '../../../domain/ports/out/event-publisher.port';
import { LeadDataProviderPort } from '../../../domain/ports/out/lead-data-provider.port';
import { LeadGrade } from '../../../domain/entities/enums/lead-grade.enum';
import { ScoreType } from '../../../domain/entities/enums/score-type.enum';

@Injectable()
export class ScoreLeadHandler implements ICommandHandler<ScoreLeadCommand> {
  constructor(
    private readonly leadScoreRepository: LeadScoreRepositoryPort,
    private readonly scoreModelRepository: ScoreModelRepositoryPort,
    private readonly scoreRuleRepository: ScoreRuleRepositoryPort,
    private readonly eventPublisher: EventPublisherPort,
    private readonly leadDataProvider: LeadDataProviderPort,
  ) {}

  async execute(command: ScoreLeadCommand): Promise<LeadScore> {
    // Get or determine score model
    let scoreModel: ScoreModel | null = null;

    if (command.scoreModelId) {
      scoreModel = await this.scoreModelRepository.findById(command.scoreModelId);
    } else {
      scoreModel = await this.scoreModelRepository.findDefaultByTenantId(command.tenantId);
    }

    if (!scoreModel) {
      throw new Error('No scoring model found');
    }

    if (scoreModel.status !== 'active') {
      throw new Error('Scoring model is not active');
    }

    // Get lead data
    const leadData = command.leadData ||
      await this.leadDataProvider.getLeadData(command.leadId, command.tenantId);

    if (!leadData) {
      throw new Error('Lead not found');
    }

    // Get rules for the model
    const rules = await this.scoreRuleRepository.findByScoreModelId(scoreModel.id);
    const activeRules = rules.filter(r => r.isActive);

    // Determine variant if A/B testing is enabled
    const variant = scoreModel.selectVariant();
    const variantId = variant?.id ?? command.variantId ?? null;
    const isControlGroup = variant?.name === 'Control' || !variant;

    // Calculate score
    const scoreResult = await this.calculateScore(
      leadData,
      activeRules,
      scoreModel.scoringConfig
    );

    // Check if lead score already exists
    const existingScore = await this.leadScoreRepository.findByLeadIdAndTenantId(
      command.leadId,
      command.tenantId
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
        leadId: command.leadId,
        tenantId: command.tenantId,
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

    return leadScore;
  }

  private async calculateScore(
    leadData: any,
    rules: any[],
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
      const ruleScore = rule.evaluate(leadData);

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
          value: leadData[condition.field],
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
}
