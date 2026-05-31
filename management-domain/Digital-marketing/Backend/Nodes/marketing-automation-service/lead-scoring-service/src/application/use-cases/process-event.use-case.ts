import { Injectable, Inject } from '@nestjs/common';
import { IEventBus, EVENT_BUS_PORT, DomainEvent } from '../../domain/ports/services/event-bus.port';
import { CalculateScoreUseCase } from './calculate-score.use-case';
import { ManageSegmentsUseCase } from './manage-segments.use-case';
import { ILeadScoreRepository, LEAD_SCORE_REPOSITORY } from '../../domain/ports/repositories/lead-score.repository';

@Injectable()
export class ProcessEventUseCase {
  constructor(
    private readonly calculateScoreUseCase: CalculateScoreUseCase,
    private readonly manageSegmentsUseCase: ManageSegmentsUseCase,
    @Inject(LEAD_SCORE_REPOSITORY) private readonly scoreRepo: ILeadScoreRepository,
    @Inject(EVENT_BUS_PORT) private readonly eventBus: IEventBus,
  ) {}

  async execute(eventType: string, leadId: string, eventData: Record<string, any>, tenantId: string): Promise<any> {
    const result = await this.calculateScoreUseCase.execute({ leadId, eventData }, tenantId);

    const matchingSegments = await this.manageSegmentsUseCase.findMatchingSegments(eventData, result.score, tenantId);

    await this.eventBus.publish('lead-events', {
      type: eventType,
      data: { leadId, tenantId, score: result.score, previousScore: result.previousScore, segments: matchingSegments.map(s => s.id), eventData },
      timestamp: new Date(),
    });

    return { leadId, score: result.score, previousScore: result.previousScore, segments: matchingSegments.map(s => ({ id: s.id, name: s.name })) };
  }
}
