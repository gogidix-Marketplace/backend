export interface SegmentCriteria {
  field: string;
  operator: 'equals' | 'not_equals' | 'contains' | 'greater_than' | 'less_than' | 'in' | 'between';
  value: any;
  valueTo?: any;
}

export interface SegmentProps {
  id?: string;
  tenantId: string;
  name: string;
  description?: string;
  criteria: SegmentCriteria[];
  criteriaLogic: 'and' | 'or';
  minScore?: number;
  maxScore?: number;
  memberCount: number;
  isActive: boolean;
  autoAssign: boolean;
  createdAt?: Date;
  updatedAt?: Date;
}

export class Segment {
  private readonly props: SegmentProps;

  constructor(props: SegmentProps) {
    this.props = {
      ...props,
      criteria: props.criteria ?? [],
      criteriaLogic: props.criteriaLogic ?? 'and',
      memberCount: props.memberCount ?? 0,
      isActive: props.isActive ?? true,
      autoAssign: props.autoAssign ?? false,
      createdAt: props.createdAt ?? new Date(),
      updatedAt: new Date(),
    };
  }

  get id(): string | undefined { return this.props.id; }
  get tenantId(): string { return this.props.tenantId; }
  get name(): string { return this.props.name; }
  get description(): string | undefined { return this.props.description; }
  get criteria(): SegmentCriteria[] { return this.props.criteria; }
  get criteriaLogic(): string { return this.props.criteriaLogic; }
  get minScore(): number | undefined { return this.props.minScore; }
  get maxScore(): number | undefined { return this.props.maxScore; }
  get memberCount(): number { return this.props.memberCount; }
  get isActive(): boolean { return this.props.isActive; }
  get autoAssign(): boolean { return this.props.autoAssign; }
  get createdAt(): Date | undefined { return this.props.createdAt; }
  get updatedAt(): Date | undefined { return this.props.updatedAt; }

  matches(data: Record<string, any>, score?: number): boolean {
    if (!this.props.isActive) return false;
    if (score !== undefined) {
      if (this.props.minScore !== undefined && score < this.props.minScore) return false;
      if (this.props.maxScore !== undefined && score > this.props.maxScore) return false;
    }
    if (this.props.criteria.length === 0) return true;
    const results = this.props.criteria.map(c => this.evaluateCriteria(c, data));
    return this.props.criteriaLogic === 'and' ? results.every(Boolean) : results.some(Boolean);
  }

  private evaluateCriteria(criteria: SegmentCriteria, data: Record<string, any>): boolean {
    const fieldValue = data[criteria.field];
    switch (criteria.operator) {
      case 'equals': return fieldValue === criteria.value;
      case 'not_equals': return fieldValue !== criteria.value;
      case 'contains': return String(fieldValue ?? '').includes(String(criteria.value));
      case 'greater_than': return Number(fieldValue) > Number(criteria.value);
      case 'less_than': return Number(fieldValue) < Number(criteria.value);
      case 'in': return Array.isArray(criteria.value) && criteria.value.includes(fieldValue);
      case 'between': return Number(fieldValue) >= Number(criteria.value) && Number(fieldValue) <= Number(criteria.valueTo);
      default: return false;
    }
  }

  incrementMembers(): void { this.props.memberCount += 1; this.props.updatedAt = new Date(); }
  decrementMembers(): void { this.props.memberCount = Math.max(0, this.props.memberCount - 1); this.props.updatedAt = new Date(); }

  toPlainObject(): SegmentProps { return { ...this.props }; }
}
