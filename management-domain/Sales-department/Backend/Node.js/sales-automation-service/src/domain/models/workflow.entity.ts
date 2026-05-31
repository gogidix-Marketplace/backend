import { BaseAggregateRoot } from '@shared/base';
import { Trigger } from './trigger.entity';
import { Action } from './action.entity';
import { WorkflowExecutionStatus } from '../enums/workflow-execution-status.enum';
import { AutomationRuleStatus } from '../enums/automation-rule-status.enum';

export interface WorkflowExecutionContext {
  triggerData: Record<string, any>;
  entityId?: string;
  entityType?: string;
  userId?: string;
  correlationId?: string;
  metadata?: Record<string, any>;
}

export interface WorkflowExecutionResult {
  executionId: string;
  status: WorkflowExecutionStatus;
  startedAt: Date;
  completedAt?: Date;
  results: ActionExecutionResult[];
  error?: string;
}

export interface ActionExecutionResult {
  actionId: string;
  actionName: string;
  status: WorkflowExecutionStatus;
  startedAt: Date;
  completedAt?: Date;
  result?: any;
  error?: string;
}

export class Workflow extends BaseAggregateRoot {
  private _name: string;
  private _description: string;
  private _status: AutomationRuleStatus;
  private _trigger: Trigger;
  private _actions: Action[];
  private _executionCount: number;
  private _lastExecutedAt?: Date;
  private _lastExecutionStatus?: WorkflowExecutionStatus;
  private _tags: string[];
  private _priority: number;
  private _isEnabled: boolean;
  private _executionHistory: WorkflowExecutionResult[];
  private _maxConcurrentExecutions: number;
  private _timeoutMs?: number;

  private constructor(
    name: string,
    trigger: Trigger,
    actions: Action[],
    tenantId: string,
    description?: string,
  ) {
    super(tenantId);
    this._name = name;
    this._description = description || '';
    this._status = AutomationRuleStatus.DRAFT;
    this._trigger = trigger;
    this._actions = actions.sort((a, b) => a.order - b.order);
    this._executionCount = 0;
    this._tags = [];
    this._priority = 0;
    this._isEnabled = true;
    this._executionHistory = [];
    this._maxConcurrentExecutions = 1;
  }

  static create(
    name: string,
    trigger: Trigger,
    actions: Action[],
    tenantId: string,
    description?: string,
  ): Workflow {
    const workflow = new Workflow(name, trigger, actions, tenantId, description);
    workflow.validate();
    return workflow;
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

  get trigger(): Trigger {
    return this._trigger;
  }

  get actions(): Action[] {
    return [...this._actions];
  }

  get executionCount(): number {
    return this._executionCount;
  }

  get lastExecutedAt(): Date | undefined {
    return this._lastExecutedAt;
  }

  get lastExecutionStatus(): WorkflowExecutionStatus | undefined {
    return this._lastExecutionStatus;
  }

  get tags(): string[] {
    return [...this._tags];
  }

  get priority(): number {
    return this._priority;
  }

  get isEnabled(): boolean {
    return this._isEnabled;
  }

  get executionHistory(): WorkflowExecutionResult[] {
    return [...this._executionHistory];
  }

  get maxConcurrentExecutions(): number {
    return this._maxConcurrentExecutions;
  }

  get timeoutMs(): number | undefined {
    return this._timeoutMs;
  }

  // Business methods
  activate(): void {
    if (this._status === AutomationRuleStatus.ACTIVE) {
      throw new Error('Workflow is already active');
    }
    this._status = AutomationRuleStatus.ACTIVE;
    this._isEnabled = true;
    this.updateTimestamp();
  }

  deactivate(): void {
    if (this._status !== AutomationRuleStatus.ACTIVE) {
      throw new Error('Only active workflows can be deactivated');
    }
    this._status = AutomationRuleStatus.PAUSED;
    this._isEnabled = false;
    this.updateTimestamp();
  }

  archive(): void {
    if (this._status === AutomationRuleStatus.ACTIVE) {
      throw new Error('Active workflows must be deactivated before archiving');
    }
    this._status = AutomationRuleStatus.ARCHIVED;
    this._isEnabled = false;
    this.updateTimestamp();
  }

  enable(): void {
    this._isEnabled = true;
    this.updateTimestamp();
  }

  disable(): void {
    if (this._status === AutomationRuleStatus.ACTIVE) {
      throw new Error('Active workflows must be deactivated before disabling');
    }
    this._isEnabled = false;
    this.updateTimestamp();
  }

  addAction(action: Action): void {
    if (this._status === AutomationRuleStatus.ACTIVE) {
      throw new Error('Cannot add actions to an active workflow');
    }
    this._actions.push(action);
    this._actions.sort((a, b) => a.order - b.order);
    this.updateTimestamp();
  }

  removeAction(actionId: string): void {
    if (this._status === AutomationRuleStatus.ACTIVE) {
      throw new Error('Cannot remove actions from an active workflow');
    }
    this._actions = this._actions.filter(a => a.id !== actionId);
    this.updateTimestamp();
  }

  updateAction(actionId: string, updatedAction: Partial<Action>): void {
    if (this._status === AutomationRuleStatus.ACTIVE) {
      throw new Error('Cannot update actions in an active workflow');
    }
    const index = this._actions.findIndex(a => a.id === actionId);
    if (index === -1) {
      throw new Error(`Action with ID ${actionId} not found`);
    }
    Object.assign(this._actions[index], updatedAction);
    this.updateTimestamp();
  }

  updateTrigger(trigger: Trigger): void {
    if (this._status === AutomationRuleStatus.ACTIVE) {
      throw new Error('Cannot update trigger of an active workflow');
    }
    this._trigger = trigger;
    this.updateTimestamp();
  }

  setPriority(priority: number): void {
    if (priority < 0 || priority > 100) {
      throw new Error('Priority must be between 0 and 100');
    }
    this._priority = priority;
    this.updateTimestamp();
  }

  setMaxConcurrentExecutions(max: number): void {
    if (max < 1) {
      throw new Error('Max concurrent executions must be at least 1');
    }
    this._maxConcurrentExecutions = max;
    this.updateTimestamp();
  }

  setTimeoutMs(timeoutMs: number): void {
    if (timeoutMs < 1000) {
      throw new Error('Timeout must be at least 1000ms');
    }
    this._timeoutMs = timeoutMs;
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
      throw new Error('Workflow name cannot be empty');
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

  shouldTrigger(context: WorkflowExecutionContext): boolean {
    return this._trigger.matchesCondition(context.triggerData);
  }

  recordExecution(result: WorkflowExecutionResult): void {
    this._executionCount++;
    this._lastExecutedAt = result.completedAt || result.startedAt;
    this._lastExecutionStatus = result.status;
    this._executionHistory.push(result);

    // Keep only last 100 executions
    if (this._executionHistory.length > 100) {
      this._executionHistory = this._executionHistory.slice(-100);
    }

    this.updateTimestamp();
  }

  getExecutionById(executionId: string): WorkflowExecutionResult | undefined {
    return this._executionHistory.find(e => e.executionId === executionId);
  }

  getRecentExecutions(limit: number = 10): WorkflowExecutionResult[] {
    return this._executionHistory.slice(-limit).reverse();
  }

  getExecutionStats(): {
    total: number;
    successful: number;
    failed: number;
    averageDuration?: number;
  } {
    const successful = this._executionHistory.filter(e => e.status === WorkflowExecutionStatus.COMPLETED).length;
    const failed = this._executionHistory.filter(e => e.status === WorkflowExecutionStatus.FAILED).length;

    const completedExecutions = this._executionHistory.filter(e => e.completedAt);
    let averageDuration: number | undefined;
    if (completedExecutions.length > 0) {
      const totalDuration = completedExecutions.reduce(
        (sum, e) => sum + (e.completedAt!.getTime() - e.startedAt.getTime()),
        0,
      );
      averageDuration = totalDuration / completedExecutions.length;
    }

    return {
      total: this._executionCount,
      successful,
      failed,
      averageDuration,
    };
  }

  validate(): void {
    if (!this._name || this._name.trim().length === 0) {
      throw new Error('Workflow name is required');
    }
    if (!this._trigger) {
      throw new Error('Workflow must have a trigger');
    }
    if (!this._actions || this._actions.length === 0) {
      throw new Error('Workflow must have at least one action');
    }

    // Check for duplicate action orders
    const orders = this._actions.map(a => a.order);
    const uniqueOrders = new Set(orders);
    if (orders.length !== uniqueOrders.size) {
      throw new Error('Workflow actions must have unique order values');
    }
  }

  toJSON(): Record<string, any> {
    return {
      id: this.id,
      name: this._name,
      description: this._description,
      status: this._status,
      trigger: this._trigger.toJSON(),
      actions: this._actions.map(a => a.toJSON()),
      executionCount: this._executionCount,
      lastExecutedAt: this._lastExecutedAt?.toISOString(),
      lastExecutionStatus: this._lastExecutionStatus,
      tags: this._tags,
      priority: this._priority,
      isEnabled: this._isEnabled,
      executionHistory: this._executionHistory,
      maxConcurrentExecutions: this._maxConcurrentExecutions,
      timeoutMs: this._timeoutMs,
      createdAt: this.createdAt.toISOString(),
      updatedAt: this.updatedAt.toISOString(),
      tenantId: this.tenantId,
      version: this.version,
    };
  }

  static fromJSON(json: Record<string, any>): Workflow {
    const trigger = Trigger.fromJSON(json.trigger);
    const actions = json.actions.map((a: any) => Action.fromJSON(a));

    const workflow = new Workflow(
      json.name,
      trigger,
      actions,
      json.tenantId,
      json.description,
    );

    workflow['id'] = json.id;
    workflow['_status'] = json.status;
    workflow['_executionCount'] = json.executionCount;
    workflow['_lastExecutedAt'] = json.lastExecutedAt ? new Date(json.lastExecutedAt) : undefined;
    workflow['_lastExecutionStatus'] = json.lastExecutionStatus;
    workflow['_tags'] = json.tags || [];
    workflow['_priority'] = json.priority;
    workflow['_isEnabled'] = json.isEnabled;
    workflow['_executionHistory'] = json.executionHistory?.map((e: any) => ({
      ...e,
      startedAt: new Date(e.startedAt),
      completedAt: e.completedAt ? new Date(e.completedAt) : undefined,
    })) || [];
    workflow['_maxConcurrentExecutions'] = json.maxConcurrentExecutions;
    workflow['_timeoutMs'] = json.timeoutMs;
    workflow['createdAt'] = new Date(json.createdAt);
    workflow['updatedAt'] = new Date(json.updatedAt);
    workflow['version'] = json.version;

    return workflow;
  }
}
