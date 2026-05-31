import { Injectable } from '@nestjs/common';
import { ICommandHandler } from './interfaces/command-handler.interface';
import { CreateScoreRuleCommand } from '../../commands/create-score-rule.command';
import { ScoreRule } from '../../../domain/entities/score-rule.entity';
import { ScoreRuleRepositoryPort } from '../../../domain/ports/in/score-rule.repository.port';
import { ScoreModelRepositoryPort } from '../../../domain/ports/in/score-model.repository.port';

@Injectable()
export class CreateScoreRuleHandler implements ICommandHandler<CreateScoreRuleCommand> {
  constructor(
    private readonly scoreRuleRepository: ScoreRuleRepositoryPort,
    private readonly scoreModelRepository: ScoreModelRepositoryPort,
  ) {}

  async execute(command: CreateScoreRuleCommand): Promise<ScoreRule> {
    const scoreModel = await this.scoreModelRepository.findById(command.scoreModelId);

    if (!scoreModel) {
      throw new Error('Score model not found');
    }

    if (scoreModel.tenantId !== command.tenantId) {
      throw new Error('Access denied');
    }

    const scoreRule = new ScoreRule({
      tenantId: command.tenantId,
      scoreModelId: command.scoreModelId,
      name: command.name,
      description: command.description,
      ruleType: command.ruleType,
      conditions: command.conditions as any,
      formula: command.formula as any,
      baseScore: command.baseScore,
      maxScore: command.maxScore,
      priority: command.priority,
      category: command.category,
      tags: command.tags,
    });

    const savedRule = await this.scoreRuleRepository.save(scoreRule);

    // Add rule to model
    scoreModel.addRule(savedRule.id);
    await this.scoreModelRepository.save(scoreModel);

    return savedRule;
  }
}
