import { Injectable, Inject } from '@nestjs/common';
import { EmailJob } from '../../domain/models/email-job';
import { IEmailJobRepository, EMAIL_JOB_REPOSITORY } from '../../domain/ports/repositories/email-job.repository';
import { IQueueService, QUEUE_SERVICE_PORT } from '../../domain/ports/services/queue-service.port';
import { SendBatchEmailDto } from '../dtos/send-email.dto';

@Injectable()
export class SendBatchEmailUseCase {
  constructor(
    @Inject(EMAIL_JOB_REPOSITORY) private readonly jobRepo: IEmailJobRepository,
    @Inject(QUEUE_SERVICE_PORT) private readonly queueService: IQueueService,
  ) {}

  async execute(dto: SendBatchEmailDto, tenantId: string): Promise<EmailJob[]> {
    const jobs: EmailJob[] = [];
    for (const emailDto of dto.emails) {
      const job = new EmailJob({
        tenantId,
        to: emailDto.to,
        cc: emailDto.cc,
        bcc: emailDto.bcc,
        subject: emailDto.subject,
        htmlBody: emailDto.htmlBody,
        textBody: emailDto.textBody,
        templateId: emailDto.templateId,
        templateData: emailDto.templateData,
        fromEmail: emailDto.fromEmail ?? process.env.DEFAULT_FROM_EMAIL ?? 'noreply@example.com',
        fromName: emailDto.fromName,
        replyTo: emailDto.replyTo,
        provider: emailDto.provider,
        scheduledAt: emailDto.scheduledAt,
        tags: emailDto.tags,
        metadata: emailDto.metadata,
      });
      const saved = await this.jobRepo.save(job);
      await this.queueService.addJob('email-processing', { jobId: saved.id });
      jobs.push(saved);
    }
    return jobs;
  }
}
