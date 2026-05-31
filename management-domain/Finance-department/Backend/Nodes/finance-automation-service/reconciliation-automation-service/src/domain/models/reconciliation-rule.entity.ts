import { UUID } from 'crypto';
import { MatchType } from '../enums/match-type.enum';

export interface ReconciliationRuleProps {
  id?: UUID;
  tenantId: string;
  name: string;
  description?: string;
  matchType: MatchType;
  priority: number;
  enabled: boolean;
  conditions: RuleCondition[];
  actions: RuleAction[];
  confidenceThreshold?: number;
  createdBy: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface RuleCondition {
  field: string;
  operator: 'equals' | 'contains' | 'startsWith' | 'endsWith' | 'greaterThan' | 'lessThan' | 'between';
  value: unknown;
  weight?: number;
}

export interface RuleAction {
  type: 'AUTO_MATCH' | 'FLAG_FOR_REVIEW' | 'AUTO_RESOLVE' | 'SEND_NOTIFICATION';
  params?: Record<string, unknown>;
}

export class ReconciliationRule {
  readonly props: ReconciliationRuleProps;

  constructor(props: ReconciliationRuleProps) {
    this.props = {
      ...props,
      id: props.id || crypto.randomUUID(),
      createdAt: props.createdAt || new Date(),
      updatedAt: props.updatedAt || new Date(),
    };
  }

  get id(): UUID {
    return this.props.id!;
  }

  get tenantId(): string {
    return this.props.tenantId;
  }

  get enabled(): boolean {
    return this.props.enabled;
  }

  get matchType(): MatchType {
    return this.props.matchType;
  }

  get priority(): number {
    return this.props.priority;
  }

  get conditions(): RuleCondition[] {
    return this.props.conditions;
  }

  get actions(): RuleAction[] {
    return this.props.actions;
  }

  get confidenceThreshold(): number | undefined {
    return this.props.confidenceThreshold;
  }

  enable(): void {
    this.props.enabled = true;
    this.props.updatedAt = new Date();
  }

  disable(): void {
    this.props.enabled = false;
    this.props.updatedAt = new Date();
  }

  updatePriority(priority: number): void {
    if (priority < 1 || priority > 100) {
      throw new Error('Priority must be between 1 and 100');
    }
    this.props.priority = priority;
    this.props.updatedAt = new Date();
  }

  evaluate(data: Record<string, unknown>): EvaluationResult {
    const results = this.props.conditions.map((condition) => {
      const fieldValue = this.getFieldValue(data, condition.field);
      return {
        condition,
        matched: this.evaluateCondition(fieldValue, condition),
        weight: condition.weight || 1,
      };
    });

    const totalWeight = results.reduce((sum, r) => sum + r.weight, 0);
    const matchedWeight = results
      .filter((r) => r.matched)
      .reduce((sum, r) => sum + r.weight, 0);

    const confidence = totalWeight > 0 ? matchedWeight / totalWeight : 0;

    return {
      matched: confidence >= (this.props.confidenceThreshold || 0.8),
      confidence,
      results,
    };
  }

  private getFieldValue(data: Record<string, unknown>, field: string): unknown {
    return field.split('.').reduce((obj: unknown, key: string) => {
      if (obj && typeof obj === 'object' && key in obj) {
        return (obj as Record<string, unknown>)[key];
      }
      return undefined;
    }, data);
  }

  private evaluateCondition(fieldValue: unknown, condition: RuleCondition): boolean {
    const value = condition.value;

    switch (condition.operator) {
      case 'equals':
        return fieldValue === value;
      case 'contains':
        return typeof fieldValue === 'string' && typeof value === 'string'
          ? fieldValue.includes(value)
          : false;
      case 'startsWith':
        return typeof fieldValue === 'string' && typeof value === 'string'
          ? fieldValue.startsWith(value)
          : false;
      case 'endsWith':
        return typeof fieldValue === 'string' && typeof value === 'string'
          ? fieldValue.endsWith(value)
          : false;
      case 'greaterThan':
        return typeof fieldValue === 'number' && typeof value === 'number'
          ? fieldValue > value
          : false;
      case 'lessThan':
        return typeof fieldValue === 'number' && typeof value === 'number'
          ? fieldValue < value
          : false;
      case 'between':
        if (
          Array.isArray(value) &&
          value.length === 2 &&
          typeof fieldValue === 'number'
        ) {
          return fieldValue >= value[0] && fieldValue <= value[1];
        }
        return false;
      default:
        return false;
    }
  }

  toJSON() {
    return {
      id: this.props.id,
      tenantId: this.props.tenantId,
      name: this.props.name,
      description: this.props.description,
      matchType: this.props.matchType,
      priority: this.props.priority,
      enabled: this.props.enabled,
      conditions: this.props.conditions,
      actions: this.props.actions,
      confidenceThreshold: this.props.confidenceThreshold,
      createdBy: this.props.createdBy,
      createdAt: this.props.createdAt,
      updatedAt: this.props.updatedAt,
    };
  }
}

export interface EvaluationResult {
  matched: boolean;
  confidence: number;
  results: Array<{
    condition: RuleCondition;
    matched: boolean;
    weight: number;
  }>;
}
