export interface ComplaintRecordProps {
  id?: string;
  tenantId: string;
  email: string;
  complaintType?: string;
  userAgent?: string;
  providerMessageId?: string;
  provider: 'sendgrid' | 'ses';
  createdAt?: Date;
}

export class ComplaintRecord {
  private readonly props: ComplaintRecordProps;
  constructor(props: ComplaintRecordProps) {
    this.props = { ...props, createdAt: props.createdAt ?? new Date() };
  }
  get id(): string | undefined { return this.props.id; }
  get tenantId(): string { return this.props.tenantId; }
  get email(): string { return this.props.email; }
  get complaintType(): string | undefined { return this.props.complaintType; }
  get userAgent(): string | undefined { return this.props.userAgent; }
  get providerMessageId(): string | undefined { return this.props.providerMessageId; }
  get provider(): string { return this.props.provider; }
  get createdAt(): Date | undefined { return this.props.createdAt; }
  toPlainObject(): ComplaintRecordProps { return { ...this.props }; }
}
