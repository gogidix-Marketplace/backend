import { DomainEvent } from './routing.events';
export type { DomainEvent } from './routing.events';

export class SentimentAnalyzedEvent implements DomainEvent {
  eventId: string;
  eventType = 'sentiment.analyzed';
  timestamp: Date;
  aggregateId: string;
  payload: { ticketId: string; score: number; label: string };

  constructor(data: { eventId: string; ticketId: string; score: number; label: string }) {
    this.eventId = data.eventId;
    this.aggregateId = data.ticketId;
    this.timestamp = new Date();
    this.payload = { ticketId: data.ticketId, score: data.score, label: data.label };
  }
}

export class SentimentAlertTriggeredEvent implements DomainEvent {
  eventId: string;
  eventType = 'sentiment.alert-triggered';
  timestamp: Date;
  aggregateId: string;
  payload: { alertId: string; ticketId: string; severity: string };

  constructor(data: { eventId: string; alertId: string; ticketId: string; severity: string }) {
    this.eventId = data.eventId;
    this.aggregateId = data.ticketId;
    this.timestamp = new Date();
    this.payload = data;
  }
}
