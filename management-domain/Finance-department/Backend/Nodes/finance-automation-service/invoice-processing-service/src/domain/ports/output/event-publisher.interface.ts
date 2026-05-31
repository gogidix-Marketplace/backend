import { InvoiceEvent } from '../../events';

/**
 * Output port for publishing domain events
 * Abstracts the event publishing mechanism from the domain
 */
export interface IEventPublisher {
  /**
   * Publish a single domain event
   */
  publish(event: InvoiceEvent): Promise<void>;

  /**
   * Publish multiple domain events
   */
  publishBatch(events: InvoiceEvent[]): Promise<void>;

  /**
   * Publish event to a specific topic
   */
  publishToTopic(topic: string, event: InvoiceEvent): Promise<void>;

  /**
   * Check if publisher is connected
   */
  isConnected(): boolean;
}

/**
 * Event topics enumeration
 */
export enum EventTopic {
  INVOICE_RECEIVED = 'invoice.received',
  INVOICE_CREATED = 'invoice.created',
  INVOICE_VALIDATED = 'invoice.validated',
  INVOICE_PROCESSED = 'invoice.processed',
  INVOICE_APPROVED = 'invoice.approved',
  INVOICE_FAILED = 'invoice.failed',
  INVOICE_STATUS_CHANGED = 'invoice.status.changed',
}
