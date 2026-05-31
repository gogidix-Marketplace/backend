import { AgentStatus, AgentRole } from '../enums';

export interface SkillProps {
  name: string;
  level: number;
  verified: boolean;
  lastUsed?: Date;
}

export class Skill {
  private constructor(private readonly props: SkillProps) {}

  get name(): string { return this.props.name; }
  get level(): number { return this.props.level; }
  get verified(): boolean { return this.props.verified; }
  get lastUsed(): Date | undefined { return this.props.lastUsed; }

  static create(props: SkillProps): Skill {
    return new Skill(props);
  }

  meetsRequirement(minLevel: number): boolean {
    return this.verified && this.level >= minLevel;
  }
}

export interface AgentProps {
  id: string;
  agentId: string;
  userId: string;
  name: string;
  email: string;
  role: AgentRole;
  status: AgentStatus;
  skills: SkillProps[];
  capacity: number;
  currentTickets: number;
  teams: string[];
  assignedQueues: string[];
  workingHours?: { start: string; end: string; timezone: string };
  performance: {
    avgResolutionTime: number;
    avgResponseTime: number;
    customerSatisfaction: number;
    ticketsResolved: number;
    ticketsEscalated: number;
  };
  lastActivity: Date;
  isActive: boolean;
  createdAt: Date;
  updatedAt: Date;
}

export class Agent {
  private constructor(private readonly props: AgentProps) {}

  get id(): string { return this.props.id; }
  get agentId(): string { return this.props.agentId; }
  get userId(): string { return this.props.userId; }
  get name(): string { return this.props.name; }
  get email(): string { return this.props.email; }
  get role(): AgentRole { return this.props.role; }
  get status(): AgentStatus { return this.props.status; }
  get skills(): SkillProps[] { return this.props.skills; }
  get capacity(): number { return this.props.capacity; }
  get currentTickets(): number { return this.props.currentTickets; }
  get teams(): string[] { return this.props.teams; }
  get assignedQueues(): string[] { return this.props.assignedQueues; }
  get performance() { return this.props.performance; }
  get lastActivity(): Date { return this.props.lastActivity; }
  get isActive(): boolean { return this.props.isActive; }

  static create(props: AgentProps): Agent {
    return new Agent(props);
  }

  get isAvailable(): boolean {
    return this.props.status === AgentStatus.AVAILABLE &&
      this.props.isActive &&
      this.props.currentTickets < this.props.capacity;
  }

  get utilizationRate(): number {
    return this.props.capacity > 0
      ? (this.props.currentTickets / this.props.capacity) * 100
      : 0;
  }

  canAcceptTicket(): boolean {
    return this.isAvailable;
  }

  hasSkill(skillName: string, minLevel: number = 1): boolean {
    const skill = this.props.skills.find(
      s => s.name.toLowerCase() === skillName.toLowerCase()
    );
    return skill ? skill.level >= minLevel && skill.verified : false;
  }

  getSkillLevel(skillName: string): number {
    const skill = this.props.skills.find(
      s => s.name.toLowerCase() === skillName.toLowerCase()
    );
    return skill ? skill.level : 0;
  }

  assignTicket(): Agent {
    return Agent.create({
      ...this.props,
      currentTickets: this.props.currentTickets + 1,
      status: AgentStatus.BUSY,
      lastActivity: new Date(),
    });
  }

  releaseTicket(): Agent {
    const newCount = Math.max(0, this.props.currentTickets - 1);
    return Agent.create({
      ...this.props,
      currentTickets: newCount,
      status: newCount === 0 && this.props.status === AgentStatus.BUSY
        ? AgentStatus.AVAILABLE
        : this.props.status,
      lastActivity: new Date(),
    });
  }

  updateStatus(status: AgentStatus): Agent {
    return Agent.create({
      ...this.props,
      status,
      lastActivity: new Date(),
    });
  }

  updatePerformance(metrics: {
    resolutionTime?: number;
    responseTime?: number;
    satisfaction?: number;
    resolved?: boolean;
    escalated?: boolean;
  }): Agent {
    const perf = { ...this.props.performance };
    if (metrics.resolutionTime !== undefined) {
      perf.avgResolutionTime =
        (perf.avgResolutionTime * perf.ticketsResolved + metrics.resolutionTime) /
        (perf.ticketsResolved + 1);
    }
    if (metrics.responseTime !== undefined) {
      perf.avgResponseTime =
        (perf.avgResponseTime * perf.ticketsResolved + metrics.responseTime) /
        (perf.ticketsResolved + 1);
    }
    if (metrics.satisfaction !== undefined) {
      perf.customerSatisfaction =
        (perf.customerSatisfaction * perf.ticketsResolved + metrics.satisfaction) /
        (perf.ticketsResolved + 1);
    }
    if (metrics.resolved) perf.ticketsResolved += 1;
    if (metrics.escalated) perf.ticketsEscalated += 1;

    return Agent.create({ ...this.props, performance: perf });
  }

  toPlainObject(): AgentProps {
    return { ...this.props };
  }
}
