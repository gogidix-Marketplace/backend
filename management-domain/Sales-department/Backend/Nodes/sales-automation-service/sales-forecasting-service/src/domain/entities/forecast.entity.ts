import { v4 as uuidv4 } from 'uuid';
import { ForecastModel, ForecastPeriod, ForecastStatus, ForecastGranularity } from '../../shared/constants';

export interface ForecastDataPoint {
  period: string;
  amount: number;
  confidence: number;
  weightedAmount: number;
  bestCase: number;
  worstCase: number;
  dealsCount: number;
}

export interface ForecastBreakdown {
  byStage: Map<string, number>;
  byProduct: Map<string, number>;
  byRep: Map<string, number>;
  byTerritory: Map<string, number>;
}

export class Forecast {
  id: string;
  tenantId: string;
  name: string;
  description: string;
  model: ForecastModel;
  period: ForecastPeriod;
  status: ForecastStatus;
  granularity: ForecastGranularity;
  granularityId?: string; // teamId, repId, or territoryId
  dataPoints: ForecastDataPoint[];
  breakdown: ForecastBreakdown;
  totalForecast: number;
  totalWeightedForecast: number;
  currency: string;
  startDate: Date;
  endDate: Date;
  generatedAt: Date;
  generatedBy: string;
  lastUpdated: Date;
  lastUpdatedBy: string;
  version: number;
  previousForecastId?: string;
  confidenceLevel: number;
  metadata: Map<string, any>;

  constructor(props: {
    id?: string;
    tenantId: string;
    name: string;
    description: string;
    model: ForecastModel;
    period: ForecastPeriod;
    status?: ForecastStatus;
    granularity: ForecastGranularity;
    granularityId?: string;
    dataPoints: ForecastDataPoint[];
    breakdown: ForecastBreakdown;
    currency: string;
    startDate: Date;
    endDate: Date;
    generatedBy: string;
    confidenceLevel?: number;
    previousForecastId?: string;
    metadata?: Map<string, any>;
  }) {
    this.id = props.id || uuidv4();
    this.tenantId = props.tenantId;
    this.name = props.name;
    this.description = props.description;
    this.model = props.model;
    this.period = props.period;
    this.status = props.status || ForecastStatus.DRAFT;
    this.granularity = props.granularity;
    this.granularityId = props.granularityId;
    this.dataPoints = props.dataPoints;
    this.breakdown = props.breakdown;
    this.currency = props.currency;
    this.startDate = props.startDate;
    this.endDate = props.endDate;
    this.generatedAt = new Date();
    this.generatedBy = props.generatedBy;
    this.lastUpdated = new Date();
    this.lastUpdatedBy = props.generatedBy;
    this.version = 1;
    this.previousForecastId = props.previousForecastId;
    this.confidenceLevel = props.confidenceLevel || 85;
    this.metadata = props.metadata || new Map();

    this.calculateTotals();
  }

  private calculateTotals(): void {
    this.totalForecast = this.dataPoints.reduce((sum, dp) => sum + dp.amount, 0);
    this.totalWeightedForecast = this.dataPoints.reduce((sum, dp) => sum + dp.weightedAmount, 0);
  }

  activate(userId: string): void {
    this.status = ForecastStatus.ACTIVE;
    this.lastUpdated = new Date();
    this.lastUpdatedBy = userId;
  }

  archive(userId: string): void {
    this.status = ForecastStatus.ARCHIVED;
    this.lastUpdated = new Date();
    this.lastUpdatedBy = userId;
  }

  updateDataPoints(dataPoints: ForecastDataPoint[], userId: string): void {
    this.dataPoints = dataPoints;
    this.lastUpdated = new Date();
    this.lastUpdatedBy = userId;
    this.version++;
    this.calculateTotals();
  }

  adjustForecast(adjustmentFactor: number, reason: string, userId: string): void {
    this.dataPoints = this.dataPoints.map(dp => ({
      ...dp,
      amount: dp.amount * adjustmentFactor,
      weightedAmount: dp.weightedAmount * adjustmentFactor,
      bestCase: dp.bestCase * adjustmentFactor,
      worstCase: dp.worstCase * adjustmentFactor,
    }));
    this.lastUpdated = new Date();
    this.lastUpdatedBy = userId;
    this.version++;
    this.metadata.set(`adjustment_${this.version}`, {
      factor: adjustmentFactor,
      reason,
      timestamp: new Date(),
      userId,
    });
    this.calculateTotals();
  }

  calculateAccuracy(actualAmount: number): number {
    const error = Math.abs(this.totalWeightedForecast - actualAmount);
    const accuracy = Math.max(0, 100 - (error / actualAmount) * 100);
    return Math.round(accuracy * 100) / 100;
  }

  toJSON() {
    return {
      id: this.id,
      tenantId: this.tenantId,
      name: this.name,
      description: this.description,
      model: this.model,
      period: this.period,
      status: this.status,
      granularity: this.granularity,
      granularityId: this.granularityId,
      dataPoints: this.dataPoints,
      breakdown: {
        byStage: Object.fromEntries(this.breakdown.byStage),
        byProduct: Object.fromEntries(this.breakdown.byProduct),
        byRep: Object.fromEntries(this.breakdown.byRep),
        byTerritory: Object.fromEntries(this.breakdown.byTerritory),
      },
      totalForecast: this.totalForecast,
      totalWeightedForecast: this.totalWeightedForecast,
      currency: this.currency,
      startDate: this.startDate,
      endDate: this.endDate,
      generatedAt: this.generatedAt,
      generatedBy: this.generatedBy,
      lastUpdated: this.lastUpdated,
      lastUpdatedBy: this.lastUpdatedBy,
      version: this.version,
      previousForecastId: this.previousForecastId,
      confidenceLevel: this.confidenceLevel,
      metadata: Object.fromEntries(this.metadata),
    };
  }

  static fromJSON(data: any): Forecast {
    const forecast = new Forecast({
      id: data.id,
      tenantId: data.tenantId,
      name: data.name,
      description: data.description,
      model: data.model,
      period: data.period,
      status: data.status,
      granularity: data.granularity,
      granularityId: data.granularityId,
      dataPoints: data.dataPoints,
      breakdown: {
        byStage: new Map(Object.entries(data.breakdown?.byStage || {})),
        byProduct: new Map(Object.entries(data.breakdown?.byProduct || {})),
        byRep: new Map(Object.entries(data.breakdown?.byRep || {})),
        byTerritory: new Map(Object.entries(data.breakdown?.byTerritory || {})),
      },
      currency: data.currency,
      startDate: new Date(data.startDate),
      endDate: new Date(data.endDate),
      generatedBy: data.generatedBy,
      confidenceLevel: data.confidenceLevel,
      previousForecastId: data.previousForecastId,
      metadata: new Map(Object.entries(data.metadata || {})),
    });
    forecast.generatedAt = new Date(data.generatedAt);
    forecast.lastUpdated = new Date(data.lastUpdated);
    forecast.lastUpdatedBy = data.lastUpdatedBy;
    forecast.version = data.version;
    return forecast;
  }
}
