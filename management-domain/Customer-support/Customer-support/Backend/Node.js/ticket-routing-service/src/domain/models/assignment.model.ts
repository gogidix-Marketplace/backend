import { RoutingStrategy } from '../enums';

export interface AssignmentProps {
  id: string;
  assignmentId: string;
  ticketId: string;
  agentId: string;
  assignedAt: Date;
  assignedBy: string;
  routingStrategy: RoutingStrategy;
  confidence: number;
  metadata: Record<string, unknown>;
  createdAt: Date;
  updatedAt: Date;
}

export class Assignment {
  private constructor(private readonly props: AssignmentProps) {}

  get id(): string { return this.props.id; }
  get assignmentId(): string { return this.props.assignmentId; }
  get ticketId(): string { return this.props.ticketId; }
  get agentId(): string { return this.props.agentId; }
  get assignedAt(): Date { return this.props.assignedAt; }
  get assignedBy(): string { return this.props.assignedBy; }
  get routingStrategy(): RoutingStrategy { return this.props.routingStrategy; }
  get confidence(): number { return this.props.confidence; }
  get metadata(): Record<string, unknown> { return this.props.metadata; }
  get createdAt(): Date { return this.props.createdAt; }

  static create(props: AssignmentProps): Assignment {
    return new Assignment(props);
  }

  toPlainObject(): AssignmentProps {
    return { ...this.props };
  }
}
