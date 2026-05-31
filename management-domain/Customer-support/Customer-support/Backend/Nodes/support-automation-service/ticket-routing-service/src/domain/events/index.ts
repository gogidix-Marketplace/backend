export interface DomainEvent { eventId: string; eventType: string; timestamp: Date; aggregateId: string; payload: Record<string, unknown>; }

export class TicketRoutedEvent implements DomainEvent {
  eventId: string; eventType = 'ticket.routed'; timestamp: Date; aggregateId: string;
  payload: { ticketId: string; agentId: string; strategy: string };
  constructor(data: { eventId: string; ticketId: string; agentId: string; strategy: string }) {
    this.eventId = data.eventId; this.aggregateId = data.ticketId; this.timestamp = new Date();
    this.payload = data;
  }
}

export class TicketQueuedEvent implements DomainEvent {
  eventId: string; eventType = 'ticket.queued'; timestamp: Date; aggregateId: string;
  payload: { ticketId: string; position: number };
  constructor(data: { eventId: string; ticketId: string; position: number }) {
    this.eventId = data.eventId; this.aggregateId = data.ticketId; this.timestamp = new Date();
    this.payload = data;
  }
}

export class TicketReassignedEvent implements DomainEvent {
  eventId: string; eventType = 'ticket.reassigned'; timestamp: Date; aggregateId: string;
  payload: { ticketId: string; fromAgentId: string; toAgentId: string; reason: string };
  constructor(data: { eventId: string; ticketId: string; fromAgentId: string; toAgentId: string; reason: string }) {
    this.eventId = data.eventId; this.aggregateId = data.ticketId; this.timestamp = new Date();
    this.payload = data;
  }
}
