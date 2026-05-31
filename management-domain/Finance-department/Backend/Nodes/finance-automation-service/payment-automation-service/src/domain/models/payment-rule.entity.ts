import { BaseEntity } from '@shared/base/base.entity';
import { Money } from './value-objects/money.value-object';
import { RuleType } from './enums/rule-type.enum';

export interface RuleCondition {
  field: string;
  operator: 'eq' | 'ne' | 'gt' | 'gte' | 'lt' | 'lte' | 'in' | 'contains' | 'regex';
  value: any;
  logicalOperator?: 'AND' | 'OR';
}

export interface RuleAction {
  type: 'APPROVE' | 'REJECT' | 'ESCALATE' | 'SCHEDULE' | 'SET_GATEWAY' | 'ADD_TAG';
  parameters?: Record<string, any>;
}

export class PaymentRule extends BaseEntity {
  private _name: string;
  private _description: string;
  private _ruleType: RuleType;
  private _conditions: RuleCondition[];
  private _actions: RuleAction[];
  private _priority: number;
  private _isActive: boolean;
  private _effectiveFrom?: Date;
  private _effectiveTo?: Date;
  private _metadata: Record<string, any>;
  private _matchCount: number;
  private _lastMatchedAt?: Date;
  private _createdBy: string;

  private constructor(
    name: string,
    ruleType: RuleType,
    conditions: RuleCondition[],
    actions: RuleAction[],
    tenantId: string,
    createdBy: string,
  ) {
    super(tenantId);
    this._name = name;
    this._description = '';
    this._ruleType = ruleType;
    this._conditions = conditions;
    this._actions = actions;
    this._priority = 0;
    this._isActive = true;
    this._metadata = {};
    this._matchCount = 0;
    this._createdBy = createdBy;
  }

  static create(
    name: string,
    ruleType: RuleType,
    conditions: RuleCondition[],
    actions: RuleAction[],
    tenantId: string,
    createdBy: string,
  ): PaymentRule {
    if (conditions.length === 0) {
      throw new Error('Rule must have at least one condition');
    }
    if (actions.length === 0) {
      throw new Error('Rule must have at least one action');
    }
    return new PaymentRule(name, ruleType, conditions, actions, tenantId, createdBy);
  }

  // Getters
  get name(): string {
    return this._name;
  }

  get description(): string {
    return this._description;
  }

  get ruleType(): RuleType {
    return this._ruleType;
  }

  get conditions(): RuleCondition[] {
    return [...this._conditions];
  }

  get actions(): RuleAction[] {
    return [...this._actions];
  }

  get priority(): number {
    return this._priority;
  }

  get isActive(): boolean {
    return this._isActive;
  }

  get effectiveFrom(): Date | undefined {
    return this._effectiveFrom;
  }

  get effectiveTo(): Date | undefined {
    return this._effectiveTo;
  }

  get metadata(): Record<string, any> {
    return { ...this._metadata };
  }

  get matchCount(): number {
    return this._matchCount;
  }

  get lastMatchedAt(): Date | undefined {
    return this._lastMatchedAt;
  }

  get createdBy(): string {
    return this._createdBy;
  }

  // Business methods
  evaluate(data: Record<string, any>): boolean {
    if (!this._isActive) {
      return false;
    }

    if (!this.isEffective()) {
      return false;
    }

    let result = true;
    let currentOperator: 'AND' | 'OR' = 'AND';

    for (let i = 0; i < this._conditions.length; i++) {
      const condition = this._conditions[i];
      const conditionResult = this.evaluateCondition(condition, data);

      if (i === 0) {
        result = conditionResult;
      } else if (condition.logicalOperator || currentOperator) {
        const op = condition.logicalOperator || currentOperator;
        if (op === 'AND') {
          result = result && conditionResult;
        } else {
          result = result || conditionResult;
        }
      }

      if (condition.logicalOperator) {
        currentOperator = condition.logicalOperator;
      }
    }

    if (result) {
      this._matchCount++;
      this._lastMatchedAt = new Date();
      this.updateTimestamp();
    }

    return result;
  }

  private evaluateCondition(condition: RuleCondition, data: Record<string, any>): boolean {
    const fieldValue = this.getNestedValue(data, condition.field);

    switch (condition.operator) {
      case 'eq':
        return fieldValue === condition.value;
      case 'ne':
        return fieldValue !== condition.value;
      case 'gt':
        return this.compareValues(fieldValue, condition.value) > 0;
      case 'gte':
        return this.compareValues(fieldValue, condition.value) >= 0;
      case 'lt':
        return this.compareValues(fieldValue, condition.value) < 0;
      case 'lte':
        return this.compareValues(fieldValue, condition.value) <= 0;
      case 'in':
        return Array.isArray(condition.value) && condition.value.includes(fieldValue);
      case 'contains':
        return typeof fieldValue === 'string' && fieldValue.includes(condition.value);
      case 'regex':
        return new RegExp(condition.value).test(String(fieldValue));
      default:
        return false;
    }
  }

  private compareValues(a: any, b: any): number {
    const numA = typeof a === 'number' ? a : parseFloat(a);
    const numB = typeof b === 'number' ? b : parseFloat(b);
    if (isNaN(numA) || isNaN(numB)) {
      return String(a).localeCompare(String(b));
    }
    return numA - numB;
  }

  private getNestedValue(obj: Record<string, any>, path: string): any {
    return path.split('.').reduce((current, key) => current?.[key], obj);
  }

  isEffective(): boolean {
    const now = new Date();

    if (this._effectiveFrom && this._effectiveFrom > now) {
      return false;
    }

    if (this._effectiveTo && this._effectiveTo < now) {
      return false;
    }

    return true;
  }

  activate(): void {
    this._isActive = true;
    this.updateTimestamp();
  }

  deactivate(): void {
    this._isActive = false;
    this.updateTimestamp();
  }

  setDescription(description: string): void {
    this._description = description;
    this.updateTimestamp();
  }

  setPriority(priority: number): void {
    if (priority < 0 || priority > 100) {
      throw new Error('Priority must be between 0 and 100');
    }
    this._priority = priority;
    this.updateTimestamp();
  }

  setEffectivePeriod(from?: Date, to?: Date): void {
    if (from && to && from > to) {
      throw new Error('Effective period start must be before end');
    }
    this._effectiveFrom = from;
    this._effectiveTo = to;
    this.updateTimestamp();
  }

  addCondition(condition: RuleCondition): void {
    this._conditions.push(condition);
    this.updateTimestamp();
  }

  removeCondition(index: number): void {
    if (index < 0 || index >= this._conditions.length) {
      throw new Error('Invalid condition index');
    }
    this._conditions.splice(index, 1);
    this.updateTimestamp();
  }

  addAction(action: RuleAction): void {
    this._actions.push(action);
    this.updateTimestamp();
  }

  removeAction(index: number): void {
    if (index < 0 || index >= this._actions.length) {
      throw new Error('Invalid action index');
    }
    this._actions.splice(index, 1);
    this.updateTimestamp();
  }

  updateMetadata(metadata: Record<string, any>): void {
    this._metadata = { ...this._metadata, ...metadata };
    this.updateTimestamp();
  }

  resetMatchCount(): void {
    this._matchCount = 0;
    this._lastMatchedAt = undefined;
    this.updateTimestamp();
  }

  toJSON(): Record<string, any> {
    return {
      id: this.id,
      name: this._name,
      description: this._description,
      ruleType: this._ruleType,
      conditions: this._conditions,
      actions: this._actions,
      priority: this._priority,
      isActive: this._isActive,
      effectiveFrom: this._effectiveFrom?.toISOString(),
      effectiveTo: this._effectiveTo?.toISOString(),
      metadata: this._metadata,
      matchCount: this._matchCount,
      lastMatchedAt: this._lastMatchedAt?.toISOString(),
      createdBy: this._createdBy,
      createdAt: this.createdAt.toISOString(),
      updatedAt: this.updatedAt.toISOString(),
      tenantId: this.tenantId,
      version: this.version,
    };
  }

  static fromJSON(json: Record<string, any>): PaymentRule {
    const rule = new PaymentRule(
      json.name,
      json.ruleType,
      json.conditions,
      json.actions,
      json.tenantId,
      json.createdBy,
    );

    rule['id'] = json.id;
    rule['_description'] = json.description;
    rule['_priority'] = json.priority;
    rule['_isActive'] = json.isActive;
    rule['_effectiveFrom'] = json.effectiveFrom ? new Date(json.effectiveFrom) : undefined;
    rule['_effectiveTo'] = json.effectiveTo ? new Date(json.effectiveTo) : undefined;
    rule['_metadata'] = json.metadata;
    rule['_matchCount'] = json.matchCount;
    rule['_lastMatchedAt'] = json.lastMatchedAt ? new Date(json.lastMatchedAt) : undefined;
    rule['createdAt'] = new Date(json.createdAt);
    rule['updatedAt'] = new Date(json.updatedAt);
    rule['version'] = json.version;

    return rule;
  }
}
