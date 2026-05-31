import { Agent, Ticket, RoutingResult } from '../../models';
import { AgentStatus, RoutingStrategy } from '../../enums';

export interface RouteTicketRequest {
  ticketId: string; strategy?: RoutingStrategy;
}

export interface RoutingInputPort {
  routeTicket(ticket: Ticket, strategy?: RoutingStrategy): Promise<RoutingResult>;
  reassignTicket(request: { ticketId: string; currentAgentId: string; reason: string; force: boolean }): Promise<RoutingResult>;
  processQueue(department?: string): Promise<void>;
  getRoutingStats(): Promise<Record<string, unknown>>;
  getRoutingHistory(ticketId: string): Promise<any[]>;
  bulkRoute(tickets: Ticket[], strategy?: RoutingStrategy): Promise<RoutingResult[]>;
}

export interface AgentManagementInputPort {
  createAgent(data: Partial<Agent>): Promise<Agent>;
  getAgent(agentId: string): Promise<Agent | null>;
  updateAgentStatus(agentId: string, status: AgentStatus): Promise<boolean>;
  getAvailableAgents(department?: string): Promise<Agent[]>;
  getAllAgents(): Promise<Agent[]>;
  deleteAgent(agentId: string): Promise<boolean>;
  assignTicket(agentId: string, ticketId: string): Promise<boolean>;
  unassignTicket(agentId: string, ticketId: string): Promise<boolean>;
  getAgentStats(agentId: string): Promise<Record<string, unknown>>;
}

export interface QueueManagementInputPort {
  enqueue(ticket: Ticket, department?: string): Promise<number>;
  dequeue(department?: string): Promise<any>;
  getQueueLength(department?: string): Promise<number>;
  getQueueStats(department?: string): Promise<Record<string, number>>;
  clearQueue(department?: string): Promise<number>;
  getTicketPosition(ticketId: string, department?: string): Promise<number | null>;
}
