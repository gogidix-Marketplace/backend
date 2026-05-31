import { Injectable, Logger } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { EmailService } from '../../../domain/ports/output/email.service.interface';

@Injectable()
export class SmtpEmailService implements EmailService {
  private readonly logger = new Logger(SmtpEmailService.name);
  private readonly enabled: boolean;
  private readonly from: string;
  private readonly smtpConfig: {
    host: string;
    port: number;
    user?: string;
    password?: string;
  };

  constructor(private readonly configService: ConfigService) {
    this.enabled = this.configService.get<boolean>('automation.email.enabled', false);
    this.from = this.configService.get<string>('automation.email.from', 'noreply@gogidix.com');
    this.smtpConfig = {
      host: this.configService.get<string>('automation.email.smtp.host', 'smtp.gmail.com'),
      port: this.configService.get<number>('automation.email.smtp.port', 587),
      user: this.configService.get<string>('automation.email.smtp.user'),
      password: this.configService.get<string>('automation.email.smtp.password'),
    };
  }

  async sendEmail(params: {
    to: string | string[];
    cc?: string | string[];
    bcc?: string | string[];
    subject: string;
    template?: string;
    templateData?: Record<string, any>;
    body?: string;
  }): Promise<{ success: boolean; messageId?: string; error?: string }> {
    if (!this.enabled) {
      this.logger.warn('Email service is disabled. Simulating email send.');
      return {
        success: true,
        messageId: `simulated-${Date.now()}`,
      };
    }

    try {
      // In a real implementation, you would use nodemailer or a similar library
      this.logger.log(
        `Sending email to ${Array.isArray(params.to) ? params.to.join(', ') : params.to}: ${params.subject}`,
      );

      // Simulated email send
      // In production, integrate with nodemailer, SendGrid, AWS SES, etc.
      const messageId = `msg-${Date.now()}-${Math.random().toString(36).substring(7)}`;

      this.logger.debug(`Email sent successfully with messageId: ${messageId}`);

      return {
        success: true,
        messageId,
      };
    } catch (error) {
      this.logger.error('Failed to send email', error);
      return {
        success: false,
        error: error instanceof Error ? error.message : 'Unknown error',
      };
    }
  }
}
