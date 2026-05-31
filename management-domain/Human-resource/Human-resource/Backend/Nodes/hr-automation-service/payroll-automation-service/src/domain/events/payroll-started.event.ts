export class PayrollStartedEvent {
  constructor(
    public readonly payrollId: string,
    public readonly tenantId: string,
    public readonly employeeIds: string[],
    public readonly periodStart: Date,
    public readonly periodEnd: Date,
    public readonly timestamp: Date = new Date(),
  ) {}
}
