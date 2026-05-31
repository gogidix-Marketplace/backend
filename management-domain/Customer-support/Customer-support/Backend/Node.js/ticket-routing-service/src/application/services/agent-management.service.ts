import { Injectable, Logger } from '@nestjs/common';
import { v4 as uuidv4 } from 'uuid';
import { AgentManagementInputPort } from '@domain/ports/input';
import { AgentRepository, EventPublisher } from '@domain/ports/output';
import { Agent } from '@domain/models';
import { AgentStatus, AgentRole } from '@domain/enums';
import { AgentStatusChangedEvent } from '@domain/events';
import { AgentNotFoundException } from '@shared/exceptions';

@Injectable()
export class AgentManagementService implements AgentManagementInputPort {
  private readonly logger = new Logger(AgentManagementService.name);

  constructor(
    private readonly agentRepository: AgentRepository,
    private readonly eventPublisher: EventPublisher,
  ) {}

  async createAgent(data: {
    userId: string;
    name: string;
    email: string;
    role: AgentRole;
    skills: Array<{ name: string; level: number; verified: boolean }>;
    capacity: number;
    teams: string[];
  }): Promise<Agent> {
    const agent = Agent.create({
      id: uuidv4(),
      agentId: `agent_${uuidv4()}`,
      userId: data.userId,
      name: data.name,
      email: data.email,
      role: data.role,
      status: AgentStatus.OFFLINE,
      skills: data.skills.map(s => ({ ...s, verified: s.verified ?? false })),
      capacity: data.capacity,
      currentTickets: 0,
      teams: data.teams,
      assignedQueues: [],
      performance: {
        avgResolutionTime: 0,
        avgResponseTime: 0,
        customerSatisfaction: 0,
        ticketsResolved: 0,
        ticketsEscalated: 0,
      },
      lastActivity: new Date(),
      isActive: true,
      createdAt: new Date(),
      updatedAt: new Date(),
    });

    return this.agentRepository.save(agent);
  }

  async getAgent(agentId: string): Promise<Agent | null> {
    return this.agentRepository.findByAgentId(agentId);
  }

  async updateAgentStatus(agentId: string, status: AgentStatus): Promise<boolean> {
    const agent = await this.agentRepository.findByAgentId(agentId);
    if (!agent) throw new AgentNotFoundException(agentId);

    const oldStatus = agent.status;
    const updated = agent.updateStatus(status);
    await this.agentRepository.update(updated);

    await this.eventPublisher.publish(new AgentStatusChangedEvent({
      eventId: uuidv4(),
      agentId,
      oldStatus,
      newStatus: status,
    }));

    return true;
  }

  async getAvailableAgents(teamId?: string, requiredSkills?: string[]): Promise<Agent[]> {
    return this.agentRepository.findAvailable(teamId, requiredSkills);
  }

  async getAllAgents(): Promise<Agent[]> {
    return this.agentRepository.findAll();
  }

  async deleteAgent(agentId: string): Promise<boolean> {
    return this.agentRepository.delete(agentId);
  }

  async getAgentStats(agentId: string): Promise<Record<string, unknown>> {
    const agent = await this.agentRepository.findByAgentId(agentId);
    if (!agent) throw new AgentNotFoundException(agentId);
    const workload = await this.agentRepository.updateWorkload ? 0 : 0;
    return {
      agentId,
      utilizationRate: agent.utilizationRate,
      performance: agent.performance,
      currentTickets: agent.currentTickets,
      capacity: agent.capacity,
    };
  }
}
