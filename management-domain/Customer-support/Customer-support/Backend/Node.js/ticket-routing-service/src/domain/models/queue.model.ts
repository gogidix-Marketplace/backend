import { QueueType, RoutingStrategy } from '../enums';

export interface EscalationRuleProps {
  id: string;
  name: string;
  condition: {
    field: string;
    operator: 'eq' | 'gt' | 'lt' | 'gte' | 'lte' | 'in';
    value: unknown;
  };
  action: {
    type: 'escalate' | 'reassign' | 'notify';
    target: string;
    priority?: string;
  };
  isActive: boolean;
}

export interface QueueProps {
  id: string;
  queueId: string;
  name: string;
  description: string;
  type: QueueType;
  priority: number;
  requiredSkills: string[];
  assignedTeams: string[];
  assignedAgents: string[];
  routingStrategy: RoutingStrategy;
  escalationRules: EscalationRuleProps[];
  isActive: boolean;
  sla: {
    firstResponseTime: number;
    resolutionTime: number;
    businessHoursOnly: boolean;
  };
  createdAt: Date;
  updatedAt: Date;
}

export class Queue {
  private constructor(private readonly props: QueueProps) {}

  get id(): string { return this.props.id; }
  get queueId(): string { return this.props.queueId; }
  get name(): string { return this.props.name; }
  get description(): string { return this.props.description; }
  get type(): QueueType { return this.props.type; }
  get priority(): number { return this.props.priority; }
  get requiredSkills(): string[] { return this.props.requiredSkills; }
  get assignedTeams(): string[] { return this.props.assignedTeams; }
  get assignedAgents(): string[] { return this.props.assignedAgents; }
  get routingStrategy(): RoutingStrategy { return this.props.routingStrategy; }
  get escalationRules(): EscalationRuleProps[] { return this.props.escalationRules; }
  get isActive(): boolean { return this.props.isActive; }
  get sla() { return this.props.sla; }

  static create(props: QueueProps): Queue {
    return new Queue(props);
  }

  hasAgent(agentId: string): boolean {
    return this.props.assignedAgents.includes(agentId);
  }

  toPlainObject(): QueueProps {
    return { ...this.props };
  }
}
