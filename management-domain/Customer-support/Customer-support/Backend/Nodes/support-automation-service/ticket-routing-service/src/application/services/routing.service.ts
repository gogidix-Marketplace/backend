import { Injectable, Logger } from '@nestjs/common';
import { v4 as uuidv4 } from 'uuid';
import { RoutingInputPort, AgentManagementInputPort, QueueManagementInputPort } from '@domain/ports/input';
import { AgentRepository, TicketRepository, AssignmentRepository, EventPublisher } from '@domain/ports/output';
import { Agent, Ticket, RoutingResult } from '@domain/models';
import { AgentStatus, RoutingStrategy, TicketPriority } from '@domain/enums';
import { TicketRoutedEvent, TicketQueuedEvent, TicketReassignedEvent } from '@domain/events';

@Injectable()
export class RoutingService implements RoutingInputPort {
  private readonly logger = new Logger(RoutingService.name);

  constructor(
    private readonly agentRepo: AgentRepository,
    private readonly ticketRepo: TicketRepository,
    private readonly assignmentRepo: AssignmentRepository,
    private readonly eventPublisher: EventPublisher,
    private readonly queueService: QueueManagementInputPort,
  ) {}

  async routeTicket(ticket: Ticket, strategy: RoutingStrategy = RoutingStrategy.LEAST_BUSY): Promise<RoutingResult> {
    const available = await this.agentRepo.findAvailable(ticket.department);
    if (available.length === 0) {
      const pos = await this.queueService.enqueue(ticket, ticket.department);
      await this.eventPublisher.publish(new TicketQueuedEvent({ eventId: uuidv4(), ticketId: ticket.id, position: pos }));
      return RoutingResult.create({ ticketId: ticket.id, strategy, reason: `No agents available. Queued at position ${pos}`, queued: true, queuePosition: pos });
    }

    let selected: Agent | null = null;
    switch (strategy) {
      case RoutingStrategy.ROUND_ROBIN: selected = available[Math.floor(Math.random() * available.length)]; break;
      case RoutingStrategy.SKILLS_BASED: selected = this.routeSkillsBased(available, ticket); break;
      case RoutingStrategy.PRIORITY_BASED: selected = this.routePriorityBased(available, ticket); break;
      default: selected = this.routeLeastBusy(available);
    }

    if (!selected) return RoutingResult.create({ ticketId: ticket.id, strategy, reason: 'No suitable agent', queued: true });

    const assigned = await this.agentRepo.save(selected.assignTicket());
    if (!assigned) {
      const pos = await this.queueService.enqueue(ticket, ticket.department);
      return RoutingResult.create({ ticketId: ticket.id, strategy, reason: 'Failed to assign', queued: true, queuePosition: pos });
    }

    await this.assignmentRepo.saveAssignment(ticket.id, selected.id);
    await this.assignmentRepo.recordHistory(ticket.id, selected.id, strategy);
    await this.eventPublisher.publish(new TicketRoutedEvent({ eventId: uuidv4(), ticketId: ticket.id, agentId: selected.id, strategy }));

    return RoutingResult.create({ ticketId: ticket.id, agentId: selected.id, strategy, reason: `Agent ${selected.name} selected`, queued: false });
  }

  async reassignTicket(request: { ticketId: string; currentAgentId: string; reason: string; force: boolean }): Promise<RoutingResult> {
    const current = await this.agentRepo.findById(request.currentAgentId);
    if (current) await this.agentRepo.save(current.unassignTicket());

    const available = (await this.agentRepo.findAvailable()).filter(a => a.id !== request.currentAgentId);
    if (available.length === 0) return RoutingResult.create({ ticketId: request.ticketId, strategy: RoutingStrategy.LEAST_BUSY, reason: 'No agents for reassignment', queued: true });

    const newAgent = this.routeLeastBusy(available);
    if (!newAgent) return RoutingResult.create({ ticketId: request.ticketId, strategy: RoutingStrategy.LEAST_BUSY, reason: 'No suitable agent', queued: true });

    await this.agentRepo.save(newAgent.assignTicket());
    await this.assignmentRepo.saveAssignment(request.ticketId, newAgent.id);
    await this.assignmentRepo.recordReassignment(request.ticketId, request.currentAgentId, newAgent.id, request.reason);
    await this.eventPublisher.publish(new TicketReassignedEvent({ eventId: uuidv4(), ticketId: request.ticketId, fromAgentId: request.currentAgentId, toAgentId: newAgent.id, reason: request.reason }));

    return RoutingResult.create({ ticketId: request.ticketId, agentId: newAgent.id, strategy: RoutingStrategy.LEAST_BUSY, reason: `Reassigned to ${newAgent.name}`, queued: false });
  }

  async processQueue(department?: string): Promise<void> {
    const available = await this.agentRepo.findAvailable(department);
    for (const agent of available) {
      if (!agent.canAcceptTicket()) continue;
      const entry = await this.queueService.dequeue(department);
      if (!entry) break;
    }
  }

  async getRoutingStats(): Promise<Record<string, unknown>> {
    const agents = await this.agentRepo.findAll();
    return { totalAgents: agents.length, available: agents.filter(a => a.isAvailable).length };
  }

  async getRoutingHistory(ticketId: string): Promise<any[]> {
    return this.assignmentRepo.getHistory(ticketId);
  }

  async bulkRoute(tickets: Ticket[], strategy?: RoutingStrategy): Promise<RoutingResult[]> {
    const results: RoutingResult[] = [];
    for (const ticket of tickets) results.push(await this.routeTicket(ticket, strategy));
    return results;
  }

  private routeLeastBusy(agents: Agent[]): Agent | null {
    return agents.sort((a, b) => a.loadFactor - b.loadFactor)[0] || null;
  }

  private routeSkillsBased(agents: Agent[], ticket: Ticket): Agent | null {
    if (ticket.requiredSkills.length === 0) return this.routeLeastBusy(agents);
    const scored = agents.map(a => ({ agent: a, score: ticket.requiredSkills.filter(s => a.hasSkill(s)).length / ticket.requiredSkills.length + (1 - a.loadFactor) * 0.3 }));
    scored.sort((a, b) => b.score - a.score);
    return scored[0]?.agent || null;
  }

  private routePriorityBased(agents: Agent[], ticket: Ticket): Agent | null {
    const priorityWeight = { [TicketPriority.CRITICAL]: 4, [TicketPriority.HIGH]: 3, [TicketPriority.MEDIUM]: 2, [TicketPriority.LOW]: 1 }[ticket.priority] || 1;
    if (priorityWeight >= 3) return this.routeSkillsBased(agents, ticket);
    return this.routeLeastBusy(agents);
  }
}

@Injectable()
export class AgentService implements AgentManagementInputPort {
  private readonly logger = new Logger(AgentService.name);
  constructor(private readonly agentRepo: AgentRepository) {}

  async createAgent(data: Partial<Agent>): Promise<Agent> {
    return this.agentRepo.save(Agent.create({
      id: uuidv4(), name: data.name!, email: data.email!,
      status: AgentStatus.AVAILABLE, skills: data.skills || [],
      maxConcurrentTickets: data.maxConcurrentTickets || 10,
      currentTicketCount: 0, assignedTickets: [],
      departments: data.departments || [], lastActiveAt: new Date(), createdAt: new Date(),
    }));
  }

  async getAgent(agentId: string): Promise<Agent | null> { return this.agentRepo.findById(agentId); }
  async getAvailableAgents(department?: string): Promise<Agent[]> { return this.agentRepo.findAvailable(department); }
  async getAllAgents(): Promise<Agent[]> { return this.agentRepo.findAll(); }

  async updateAgentStatus(agentId: string, status: AgentStatus): Promise<boolean> {
    const agent = await this.agentRepo.findById(agentId);
    if (!agent) return false;
    await this.agentRepo.save(Agent.create({ ...agent.toPlainObject(), status, lastActiveAt: new Date() }));
    return true;
  }

  async deleteAgent(agentId: string): Promise<boolean> { return this.agentRepo.delete(agentId); }

  async assignTicket(agentId: string, ticketId: string): Promise<boolean> {
    const agent = await this.agentRepo.findById(agentId);
    if (!agent || !agent.canAcceptTicket()) return false;
    await this.agentRepo.save(agent.assignTicket());
    return true;
  }

  async unassignTicket(agentId: string, ticketId: string): Promise<boolean> {
    const agent = await this.agentRepo.findById(agentId);
    if (!agent) return false;
    await this.agentRepo.save(agent.unassignTicket());
    return true;
  }

  async getAgentStats(agentId: string): Promise<Record<string, unknown>> {
    const agent = await this.agentRepo.findById(agentId);
    return agent ? { agentId, loadFactor: agent.loadFactor, currentTickets: agent.currentTicketCount, capacity: agent.maxConcurrentTickets } : {};
  }
}
