export class KpiDeletedEvent {
  constructor(
    public readonly kpiId: string,
    public readonly tenantId: string,
    public readonly timestamp: Date = new Date(),
  ) {}
}
