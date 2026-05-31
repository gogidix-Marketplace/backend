export interface EventPublisherPort {
  publish(event: DomainEvent): Promise<void>;
  publishBatch(events: DomainEvent[]): Promise<void>;
}

export interface DomainEvent {
  eventType: string;
  eventId: string;
  occurredAt: Date;
  toPrimitives(): Record<string, any>;
}
