import { Injectable, Inject } from '@nestjs/common';
import { WebhookEventDto } from '../dtos/webhook-event.dto';
import { IEmailJobRepository, EMAIL_JOB_REPOSITORY } from '../../domain/ports/repositories/email-job.repository';
import { IBounceRecordRepository, BOUNCE_RECORD_REPOSITORY } from '../../domain/ports/repositories/bounce-record.repository';
import { IComplaintRecordRepository, COMPLAINT_RECORD_REPOSITORY } from '../../domain/ports/repositories/complaint-record.repository';
import { BounceRecord } from '../../domain/models/bounce-record';
import { ComplaintRecord } from '../../domain/models/complaint-record';

@Injectable()
export class HandleWebhookUseCase {
  constructor(
    @Inject(EMAIL_JOB_REPOSITORY) private readonly jobRepo: IEmailJobRepository,
    @Inject(BOUNCE_RECORD_REPOSITORY) private readonly bounceRepo: IBounceRecordRepository,
    @Inject(COMPLAINT_RECORD_REPOSITORY) private readonly complaintRepo: IComplaintRecordRepository,
  ) {}

  async execute(events: WebhookEventDto[], tenantId: string): Promise<void> {
    for (const event of events) {
      switch (event.event) {
        case 'bounce':
          await this.handleBounce(event, tenantId);
          break;
        case 'complaint':
        case 'spamreport':
          await this.handleComplaint(event, tenantId);
          break;
        case 'delivered':
          break;
        case 'processed':
          break;
      }
    }
  }

  private async handleBounce(event: WebhookEventDto, tenantId: string): Promise<void> {
    const bounce = new BounceRecord({
      tenantId,
      email: event.email,
      bounceType: event.bounceType === 'Permanent' ? 'hard' : 'soft',
      bounceSubType: event.bounceSubType,
      diagnosticCode: event.diagnosticCode,
      providerMessageId: event.sgMessageId ?? event.messageId,
      provider: event.sgMessageId ? 'sendgrid' : 'ses',
    });
    await this.bounceRepo.save(bounce);
  }

  private async handleComplaint(event: WebhookEventDto, tenantId: string): Promise<void> {
    const complaint = new ComplaintRecord({
      tenantId,
      email: event.email,
      complaintType: event.complaintType,
      userAgent: event.userAgent,
      providerMessageId: event.sgMessageId ?? event.messageId,
      provider: event.sgMessageId ? 'sendgrid' : 'ses',
    });
    await this.complaintRepo.save(complaint);
  }
}
