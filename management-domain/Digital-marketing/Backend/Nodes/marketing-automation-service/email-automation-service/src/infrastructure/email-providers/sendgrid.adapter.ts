import { Injectable } from '@nestjs/common';
import { IEmailSender, EmailMessage, SendResult } from '../../../domain/ports/services/email-sender.port';

@Injectable()
export class SendGridAdapter implements IEmailSender {
  private apiKey: string;

  constructor() {
    this.apiKey = process.env.SENDGRID_API_KEY ?? '';
  }

  async send(message: EmailMessage): Promise<SendResult> {
    const sgMail = require('@sendgrid/mail');
    sgMail.setApiKey(this.apiKey);
    const msg = {
      to: message.to,
      cc: message.cc,
      bcc: message.bcc,
      subject: message.subject,
      html: message.htmlBody,
      text: message.textBody,
      from: { email: message.from.email, name: message.from.name },
      replyTo: message.replyTo,
      tags: message.tags,
      customArgs: message.customArgs,
    };
    const response = await sgMail.send(msg);
    const messageId = response[0]?.headers?.['x-message-id'] ?? 'unknown';
    return { messageId, provider: 'sendgrid' };
  }

  async sendBatch(messages: EmailMessage[]): Promise<SendResult[]> {
    const sgMail = require('@sendgrid/mail');
    sgMail.setApiKey(this.apiKey);
    const msg = messages.map(m => ({
      to: m.to, cc: m.cc, bcc: m.bcc, subject: m.subject,
      html: m.htmlBody, text: m.textBody,
      from: { email: m.from.email, name: m.from.name },
      replyTo: m.replyTo, tags: m.tags,
    }));
    const response = await sgMail.send(msg);
    return response.map((r: any) => ({ messageId: r?.headers?.['x-message-id'] ?? 'unknown', provider: 'sendgrid' }));
  }
}
