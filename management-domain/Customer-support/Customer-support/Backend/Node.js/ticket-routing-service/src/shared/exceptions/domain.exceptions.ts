export class RoutingException extends Error {
  constructor(
    message: string,
    public readonly code: string,
    public readonly statusCode: number = 500,
  ) {
    super(message);
    this.name = 'RoutingException';
  }
}

export class AgentNotFoundException extends RoutingException {
  constructor(agentId: string) {
    super(`Agent ${agentId} not found`, 'AGENT_NOT_FOUND', 404);
    this.name = 'AgentNotFoundException';
  }
}

export class NoAvailableAgentException extends RoutingException {
  constructor(requiredSkills?: string[]) {
    super(
      requiredSkills
        ? `No available agents with skills: ${requiredSkills.join(', ')}`
        : 'No available agents',
      'NO_AVAILABLE_AGENT',
      503,
    );
    this.name = 'NoAvailableAgentException';
  }
}

export class QueueFullException extends RoutingException {
  constructor(queueId: string) {
    super(`Queue ${queueId} is full`, 'QUEUE_FULL', 503);
    this.name = 'QueueFullException';
  }
}
