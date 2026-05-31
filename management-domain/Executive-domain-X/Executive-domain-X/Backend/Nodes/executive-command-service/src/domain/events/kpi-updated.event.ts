export class KpiUpdatedEvent {
  constructor(
    public readonly kpiId: string,
    public readonly tenantId: string,
    public readonly name: string,
    public readonly value: number,
    public readonly status: string,
    public readonly executiveLevel: string,
    public readonly category: string,
    public readonly timestamp: Date = new Date(),
  ) {}
}
