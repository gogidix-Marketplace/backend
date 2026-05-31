import { Injectable, Inject } from '@nestjs/common';
import { EmailTemplate } from '../../domain/models/email-template';
import { IEmailTemplateRepository, EMAIL_TEMPLATE_REPOSITORY } from '../../domain/ports/repositories/email-template.repository';
import { CreateEmailTemplateDto, UpdateEmailTemplateDto } from '../dtos/email-template.dto';
import { TemplateNotFoundException } from '../../domain/exceptions/domain.exceptions';

@Injectable()
export class ManageTemplatesUseCase {
  constructor(
    @Inject(EMAIL_TEMPLATE_REPOSITORY) private readonly templateRepo: IEmailTemplateRepository,
  ) {}

  async create(dto: CreateEmailTemplateDto, tenantId: string): Promise<EmailTemplate> {
    const template = new EmailTemplate({ tenantId, name: dto.name, slug: dto.slug, subject: dto.subject, htmlContent: dto.htmlContent, textContent: dto.textContent, variables: dto.variables ?? [], category: dto.category });
    return this.templateRepo.save(template);
  }

  async findAll(tenantId: string, category?: string): Promise<EmailTemplate[]> {
    return this.templateRepo.findByTenantId(tenantId, { category, isActive: true });
  }

  async findById(id: string): Promise<EmailTemplate> {
    const template = await this.templateRepo.findById(id);
    if (!template) throw new TemplateNotFoundException(id);
    return template;
  }

  async findBySlug(slug: string, tenantId: string): Promise<EmailTemplate> {
    const template = await this.templateRepo.findBySlug(slug, tenantId);
    if (!template) throw new TemplateNotFoundException(slug);
    return template;
  }

  async update(id: string, dto: UpdateEmailTemplateDto): Promise<EmailTemplate> {
    const template = await this.findById(id);
    if (dto.name) template.toPlainObject().name = dto.name;
    if (dto.htmlContent) template.updateContent(dto.htmlContent, dto.textContent);
    if (dto.isActive === false) template.deactivate();
    if (dto.isActive === true) template.activate();
    return this.templateRepo.update(template);
  }

  async delete(id: string): Promise<boolean> {
    return this.templateRepo.delete(id);
  }
}
