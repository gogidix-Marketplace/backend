export class ScalingFailedEvent {
  constructor(
    public readonly policyId: string,
    public readonly policyName: string,
    public readonly error: string,
    public readonly timestamp: Date = new Date(),
  ) {}
}
