export interface EmailJobProps {
  id?: string;
  tenantId: string;
  to: string | string[];
  cc?: string[];
  bcc?: string[];
  subject: string;
  htmlBody?: string;
  textBody?: string;
  templateId?: string;
  templateData?: Record<string, any>;
  fromEmail: string;
  fromName?: string;
  replyTo?: string;
  provider?: 'sendgrid' | 'ses';
  status: 'pending' | 'queued' | 'sending' | 'sent' | 'failed' | 'bounced';
  providerMessageId?: string;
  attempts: number;
  maxAttempts: number;
  nextRetryAt?: Date;
  scheduledAt?: Date;
  sentAt?: Date;
  failureReason?: string;
  metadata?: Record<string, any>;
  tags?: string[];
  createdAt?: Date;
  updatedAt?: Date;
}

export class EmailJob {
  private readonly props: EmailJobProps;

  constructor(props: EmailJobProps) {
    this.props = { ...props, attempts: props.attempts ?? 0, maxAttempts: props.maxAttempts ?? 3, status: props.status ?? 'pending', createdAt: props.createdAt ?? new Date(), updatedAt: new Date() };
  }

  get id(): string | undefined { return this.props.id; }
  get tenantId(): string { return this.props.tenantId; }
  get to(): string | string[] { return this.props.to; }
  get cc(): string[] | undefined { return this.props.cc; }
  get bcc(): string[] | undefined { return this.props.bcc; }
  get subject(): string { return this.props.subject; }
  get htmlBody(): string | undefined { return this.props.htmlBody; }
  get textBody(): string | undefined { return this.props.textBody; }
  get templateId(): string | undefined { return this.props.templateId; }
  get templateData(): Record<string, any> | undefined { return this.props.templateData; }
  get fromEmail(): string { return this.props.fromEmail; }
  get fromName(): string | undefined { return this.props.fromName; }
  get replyTo(): string | undefined { return this.props.replyTo; }
  get provider(): 'sendgrid' | 'ses' | undefined { return this.props.provider; }
  get status(): string { return this.props.status; }
  get providerMessageId(): string | undefined { return this.props.providerMessageId; }
  get attempts(): number { return this.props.attempts; }
  get maxAttempts(): number { return this.props.maxAttempts; }
  get nextRetryAt(): Date | undefined { return this.props.nextRetryAt; }
  get scheduledAt(): Date | undefined { return this.props.scheduledAt; }
  get sentAt(): Date | undefined { return this.props.sentAt; }
  get failureReason(): string | undefined { return this.props.failureReason; }
  get metadata(): Record<string, any> | undefined { return this.props.metadata; }
  get tags(): string[] | undefined { return this.props.tags; }
  get createdAt(): Date | undefined { return this.props.createdAt; }
  get updatedAt(): Date | undefined { return this.props.updatedAt; }

  markAsQueued(): void { this.props.status = 'queued'; this.props.updatedAt = new Date(); }
  markAsSending(): void { this.props.status = 'sending'; this.props.updatedAt = new Date(); }
  markAsSent(providerMessageId: string): void { this.props.status = 'sent'; this.props.providerMessageId = providerMessageId; this.props.sentAt = new Date(); this.props.updatedAt = new Date(); }
  markAsFailed(reason: string): void { this.props.attempts += 1; this.props.failureReason = reason; this.props.status = this.props.attempts >= this.props.maxAttempts ? 'failed' : 'pending'; this.props.nextRetryAt = new Date(Date.now() + Math.pow(2, this.props.attempts) * 60000); this.props.updatedAt = new Date(); }
  markAsBounced(): void { this.props.status = 'bounced'; this.props.updatedAt = new Date(); }
  canRetry(): boolean { return this.props.attempts < this.props.maxAttempts && this.props.status === 'pending'; }
  toPlainObject(): EmailJobProps { return { ...this.props }; }
}
