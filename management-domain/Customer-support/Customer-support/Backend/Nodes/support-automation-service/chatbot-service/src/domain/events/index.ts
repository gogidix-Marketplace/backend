export interface DomainEvent {
  eventId: string;
  eventType: string;
  timestamp: Date;
  aggregateId: string;
  payload: Record<string, unknown>;
}

export class ConversationStartedEvent implements DomainEvent {
  eventId: string; eventType = 'conversation.started'; timestamp: Date; aggregateId: string;
  payload: { sessionId: string; customerId?: string };
  constructor(data: { eventId: string; sessionId: string; customerId?: string }) {
    this.eventId = data.eventId; this.aggregateId = data.sessionId; this.timestamp = new Date();
    this.payload = { sessionId: data.sessionId, customerId: data.customerId };
  }
}

export class MessageProcessedEvent implements DomainEvent {
  eventId: string; eventType = 'conversation.message-processed'; timestamp: Date; aggregateId: string;
  payload: { sessionId: string; intent: string; confidence: number };
  constructor(data: { eventId: string; sessionId: string; intent: string; confidence: number }) {
    this.eventId = data.eventId; this.aggregateId = data.sessionId; this.timestamp = new Date();
    this.payload = data;
  }
}

export class HandoffRequestedEvent implements DomainEvent {
  eventId: string; eventType = 'conversation.handoff-requested'; timestamp: Date; aggregateId: string;
  payload: { sessionId: string; reason: string };
  constructor(data: { eventId: string; sessionId: string; reason: string }) {
    this.eventId = data.eventId; this.aggregateId = data.sessionId; this.timestamp = new Date();
    this.payload = data;
  }
}
