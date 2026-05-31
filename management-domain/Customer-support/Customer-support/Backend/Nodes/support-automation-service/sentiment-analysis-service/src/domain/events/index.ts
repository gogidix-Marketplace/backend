export interface DomainEvent { eventId: string; eventType: string; timestamp: Date; aggregateId: string; payload: Record<string, unknown>; }

export class SentimentAnalyzedEvent implements DomainEvent {
  eventId: string; eventType = 'sentiment.analyzed'; timestamp: Date; aggregateId: string;
  payload: { text: string; score: number; sentiment: string };
  constructor(data: { eventId: string; text: string; score: number; sentiment: string }) {
    this.eventId = data.eventId; this.aggregateId = data.text.slice(0, 50); this.timestamp = new Date();
    this.payload = data;
  }
}
