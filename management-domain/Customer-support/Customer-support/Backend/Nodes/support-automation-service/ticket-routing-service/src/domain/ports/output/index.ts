import { Agent, Ticket } from '../../models';

export interface AgentRepository {
  save(agent: Agent): Promise<Agent>;
  findById(agentId: string): Promise<Agent | null>;
  findAll(): Promise<Agent[]>;
  findAvailable(department?: string): Promise<Agent[]>;
  update(agent: Agent): Promise<Agent>;
  delete(agentId: string): Promise<boolean>;
}

export interface TicketRepository {
  save(ticket: Ticket): Promise<Ticket>;
  findById(ticketId: string): Promise<Ticket | null>;
}

export interface AssignmentRepository {
  saveAssignment(ticketId: string, agentId: string): Promise<void>;
  getAssignment(ticketId: string): Promise<{ agentId: string; assignedAt: Date } | null>;
  recordHistory(ticketId: string, agentId: string, strategy: string): Promise<void>;
  getHistory(ticketId: string): Promise<any[]>;
  recordReassignment(ticketId: string, fromAgentId: string, toAgentId: string, reason: string): Promise<void>;
}

export interface EventPublisher {
  publish(event: any): Promise<void>;
}
