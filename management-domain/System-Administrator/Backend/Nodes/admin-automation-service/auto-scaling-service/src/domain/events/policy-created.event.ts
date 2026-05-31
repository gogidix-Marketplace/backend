export class PolicyCreatedEvent {
  constructor(
    public readonly policyId: string,
    public readonly policyName: string,
    public readonly resourceId: string,
    public readonly timestamp: Date = new Date(),
  ) {}
}
