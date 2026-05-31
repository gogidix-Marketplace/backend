/**
 * Base interface for all domain events
 */
export interface BaseDomainEvent {
  eventType: string;
  eventId: string;
  occurredAt: Date;
  tenantId: string;
  organizationId: string;
  correlationId?: string;
}

/**
 * Union type of all invoice domain events
 */
export type InvoiceDomainEvent =
  | import('./invoice-created.event').InvoiceCreatedEvent
  | import('./invoice-validated.event').InvoiceValidatedEvent
  | import('./invoice-processed.event').InvoiceProcessedEvent
  | import('./invoice-status-changed.event').InvoiceStatusChangedEvent;
