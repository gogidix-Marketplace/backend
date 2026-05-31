export interface EmailMessage {
  to: string | string[];
  cc?: string[];
  bcc?: string[];
  subject: string;
  htmlBody?: string;
  textBody?: string;
  from: { email: string; name?: string };
  replyTo?: string;
  tags?: string[];
  customArgs?: Record<string, string>;
}
export interface SendResult {
  messageId: string;
  provider: string;
}
export const EMAIL_SENDER_PORT = Symbol('EMAIL_SENDER_PORT');
export interface IEmailSender {
  send(message: EmailMessage): Promise<SendResult>;
  sendBatch(messages: EmailMessage[]): Promise<SendResult[]>;
}
