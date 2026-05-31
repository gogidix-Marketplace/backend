import { v4 as uuidv4 } from 'uuid';
import { ScoreRuleType, ScoreRuleOperator } from './enums/score-type.enum';

export interface RuleCondition {
  field: string;
  operator: ScoreRuleOperator;
  value: any;
  weight?: number;
}

export interface RuleFormula {
  expression: string;
  variables: string[];
}

export class ScoreRule {
  private _id: string;
  private _tenantId: string;
  private _scoreModelId: string;
  private _name: string;
  private _description: string;
  private _ruleType: ScoreRuleType;
  private _conditions: RuleCondition[];
  private _formula: RuleFormula | null;
  private _baseScore: number;
  private _maxScore: number;
  private _priority: number;
  private _isActive: boolean;
  private _category: string;
  private _tags: string[];
  private _metadata: Record<string, any>;
  private _createdAt: Date;
  private _updatedAt: Date;

  constructor(props: {
    id?: string;
    tenantId: string;
    scoreModelId: string;
    name: string;
    description: string;
    ruleType: ScoreRuleType;
    conditions: RuleCondition[];
    formula?: RuleFormula | null;
    baseScore: number;
    maxScore: number;
    priority?: number;
    isActive?: boolean;
    category?: string;
    tags?: string[];
    metadata?: Record<string, any>;
  }) {
    this._id = props.id || uuidv4();
    this._tenantId = props.tenantId;
    this._scoreModelId = props.scoreModelId;
    this._name = props.name;
    this._description = props.description;
    this._ruleType = props.ruleType;
    this._conditions = props.conditions;
    this._formula = props.formula ?? null;
    this._baseScore = props.baseScore;
    this._maxScore = props.maxScore;
    this._priority = props.priority ?? 0;
    this._isActive = props.isActive ?? true;
    this._category = props.category ?? 'general';
    this._tags = props.tags ?? [];
    this._metadata = props.metadata ?? {};
    this._createdAt = new Date();
    this._updatedAt = new Date();
  }

  // Getters
  get id(): string { return this._id; }
  get tenantId(): string { return this._tenantId; }
  get scoreModelId(): string { return this._scoreModelId; }
  get name(): string { return this._name; }
  get description(): string { return this._description; }
  get ruleType(): ScoreRuleType { return this._ruleType; }
  get conditions(): RuleCondition[] { return this._conditions; }
  get formula(): RuleFormula | null { return this._formula; }
  get baseScore(): number { return this._baseScore; }
  get maxScore(): number { return this._maxScore; }
  get priority(): number { return this._priority; }
  get isActive(): boolean { return this._isActive; }
  get category(): string { return this._category; }
  get tags(): string[] { return this._tags; }
  get metadata(): Record<string, any> { return this._metadata; }
  get createdAt(): Date { return this._createdAt; }
  get updatedAt(): Date { return this._updatedAt; }

  // Domain Methods
  activate(): void {
    this._isActive = true;
    this._updatedAt = new Date();
  }

  deactivate(): void {
    this._isActive = false;
    this._updatedAt = new Date();
  }

  updateScore(baseScore: number, maxScore: number): void {
    if (baseScore < 0 || maxScore < 0) {
      throw new Error('Scores cannot be negative');
    }
    if (baseScore > maxScore) {
      throw new Error('Base score cannot exceed max score');
    }
    this._baseScore = baseScore;
    this._maxScore = maxScore;
    this._updatedAt = new Date();
  }

  addCondition(condition: RuleCondition): void {
    this._conditions.push(condition);
    this._updatedAt = new Date();
  }

  removeCondition(conditionIndex: number): void {
    if (conditionIndex >= 0 && conditionIndex < this._conditions.length) {
      this._conditions.splice(conditionIndex, 1);
      this._updatedAt = new Date();
    }
  }

  updateCondition(conditionIndex: number, condition: RuleCondition): void {
    if (conditionIndex >= 0 && conditionIndex < this._conditions.length) {
      this._conditions[conditionIndex] = condition;
      this._updatedAt = new Date();
    }
  }

  setPriority(priority: number): void {
    this._priority = priority;
    this._updatedAt = new Date();
  }

  addTag(tag: string): void {
    if (!this._tags.includes(tag)) {
      this._tags.push(tag);
      this._updatedAt = new Date();
    }
  }

  removeTag(tag: string): void {
    this._tags = this._tags.filter(t => t !== tag);
    this._updatedAt = new Date();
  }

  evaluate(attributes: Record<string, any>): number {
    if (!this._isActive) {
      return 0;
    }

    if (this._conditions.length === 0) {
      return this._baseScore;
    }

    let totalScore = 0;
    let conditionsMet = 0;

    for (const condition of this._conditions) {
      if (this.evaluateCondition(condition, attributes)) {
        totalScore += condition.weight ?? this._baseScore;
        conditionsMet++;
      }
    }

    // If all conditions must be met (AND logic)
    if (this._metadata.requireAllConditions && conditionsMet < this._conditions.length) {
      return 0;
    }

    return Math.min(totalScore, this._maxScore);
  }

  private evaluateCondition(condition: RuleCondition, attributes: Record<string, any>): boolean {
    const fieldValue = attributes[condition.field];
    const conditionValue = condition.value;

    switch (condition.operator) {
      case ScoreRuleOperator.EQUALS:
        return fieldValue === conditionValue;
      case ScoreRuleOperator.NOT_EQUALS:
        return fieldValue !== conditionValue;
      case ScoreRuleOperator.GREATER_THAN:
        return fieldValue > conditionValue;
      case ScoreRuleOperator.LESS_THAN:
        return fieldValue < conditionValue;
      case ScoreRuleOperator.GREATER_THAN_OR_EQUAL:
        return fieldValue >= conditionValue;
      case ScoreRuleOperator.LESS_THAN_OR_EQUAL:
        return fieldValue <= conditionValue;
      case ScoreRuleOperator.CONTAINS:
        return Array.isArray(fieldValue)
          ? fieldValue.includes(conditionValue)
          : String(fieldValue).includes(conditionValue);
      case ScoreRuleOperator.NOT_CONTAINS:
        return Array.isArray(fieldValue)
          ? !fieldValue.includes(conditionValue)
          : !String(fieldValue).includes(conditionValue);
      case ScoreRuleOperator.STARTS_WITH:
        return String(fieldValue).startsWith(conditionValue);
      case ScoreRuleOperator.ENDS_WITH:
        return String(fieldValue).endsWith(conditionValue);
      case ScoreRuleOperator.IN:
        return Array.isArray(conditionValue) && conditionValue.includes(fieldValue);
      case ScoreRuleOperator.NOT_IN:
        return Array.isArray(conditionValue) && !conditionValue.includes(fieldValue);
      case ScoreRuleOperator.BETWEEN:
        return Array.isArray(conditionValue) &&
          fieldValue >= conditionValue[0] &&
          fieldValue <= conditionValue[1];
      case ScoreRuleOperator.REGEX:
        return new RegExp(conditionValue).test(String(fieldValue));
      default:
        return false;
    }
  }

  toPrimitives(): Record<string, any> {
    return {
      id: this._id,
      tenantId: this._tenantId,
      scoreModelId: this._scoreModelId,
      name: this._name,
      description: this._description,
      ruleType: this._ruleType,
      conditions: this._conditions,
      formula: this._formula,
      baseScore: this._baseScore,
      maxScore: this._maxScore,
      priority: this._priority,
      isActive: this._isActive,
      category: this._category,
      tags: this._tags,
      metadata: this._metadata,
      createdAt: this._createdAt,
      updatedAt: this._updatedAt,
    };
  }

  static fromPrimitives(data: Record<string, any>): ScoreRule {
    const rule = new ScoreRule({
      id: data.id,
      tenantId: data.tenantId,
      scoreModelId: data.scoreModelId,
      name: data.name,
      description: data.description,
      ruleType: data.ruleType,
      conditions: data.conditions,
      formula: data.formula,
      baseScore: data.baseScore,
      maxScore: data.maxScore,
      priority: data.priority,
      isActive: data.isActive,
      category: data.category,
      tags: data.tags,
      metadata: data.metadata,
    });

    rule._createdAt = new Date(data.createdAt);
    rule._updatedAt = new Date(data.updatedAt);

    return rule;
  }
}
