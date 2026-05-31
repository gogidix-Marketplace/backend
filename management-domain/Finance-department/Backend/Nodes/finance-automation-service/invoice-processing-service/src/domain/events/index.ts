export * from './invoice-received.event';
export * from './invoice-created.event';
export * from './invoice-validated.event';
export * from './invoice-processed.event';
export * from './invoice-approved.event';
export * from './invoice-failed.event';
export * from './invoice-status-changed.event';

// Union type for all invoice events
export type InvoiceEvent =
  | import('./invoice-received.event').InvoiceReceivedEvent
  | import('./invoice-created.event').InvoiceCreatedEvent
  | import('./invoice-validated.event').InvoiceValidatedEvent
  | import('./invoice-processed.event').InvoiceProcessedEvent
  | import('./invoice-approved.event').InvoiceApprovedEvent
  | import('./invoice-failed.event').InvoiceFailedEvent
  | import('./invoice-status-changed.event').InvoiceStatusChangedEvent;
