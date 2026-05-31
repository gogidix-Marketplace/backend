export class LogIngestedEvent {
  constructor(
    public readonly logId: string,
    public readonly sourceName: string,
    public readonly level: string,
    public readonly timestamp: Date = new Date(),
  ) {}
}
