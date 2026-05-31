import { Injectable, Inject } from '@nestjs/common';
import { SendEmailUseCase } from '../use-cases/send-email.use-case';
import { SendBatchEmailUseCase } from '../use-cases/send-batch-email.use-case';
import { ManageTemplatesUseCase } from '../use-cases/manage-templates.use-case';
import { ProcessEmailQueueUseCase } from '../use-cases/process-email-queue.use-case';
import { HandleWebhookUseCase } from '../use-cases/handle-webhook.use-case';
import { SendEmailDto, SendBatchEmailDto } from '../dtos/send-email.dto';
import { CreateEmailTemplateDto, UpdateEmailTemplateDto } from '../dtos/email-template.dto';
import { WebhookEventDto } from '../dtos/webhook-event.dto';

@Injectable()
export class EmailOrchestrationService {
  constructor(
    private readonly sendEmailUseCase: SendEmailUseCase,
    private readonly sendBatchEmailUseCase: SendBatchEmailUseCase,
    private readonly manageTemplatesUseCase: ManageTemplatesUseCase,
    private readonly processEmailQueueUseCase: ProcessEmailQueueUseCase,
    private readonly handleWebhookUseCase: HandleWebhookUseCase,
  ) {}

  async sendEmail(dto: SendEmailDto, tenantId: string) { return this.sendEmailUseCase.execute(dto, tenantId); }
  async sendBatchEmail(dto: SendBatchEmailDto, tenantId: string) { return this.sendBatchEmailUseCase.execute(dto, tenantId); }
  async createTemplate(dto: CreateEmailTemplateDto, tenantId: string) { return this.manageTemplatesUseCase.create(dto, tenantId); }
  async getTemplates(tenantId: string, category?: string) { return this.manageTemplatesUseCase.findAll(tenantId, category); }
  async getTemplate(id: string) { return this.manageTemplatesUseCase.findById(id); }
  async updateTemplate(id: string, dto: UpdateEmailTemplateDto) { return this.manageTemplatesUseCase.update(id, dto); }
  async deleteTemplate(id: string) { return this.manageTemplatesUseCase.delete(id); }
  async processQueueJob(jobId: string) { return this.processEmailQueueUseCase.execute(jobId); }
  async handleWebhook(events: WebhookEventDto[], tenantId: string) { return this.handleWebhookUseCase.execute(events, tenantId); }
}
