export class EmployeePayrollCalculatedEvent {
  constructor(
    public readonly payrollId: string,
    public readonly employeeId: string,
    public readonly tenantId: string,
    public readonly grossPay: number,
    public readonly netPay: number,
    public readonly currency: string,
    public readonly timestamp: Date = new Date(),
  ) {}
}
