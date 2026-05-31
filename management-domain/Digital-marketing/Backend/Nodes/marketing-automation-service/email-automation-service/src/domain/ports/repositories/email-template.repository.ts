import { EmailTemplate } from '../models/email-template';
export const EMAIL_TEMPLATE_REPOSITORY = Symbol('EMAIL_TEMPLATE_REPOSITORY');
export interface IEmailTemplateRepository {
  save(template: EmailTemplate): Promise<EmailTemplate>;
  findById(id: string): Promise<EmailTemplate | null>;
  findBySlug(slug: string, tenantId: string): Promise<EmailTemplate | null>;
  findByTenantId(tenantId: string, options?: { category?: string; isActive?: boolean }): Promise<EmailTemplate[]>;
  update(template: EmailTemplate): Promise<EmailTemplate>;
  delete(id: string): Promise<boolean>;
}
