import { Injectable } from '@nestjs/common';
import { CalculateScoreUseCase } from '../use-cases/calculate-score.use-case';
import { ManageRulesUseCase } from '../use-cases/manage-rules.use-case';
import { ManageSegmentsUseCase } from '../use-cases/manage-segments.use-case';
import { ProcessEventUseCase } from '../use-cases/process-event.use-case';
import { CalculateScoreDto } from '../dtos/scoring.dto';
import { CreateScoringRuleDto, UpdateScoringRuleDto } from '../dtos/scoring-rule.dto';
import { CreateSegmentDto, UpdateSegmentDto } from '../dtos/segment.dto';

@Injectable()
export class ScoringOrchestrationService {
  constructor(
    private readonly calculateScoreUseCase: CalculateScoreUseCase,
    private readonly manageRulesUseCase: ManageRulesUseCase,
    private readonly manageSegmentsUseCase: ManageSegmentsUseCase,
    private readonly processEventUseCase: ProcessEventUseCase,
  ) {}

  async calculateScore(dto: CalculateScoreDto, tenantId: string) { return this.calculateScoreUseCase.execute(dto, tenantId); }
  async createRule(dto: CreateScoringRuleDto, tenantId: string) { return this.manageRulesUseCase.create(dto, tenantId); }
  async getRules(tenantId: string, category?: string) { return this.manageRulesUseCase.findAll(tenantId, category); }
  async getRule(id: string) { return this.manageRulesUseCase.findById(id); }
  async updateRule(id: string, dto: UpdateScoringRuleDto) { return this.manageRulesUseCase.update(id, dto); }
  async deleteRule(id: string) { return this.manageRulesUseCase.delete(id); }
  async createSegment(dto: CreateSegmentDto, tenantId: string) { return this.manageSegmentsUseCase.create(dto, tenantId); }
  async getSegments(tenantId: string) { return this.manageSegmentsUseCase.findAll(tenantId); }
  async getSegment(id: string) { return this.manageSegmentsUseCase.findById(id); }
  async updateSegment(id: string, dto: UpdateSegmentDto) { return this.manageSegmentsUseCase.update(id, dto); }
  async deleteSegment(id: string) { return this.manageSegmentsUseCase.delete(id); }
  async processEvent(eventType: string, leadId: string, data: Record<string, any>, tenantId: string) { return this.processEventUseCase.execute(eventType, leadId, data, tenantId); }
}
