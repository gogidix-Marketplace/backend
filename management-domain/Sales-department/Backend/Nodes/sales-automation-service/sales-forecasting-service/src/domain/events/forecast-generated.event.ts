import { v4 as uuidv4 } from 'uuid';
import { ForecastModel, ForecastPeriod, ForecastGranularity } from '../../shared/constants';

export class ForecastGeneratedEvent {
  eventId: string;
  eventType: string;
  occurredAt: Date;
  tenantId: string;
  correlationId: string;

  forecastId: string;
  forecastName: string;
  model: ForecastModel;
  period: ForecastPeriod;
  granularity: ForecastGranularity;
  granularityId?: string;
  totalForecast: number;
  weightedForecast: number;
  currency: string;
  startDate: Date;
  endDate: Date;
  generatedBy: string;
  dataPointsCount: number;
  confidenceLevel: number;

  constructor(payload: {
    tenantId: string;
    correlationId?: string;
    forecastId: string;
    forecastName: string;
    model: ForecastModel;
    period: ForecastPeriod;
    granularity: ForecastGranularity;
    granularityId?: string;
    totalForecast: number;
    weightedForecast: number;
    currency: string;
    startDate: Date;
    endDate: Date;
    generatedBy: string;
    dataPointsCount: number;
    confidenceLevel: number;
  }) {
    this.eventId = uuidv4();
    this.eventType = 'ForecastGeneratedEvent';
    this.occurredAt = new Date();
    this.tenantId = payload.tenantId;
    this.correlationId = payload.correlationId || uuidv4();

    this.forecastId = payload.forecastId;
    this.forecastName = payload.forecastName;
    this.model = payload.model;
    this.period = payload.period;
    this.granularity = payload.granularity;
    this.granularityId = payload.granularityId;
    this.totalForecast = payload.totalForecast;
    this.weightedForecast = payload.weightedForecast;
    this.currency = payload.currency;
    this.startDate = payload.startDate;
    this.endDate = payload.endDate;
    this.generatedBy = payload.generatedBy;
    this.dataPointsCount = payload.dataPointsCount;
    this.confidenceLevel = payload.confidenceLevel;
  }

  toJSON() {
    return {
      eventId: this.eventId,
      eventType: this.eventType,
      occurredAt: this.occurredAt,
      tenantId: this.tenantId,
      correlationId: this.correlationId,
      forecastId: this.forecastId,
      forecastName: this.forecastName,
      model: this.model,
      period: this.period,
      granularity: this.granularity,
      granularityId: this.granularityId,
      totalForecast: this.totalForecast,
      weightedForecast: this.weightedForecast,
      currency: this.currency,
      startDate: this.startDate,
      endDate: this.endDate,
      generatedBy: this.generatedBy,
      dataPointsCount: this.dataPointsCount,
      confidenceLevel: this.confidenceLevel,
    };
  }

  static fromJSON(data: any): ForecastGeneratedEvent {
    const event = new ForecastGeneratedEvent({
      tenantId: data.tenantId,
      correlationId: data.correlationId,
      forecastId: data.forecastId,
      forecastName: data.forecastName,
      model: data.model,
      period: data.period,
      granularity: data.granularity,
      granularityId: data.granularityId,
      totalForecast: data.totalForecast,
      weightedForecast: data.weightedForecast,
      currency: data.currency,
      startDate: new Date(data.startDate),
      endDate: new Date(data.endDate),
      generatedBy: data.generatedBy,
      dataPointsCount: data.dataPointsCount,
      confidenceLevel: data.confidenceLevel,
    });
    event.eventId = data.eventId;
    event.eventType = data.eventType;
    event.occurredAt = new Date(data.occurredAt);
    return event;
  }
}
