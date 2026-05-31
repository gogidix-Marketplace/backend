export interface EmailTemplateProps {
  id?: string;
  tenantId: string;
  name: string;
  slug: string;
  subject: string;
  htmlContent: string;
  textContent?: string;
  variables: string[];
  category?: string;
  isActive: boolean;
  version: number;
  createdAt?: Date;
  updatedAt?: Date;
}

export class EmailTemplate {
  private readonly props: EmailTemplateProps;
  constructor(props: EmailTemplateProps) {
    this.props = { ...props, isActive: props.isActive ?? true, version: props.version ?? 1, variables: props.variables ?? [], createdAt: props.createdAt ?? new Date(), updatedAt: new Date() };
  }
  get id(): string | undefined { return this.props.id; }
  get tenantId(): string { return this.props.tenantId; }
  get name(): string { return this.props.name; }
  get slug(): string { return this.props.slug; }
  get subject(): string { return this.props.subject; }
  get htmlContent(): string { return this.props.htmlContent; }
  get textContent(): string | undefined { return this.props.textContent; }
  get variables(): string[] { return this.props.variables; }
  get category(): string | undefined { return this.props.category; }
  get isActive(): boolean { return this.props.isActive; }
  get version(): number { return this.props.version; }
  get createdAt(): Date | undefined { return this.props.createdAt; }
  get updatedAt(): Date | undefined { return this.props.updatedAt; }
  updateContent(html: string, text?: string): void { this.props.htmlContent = html; if (text) this.props.textContent = text; this.props.version += 1; this.props.updatedAt = new Date(); }
  activate(): void { this.props.isActive = true; this.props.updatedAt = new Date(); }
  deactivate(): void { this.props.isActive = false; this.props.updatedAt = new Date(); }
  toPlainObject(): EmailTemplateProps { return { ...this.props }; }
}
