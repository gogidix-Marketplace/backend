import {
  PaymentInitiatedEvent,
  PaymentCompletedEvent,
  PaymentFailedEvent,
  BatchProcessedEvent,
} from '../../events';

export interface EventPublisher {
  publish(event: PaymentInitiatedEvent | PaymentCompletedEvent | PaymentFailedEvent | BatchProcessedEvent): Promise<void>;
  publishBatch(events: Array<PaymentInitiatedEvent | PaymentCompletedEvent | PaymentFailedEvent | BatchProcessedEvent>): Promise<void>;
}

export interface MessageBroker {
  connect(): Promise<void>;
  disconnect(): Promise<void>;
  publish(topic: string, message: any): Promise<void>;
  subscribe(topic: string, handler: (message: any) => void): Promise<void>;
  isConnected(): boolean;
}
