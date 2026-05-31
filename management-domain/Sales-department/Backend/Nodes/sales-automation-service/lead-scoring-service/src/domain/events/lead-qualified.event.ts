import { v4 as uuidv4 } from 'uuid';
import { LeadGrade } from '../entities/enums/lead-grade.enum';

export class LeadQualifiedEvent {
  readonly eventId: string;
  readonly occurredAt: Date;
  readonly eventType = 'LeadQualified';

  constructor(
    readonly leadId: string,
    readonly tenantId: string,
    readonly scoreId: string,
    readonly score: number,
    readonly grade: LeadGrade,
    readonly qualifyingThreshold: number,
    readonly scoreModelId: string,
    readonly qualifiedBy: string,
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
      leadId: this.leadId,
      tenantId: this.tenantId,
      scoreId: this.scoreId,
      score: this.score,
      grade: this.grade,
      qualifyingThreshold: this.qualifyingThreshold,
      scoreModelId: this.scoreModelId,
      qualifiedBy: this.qualifiedBy,
      metadata: this.metadata,
    };
  }

  static fromPrimitives(data: Record<string, any>): LeadQualifiedEvent {
    const event = new LeadQualifiedEvent(
      data.leadId,
      data.tenantId,
      data.scoreId,
      data.score,
      data.grade,
      data.qualifyingThreshold,
      data.scoreModelId,
      data.qualifiedBy,
      data.metadata
    );
    (event as any).eventId = data.eventId;
    (event as any).occurredAt = new Date(data.occurredAt);
    return event;
  }
}
