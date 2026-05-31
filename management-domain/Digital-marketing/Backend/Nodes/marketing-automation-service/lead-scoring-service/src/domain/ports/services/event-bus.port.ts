export interface DomainEvent {
  type: string;
  data: Record<string, any>;
  timestamp: Date;
}
export const EVENT_BUS_PORT = Symbol('EVENT_BUS_PORT');
export interface IEventBus {
  publish(channel: string, event: DomainEvent): Promise<void>;
  subscribe(channel: string, handler: (event: DomainEvent) => Promise<void>): void;
}
