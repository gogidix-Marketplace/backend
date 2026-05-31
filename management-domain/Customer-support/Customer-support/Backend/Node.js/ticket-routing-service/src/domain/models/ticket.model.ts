import { TicketPriority, TicketStatus } from '../enums';

export interface TicketProps {
  id: string;
  ticketId: string;
  customerId?: string;
  subject: string;
  description: string;
  category: string;
  subCategory?: string;
  priority: TicketPriority;
  status: TicketStatus;
  requiredSkills: string[];
  assignedAgentId?: string;
  assignedAt?: Date;
  createdAt: Date;
  updatedAt: Date;
  dueDate?: Date;
  resolvedAt?: Date;
  escalationLevel: number;
  tags: string[];
}

export class Ticket {
  private constructor(private readonly props: TicketProps) {}

  get id(): string { return this.props.id; }
  get ticketId(): string { return this.props.ticketId; }
  get customerId(): string | undefined { return this.props.customerId; }
  get subject(): string { return this.props.subject; }
  get description(): string { return this.props.description; }
  get category(): string { return this.props.category; }
  get subCategory(): string | undefined { return this.props.subCategory; }
  get priority(): TicketPriority { return this.props.priority; }
  get status(): TicketStatus { return this.props.status; }
  get requiredSkills(): string[] { return this.props.requiredSkills; }
  get assignedAgentId(): string | undefined { return this.props.assignedAgentId; }
  get assignedAt(): Date | undefined { return this.props.assignedAt; }
  get createdAt(): Date { return this.props.createdAt; }
  get updatedAt(): Date { return this.props.updatedAt; }
  get dueDate(): Date | undefined { return this.props.dueDate; }
  get resolvedAt(): Date | undefined { return this.props.resolvedAt; }
  get escalationLevel(): number { return this.props.escalationLevel; }
  get tags(): string[] { return this.props.tags; }

  static create(props: TicketProps): Ticket {
    return new Ticket(props);
  }

  assignTo(agentId: string): Ticket {
    return Ticket.create({
      ...this.props,
      assignedAgentId: agentId,
      assignedAt: new Date(),
      status: TicketStatus.ASSIGNED,
      updatedAt: new Date(),
    });
  }

  resolve(): Ticket {
    return Ticket.create({
      ...this.props,
      status: TicketStatus.RESOLVED,
      resolvedAt: new Date(),
      updatedAt: new Date(),
    });
  }

  escalate(): Ticket {
    return Ticket.create({
      ...this.props,
      escalationLevel: this.props.escalationLevel + 1,
      status: TicketStatus.ESCALATED,
      updatedAt: new Date(),
    });
  }

  updateStatus(status: TicketStatus): Ticket {
    return Ticket.create({
      ...this.props,
      status,
      updatedAt: new Date(),
    });
  }

  toPlainObject(): TicketProps {
    return { ...this.props };
  }
}
