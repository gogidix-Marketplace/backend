import { v4 as uuidv4 } from 'uuid';
import { ForecastAccuracyMetric } from '../../shared/constants';

export interface MetricResult {
  metric: ForecastAccuracyMetric;
  value: number;
  threshold: number;
  withinThreshold: boolean;
}

export class ForecastAccuracyCalculatedEvent {
  eventId: string;
  eventType: string;
  occurredAt: Date;
  tenantId: string;
  correlationId: string;

  accuracyId: string;
  forecastId: string;
  forecastName: string;
  modelUsed: string;
  overallAccuracy: number;
  accuracyThreshold: number;
  withinThreshold: boolean;
  metrics: MetricResult[];
  sampleSize: number;
  trend: 'IMPROVING' | 'DECLINING' | 'STABLE';
  previousAccuracy?: number;
  calculatedBy: string;

  constructor(payload: {
    tenantId: string;
    correlationId?: string;
    accuracyId: string;
    forecastId: string;
    forecastName: string;
    modelUsed: string;
    overallAccuracy: number;
    accuracyThreshold: number;
    metrics: MetricResult[];
    sampleSize: number;
    trend: 'IMPROVING' | 'DECLINING' | 'STABLE';
    previousAccuracy?: number;
    calculatedBy: string;
  }) {
    this.eventId = uuidv4();
    this.eventType = 'ForecastAccuracyCalculatedEvent';
    this.occurredAt = new Date();
    this.tenantId = payload.tenantId;
    this.correlationId = payload.correlationId || uuidv4();

    this.accuracyId = payload.accuracyId;
    this.forecastId = payload.forecastId;
    this.forecastName = payload.forecastName;
    this.modelUsed = payload.modelUsed;
    this.overallAccuracy = payload.overallAccuracy;
    this.accuracyThreshold = payload.accuracyThreshold;
    this.withinThreshold = payload.overallAccuracy >= payload.accuracyThreshold;
    this.metrics = payload.metrics;
    this.sampleSize = payload.sampleSize;
    this.trend = payload.trend;
    this.previousAccuracy = payload.previousAccuracy;
    this.calculatedBy = payload.calculatedBy;
  }

  toJSON() {
    return {
      eventId: this.eventId,
      eventType: this.eventType,
      occurredAt: this.occurredAt,
      tenantId: this.tenantId,
      correlationId: this.correlationId,
      accuracyId: this.accuracyId,
      forecastId: this.forecastId,
      forecastName: this.forecastName,
      modelUsed: this.modelUsed,
      overallAccuracy: this.overallAccuracy,
      accuracyThreshold: this.accuracyThreshold,
      withinThreshold: this.withinThreshold,
      metrics: this.metrics,
      sampleSize: this.sampleSize,
      trend: this.trend,
      previousAccuracy: this.previousAccuracy,
      calculatedBy: this.calculatedBy,
    };
  }

  static fromJSON(data: any): ForecastAccuracyCalculatedEvent {
    const event = new ForecastAccuracyCalculatedEvent({
      tenantId: data.tenantId,
      correlationId: data.correlationId,
      accuracyId: data.accuracyId,
      forecastId: data.forecastId,
      forecastName: data.forecastName,
      modelUsed: data.modelUsed,
      overallAccuracy: data.overallAccuracy,
      accuracyThreshold: data.accuracyThreshold,
      metrics: data.metrics,
      sampleSize: data.sampleSize,
      trend: data.trend,
      previousAccuracy: data.previousAccuracy,
      calculatedBy: data.calculatedBy,
    });
    event.eventId = data.eventId;
    event.eventType = data.eventType;
    event.occurredAt = new Date(data.occurredAt);
    return event;
  }
}
