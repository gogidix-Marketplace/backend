import { BaseEntity } from '@shared/base';
import { ActionType } from '../enums/action-type.enum';

export interface ActionParameters {
  [key: string]: any;
}

export interface EmailActionParams extends ActionParameters {
  to: string | string[];
  cc?: string | string[];
  bcc?: string | string[];
  subject: string;
  template?: string;
  templateData?: Record<string, any>;
  body?: string;
}

export interface TaskActionParams extends ActionParameters {
  title: string;
  description?: string;
  assignTo?: string;
  dueIn?: number;
  priority?: 'low' | 'medium' | 'high';
  relatedTo?: {
    entityType: string;
    entityId: string;
  };
}

export interface FieldUpdateActionParams extends ActionParameters {
  entityType: string;
  entityId?: string;
  field: string;
  value: any;
}

export interface LeadScoreParams extends ActionParameters {
  leadId?: string;
  score: number;
  reason?: string;
}

export interface WebhookActionParams extends ActionParameters {
  url: string;
  method?: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
  headers?: Record<string, string>;
  body?: any;
}

export interface NotificationActionParams extends ActionParameters {
  recipient: string | string[];
  title: string;
  message: string;
  type?: 'info' | 'warning' | 'error' | 'success';
}

export class Action extends BaseEntity {
  private _name: string;
  private _description: string;
  private _type: ActionType;
  private _parameters: ActionParameters;
  private _order: number;
  private _continueOnError: boolean;
  private _delayMs?: number;
  private _executionCount: number;
  private _lastExecutedAt?: Date;

  private constructor(
    name: string,
    type: ActionType,
    parameters: ActionParameters,
    order: number,
    tenantId: string,
    description?: string,
  ) {
    super(tenantId);
    this._name = name;
    this._description = description || '';
    this._type = type;
    this._parameters = parameters;
    this._order = order;
    this._continueOnError = false;
    this._executionCount = 0;
  }

  static create(
    name: string,
    type: ActionType,
    parameters: ActionParameters,
    order: number,
    tenantId: string,
    description?: string,
  ): Action {
    const action = new Action(name, type, parameters, order, tenantId, description);
    action.validateParameters();
    return action;
  }

  // Getters
  get name(): string {
    return this._name;
  }

  get description(): string {
    return this._description;
  }

  get type(): ActionType {
    return this._type;
  }

  get parameters(): ActionParameters {
    return { ...this._parameters };
  }

  get order(): number {
    return this._order;
  }

  get continueOnError(): boolean {
    return this._continueOnError;
  }

  get delayMs(): number | undefined {
    return this._delayMs;
  }

  get executionCount(): number {
    return this._executionCount;
  }

  get lastExecutedAt(): Date | undefined {
    return this._lastExecutedAt;
  }

  // Business methods
  validateParameters(): void {
    switch (this._type) {
      case ActionType.SEND_EMAIL:
        this.validateEmailParameters();
        break;
      case ActionType.CREATE_TASK:
      case ActionType.ASSIGN_TASK:
        this.validateTaskParameters();
        break;
      case ActionType.UPDATE_FIELD:
      case ActionType.UPDATE_LEAD_STATUS:
      case ActionType.UPDATE_DEAL_STAGE:
        this.validateFieldUpdateParameters();
        break;
      case ActionType.SEND_WEBHOOK:
        this.validateWebhookParameters();
        break;
      case ActionType.SEND_NOTIFICATION:
        this.validateNotificationParameters();
        break;
      default:
        // Custom actions may have their own validation
        break;
    }
  }

  private validateEmailParameters(): void {
    const params = this._parameters as EmailActionParams;
    if (!params.to || (Array.isArray(params.to) && params.to.length === 0)) {
      throw new Error('Email action requires at least one recipient');
    }
    if (!params.subject && !params.template) {
      throw new Error('Email action requires either a subject or template');
    }
  }

  private validateTaskParameters(): void {
    const params = this._parameters as TaskActionParams;
    if (!params.title) {
      throw new Error('Task action requires a title');
    }
  }

  private validateFieldUpdateParameters(): void {
    const params = this._parameters as FieldUpdateActionParams;
    if (!params.entityType) {
      throw new Error('Field update action requires an entityType');
    }
    if (!params.field) {
      throw new Error('Field update action requires a field name');
    }
  }

  private validateWebhookParameters(): void {
    const params = this._parameters as WebhookActionParams;
    if (!params.url) {
      throw new Error('Webhook action requires a URL');
    }
    try {
      new URL(params.url);
    } catch {
      throw new Error('Webhook URL is invalid');
    }
  }

  private validateNotificationParameters(): void {
    const params = this._parameters as NotificationActionParams;
    if (!params.recipient || (Array.isArray(params.recipient) && params.recipient.length === 0)) {
      throw new Error('Notification action requires at least one recipient');
    }
    if (!params.title) {
      throw new Error('Notification action requires a title');
    }
    if (!params.message) {
      throw new Error('Notification action requires a message');
    }
  }

  recordExecution(): void {
    this._executionCount++;
    this._lastExecutedAt = new Date();
    this.updateTimestamp();
  }

  updateParameters(parameters: Partial<ActionParameters>): void {
    this._parameters = { ...this._parameters, ...parameters };
    this.validateParameters();
    this.updateTimestamp();
  }

  setContinueOnError(continueOnError: boolean): void {
    this._continueOnError = continueOnError;
    this.updateTimestamp();
  }

  setDelayMs(delayMs: number): void {
    if (delayMs < 0) {
      throw new Error('Delay cannot be negative');
    }
    this._delayMs = delayMs;
    this.updateTimestamp();
  }

  setOrder(order: number): void {
    if (order < 0) {
      throw new Error('Order cannot be negative');
    }
    this._order = order;
    this.updateTimestamp();
  }

  updateName(name: string): void {
    if (!name || name.trim().length === 0) {
      throw new Error('Action name cannot be empty');
    }
    this._name = name.trim();
    this.updateTimestamp();
  }

  updateDescription(description: string): void {
    this._description = description;
    this.updateTimestamp();
  }

  toJSON(): Record<string, any> {
    return {
      id: this.id,
      name: this._name,
      description: this._description,
      type: this._type,
      parameters: this._parameters,
      order: this._order,
      continueOnError: this._continueOnError,
      delayMs: this._delayMs,
      executionCount: this._executionCount,
      lastExecutedAt: this._lastExecutedAt?.toISOString(),
      createdAt: this.createdAt.toISOString(),
      updatedAt: this.updatedAt.toISOString(),
      tenantId: this.tenantId,
      version: this.version,
    };
  }

  static fromJSON(json: Record<string, any>): Action {
    const action = new Action(
      json.name,
      json.type,
      json.parameters,
      json.order,
      json.tenantId,
      json.description,
    );

    action['id'] = json.id;
    action['_continueOnError'] = json.continueOnError;
    action['_delayMs'] = json.delayMs;
    action['_executionCount'] = json.executionCount;
    action['_lastExecutedAt'] = json.lastExecutedAt ? new Date(json.lastExecutedAt) : undefined;
    action['createdAt'] = new Date(json.createdAt);
    action['updatedAt'] = new Date(json.updatedAt);
    action['version'] = json.version;

    return action;
  }
}
