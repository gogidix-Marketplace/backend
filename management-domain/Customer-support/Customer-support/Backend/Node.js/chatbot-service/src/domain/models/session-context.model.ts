export interface SessionContextProps {
  userId?: string;
  customerId?: string;
  locale: string;
  timezone?: string;
  metadata: Record<string, unknown>;
  accumulatedContext: Record<string, unknown>;
  turnCount: number;
  lastActivity: Date;
}

export class SessionContext {
  private constructor(private props: SessionContextProps) {}

  static create(props: Partial<SessionContextProps>): SessionContext {
    return new SessionContext({
      locale: props.locale || 'en',
      timezone: props.timezone || 'UTC',
      metadata: props.metadata || {},
      accumulatedContext: props.accumulatedContext || {},
      turnCount: props.turnCount || 0,
      lastActivity: props.lastActivity || new Date(),
      userId: props.userId,
      customerId: props.customerId,
    });
  }

  get userId(): string | undefined {
    return this.props.userId;
  }

  get customerId(): string | undefined {
    return this.props.customerId;
  }

  get locale(): string {
    return this.props.locale;
  }

  get timezone(): string | undefined {
    return this.props.timezone;
  }

  get metadata(): Record<string, unknown> {
    return this.props.metadata;
  }

  get accumulatedContext(): Record<string, unknown> {
    return this.props.accumulatedContext;
  }

  get turnCount(): number {
    return this.props.turnCount;
  }

  get lastActivity(): Date {
    return this.props.lastActivity;
  }

  incrementTurn(): void {
    this.props.turnCount += 1;
    this.props.lastActivity = new Date();
  }

  toPlainObject(): SessionContextProps {
    return { ...this.props };
  }
}
