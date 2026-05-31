export class SendEmailDto {
  to!: string | string[];
  cc?: string[];
  bcc?: string[];
  subject!: string;
  htmlBody?: string;
  textBody?: string;
  templateId?: string;
  templateData?: Record<string, any>;
  fromEmail?: string;
  fromName?: string;
  replyTo?: string;
  provider?: 'sendgrid' | 'ses';
  scheduledAt?: Date;
  tags?: string[];
  metadata?: Record<string, any>;
}
export class SendBatchEmailDto {
  emails!: SendEmailDto[];
}
export class EmailStatusResponseDto {
  id!: string;
  status!: string;
  provider?: string;
  providerMessageId?: string;
  attempts!: number;
  sentAt?: Date;
  failureReason?: string;
}
