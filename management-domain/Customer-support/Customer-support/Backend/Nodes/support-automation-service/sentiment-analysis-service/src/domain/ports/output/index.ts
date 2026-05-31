export interface EventPublisher {
  publish(event: import('../../events').DomainEvent): Promise<void>;
}
