export interface BounceRecordProps {
  id?: string;
  tenantId: string;
  email: string;
  bounceType: 'hard' | 'soft' | 'complaint';
  bounceSubType?: string;
  diagnosticCode?: string;
  providerMessageId?: string;
  provider: 'sendgrid' | 'ses';
  createdAt?: Date;
}

export class BounceRecord {
  private readonly props: BounceRecordProps;
  constructor(props: BounceRecordProps) {
    this.props = { ...props, createdAt: props.createdAt ?? new Date() };
  }
  get id(): string | undefined { return this.props.id; }
  get tenantId(): string { return this.props.tenantId; }
  get email(): string { return this.props.email; }
  get bounceType(): string { return this.props.bounceType; }
  get bounceSubType(): string | undefined { return this.props.bounceSubType; }
  get diagnosticCode(): string | undefined { return this.props.diagnosticCode; }
  get providerMessageId(): string | undefined { return this.props.providerMessageId; }
  get provider(): string { return this.props.provider; }
  get createdAt(): Date | undefined { return this.props.createdAt; }
  isHardBounce(): boolean { return this.props.bounceType === 'hard'; }
  toPlainObject(): BounceRecordProps { return { ...this.props }; }
}
