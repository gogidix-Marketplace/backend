import { v4 as uuidv4 } from 'uuid';
import { ScoreType } from './enums/score-type.enum';

export class ScoreAttribute {
  private _id: string;
  private _tenantId: string;
  private _name: string;
  private _description: string;
  private _type: ScoreType;
  private _dataType: string;
  private _weight: number;
  private _defaultValue: any;
  private _isRequired: boolean;
  private _options: string[];
  private _validationRule: string | null;
  private _sourceField: string;
  private _isActive: boolean;
  private _displayOrder: number;
  private _metadata: Record<string, any>;
  private _createdAt: Date;
  private _updatedAt: Date;

  constructor(props: {
    id?: string;
    tenantId: string;
    name: string;
    description: string;
    type: ScoreType;
    dataType: string;
    weight: number;
    defaultValue?: any;
    isRequired?: boolean;
    options?: string[];
    validationRule?: string | null;
    sourceField: string;
    isActive?: boolean;
    displayOrder?: number;
    metadata?: Record<string, any>;
  }) {
    this._id = props.id || uuidv4();
    this._tenantId = props.tenantId;
    this._name = props.name;
    this._description = props.description;
    this._type = props.type;
    this._dataType = props.dataType;
    this._weight = props.weight;
    this._defaultValue = props.defaultValue;
    this._isRequired = props.isRequired ?? false;
    this._options = props.options ?? [];
    this._validationRule = props.validationRule ?? null;
    this._sourceField = props.sourceField;
    this._isActive = props.isActive ?? true;
    this._displayOrder = props.displayOrder ?? 0;
    this._metadata = props.metadata ?? {};
    this._createdAt = new Date();
    this._updatedAt = new Date();
  }

  // Getters
  get id(): string { return this._id; }
  get tenantId(): string { return this._tenantId; }
  get name(): string { return this._name; }
  get description(): string { return this._description; }
  get type(): ScoreType { return this._type; }
  get dataType(): string { return this._dataType; }
  get weight(): number { return this._weight; }
  get defaultValue(): any { return this._defaultValue; }
  get isRequired(): boolean { return this._isRequired; }
  get options(): string[] { return this._options; }
  get validationRule(): string | null { return this._validationRule; }
  get sourceField(): string { return this._sourceField; }
  get isActive(): boolean { return this._isActive; }
  get displayOrder(): number { return this._displayOrder; }
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

  updateWeight(weight: number): void {
    if (weight < 0 || weight > 1) {
      throw new Error('Weight must be between 0 and 1');
    }
    this._weight = weight;
    this._updatedAt = new Date();
  }

  setDefaultValue(value: any): void {
    this._defaultValue = value;
    this._updatedAt = new Date();
  }

  addOption(option: string): void {
    if (!this._options.includes(option)) {
      this._options.push(option);
      this._updatedAt = new Date();
    }
  }

  removeOption(option: string): void {
    this._options = this._options.filter(o => o !== option);
    this._updatedAt = new Date();
  }

  setDisplayOrder(order: number): void {
    this._displayOrder = order;
    this._updatedAt = new Date();
  }

  validateValue(value: any): { valid: boolean; error?: string } {
    if (this._isRequired && (value === null || value === undefined)) {
      return { valid: false, error: 'This attribute is required' };
    }

    if (value === null || value === undefined) {
      return { valid: true };
    }

    switch (this._dataType) {
      case 'string':
        if (typeof value !== 'string') {
          return { valid: false, error: 'Value must be a string' };
        }
        break;
      case 'number':
        if (typeof value !== 'number') {
          return { valid: false, error: 'Value must be a number' };
        }
        break;
      case 'boolean':
        if (typeof value !== 'boolean') {
          return { valid: false, error: 'Value must be a boolean' };
        }
        break;
      case 'array':
        if (!Array.isArray(value)) {
          return { valid: false, error: 'Value must be an array' };
        }
        break;
      case 'date':
        if (!(value instanceof Date) && isNaN(Date.parse(value))) {
          return { valid: false, error: 'Value must be a valid date' };
        }
        break;
    }

    if (this._options.length > 0 && Array.isArray(value)) {
      const invalidOptions = value.filter(v => !this._options.includes(v));
      if (invalidOptions.length > 0) {
        return { valid: false, error: `Invalid options: ${invalidOptions.join(', ')}` };
      }
    }

    if (this._validationRule) {
      try {
        const regex = new RegExp(this._validationRule);
        if (!regex.test(String(value))) {
          return { valid: false, error: 'Value does not match the validation rule' };
        }
      } catch (e) {
        return { valid: false, error: 'Invalid validation rule' };
      }
    }

    return { valid: true };
  }

  calculateContribution(value: any): number {
    if (!this._isActive) {
      return 0;
    }

    const validation = this.validateValue(value);
    if (!validation.valid) {
      return 0;
    }

    let baseScore = this._weight * 100;

    // Apply any custom scoring logic based on value
    if (this._metadata.scoringLogic) {
      const logic = this._metadata.scoringLogic;
      if (logic.type === 'range' && logic.ranges) {
        for (const range of logic.ranges) {
          if (value >= range.min && value <= range.max) {
            baseScore = range.score;
            break;
          }
        }
      } else if (logic.type === 'map' && logic.valueMap) {
        baseScore = logic.valueMap[value] ?? 0;
      }
    }

    return Math.max(0, Math.min(100, baseScore));
  }

  toPrimitives(): Record<string, any> {
    return {
      id: this._id,
      tenantId: this._tenantId,
      name: this._name,
      description: this._description,
      type: this._type,
      dataType: this._dataType,
      weight: this._weight,
      defaultValue: this._defaultValue,
      isRequired: this._isRequired,
      options: this._options,
      validationRule: this._validationRule,
      sourceField: this._sourceField,
      isActive: this._isActive,
      displayOrder: this._displayOrder,
      metadata: this._metadata,
      createdAt: this._createdAt,
      updatedAt: this._updatedAt,
    };
  }

  static fromPrimitives(data: Record<string, any>): ScoreAttribute {
    const attribute = new ScoreAttribute({
      id: data.id,
      tenantId: data.tenantId,
      name: data.name,
      description: data.description,
      type: data.type,
      dataType: data.dataType,
      weight: data.weight,
      defaultValue: data.defaultValue,
      isRequired: data.isRequired,
      options: data.options,
      validationRule: data.validationRule,
      sourceField: data.sourceField,
      isActive: data.isActive,
      displayOrder: data.displayOrder,
      metadata: data.metadata,
    });

    attribute._createdAt = new Date(data.createdAt);
    attribute._updatedAt = new Date(data.updatedAt);

    return attribute;
  }
}
