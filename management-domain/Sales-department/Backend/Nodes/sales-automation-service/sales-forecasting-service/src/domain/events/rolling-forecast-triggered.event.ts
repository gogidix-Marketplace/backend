import { v4 as uuidv4 } from 'uuid';
import { ForecastPeriod, ForecastGranularity } from '../../shared/constants';

export class RollingForecastTriggeredEvent {
  eventId: string;
  eventType: string;
  occurredAt: Date;
  tenantId: string;
  correlationId: string;

  previousForecastId?: string;
  triggerDate: Date;
  periodType: ForecastPeriod;
  granularity: ForecastGranularity;
  triggeredBy: 'SCHEDULER' | 'MANUAL' | 'EVENT';
  reason: string;

  constructor(payload: {
    tenantId: string;
    correlationId?: string;
    previousForecastId?: string;
    triggerDate: Date;
    periodType: ForecastPeriod;
    granularity: ForecastGranularity;
    triggeredBy: 'SCHEDULER' | 'MANUAL' | 'EVENT';
    reason: string;
  }) {
    this.eventId = uuidv4();
    this.eventType = 'RollingForecastTriggeredEvent';
    this.occurredAt = new Date();
    this.tenantId = payload.tenantId;
    this.correlationId = payload.correlationId || uuidv4();

    this.previousForecastId = payload.previousForecastId;
    this.triggerDate = payload.triggerDate;
    this.periodType = payload.periodType;
    this.granularity = payload.granularity;
    this.triggeredBy = payload.triggeredBy;
    this.reason = payload.reason;
  }

  toJSON() {
    return {
      eventId: this.eventId,
      eventType: this.eventType,
      occurredAt: this.occurredAt,
      tenantId: this.tenantId,
      correlationId: this.correlationId,
      previousForecastId: this.previousForecastId,
      triggerDate: this.triggerDate,
      periodType: this.periodType,
      granularity: this.granularity,
      triggeredBy: this.triggeredBy,
      reason: this.reason,
    };
  }

  static fromJSON(data: any): RollingForecastTriggeredEvent {
    const event = new RollingForecastTriggeredEvent({
      tenantId: data.tenantId,
      correlationId: data.correlationId,
      previousForecastId: data.previousForecastId,
      triggerDate: new Date(data.triggerDate),
      periodType: data.periodType,
      granularity: data.granularity,
      triggeredBy: data.triggeredBy,
      reason: data.reason,
    });
    event.eventId = data.eventId;
    event.eventType = data.eventType;
    event.occurredAt = new Date(data.occurredAt);
    return event;
  }
}
