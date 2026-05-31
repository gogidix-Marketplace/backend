export class SourceStatusChangedEvent {
  constructor(
    public readonly sourceId: string,
    public readonly sourceName: string,
    public readonly oldStatus: string,
    public readonly newStatus: string,
    public readonly timestamp: Date = new Date(),
  ) {}
}
