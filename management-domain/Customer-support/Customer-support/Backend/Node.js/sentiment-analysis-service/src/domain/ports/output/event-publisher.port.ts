export interface EventPublisher {
  publish(event: import('../../events').DomainEvent): Promise<void>;
  publishAll(events: import('../../events').DomainEvent[]): Promise<void>;
}
