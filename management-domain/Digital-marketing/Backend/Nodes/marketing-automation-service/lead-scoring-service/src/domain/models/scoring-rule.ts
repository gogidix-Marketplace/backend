export interface ScoringCondition {
  field: string;
  operator: 'equals' | 'not_equals' | 'contains' | 'not_contains' | 'greater_than' | 'less_than' | 'in' | 'not_in' | 'exists' | 'not_exists' | 'starts_with' | 'ends_with';
  value: any;
}

export interface ScoringRuleProps {
  id?: string;
  tenantId: string;
  name: string;
  description?: string;
  category: string;
  conditions: ScoringCondition[];
  conditionLogic: 'and' | 'or';
  points: number;
  maxPoints?: number;
  priority: number;
  isActive: boolean;
  validFrom?: Date;
  validTo?: Date;
  applicationCount: number;
  lastAppliedAt?: Date;
  createdAt?: Date;
  updatedAt?: Date;
}

export class ScoringRule {
  private readonly props: ScoringRuleProps;

  constructor(props: ScoringRuleProps) {
    this.props = {
      ...props,
      conditions: props.conditions ?? [],
      conditionLogic: props.conditionLogic ?? 'and',
      isActive: props.isActive ?? true,
      applicationCount: props.applicationCount ?? 0,
      createdAt: props.createdAt ?? new Date(),
      updatedAt: new Date(),
    };
  }

  get id(): string | undefined { return this.props.id; }
  get tenantId(): string { return this.props.tenantId; }
  get name(): string { return this.props.name; }
  get description(): string | undefined { return this.props.description; }
  get category(): string { return this.props.category; }
  get conditions(): ScoringCondition[] { return this.props.conditions; }
  get conditionLogic(): string { return this.props.conditionLogic; }
  get points(): number { return this.props.points; }
  get maxPoints(): number | undefined { return this.props.maxPoints; }
  get priority(): number { return this.props.priority; }
  get isActive(): boolean { return this.props.isActive; }
  get validFrom(): Date | undefined { return this.props.validFrom; }
  get validTo(): Date | undefined { return this.props.validTo; }
  get applicationCount(): number { return this.props.applicationCount; }
  get lastAppliedAt(): Date | undefined { return this.props.lastAppliedAt; }
  get createdAt(): Date | undefined { return this.props.createdAt; }
  get updatedAt(): Date | undefined { return this.props.updatedAt; }

  evaluate(data: Record<string, any>): boolean {
    const now = new Date();
    if (this.props.validFrom && now < this.props.validFrom) return false;
    if (this.props.validTo && now > this.props.validTo) return false;
    if (!this.props.isActive) return false;

    const results = this.props.conditions.map(c => this.evaluateCondition(c, data));
    return this.props.conditionLogic === 'and' ? results.every(Boolean) : results.some(Boolean);
  }

  private evaluateCondition(condition: ScoringCondition, data: Record<string, any>): boolean {
    const fieldValue = data[condition.field];
    switch (condition.operator) {
      case 'equals': return fieldValue === condition.value;
      case 'not_equals': return fieldValue !== condition.value;
      case 'contains': return String(fieldValue ?? '').includes(String(condition.value));
      case 'not_contains': return !String(fieldValue ?? '').includes(String(condition.value));
      case 'greater_than': return Number(fieldValue) > Number(condition.value);
      case 'less_than': return Number(fieldValue) < Number(condition.value);
      case 'in': return Array.isArray(condition.value) && condition.value.includes(fieldValue);
      case 'not_in': return Array.isArray(condition.value) && !condition.value.includes(fieldValue);
      case 'exists': return fieldValue !== undefined && fieldValue !== null;
      case 'not_exists': return fieldValue === undefined || fieldValue === null;
      case 'starts_with': return String(fieldValue ?? '').startsWith(String(condition.value));
      case 'ends_with': return String(fieldValue ?? '').endsWith(String(condition.value));
      default: return false;
    }
  }

  markApplied(): void {
    this.props.applicationCount += 1;
    this.props.lastAppliedAt = new Date();
    this.props.updatedAt = new Date();
  }

  activate(): void { this.props.isActive = true; this.props.updatedAt = new Date(); }
  deactivate(): void { this.props.isActive = false; this.props.updatedAt = new Date(); }

  toPlainObject(): ScoringRuleProps { return { ...this.props }; }
}
