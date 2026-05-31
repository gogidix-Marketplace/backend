import { v4 as uuidv4 } from 'uuid';
import { LeadGrade } from '../entities/enums/lead-grade.enum';

export class ScoreThresholdReachedEvent {
  readonly eventId: string;
  readonly occurredAt: Date;
  readonly eventType = 'ScoreThresholdReached';

  constructor(
    readonly scoreId: string,
    readonly leadId: string,
    readonly tenantId: string,
    readonly score: number,
    readonly previousGrade: LeadGrade,
    readonly newGrade: LeadGrade,
    readonly threshold: number,
    readonly thresholdType: 'minimum' | 'maximum' | 'custom',
    readonly scoreModelId: string,
    readonly notifiedAt?: Date,
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
      score: this.score,
      previousGrade: this.previousGrade,
      newGrade: this.newGrade,
      threshold: this.threshold,
      thresholdType: this.thresholdType,
      scoreModelId: this.scoreModelId,
      notifiedAt: this.notifiedAt,
      metadata: this.metadata,
    };
  }

  static fromPrimitives(data: Record<string, any>): ScoreThresholdReachedEvent {
    const event = new ScoreThresholdReachedEvent(
      data.scoreId,
      data.leadId,
      data.tenantId,
      data.score,
      data.previousGrade,
      data.newGrade,
      data.threshold,
      data.thresholdType,
      data.scoreModelId,
      data.notifiedAt,
      data.metadata
    );
    (event as any).eventId = data.eventId;
    (event as any).occurredAt = new Date(data.occurredAt);
    return event;
  }
}
