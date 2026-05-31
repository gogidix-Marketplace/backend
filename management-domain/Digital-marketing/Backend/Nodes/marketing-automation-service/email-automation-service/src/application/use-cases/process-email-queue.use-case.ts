import { Injectable, Inject } from '@nestjs/common';
import { IEmailJobRepository, EMAIL_JOB_REPOSITORY } from '../../domain/ports/repositories/email-job.repository';
import { IEmailSender, EMAIL_SENDER_PORT } from '../../domain/ports/services/email-sender.port';
import { IEmailTemplateRepository, EMAIL_TEMPLATE_REPOSITORY } from '../../domain/ports/repositories/email-template.repository';
import { EmailSendFailedException } from '../../domain/exceptions/domain.exceptions';

@Injectable()
export class ProcessEmailQueueUseCase {
  constructor(
    @Inject(EMAIL_JOB_REPOSITORY) private readonly jobRepo: IEmailJobRepository,
    @Inject(EMAIL_SENDER_PORT) private readonly emailSender: IEmailSender,
    @Inject(EMAIL_TEMPLATE_REPOSITORY) private readonly templateRepo: IEmailTemplateRepository,
  ) {}

  async execute(jobId: string): Promise<void> {
    const emailJob = await this.jobRepo.findById(jobId);
    if (!emailJob) return;

    emailJob.markAsSending();
    await this.jobRepo.update(emailJob);

    try {
      let htmlBody = emailJob.htmlBody;
      let textBody = emailJob.textBody;
      let subject = emailJob.subject;

      if (emailJob.templateId) {
        const template = await this.templateRepo.findById(emailJob.templateId);
        if (template) {
          subject = template.subject;
          htmlBody = template.htmlContent;
          textBody = template.textContent;
        }
      }

      const result = await this.emailSender.send({
        to: emailJob.to,
        cc: emailJob.cc,
        bcc: emailJob.bcc,
        subject,
        htmlBody,
        textBody,
        from: { email: emailJob.fromEmail, name: emailJob.fromName },
        replyTo: emailJob.replyTo,
        tags: emailJob.tags,
      });

      emailJob.markAsSent(result.messageId);
      await this.jobRepo.update(emailJob);
    } catch (error) {
      emailJob.markAsFailed(error.message);
      await this.jobRepo.update(emailJob);
      if (!emailJob.canRetry()) {
        throw new EmailSendFailedException(error.message);
      }
    }
  }
}
