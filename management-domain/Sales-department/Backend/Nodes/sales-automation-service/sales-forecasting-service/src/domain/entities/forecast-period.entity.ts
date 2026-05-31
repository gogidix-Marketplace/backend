import { v4 as uuidv4 } from 'uuid';
import { ForecastPeriod as PeriodType } from '../../shared/constants';

export class ForecastPeriod {
  id: string;
  tenantId: string;
  forecastId: string;
  periodType: PeriodType;
  periodStartDate: Date;
  periodEndDate: Date;
  periodName: string;
  sequence: number;
  forecastAmount: number;
  weightedAmount: number;
  bestCase: number;
  worstCase: number;
  confidence: number;
  dealsCount: number;
  wonDealsCount: number;
  actualAmount?: number;
  variance?: number;
  createdAt: Date;
  updatedAt: Date;

  constructor(props: {
    id?: string;
    tenantId: string;
    forecastId: string;
    periodType: PeriodType;
    periodStartDate: Date;
    periodEndDate: Date;
    periodName: string;
    sequence: number;
    forecastAmount: number;
    weightedAmount: number;
    bestCase: number;
    worstCase: number;
    confidence: number;
    dealsCount: number;
    wonDealsCount?: number;
    actualAmount?: number;
    variance?: number;
  }) {
    this.id = props.id || uuidv4();
    this.tenantId = props.tenantId;
    this.forecastId = props.forecastId;
    this.periodType = props.periodType;
    this.periodStartDate = props.periodStartDate;
    this.periodEndDate = props.periodEndDate;
    this.periodName = props.periodName;
    this.sequence = props.sequence;
    this.forecastAmount = props.forecastAmount;
    this.weightedAmount = props.weightedAmount;
    this.bestCase = props.bestCase;
    this.worstCase = props.worstCase;
    this.confidence = props.confidence;
    this.dealsCount = props.dealsCount;
    this.wonDealsCount = props.wonDealsCount || 0;
    this.actualAmount = props.actualAmount;
    this.variance = props.variance;
    this.createdAt = new Date();
    this.updatedAt = new Date();
  }

  updateActuals(actualAmount: number, wonDealsCount: number): void {
    this.actualAmount = actualAmount;
    this.wonDealsCount = wonDealsCount;
    this.variance = this.weightedAmount - actualAmount;
    this.updatedAt = new Date();
  }

  calculateAccuracy(): number {
    if (!this.actualAmount || this.actualAmount === 0) {
      return 0;
    }
    const error = Math.abs(this.variance || 0);
    const accuracy = Math.max(0, 100 - (error / this.actualAmount) * 100);
    return Math.round(accuracy * 100) / 100;
  }

  toJSON() {
    return {
      id: this.id,
      tenantId: this.tenantId,
      forecastId: this.forecastId,
      periodType: this.periodType,
      periodStartDate: this.periodStartDate,
      periodEndDate: this.periodEndDate,
      periodName: this.periodName,
      sequence: this.sequence,
      forecastAmount: this.forecastAmount,
      weightedAmount: this.weightedAmount,
      bestCase: this.bestCase,
      worstCase: this.worstCase,
      confidence: this.confidence,
      dealsCount: this.dealsCount,
      wonDealsCount: this.wonDealsCount,
      actualAmount: this.actualAmount,
      variance: this.variance,
      accuracy: this.calculateAccuracy(),
      createdAt: this.createdAt,
      updatedAt: this.updatedAt,
    };
  }

  static fromJSON(data: any): ForecastPeriod {
    const period = new ForecastPeriod({
      id: data.id,
      tenantId: data.tenantId,
      forecastId: data.forecastId,
      periodType: data.periodType,
      periodStartDate: new Date(data.periodStartDate),
      periodEndDate: new Date(data.periodEndDate),
      periodName: data.periodName,
      sequence: data.sequence,
      forecastAmount: data.forecastAmount,
      weightedAmount: data.weightedAmount,
      bestCase: data.bestCase,
      worstCase: data.worstCase,
      confidence: data.confidence,
      dealsCount: data.dealsCount,
      wonDealsCount: data.wonDealsCount,
      actualAmount: data.actualAmount,
      variance: data.variance,
    });
    period.createdAt = new Date(data.createdAt);
    period.updatedAt = new Date(data.updatedAt);
    return period;
  }
}
