import { v4 as uuidv4 } from 'uuid';

export class ScoreModelUpdatedEvent {
  readonly eventId: string;
  readonly occurredAt: Date;
  readonly eventType = 'ScoreModelUpdated';

  constructor(
    readonly modelId: string,
    readonly tenantId: string,
    readonly modelName: string,
    readonly version: number,
    readonly updatedBy: string,
    readonly changes: Record<string, any>,
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
      updatedBy: this.updatedBy,
      changes: this.changes,
      metadata: this.metadata,
    };
  }

  static fromPrimitives(data: Record<string, any>): ScoreModelUpdatedEvent {
    const event = new ScoreModelUpdatedEvent(
      data.modelId,
      data.tenantId,
      data.modelName,
      data.version,
      data.updatedBy,
      data.changes,
      data.metadata
    );
    (event as any).eventId = data.eventId;
    (event as any).occurredAt = new Date(data.occurredAt);
    return event;
  }
}
