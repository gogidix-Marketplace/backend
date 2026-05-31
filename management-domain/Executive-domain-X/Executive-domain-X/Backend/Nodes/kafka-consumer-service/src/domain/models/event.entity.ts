export interface EventProps {
  id?: string;
  eventId: string;
  eventType: string;
  eventVersion: string;
  aggregateId: string;
  aggregateType: string;
  tenantId: string;
  payload: Record<string, unknown>;
  metadata: Record<string, unknown>;
  causationId?: string | null;
  correlationId?: string | null;
  version: number;
  status: string;
  error?: string;
  processedAt?: Date | null;
  retryCount: number;
  timestamp: Date;
  createdAt: Date;
}

export class DomainEvent {
  readonly props: EventProps;

  constructor(props: EventProps) {
    this.props = {
      ...props,
      id: props.id || crypto.randomUUID(),
      eventVersion: props.eventVersion || '1.0',
      status: props.status || 'PENDING',
      retryCount: props.retryCount || 0,
      timestamp: props.timestamp || new Date(),
      createdAt: props.createdAt || new Date(),
      metadata: props.metadata || {},
      payload: props.payload || {},
    };
  }

  get id(): string { return this.props.id!; }
  get eventId(): string { return this.props.eventId; }
  get eventType(): string { return this.props.eventType; }
  get aggregateId(): string { return this.props.aggregateId; }

  markAsProcessing() { this.props.status = 'PROCESSING'; }
  markAsCompleted() { this.props.status = 'COMPLETED'; this.props.processedAt = new Date(); }
  markAsFailed(error: string) { this.props.status = 'FAILED'; this.props.error = error; this.props.retryCount++; }

  toJSON() {
    return { ...this.props };
  }
}
