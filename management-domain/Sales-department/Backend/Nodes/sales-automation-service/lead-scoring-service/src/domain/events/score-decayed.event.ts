import { v4 as uuidv4 } from 'uuid';
import { LeadGrade } from '../entities/enums/lead-grade.enum';

export class ScoreDecayedEvent {
  readonly eventId: string;
  readonly occurredAt: Date;
  readonly eventType = 'ScoreDecayed';

  constructor(
    readonly scoreId: string,
    readonly leadId: string,
    readonly tenantId: string,
    readonly previousScore: number,
    readonly newScore: number,
    readonly grade: LeadGrade,
    readonly daysSinceLastActivity: number,
    readonly totalDecayApplied: number,
    readonly metadata?: Record<string, any>
  ) {
    this.eventId = uuidv4();
    this.occurredAt = new Date();
  }

  toPrimitives(): Record<string, any> {
    return {
      eventId: this.eventId,
      eventType: this.eventType,
      occurredAt: this.occurredAt,
      scoreId: this.scoreId,
      leadId: this.leadId,
      tenantId: this.tenantId,
      previousScore: this.previousScore,
      newScore: this.newScore,
      grade: this.grade,
      daysSinceLastActivity: this.daysSinceLastActivity,
      totalDecayApplied: this.totalDecayApplied,
      metadata: this.metadata,
    };
  }

  static fromPrimitives(data: Record<string, any>): ScoreDecayedEvent {
    const event = new ScoreDecayedEvent(
      data.scoreId,
      data.leadId,
      data.tenantId,
      data.previousScore,
      data.newScore,
      data.grade,
      data.daysSinceLastActivity,
      data.totalDecayApplied,
      data.metadata
    );
    (event as any).eventId = data.eventId;
    (event as any).occurredAt = new Date(data.occurredAt);
    return event;
  }
}
