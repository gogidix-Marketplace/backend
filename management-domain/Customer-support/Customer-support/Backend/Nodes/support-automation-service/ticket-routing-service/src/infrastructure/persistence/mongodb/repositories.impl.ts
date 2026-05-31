import { Injectable, Logger } from '@nestjs/common';
import Redis from 'ioredis';
import { AgentRepository, TicketRepository, AssignmentRepository } from '@domain/ports/output';
import { Agent, Ticket, RoutingResult } from '@domain/models';
import { AgentStatus } from '@domain/enums';

@Injectable()
export class RedisAgentRepository implements AgentRepository {
  private readonly redis: Redis;
  private readonly logger = new Logger(RedisAgentRepository.name);
  private readonly PREFIX = 'agent:';

  constructor() {
    this.redis = new Redis({ host: process.env.REDIS_HOST || 'localhost', port: parseInt(process.env.REDIS_PORT || '6379') });
  }

  async save(agent: Agent): Promise<Agent> {
    const key = `${this.PREFIX}${agent.id}`;
    await this.redis.set(key, JSON.stringify(agent.toPlainObject()));
    return agent;
  }

  async findById(agentId: string): Promise<Agent | null> {
    const data = await this.redis.get(`${this.PREFIX}${agentId}`);
    return data ? Agent.create(JSON.parse(data)) : null;
  }

  async findAll(): Promise<Agent[]> {
    const keys = await this.redis.keys(`${this.PREFIX}*`);
    const agents: Agent[] = [];
    for (const key of keys) {
      const data = await this.redis.get(key);
      if (data) agents.push(Agent.create(JSON.parse(data)));
    }
    return agents;
  }

  async findAvailable(department?: string): Promise<Agent[]> {
    const all = await this.findAll();
    let available = all.filter(a => a.isAvailable);
    if (department) available = available.filter(a => a.departments.includes(department));
    return available;
  }

  async update(agent: Agent): Promise<Agent> { return this.save(agent); }
  async delete(agentId: string): Promise<boolean> { const result = await this.redis.del(`${this.PREFIX}${agentId}`); return result > 0; }
}

@Injectable()
export class RedisTicketRepository implements TicketRepository {
  private readonly redis: Redis;
  private readonly PREFIX = 'ticket:';

  constructor() {
    this.redis = new Redis({ host: process.env.REDIS_HOST || 'localhost', port: parseInt(process.env.REDIS_PORT || '6379') });
  }

  async save(ticket: Ticket): Promise<Ticket> {
    await this.redis.set(`${this.PREFIX}${ticket.id}`, JSON.stringify(ticket.toPlainObject()));
    return ticket;
  }

  async findById(ticketId: string): Promise<Ticket | null> {
    const data = await this.redis.get(`${this.PREFIX}${ticketId}`);
    return data ? Ticket.create(JSON.parse(data)) : null;
  }
}

@Injectable()
export class RedisAssignmentRepository implements AssignmentRepository {
  private readonly redis: Redis;
  private readonly ASSIGN_PREFIX = 'assignment:';
  private readonly HISTORY_PREFIX = 'routing:history:';

  constructor() {
    this.redis = new Redis({ host: process.env.REDIS_HOST || 'localhost', port: parseInt(process.env.REDIS_PORT || '6379') });
  }

  async saveAssignment(ticketId: string, agentId: string): Promise<void> {
    await this.redis.set(`${this.ASSIGN_PREFIX}${ticketId}`, JSON.stringify({ agentId, assignedAt: new Date() }));
  }

  async getAssignment(ticketId: string): Promise<{ agentId: string; assignedAt: Date } | null> {
    const data = await this.redis.get(`${this.ASSIGN_PREFIX}${ticketId}`);
    return data ? JSON.parse(data) : null;
  }

  async recordHistory(ticketId: string, agentId: string, strategy: string): Promise<void> {
    await this.redis.lpush(`${this.HISTORY_PREFIX}${ticketId}`, JSON.stringify({ agentId, strategy, timestamp: new Date() }));
  }

  async getHistory(ticketId: string): Promise<any[]> {
    const entries = await this.redis.lrange(`${this.HISTORY_PREFIX}${ticketId}`, 0, -1);
    return entries.map(e => JSON.parse(e));
  }

  async recordReassignment(ticketId: string, fromAgentId: string, toAgentId: string, reason: string): Promise<void> {
    await this.redis.lpush(`${this.HISTORY_PREFIX}${ticketId}`, JSON.stringify({ type: 'reassignment', fromAgentId, toAgentId, reason, timestamp: new Date() }));
  }
}
