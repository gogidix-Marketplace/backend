export interface DeadLetterEntryProps {
  id?: string;
  dlqEventId: string;
  originalEventId: string;
  eventType: string;
  tenantId: string;
  payload: Record<string, unknown>;
  error: string;
  retryCount: number;
  maxRetryAttempts: number;
  status: string;
  metadata: Record<string, unknown>;
  createdAt: Date;
  updatedAt: Date;
}

export class DeadLetterEntry {
  readonly props: DeadLetterEntryProps;

  constructor(props: DeadLetterEntryProps) {
    this.props = { ...props, id: props.id || crypto.randomUUID(), retryCount: props.retryCount || 0, maxRetryAttempts: props.maxRetryAttempts || 3, status: props.status || 'PENDING', metadata: props.metadata || {}, createdAt: props.createdAt || new Date(), updatedAt: props.updatedAt || new Date() };
  }

  isRetryable(): boolean { return this.props.retryCount < this.props.maxRetryAttempts; }
  incrementRetry() { this.props.retryCount++; this.props.updatedAt = new Date(); }
  markAsResolved() { this.props.status = 'RESOLVED'; this.props.updatedAt = new Date(); }
  markAsFailed(_reason: string) { this.props.status = 'EXHAUSTED'; this.props.updatedAt = new Date(); }
  markForRetry() { this.props.status = 'PENDING'; this.props.updatedAt = new Date(); }

  toJSON() { return { ...this.props }; }
}
