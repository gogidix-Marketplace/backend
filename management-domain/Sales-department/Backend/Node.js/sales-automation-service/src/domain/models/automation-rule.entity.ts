import { BaseAggregateRoot } from '@shared/base';
import { TriggerType } from '../enums/trigger-type.enum';
import { ActionType } from '../enums/action-type.enum';
import { AutomationRuleStatus } from '../enums/automation-rule-status.enum';

export interface RuleCondition {
  field: string;
  operator: 'eq' | 'ne' | 'gt' | 'lt' | 'gte' | 'lte' | 'in' | 'nin' | 'contains' | 'startsWith' | 'endsWith' | 'empty' | 'notEmpty';
  value?: any;
  values?: any[];
}

export interface RuleAction {
  type: ActionType;
  order: number;
  parameters: Record<string, any>;
  continueOnError?: boolean;
  delayMs?: number;
  name: string;
}

export interface RuleTrigger {
  type: TriggerType;
  conditions?: RuleCondition[];
  entityTypes?: string[];
  cronExpression?: string;
  debounceMs?: number;
}

export interface LeadScoringRule {
  enabled: boolean;
  conditions: RuleCondition[];
  score: number;
  maxScore?: number;
  category?: 'demographic' | 'behavioral' | 'engagement';
}

export interface DealStageRule {
  enabled: boolean;
  currentStage?: string;
  targetStage?: string;
  conditions?: RuleCondition[];
  autoTransition?: boolean;
  notifyAssignee?: boolean;
}

export class AutomationRule extends BaseAggregateRoot {
  private _name: string;
  private _description: string;
  private _status: AutomationRuleStatus;
  private _trigger: RuleTrigger;
  private _actions: RuleAction[];
  private _leadScoring?: LeadScoringRule;
  private _dealStageRule?: DealStageRule;
  private _tags: string[];
  private _priority: number;
  private _executionCount: number;
  private _lastExecutedAt?: Date;
  private _lastExecutionStatus?: 'SUCCESS' | 'FAILURE' | 'PARTIAL';
  private _category?: string;
  private _templateId?: string;
  private _isEnabled: boolean;
  private _schedule?: {
    frequency: 'once' | 'daily' | 'weekly' | 'monthly' | 'cron';
    cronExpression?: string;
    timezone?: string;
    startDate?: Date;
    endDate?: Date;
  };

  private constructor(
    name: string,
    trigger: RuleTrigger,
    actions: RuleAction[],
    tenantId: string,
    description?: string,
  ) {
    super(tenantId);
    this._name = name;
    this._description = description || '';
    this._status = AutomationRuleStatus.DRAFT;
    this._trigger = trigger;
    this._actions = actions.sort((a, b) => a.order - b.order);
    this._tags = [];
    this._priority = 0;
    this._executionCount = 0;
    this._isEnabled = false;
  }

  static create(
    name: string,
    trigger: RuleTrigger,
    actions: RuleAction[],
    tenantId: string,
    description?: string,
  ): AutomationRule {
    const rule = new AutomationRule(name, trigger, actions, tenantId, description);
    rule.validate();
    return rule;
  }

  static createLeadScoringRule(
    name: string,
    scoringRule: LeadScoringRule,
    tenantId: string,
    description?: string,
  ): AutomationRule {
    const rule = new AutomationRule(
      name,
      { type: TriggerType.CUSTOM },
      [],
      tenantId,
      description,
    );
    rule._leadScoring = scoringRule;
    rule._category = 'lead-scoring';
    return rule;
  }

  static createDealStageRule(
    name: string,
    dealStageRule: DealStageRule,
    actions: RuleAction[],
    tenantId: string,
    description?: string,
  ): AutomationRule {
    const rule = new AutomationRule(
      name,
      { type: TriggerType.DEAL_STAGE_CHANGED },
      actions,
      tenantId,
      description,
    );
    rule._dealStageRule = dealStageRule;
    rule._category = 'deal-stage';
    return rule;
  }

  // Getters
  get name(): string {
    return this._name;
  }

  get description(): string {
    return this._description;
  }

  get status(): AutomationRuleStatus {
    return this._status;
  }

  get trigger(): RuleTrigger {
    return { ...this._trigger };
  }

  get actions(): RuleAction[] {
    return [...this._actions];
  }

  get leadScoring(): LeadScoringRule | undefined {
    return this._leadScoring ? { ...this._leadScoring } : undefined;
  }

  get dealStageRule(): DealStageRule | undefined {
    return this._dealStageRule ? { ...this._dealStageRule } : undefined;
  }

  get tags(): string[] {
    return [...this._tags];
  }

  get priority(): number {
    return this._priority;
  }

  get executionCount(): number {
    return this._executionCount;
  }

  get lastExecutedAt(): Date | undefined {
    return this._lastExecutedAt;
  }

  get lastExecutionStatus(): 'SUCCESS' | 'FAILURE' | 'PARTIAL' | undefined {
    return this._lastExecutionStatus;
  }

  get category(): string | undefined {
    return this._category;
  }

  get templateId(): string | undefined {
    return this._templateId;
  }

  get isEnabled(): boolean {
    return this._isEnabled;
  }

  get schedule(): AutomationRule['_schedule'] {
    return this._schedule ? { ...this._schedule } : undefined;
  }

  // Business methods
  activate(): void {
    if (this._status === AutomationRuleStatus.ACTIVE) {
      throw new Error('Automation rule is already active');
    }
    this._status = AutomationRuleStatus.ACTIVE;
    this._isEnabled = true;
    this.updateTimestamp();
  }

  pause(): void {
    if (this._status !== AutomationRuleStatus.ACTIVE) {
      throw new Error('Only active rules can be paused');
    }
    this._status = AutomationRuleStatus.PAUSED;
    this._isEnabled = false;
    this.updateTimestamp();
  }

  resume(): void {
    if (this._status !== AutomationRuleStatus.PAUSED) {
      throw new Error('Only paused rules can be resumed');
    }
    this._status = AutomationRuleStatus.ACTIVE;
    this._isEnabled = true;
    this.updateTimestamp();
  }

  archive(): void {
    if (this._status === AutomationRuleStatus.ACTIVE) {
      throw new Error('Active rules must be paused before archiving');
    }
    this._status = AutomationRuleStatus.ARCHIVED;
    this._isEnabled = false;
    this.updateTimestamp();
  }

  enable(): void {
    this._isEnabled = true;
    if (this._status === AutomationRuleStatus.DRAFT) {
      this._status = AutomationRuleStatus.ACTIVE;
    }
    this.updateTimestamp();
  }

  disable(): void {
    if (this._status === AutomationRuleStatus.ACTIVE) {
      this._status = AutomationRuleStatus.PAUSED;
    }
    this._isEnabled = false;
    this.updateTimestamp();
  }

  addAction(action: RuleAction): void {
    if (this._status === AutomationRuleStatus.ACTIVE) {
      throw new Error('Cannot add actions to an active rule');
    }
    this._actions.push(action);
    this._actions.sort((a, b) => a.order - b.order);
    this.updateTimestamp();
  }

  removeAction(actionOrder: number): void {
    if (this._status === AutomationRuleStatus.ACTIVE) {
      throw new Error('Cannot remove actions from an active rule');
    }
    this._actions = this._actions.filter(a => a.order !== actionOrder);
    this.updateTimestamp();
  }

  updateAction(actionOrder: number, updatedAction: Partial<RuleAction>): void {
    if (this._status === AutomationRuleStatus.ACTIVE) {
      throw new Error('Cannot update actions in an active rule');
    }
    const index = this._actions.findIndex(a => a.order === actionOrder);
    if (index === -1) {
      throw new Error(`Action with order ${actionOrder} not found`);
    }
    this._actions[index] = { ...this._actions[index], ...updatedAction };
    this.updateTimestamp();
  }

  updateTrigger(trigger: Partial<RuleTrigger>): void {
    if (this._status === AutomationRuleStatus.ACTIVE) {
      throw new Error('Cannot update trigger of an active rule');
    }
    this._trigger = { ...this._trigger, ...trigger };
    this.updateTimestamp();
  }

  setLeadScoringRule(scoringRule: LeadScoringRule): void {
    this._leadScoring = scoringRule;
    this._category = 'lead-scoring';
    this.updateTimestamp();
  }

  setDealStageRule(dealStageRule: DealStageRule): void {
    this._dealStageRule = dealStageRule;
    this._category = 'deal-stage';
    this.updateTimestamp();
  }

  setPriority(priority: number): void {
    if (priority < 0 || priority > 100) {
      throw new Error('Priority must be between 0 and 100');
    }
    this._priority = priority;
    this.updateTimestamp();
  }

  setSchedule(schedule: AutomationRule['_schedule']): void {
    this._schedule = schedule;
    this.updateTimestamp();
  }

  setTemplateId(templateId: string): void {
    this._templateId = templateId;
    this.updateTimestamp();
  }

  addTag(tag: string): void {
    if (!this._tags.includes(tag)) {
      this._tags.push(tag);
      this.updateTimestamp();
    }
  }

  removeTag(tag: string): void {
    this._tags = this._tags.filter(t => t !== tag);
    this.updateTimestamp();
  }

  setTags(tags: string[]): void {
    this._tags = [...tags];
    this.updateTimestamp();
  }

  updateName(name: string): void {
    if (!name || name.trim().length === 0) {
      throw new Error('Rule name cannot be empty');
    }
    this._name = name.trim();
    this.updateTimestamp();
  }

  updateDescription(description: string): void {
    this._description = description;
    this.updateTimestamp();
  }

  canExecute(): boolean {
    return this._status === AutomationRuleStatus.ACTIVE && this._isEnabled;
  }

  shouldTrigger(data: Record<string, any>): boolean {
    if (!this._trigger.conditions || this._trigger.conditions.length === 0) {
      return true;
    }

    return this._trigger.conditions.every(condition => {
      const fieldValue = this.getNestedValue(data, condition.field);

      switch (condition.operator) {
        case 'eq':
          return fieldValue === condition.value;
        case 'ne':
          return fieldValue !== condition.value;
        case 'gt':
          return fieldValue > condition.value;
        case 'lt':
          return fieldValue < condition.value;
        case 'gte':
          return fieldValue >= condition.value;
        case 'lte':
          return fieldValue <= condition.value;
        case 'in':
          return Array.isArray(condition.values) && condition.values.includes(fieldValue);
        case 'nin':
          return Array.isArray(condition.values) && !condition.values.includes(fieldValue);
        case 'contains':
          return typeof fieldValue === 'string' && fieldValue.includes(condition.value);
        case 'startsWith':
          return typeof fieldValue === 'string' && fieldValue.startsWith(condition.value);
        case 'endsWith':
          return typeof fieldValue === 'string' && fieldValue.endsWith(condition.value);
        case 'empty':
          return fieldValue === null || fieldValue === undefined || fieldValue === '';
        case 'notEmpty':
          return fieldValue !== null && fieldValue !== undefined && fieldValue !== '';
        default:
          return false;
      }
    });
  }

  private getNestedValue(obj: any, path: string): any {
    return path.split('.').reduce((current, key) => current?.[key], obj);
  }

  recordExecution(status: 'SUCCESS' | 'FAILURE' | 'PARTIAL'): void {
    this._executionCount++;
    this._lastExecutedAt = new Date();
    this._lastExecutionStatus = status;
    this.updateTimestamp();
  }

  calculateLeadScore(data: Record<string, any>): number {
    if (!this._leadScoring || !this._leadScoring.enabled) {
      return 0;
    }

    const matchedConditions = this._leadScoring.conditions.filter(condition =>
      this.evaluateCondition(data, condition),
    );

    const totalScore = matchedConditions.length * this._leadScoring.score;
    const maxScore = this._leadScoring.maxScore || 100;

    return Math.min(totalScore, maxScore);
  }

  private evaluateCondition(data: Record<string, any>, condition: RuleCondition): boolean {
    const fieldValue = this.getNestedValue(data, condition.field);

    switch (condition.operator) {
      case 'eq':
        return fieldValue === condition.value;
      case 'ne':
        return fieldValue !== condition.value;
      case 'gt':
        return fieldValue > condition.value;
      case 'lt':
        return fieldValue < condition.value;
      case 'gte':
        return fieldValue >= condition.value;
      case 'lte':
        return fieldValue <= condition.value;
      case 'in':
        return Array.isArray(condition.values) && condition.values.includes(fieldValue);
      case 'nin':
        return Array.isArray(condition.values) && !condition.values.includes(fieldValue);
      case 'contains':
        return typeof fieldValue === 'string' && fieldValue.includes(condition.value);
      case 'startsWith':
        return typeof fieldValue === 'string' && fieldValue.startsWith(condition.value);
      case 'endsWith':
        return typeof fieldValue === 'string' && fieldValue.endsWith(condition.value);
      case 'empty':
        return fieldValue === null || fieldValue === undefined || fieldValue === '';
      case 'notEmpty':
        return fieldValue !== null && fieldValue !== undefined && fieldValue !== '';
      default:
        return false;
    }
  }

  validate(): void {
    if (!this._name || this._name.trim().length === 0) {
      throw new Error('Automation rule name is required');
    }
    if (!this._trigger) {
      throw new Error('Automation rule must have a trigger');
    }
    if (!this._actions || this._actions.length === 0) {
      // Allow rules with no actions for lead scoring
      if (!this._leadScoring) {
        throw new Error('Automation rule must have at least one action');
      }
    }

    // Check for duplicate action orders
    const orders = this._actions.map(a => a.order);
    const uniqueOrders = new Set(orders);
    if (orders.length !== uniqueOrders.size) {
      throw new Error('Rule actions must have unique order values');
    }
  }

  toJSON(): Record<string, any> {
    return {
      id: this.id,
      name: this._name,
      description: this._description,
      status: this._status,
      trigger: this._trigger,
      actions: this._actions,
      leadScoring: this._leadScoring,
      dealStageRule: this._dealStageRule,
      tags: this._tags,
      priority: this._priority,
      executionCount: this._executionCount,
      lastExecutedAt: this._lastExecutedAt?.toISOString(),
      lastExecutionStatus: this._lastExecutionStatus,
      category: this._category,
      templateId: this._templateId,
      isEnabled: this._isEnabled,
      schedule: this._schedule,
      createdAt: this.createdAt.toISOString(),
      updatedAt: this.updatedAt.toISOString(),
      tenantId: this.tenantId,
      version: this.version,
    };
  }

  static fromJSON(json: Record<string, any>): AutomationRule {
    const rule = new AutomationRule(
      json.name,
      json.trigger,
      json.actions || [],
      json.tenantId,
      json.description,
    );

    rule['id'] = json.id;
    rule['_status'] = json.status;
    rule['_leadScoring'] = json.leadScoring;
    rule['_dealStageRule'] = json.dealStageRule;
    rule['_tags'] = json.tags || [];
    rule['_priority'] = json.priority;
    rule['_executionCount'] = json.executionCount;
    rule['_lastExecutedAt'] = json.lastExecutedAt ? new Date(json.lastExecutedAt) : undefined;
    rule['_lastExecutionStatus'] = json.lastExecutionStatus;
    rule['_category'] = json.category;
    rule['_templateId'] = json.templateId;
    rule['_isEnabled'] = json.isEnabled;
    rule['_schedule'] = json.schedule;
    rule['createdAt'] = new Date(json.createdAt);
    rule['updatedAt'] = new Date(json.updatedAt);
    rule['version'] = json.version;

    return rule;
  }
}
