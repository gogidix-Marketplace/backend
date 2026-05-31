export class PayrollFailedEvent {
  constructor(
    public readonly payrollId: string,
    public readonly tenantId: string,
    public readonly error: string,
    public readonly timestamp: Date = new Date(),
  ) {}
}
