export interface DomainEvent {
  eventId: string;
  eventType: string;
  aggregateId: string;
  occurredAt: Date;
  payload: unknown;
}

export class SessionCreatedEvent implements DomainEvent {
  eventId: string;
  eventType = 'SESSION_CREATED';
  aggregateId: string;
  occurredAt: Date;
  payload: { sessionId: string; customerId?: string; language: string };

  constructor(sessionId: string, customerId?: string, language?: string) {
    this.eventId = `evt_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
    this.aggregateId = sessionId;
    this.occurredAt = new Date();
    this.payload = { sessionId, customerId, language: language || 'en' };
  }
}

export class SessionClosedEvent implements DomainEvent {
  eventId: string;
  eventType = 'SESSION_CLOSED';
  aggregateId: string;
  occurredAt: Date;
  payload: { sessionId: string; duration: number; messageCount: number };

  constructor(sessionId: string, duration: number, messageCount: number) {
    this.eventId = `evt_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
    this.aggregateId = sessionId;
    this.occurredAt = new Date();
    this.payload = { sessionId, duration, messageCount };
  }
}

export class MessageSentEvent implements DomainEvent {
  eventId: string;
  eventType = 'MESSAGE_SENT';
  aggregateId: string;
  occurredAt: Date;
  payload: { sessionId: string; sender: string; intent?: string };

  constructor(sessionId: string, sender: string, intent?: string) {
    this.eventId = `evt_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
    this.aggregateId = sessionId;
    this.occurredAt = new Date();
    this.payload = { sessionId, sender, intent };
  }
}

export class HandoffRequestedEvent implements DomainEvent {
  eventId: string;
  eventType = 'HANDOFF_REQUESTED';
  aggregateId: string;
  occurredAt: Date;
  payload: { sessionId: string; reason: string; priority: string };

  constructor(sessionId: string, reason: string, priority: string) {
    this.eventId = `evt_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
    this.aggregateId = sessionId;
    this.occurredAt = new Date();
    this.payload = { sessionId, reason, priority };
  }
}

export class IntentCreatedEvent implements DomainEvent {
  eventId: string;
  eventType = 'INTENT_CREATED';
  aggregateId: string;
  occurredAt: Date;
  payload: { intentName: string; category: string };

  constructor(intentName: string, category: string) {
    this.eventId = `evt_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
    this.aggregateId = intentName;
    this.occurredAt = new Date();
    this.payload = { intentName, category };
  }
}
