export interface BaseEvent {
  eventType: string;
  eventId: string;
  occurredAt: Date;
  correlationId?: string;
  tenantId: string;
  version: number;
}

export interface DomainEvent extends BaseEvent {
  aggregateId: string;
  aggregateType: string;
}
