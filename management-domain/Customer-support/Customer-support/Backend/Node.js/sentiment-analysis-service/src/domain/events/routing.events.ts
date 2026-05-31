export interface DomainEvent {
  eventId: string;
  eventType: string;
  timestamp: Date;
  aggregateId: string;
  payload: Record<string, unknown>;
}
