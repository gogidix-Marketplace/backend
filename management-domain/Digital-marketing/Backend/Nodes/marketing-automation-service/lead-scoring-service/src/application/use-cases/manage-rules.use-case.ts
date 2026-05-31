import { Injectable, Inject } from '@nestjs/common';
import { ScoringRule } from '../../domain/models/scoring-rule';
import { IScoringRuleRepository, SCORING_RULE_REPOSITORY } from '../../domain/ports/repositories/scoring-rule.repository';
import { CreateScoringRuleDto, UpdateScoringRuleDto } from '../dtos/scoring-rule.dto';
import { ScoringRuleNotFoundException } from '../../domain/exceptions/domain.exceptions';

@Injectable()
export class ManageRulesUseCase {
  constructor(
    @Inject(SCORING_RULE_REPOSITORY) private readonly ruleRepo: IScoringRuleRepository,
  ) {}

  async create(dto: CreateScoringRuleDto, tenantId: string): Promise<ScoringRule> {
    const rule = new ScoringRule({ tenantId, name: dto.name, description: dto.description, category: dto.category, conditions: dto.conditions, conditionLogic: dto.conditionLogic ?? 'and', points: dto.points, maxPoints: dto.maxPoints, priority: dto.priority ?? 0, validFrom: dto.validFrom, validTo: dto.validTo });
    return this.ruleRepo.save(rule);
  }

  async findAll(tenantId: string, category?: string): Promise<ScoringRule[]> {
    return this.ruleRepo.findByTenantId(tenantId, { category });
  }

  async findById(id: string): Promise<ScoringRule> {
    const rule = await this.ruleRepo.findById(id);
    if (!rule) throw new ScoringRuleNotFoundException(id);
    return rule;
  }

  async update(id: string, dto: UpdateScoringRuleDto): Promise<ScoringRule> {
    const rule = await this.findById(id);
    const updated = new ScoringRule({ ...rule.toPlainObject(), ...dto, updatedAt: new Date() });
    return this.ruleRepo.update(updated);
  }

  async delete(id: string): Promise<boolean> {
    return this.ruleRepo.delete(id);
  }
}
