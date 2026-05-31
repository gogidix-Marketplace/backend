import { Injectable, Logger } from '@nestjs/common';
import { v4 as uuidv4 } from 'uuid';
import { RoutingInputPort, RouteTicketRequest, RoutingResult, RoutingDecision } from '@domain/ports/input';
import { AgentRepository, AssignmentRepository, QueueRepository, EventPublisher } from '@domain/ports/output';
import { RoutingStrategy, TicketPriority } from '@domain/enums';
import { Agent, Assignment } from '@domain/models';
import { TicketRoutedEvent, TicketQueuedEvent, TicketReassignedEvent } from '@domain/events';
import { NoAvailableAgentException } from '@shared/exceptions';

interface AgentScore {
  agentId: string;
  score: number;
  reasons: string[];
  agent: Agent;
}

@Injectable()
export class RoutingService implements RoutingInputPort {
  private readonly logger = new Logger(RoutingService.name);
  private readonly SKILL_MATCH_THRESHOLD = 0.6;

  constructor(
    private readonly agentRepository: AgentRepository,
    private readonly assignmentRepository: AssignmentRepository,
    private readonly queueRepository: QueueRepository,
    private readonly eventPublisher: EventPublisher,
  ) {}

  async routeTicket(request: RouteTicketRequest): Promise<RoutingResult> {
    this.logger.log(`Routing ticket ${request.ticketId}`);

    let targetAgentId: string | undefined;
    let targetQueueId: string | undefined;

    if (request.queueId) {
      const queue = await this.queueRepository.findByQueueId(request.queueId);
      if (queue) {
        targetQueueId = queue.queueId;
        targetAgentId = await this.findAgentInQueue(queue, request);
      }
    }

    if (!targetAgentId) {
      targetAgentId = await this.findBestAgent(request);
    }

    if (targetAgentId) {
      const assignment = Assignment.create({
        id: uuidv4(),
        assignmentId: `assign_${uuidv4()}`,
        ticketId: request.ticketId,
        agentId: targetAgentId,
        assignedAt: new Date(),
        assignedBy: 'system',
        routingStrategy: RoutingStrategy.LEAST_BUSY,
        confidence: 0.85,
        metadata: {},
        createdAt: new Date(),
        updatedAt: new Date(),
      });

      await this.assignmentRepository.save(assignment);
      await this.agentRepository.updateWorkload(targetAgentId, 1);

      await this.eventPublisher.publish(new TicketRoutedEvent({
        eventId: uuidv4(),
        ticketId: request.ticketId,
        agentId: targetAgentId,
        strategy: RoutingStrategy.LEAST_BUSY,
        confidence: 0.85,
      }));

      return {
        success: true,
        agentId: targetAgentId,
        queueId: targetQueueId,
        reason: 'Agent assigned based on skills and availability',
      };
    }

    return {
      success: false,
      queueId: targetQueueId,
      reason: 'No available agents, ticket queued',
    };
  }

  async getRoutingDecision(request: RouteTicketRequest): Promise<RoutingDecision> {
    const agents = await this.agentRepository.findAvailable(request.teamId, request.requiredSkills);

    if (agents.length === 0) {
      throw new NoAvailableAgentException(request.requiredSkills);
    }

    const scoredAgents = agents.map(agent => {
      const skillMatch = this.calculateSkillMatch(agent, request.requiredSkills);
      const availability = 1 - agent.currentTickets / agent.capacity;
      const confidence = skillMatch * 0.6 + availability * 0.4;

      return {
        agentId: agent.agentId,
        confidence,
        reason: `Skill match: ${Math.round(skillMatch * 100)}%, Availability: ${Math.round(availability * 100)}%`,
      };
    });

    scoredAgents.sort((a, b) => b.confidence - a.confidence);
    const [selected, ...alternatives] = scoredAgents;

    return {
      ticketId: request.ticketId,
      agentId: selected.agentId,
      confidence: selected.confidence,
      reasons: [selected.reason],
      alternatives: alternatives.slice(0, 3),
    };
  }

  async reassignTicket(ticketId: string, currentAgentId: string, reason: string): Promise<RoutingResult> {
    await this.agentRepository.updateWorkload(currentAgentId, -1);

    const newAgentId = await this.findBestAgent({
      ticketId,
      priority: TicketPriority.MEDIUM,
      requiredSkills: [],
    });

    if (newAgentId) {
      const assignment = Assignment.create({
        id: uuidv4(),
        assignmentId: `assign_${uuidv4()}`,
        ticketId,
        agentId: newAgentId,
        assignedAt: new Date(),
        assignedBy: 'system',
        routingStrategy: RoutingStrategy.LEAST_BUSY,
        confidence: 0.75,
        metadata: {},
        createdAt: new Date(),
        updatedAt: new Date(),
      });

      await this.assignmentRepository.save(assignment);
      await this.agentRepository.updateWorkload(newAgentId, 1);

      await this.eventPublisher.publish(new TicketReassignedEvent({
        eventId: uuidv4(),
        ticketId,
        fromAgentId: currentAgentId,
        toAgentId: newAgentId,
        reason,
      }));

      return {
        success: true,
        agentId: newAgentId,
        reason: `Reassigned from ${currentAgentId}: ${reason}`,
      };
    }

    return { success: false, reason: 'No available agents for reassignment' };
  }

  async getRoutingStats(): Promise<Record<string, unknown>> {
    const agents = await this.agentRepository.findAll();
    const available = agents.filter(a => a.isAvailable);
    return {
      totalAgents: agents.length,
      availableAgents: available.length,
      busyAgents: agents.filter(a => a.status === 'busy').length,
    };
  }

  private async findBestAgent(request: RouteTicketRequest): Promise<string | undefined> {
    const agents = await this.agentRepository.findAvailable(request.teamId, request.requiredSkills);
    if (agents.length === 0) return undefined;

    const scored: AgentScore[] = agents.map(agent => {
      const capacityFactor = 1 - agent.currentTickets / agent.capacity;
      const satisfactionFactor = agent.performance.customerSatisfaction / 100;

      let skillFactor = 0.5;
      if (request.requiredSkills.length > 0) {
        let matched = 0;
        for (const skill of request.requiredSkills) {
          if (agent.hasSkill(skill)) matched++;
        }
        skillFactor = matched / request.requiredSkills.length;
      }

      const score = capacityFactor * 0.3 + satisfactionFactor * 0.25 + skillFactor * 0.3 + 0.15 * 0.5;
      return { agentId: agent.agentId, score, reasons: [], agent };
    });

    scored.sort((a, b) => b.score - a.score);
    return scored[0]?.agentId;
  }

  private async findAgentInQueue(queue: any, request: RouteTicketRequest): Promise<string | undefined> {
    const agents = await this.agentRepository.findAvailable(undefined, request.requiredSkills);
    if (agents.length === 0) return undefined;

    const queueAgents = agents.filter(a =>
      queue.assignedAgents.includes(a.agentId) && a.canAcceptTicket()
    );

    if (queueAgents.length === 0) return undefined;
    queueAgents.sort((a, b) => a.currentTickets - b.currentTickets);
    return queueAgents[0].agentId;
  }

  private calculateSkillMatch(agent: Agent, requiredSkills: string[]): number {
    if (requiredSkills.length === 0) return 1;
    let totalLevel = 0;
    for (const skill of requiredSkills) {
      totalLevel += agent.getSkillLevel(skill);
    }
    return Math.min(totalLevel / (requiredSkills.length * 10), 1);
  }
}
