import { v4 as uuidv4 } from 'uuid';

export class ForecastAdjustedEvent {
  eventId: string;
  eventType: string;
  occurredAt: Date;
  tenantId: string;
  correlationId: string;

  forecastId: string;
  forecastName: string;
  adjustedBy: string;
  adjustmentFactor: number;
  previousTotal: number;
  newTotal: number;
  previousWeighted: number;
  newWeighted: number;
  reason: string;
  version: number;
  adjustmentType: 'MANUAL' | 'AUTOMATIC' | 'ROLLING';

  constructor(payload: {
    tenantId: string;
    correlationId?: string;
    forecastId: string;
    forecastName: string;
    adjustedBy: string;
    adjustmentFactor: number;
    previousTotal: number;
    newTotal: number;
    previousWeighted: number;
    newWeighted: number;
    reason: string;
    version: number;
    adjustmentType: 'MANUAL' | 'AUTOMATIC' | 'ROLLING';
  }) {
    this.eventId = uuidv4();
    this.eventType = 'ForecastAdjustedEvent';
    this.occurredAt = new Date();
    this.tenantId = payload.tenantId;
    this.correlationId = payload.correlationId || uuidv4();

    this.forecastId = payload.forecastId;
    this.forecastName = payload.forecastName;
    this.adjustedBy = payload.adjustedBy;
    this.adjustmentFactor = payload.adjustmentFactor;
    this.previousTotal = payload.previousTotal;
    this.newTotal = payload.newTotal;
    this.previousWeighted = payload.previousWeighted;
    this.newWeighted = payload.newWeighted;
    this.reason = payload.reason;
    this.version = payload.version;
    this.adjustmentType = payload.adjustmentType;
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
      adjustedBy: this.adjustedBy,
      adjustmentFactor: this.adjustmentFactor,
      previousTotal: this.previousTotal,
      newTotal: this.newTotal,
      previousWeighted: this.previousWeighted,
      newWeighted: this.newWeighted,
      reason: this.reason,
      version: this.version,
      adjustmentType: this.adjustmentType,
    };
  }

  static fromJSON(data: any): ForecastAdjustedEvent {
    const event = new ForecastAdjustedEvent({
      tenantId: data.tenantId,
      correlationId: data.correlationId,
      forecastId: data.forecastId,
      forecastName: data.forecastName,
      adjustedBy: data.adjustedBy,
      adjustmentFactor: data.adjustmentFactor,
      previousTotal: data.previousTotal,
      newTotal: data.newTotal,
      previousWeighted: data.previousWeighted,
      newWeighted: data.newWeighted,
      reason: data.reason,
      version: data.version,
      adjustmentType: data.adjustmentType,
    });
    event.eventId = data.eventId;
    event.eventType = data.eventType;
    event.occurredAt = new Date(data.occurredAt);
    return event;
  }
}
