import { v4 as uuidv4 } from 'uuid';

export class ScoreModelActivatedEvent {
  readonly eventId: string;
  readonly occurredAt: Date;
  readonly eventType = 'ScoreModelActivated';

  constructor(
    readonly modelId: string,
    readonly tenantId: string,
    readonly modelName: string,
    readonly version: number,
    readonly activatedBy: string,
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
      modelId: this.modelId,
      tenantId: this.tenantId,
      modelName: this.modelName,
      version: this.version,
      activatedBy: this.activatedBy,
      metadata: this.metadata,
    };
  }

  static fromPrimitives(data: Record<string, any>): ScoreModelActivatedEvent {
    const event = new ScoreModelActivatedEvent(
      data.modelId,
      data.tenantId,
      data.modelName,
      data.version,
      data.activatedBy,
      data.metadata
    );
    (event as any).eventId = data.eventId;
    (event as any).occurredAt = new Date(data.occurredAt);
    return event;
  }
}
