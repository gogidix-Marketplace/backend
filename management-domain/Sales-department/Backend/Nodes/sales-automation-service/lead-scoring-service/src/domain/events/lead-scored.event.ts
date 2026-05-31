import { v4 as uuidv4 } from 'uuid';
import { LeadGrade } from '../entities/enums/lead-grade.enum';

export class LeadScoredEvent {
  readonly eventId: string;
  readonly occurredAt: Date;
  readonly eventType = 'LeadScored';

  constructor(
    readonly scoreId: string,
    readonly leadId: string,
    readonly tenantId: string,
    readonly previousScore: number,
    readonly newScore: number,
    readonly grade: LeadGrade,
    readonly scoreModelId: string,
    readonly variantId: string | null,
    readonly isControlGroup: boolean,
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
      scoreModelId: this.scoreModelId,
      variantId: this.variantId,
      isControlGroup: this.isControlGroup,
      metadata: this.metadata,
    };
  }

  static fromPrimitives(data: Record<string, any>): LeadScoredEvent {
    const event = new LeadScoredEvent(
      data.scoreId,
      data.leadId,
      data.tenantId,
      data.previousScore,
      data.newScore,
      data.grade,
      data.scoreModelId,
      data.variantId,
      data.isControlGroup,
      data.metadata
    );
    (event as any).eventId = data.eventId;
    (event as any).occurredAt = new Date(data.occurredAt);
    return event;
  }
}
