export interface DomainEvent {
  eventId: string;
  eventType: string;
  timestamp: Date;
  aggregateId: string;
  payload: Record<string, unknown>;
}

export class TicketRoutedEvent implements DomainEvent {
  eventId: string;
  eventType = 'ticket.routed';
  timestamp: Date;
  aggregateId: string;
  payload: { ticketId: string; agentId: string; strategy: string; confidence: number };

  constructor(data: { eventId: string; ticketId: string; agentId: string; strategy: string; confidence: number }) {
    this.eventId = data.eventId;
    this.aggregateId = data.ticketId;
    this.timestamp = new Date();
    this.payload = { ticketId: data.ticketId, agentId: data.agentId, strategy: data.strategy, confidence: data.confidence };
  }
}

export class TicketQueuedEvent implements DomainEvent {
  eventId: string;
  eventType = 'ticket.queued';
  timestamp: Date;
  aggregateId: string;
  payload: { ticketId: string; queueId?: string; position: number };

  constructor(data: { eventId: string; ticketId: string; queueId?: string; position: number }) {
    this.eventId = data.eventId;
    this.aggregateId = data.ticketId;
    this.timestamp = new Date();
    this.payload = { ticketId: data.ticketId, queueId: data.queueId, position: data.position };
  }
}

export class TicketReassignedEvent implements DomainEvent {
  eventId: string;
  eventType = 'ticket.reassigned';
  timestamp: Date;
  aggregateId: string;
  payload: { ticketId: string; fromAgentId: string; toAgentId: string; reason: string };

  constructor(data: { eventId: string; ticketId: string; fromAgentId: string; toAgentId: string; reason: string }) {
    this.eventId = data.eventId;
    this.aggregateId = data.ticketId;
    this.timestamp = new Date();
    this.payload = data;
  }
}

export class AgentStatusChangedEvent implements DomainEvent {
  eventId: string;
  eventType = 'agent.status-changed';
  timestamp: Date;
  aggregateId: string;
  payload: { agentId: string; oldStatus: string; newStatus: string };

  constructor(data: { eventId: string; agentId: string; oldStatus: string; newStatus: string }) {
    this.eventId = data.eventId;
    this.aggregateId = data.agentId;
    this.timestamp = new Date();
    this.payload = data;
  }
}
