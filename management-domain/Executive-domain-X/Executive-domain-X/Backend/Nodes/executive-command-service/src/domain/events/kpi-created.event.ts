export class KpiCreatedEvent {
  constructor(
    public readonly kpiId: string,
    public readonly tenantId: string,
    public readonly name: string,
    public readonly category: string,
    public readonly executiveLevel: string,
    public readonly timestamp: Date = new Date(),
  ) {}
}
