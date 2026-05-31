export class PayrollCompletedEvent {
  constructor(
    public readonly payrollId: string,
    public readonly tenantId: string,
    public readonly processedCount: number,
    public readonly totalGrossPay: number,
    public readonly totalNetPay: number,
    public readonly currency: string,
    public readonly timestamp: Date = new Date(),
  ) {}
}
