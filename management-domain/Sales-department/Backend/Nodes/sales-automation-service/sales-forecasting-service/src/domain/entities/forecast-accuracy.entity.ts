import { v4 as uuidv4 } from 'uuid';
import { ForecastAccuracyMetric } from '../../shared/constants';

export interface MetricValue {
  metric: ForecastAccuracyMetric;
  value: number;
  description?: string;
}

export class ForecastAccuracy {
  id: string;
  tenantId: string;
  forecastId: string;
  periodId?: string;
  modelUsed: string;
  metrics: Map<ForecastAccuracyMetric, MetricValue>;
  overallAccuracy: number;
  sampleSize: number;
  comparisonStartDate: Date;
  comparisonEndDate: Date;
  calculatedAt: Date;
  calculatedBy: string;
  trend: 'IMPROVING' | 'DECLINING' | 'STABLE';
  previousAccuracy?: number;
  notes?: string;

  constructor(props: {
    id?: string;
    tenantId: string;
    forecastId: string;
    periodId?: string;
    modelUsed: string;
    metrics: MetricValue[];
    overallAccuracy: number;
    sampleSize: number;
    comparisonStartDate: Date;
    comparisonEndDate: Date;
    calculatedBy: string;
    trend?: 'IMPROVING' | 'DECLINING' | 'STABLE';
    previousAccuracy?: number;
    notes?: string;
  }) {
    this.id = props.id || uuidv4();
    this.tenantId = props.tenantId;
    this.forecastId = props.forecastId;
    this.periodId = props.periodId;
    this.modelUsed = props.modelUsed;
    this.metrics = new Map(props.metrics.map(m => [m.metric, m]));
    this.overallAccuracy = props.overallAccuracy;
    this.sampleSize = props.sampleSize;
    this.comparisonStartDate = props.comparisonStartDate;
    this.comparisonEndDate = props.comparisonEndDate;
    this.calculatedAt = new Date();
    this.calculatedBy = props.calculatedBy;
    this.trend = props.trend || 'STABLE';
    this.previousAccuracy = props.previousAccuracy;
    this.notes = props.notes;
  }

  calculateTrend(previousAccuracyValue: number): void {
    const difference = this.overallAccuracy - previousAccuracyValue;
    if (Math.abs(difference) < 5) {
      this.trend = 'STABLE';
    } else if (difference > 0) {
      this.trend = 'IMPROVING';
    } else {
      this.trend = 'DECLINING';
    }
    this.previousAccuracy = previousAccuracyValue;
  }

  getMetric(metric: ForecastAccuracyMetric): MetricValue | undefined {
    return this.metrics.get(metric);
  }

  addMetric(metric: MetricValue): void {
    this.metrics.set(metric.metric, metric);
  }

  isWithinThreshold(threshold: number): boolean {
    return this.overallAccuracy >= threshold;
  }

  toJSON() {
    return {
      id: this.id,
      tenantId: this.tenantId,
      forecastId: this.forecastId,
      periodId: this.periodId,
      modelUsed: this.modelUsed,
      metrics: Array.from(this.metrics.values()),
      overallAccuracy: this.overallAccuracy,
      sampleSize: this.sampleSize,
      comparisonStartDate: this.comparisonStartDate,
      comparisonEndDate: this.comparisonEndDate,
      calculatedAt: this.calculatedAt,
      calculatedBy: this.calculatedBy,
      trend: this.trend,
      previousAccuracy: this.previousAccuracy,
      notes: this.notes,
    };
  }

  static fromJSON(data: any): ForecastAccuracy {
    const accuracy = new ForecastAccuracy({
      id: data.id,
      tenantId: data.tenantId,
      forecastId: data.forecastId,
      periodId: data.periodId,
      modelUsed: data.modelUsed,
      metrics: data.metrics,
      overallAccuracy: data.overallAccuracy,
      sampleSize: data.sampleSize,
      comparisonStartDate: new Date(data.comparisonStartDate),
      comparisonEndDate: new Date(data.comparisonEndDate),
      calculatedBy: data.calculatedBy,
      trend: data.trend,
      previousAccuracy: data.previousAccuracy,
      notes: data.notes,
    });
    accuracy.calculatedAt = new Date(data.calculatedAt);
    return accuracy;
  }
}
