import { AgentStatus, TicketPriority, TicketStatus, RoutingStrategy } from '../enums';

export interface SkillProps { name: string; level: number; certifiedAt?: Date; }

export interface AgentProps {
  id: string; name: string; email: string; status: AgentStatus;
  skills: SkillProps[]; maxConcurrentTickets: number; currentTicketCount: number;
  assignedTickets: string[]; departments: string[]; lastActiveAt: Date; createdAt: Date;
}

export class Agent {
  private constructor(private readonly props: AgentProps) {}
  get id(): string { return this.props.id; }
  get name(): string { return this.props.name; }
  get email(): string { return this.props.email; }
  get status(): AgentStatus { return this.props.status; }
  get skills(): SkillProps[] { return this.props.skills; }
  get maxConcurrentTickets(): number { return this.props.maxConcurrentTickets; }
  get currentTicketCount(): number { return this.props.currentTicketCount; }
  get departments(): string[] { return this.props.departments; }
  static create(props: AgentProps): Agent { return new Agent(props); }
  get isAvailable(): boolean { return this.props.status === AgentStatus.AVAILABLE && this.props.currentTicketCount < this.props.maxConcurrentTickets; }
  canAcceptTicket(): boolean { return this.isAvailable; }
  hasSkill(skillName: string, minLevel = 1): boolean {
    const skill = this.props.skills.find(s => s.name.toLowerCase() === skillName.toLowerCase());
    return skill ? skill.level >= minLevel : false;
  }
  get loadFactor(): number { return this.props.maxConcurrentTickets > 0 ? this.props.currentTicketCount / this.props.maxConcurrentTickets : 0; }
  assignTicket(): Agent { return Agent.create({ ...this.props, currentTicketCount: this.props.currentTicketCount + 1 }); }
  unassignTicket(): Agent { return Agent.create({ ...this.props, currentTicketCount: Math.max(0, this.props.currentTicketCount - 1) }); }
  toPlainObject(): AgentProps { return { ...this.props }; }
}

export interface TicketProps {
  id: string; customerId: string; subject: string; description: string;
  priority: TicketPriority; status: TicketStatus;
  department: string; requiredSkills: string[]; assignedAgentId?: string;
  createdAt: Date; updatedAt: Date; resolvedAt?: Date; slaDeadline?: Date;
}

export class Ticket {
  private constructor(private readonly props: TicketProps) {}
  get id(): string { return this.props.id; }
  get customerId(): string { return this.props.customerId; }
  get subject(): string { return this.props.subject; }
  get priority(): TicketPriority { return this.props.priority; }
  get status(): TicketStatus { return this.props.status; }
  get department(): string { return this.props.department; }
  get requiredSkills(): string[] { return this.props.requiredSkills; }
  get assignedAgentId(): string | undefined { return this.props.assignedAgentId; }
  static create(props: TicketProps): Ticket { return new Ticket(props); }
  assignTo(agentId: string): Ticket { return Ticket.create({ ...this.props, assignedAgentId: agentId, status: TicketStatus.ASSIGNED, updatedAt: new Date() }); }
  toPlainObject(): TicketProps { return { ...this.props }; }
}

export interface RoutingResultProps {
  ticketId: string; agentId?: string; strategy: RoutingStrategy;
  reason: string; queued: boolean; queuePosition?: number; estimatedWaitTime?: number;
}

export class RoutingResult {
  private constructor(private readonly props: RoutingResultProps) {}
  get ticketId(): string { return this.props.ticketId; }
  get agentId(): string | undefined { return this.props.agentId; }
  get strategy(): RoutingStrategy { return this.props.strategy; }
  get reason(): string { return this.props.reason; }
  get queued(): boolean { return this.props.queued; }
  static create(props: RoutingResultProps): RoutingResult { return new RoutingResult(props); }
  toPlainObject(): RoutingResultProps { return { ...this.props }; }
}
