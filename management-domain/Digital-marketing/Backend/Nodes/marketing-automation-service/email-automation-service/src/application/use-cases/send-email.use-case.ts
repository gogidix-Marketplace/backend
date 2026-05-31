import { Injectable, Inject } from '@nestjs/common';
import { EmailJob } from '../../domain/models/email-job';
import { IEmailJobRepository, EMAIL_JOB_REPOSITORY } from '../../domain/ports/repositories/email-job.repository';
import { IQueueService, QUEUE_SERVICE_PORT } from '../../domain/ports/services/queue-service.port';
import { SendEmailDto } from '../dtos/send-email.dto';

@Injectable()
export class SendEmailUseCase {
  constructor(
    @Inject(EMAIL_JOB_REPOSITORY) private readonly jobRepo: IEmailJobRepository,
    @Inject(QUEUE_SERVICE_PORT) private readonly queueService: IQueueService,
  ) {}

  async execute(dto: SendEmailDto, tenantId: string): Promise<EmailJob> {
    const job = new EmailJob({
      tenantId,
      to: dto.to,
      cc: dto.cc,
      bcc: dto.bcc,
      subject: dto.subject,
      htmlBody: dto.htmlBody,
      textBody: dto.textBody,
      templateId: dto.templateId,
      templateData: dto.templateData,
      fromEmail: dto.fromEmail ?? process.env.DEFAULT_FROM_EMAIL ?? 'noreply@example.com',
      fromName: dto.fromName,
      replyTo: dto.replyTo,
      provider: dto.provider,
      scheduledAt: dto.scheduledAt,
      tags: dto.tags,
      metadata: dto.metadata,
    });

    const saved = await this.jobRepo.save(job);

    if (dto.scheduledAt && dto.scheduledAt > new Date()) {
      const delay = dto.scheduledAt.getTime() - Date.now();
      await this.queueService.addJob('email-processing', { jobId: saved.id }, { delay });
    } else {
      await this.queueService.addJob('email-processing', { jobId: saved.id });
    }

    return saved;
  }
}
