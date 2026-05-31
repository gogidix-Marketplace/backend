export class RetentionAppliedEvent {
  constructor(
    public readonly sourceName: string,
    public readonly deletedIndices: number,
    public readonly timestamp: Date = new Date(),
  ) {}
}
