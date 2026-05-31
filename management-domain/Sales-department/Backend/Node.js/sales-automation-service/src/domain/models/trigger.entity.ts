import { BaseEntity } from '@shared/base';
import { TriggerType } from '../enums/trigger-type.enum';

export interface TriggerCondition {
  field: string;
  operator: 'eq' | 'ne' | 'gt' | 'lt' | 'gte' | 'lte' | 'in' | 'nin' | 'contains' | 'startsWith' | 'endsWith';
  value: any;
  logicalOperator?: 'AND' | 'OR';
}

export interface TriggerConfiguration {
  type: TriggerType;
  conditions?: TriggerCondition[];
  entityTypes?: string[];
  cronExpression?: string;
  webhookUrl?: string;
  customScript?: string;
  filters?: Record<string, any>;
  debounceMs?: number;
  throttleMs?: number;
}

export class Trigger extends BaseEntity {
  private _name: string;
  private _description: string;
  private _configuration: TriggerConfiguration;
  private _isActive: boolean;
  private _lastTriggeredAt?: Date;
  private _triggerCount: number;
  private _tags: string[];

  private constructor(
    name: string,
    configuration: TriggerConfiguration,
    tenantId: string,
    description?: string,
  ) {
    super(tenantId);
    this._name = name;
    this._description = description || '';
    this._configuration = configuration;
    this._isActive = false;
    this._triggerCount = 0;
    this._tags = [];
  }

  static create(
    name: string,
    configuration: TriggerConfiguration,
    tenantId: string,
    description?: string,
  ): Trigger {
    return new Trigger(name, configuration, tenantId, description);
  }

  // Getters
  get name(): string {
    return this._name;
  }

  get description(): string {
    return this._description;
  }

  get configuration(): TriggerConfiguration {
    return { ...this._configuration };
  }

  get isActive(): boolean {
    return this._isActive;
  }

  get lastTriggeredAt(): Date | undefined {
    return this._lastTriggeredAt;
  }

  get triggerCount(): number {
    return this._triggerCount;
  }

  get tags(): string[] {
    return [...this._tags];
  }

  // Business methods
  activate(): void {
    this._isActive = true;
    this.updateTimestamp();
  }

  deactivate(): void {
    this._isActive = false;
    this.updateTimestamp();
  }

  recordTrigger(): void {
    this._lastTriggeredAt = new Date();
    this._triggerCount++;
    this.updateTimestamp();
  }

  updateConfiguration(configuration: Partial<TriggerConfiguration>): void {
    this._configuration = { ...this._configuration, ...configuration };
    this.updateTimestamp();
  }

  updateName(name: string): void {
    if (!name || name.trim().length === 0) {
      throw new Error('Trigger name cannot be empty');
    }
    this._name = name.trim();
    this.updateTimestamp();
  }

  updateDescription(description: string): void {
    this._description = description;
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

  matchesCondition(data: Record<string, any>): boolean {
    if (!this._configuration.conditions || this._configuration.conditions.length === 0) {
      return true;
    }

    return this._configuration.conditions.every(condition => {
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
          return Array.isArray(condition.value) && condition.value.includes(fieldValue);
        case 'nin':
          return Array.isArray(condition.value) && !condition.value.includes(fieldValue);
        case 'contains':
          return typeof fieldValue === 'string' && fieldValue.includes(condition.value);
        case 'startsWith':
          return typeof fieldValue === 'string' && fieldValue.startsWith(condition.value);
        case 'endsWith':
          return typeof fieldValue === 'string' && fieldValue.endsWith(condition.value);
        default:
          return false;
      }
    });
  }

  private getNestedValue(obj: any, path: string): any {
    return path.split('.').reduce((current, key) => current?.[key], obj);
  }

  toJSON(): Record<string, any> {
    return {
      id: this.id,
      name: this._name,
      description: this._description,
      configuration: this._configuration,
      isActive: this._isActive,
      lastTriggeredAt: this._lastTriggeredAt?.toISOString(),
      triggerCount: this._triggerCount,
      tags: this._tags,
      createdAt: this.createdAt.toISOString(),
      updatedAt: this.updatedAt.toISOString(),
      tenantId: this.tenantId,
      version: this.version,
    };
  }

  static fromJSON(json: Record<string, any>): Trigger {
    const trigger = new Trigger(
      json.name,
      json.configuration,
      json.tenantId,
      json.description,
    );

    trigger['id'] = json.id;
    trigger['_isActive'] = json.isActive;
    trigger['_lastTriggeredAt'] = json.lastTriggeredAt ? new Date(json.lastTriggeredAt) : undefined;
    trigger['_triggerCount'] = json.triggerCount;
    trigger['_tags'] = json.tags || [];
    trigger['createdAt'] = new Date(json.createdAt);
    trigger['updatedAt'] = new Date(json.updatedAt);
    trigger['version'] = json.version;

    return trigger;
  }
}
