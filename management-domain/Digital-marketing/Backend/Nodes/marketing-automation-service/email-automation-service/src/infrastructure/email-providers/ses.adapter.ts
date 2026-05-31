import { Injectable } from '@nestjs/common';
import { IEmailSender, EmailMessage, SendResult } from '../../../domain/ports/services/email-sender.port';

@Injectable()
export class SesAdapter implements IEmailSender {
  private ses: any;

  constructor() {
    const AWS = require('aws-sdk');
    this.ses = new AWS.SES({ region: process.env.AWS_REGION ?? 'us-east-1' });
  }

  async send(message: EmailMessage): Promise<SendResult> {
    const params = {
      Source: message.from.name ? `${message.from.name} <${message.from.email}>` : message.from.email,
      Destination: {
        ToAddresses: Array.isArray(message.to) ? message.to : [message.to],
        CcAddresses: message.cc ?? [],
        BccAddresses: message.bcc ?? [],
      },
      Message: {
        Subject: { Data: message.subject, Charset: 'UTF-8' },
        Body: {
          ...(message.htmlBody ? { Html: { Data: message.htmlBody, Charset: 'UTF-8' } } : {}),
          ...(message.textBody ? { Text: { Data: message.textBody, Charset: 'UTF-8' } } : {}),
        },
      },
      ReplyToAddresses: message.replyTo ? [message.replyTo] : undefined,
    };
    const result = await this.ses.sendEmail(params).promise();
    return { messageId: result.MessageId, provider: 'ses' };
  }

  async sendBatch(messages: EmailMessage[]): Promise<SendResult[]> {
    const results: SendResult[] = [];
    for (const message of messages) {
      results.push(await this.send(message));
    }
    return results;
  }
}
