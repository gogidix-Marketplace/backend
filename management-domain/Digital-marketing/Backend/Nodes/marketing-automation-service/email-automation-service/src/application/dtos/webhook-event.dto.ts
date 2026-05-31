export class WebhookEventDto {
  event!: string;
  email!: string;
  timestamp!: number | string;
  messageId?: string;
  bounceType?: string;
  bounceSubType?: string;
  diagnosticCode?: string;
  complaintType?: string;
  userAgent?: string;
  reason?: string;
  ip?: string;
  sgEventId?: string;
  sgMessageId?: string;
}
