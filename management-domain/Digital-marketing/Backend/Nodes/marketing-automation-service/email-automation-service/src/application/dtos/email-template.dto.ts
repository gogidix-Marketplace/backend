export class CreateEmailTemplateDto {
  name!: string;
  slug!: string;
  subject!: string;
  htmlContent!: string;
  textContent?: string;
  variables?: string[];
  category?: string;
}
export class UpdateEmailTemplateDto {
  name?: string;
  subject?: string;
  htmlContent?: string;
  textContent?: string;
  variables?: string[];
  category?: string;
  isActive?: boolean;
}
