import { RoutingStrategy, TicketPriority } from '../../enums';

export interface RouteTicketRequest {
  ticketId: string;
  priority: TicketPriority;
  requiredSkills: string[];
  customerId?: string;
  language?: string;
  teamId?: string;
  queueId?: string;
  metadata?: Record<string, unknown>;
}

export interface RoutingResult {
  success: boolean;
  agentId?: string;
  queueId?: string;
  reason: string;
  estimatedWaitTime?: number;
  positionInQueue?: number;
}

export interface RoutingDecision {
  ticketId: string;
  agentId: string;
  confidence: number;
  reasons: string[];
  alternatives: Array<{
    agentId: string;
    confidence: number;
    reason: string;
  }>;
}

export interface RoutingInputPort {
  routeTicket(request: RouteTicketRequest): Promise<RoutingResult>;
  getRoutingDecision(request: RouteTicketRequest): Promise<RoutingDecision>;
  reassignTicket(ticketId: string, currentAgentId: string, reason: string): Promise<RoutingResult>;
  getRoutingStats(): Promise<Record<string, unknown>>;
}
