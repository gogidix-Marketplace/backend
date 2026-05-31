import { v4 as uuidv4 } from 'uuid';
import { ForecastModel } from '../../shared/constants';

export interface ModelParameter {
  name: string;
  value: number | string | boolean;
  description?: string;
}

export interface ModelMetrics {
  accuracy: number;
  mape: number;
  mae: number;
  rmse: number;
  bias: number;
  lastCalculatedAt: Date;
}

export class ForecastModelEntity {
  id: string;
  tenantId: string;
  name: string;
  modelType: ForecastModel;
  description: string;
  version: string;
  isActive: boolean;
  isDefault: boolean;
  parameters: ModelParameter[];
  weights: Map<string, number>;
  metrics: ModelMetrics;
  createdAt: Date;
  updatedAt: Date;
  createdBy: string;
  lastCalibratedAt?: Date;

  constructor(props: {
    id?: string;
    tenantId: string;
    name: string;
    modelType: ForecastModel;
    description: string;
    version: string;
    isActive?: boolean;
    isDefault?: boolean;
    parameters: ModelParameter[];
    weights: Map<string, number>;
    metrics: ModelMetrics;
    createdBy: string;
    lastCalibratedAt?: Date;
  }) {
    this.id = props.id || uuidv4();
    this.tenantId = props.tenantId;
    this.name = props.name;
    this.modelType = props.modelType;
    this.description = props.description;
    this.version = props.version;
    this.isActive = props.isActive !== undefined ? props.isActive : true;
    this.isDefault = props.isDefault !== undefined ? props.isDefault : false;
    this.parameters = props.parameters;
    this.weights = props.weights;
    this.metrics = props.metrics;
    this.createdAt = new Date();
    this.updatedAt = new Date();
    this.createdBy = props.createdBy;
    this.lastCalibratedAt = props.lastCalibratedAt;
  }

  activate(): void {
    this.isActive = true;
    this.updatedAt = new Date();
  }

  deactivate(): void {
    this.isActive = false;
    this.updatedAt = new Date();
  }

  setAsDefault(): void {
    this.isDefault = true;
    this.updatedAt = new Date();
  }

  updateMetrics(metrics: Partial<ModelMetrics>): void {
    this.metrics = {
      ...this.metrics,
      ...metrics,
      lastCalculatedAt: new Date(),
    };
    this.updatedAt = new Date();
  }

  updateWeights(weights: Map<string, number>): void {
    this.weights = new Map([...this.weights, ...weights]);
    this.updatedAt = new Date();
  }

  updateParameter(name: string, value: number | string | boolean): void {
    const paramIndex = this.parameters.findIndex(p => p.name === name);
    if (paramIndex >= 0) {
      this.parameters[paramIndex].value = value;
    } else {
      this.parameters.push({ name, value });
    }
    this.updatedAt = new Date();
  }

  calibrate(newMetrics: ModelMetrics): void {
    this.metrics = newMetrics;
    this.lastCalibratedAt = new Date();
    this.updatedAt = new Date();
  }

  toJSON() {
    return {
      id: this.id,
      tenantId: this.tenantId,
      name: this.name,
      modelType: this.modelType,
      description: this.description,
      version: this.version,
      isActive: this.isActive,
      isDefault: this.isDefault,
      parameters: this.parameters,
      weights: Object.fromEntries(this.weights),
      metrics: this.metrics,
      createdAt: this.createdAt,
      updatedAt: this.updatedAt,
      createdBy: this.createdBy,
      lastCalibratedAt: this.lastCalibratedAt,
    };
  }

  static fromJSON(data: any): ForecastModelEntity {
    const model = new ForecastModelEntity({
      id: data.id,
      tenantId: data.tenantId,
      name: data.name,
      modelType: data.modelType,
      description: data.description,
      version: data.version,
      isActive: data.isActive,
      isDefault: data.isDefault,
      parameters: data.parameters,
      weights: new Map(Object.entries(data.weights || {})),
      metrics: data.metrics,
      createdBy: data.createdBy,
      lastCalibratedAt: data.lastCalibratedAt ? new Date(data.lastCalibratedAt) : undefined,
    });
    model.createdAt = new Date(data.createdAt);
    model.updatedAt = new Date(data.updatedAt);
    return model;
  }
}
