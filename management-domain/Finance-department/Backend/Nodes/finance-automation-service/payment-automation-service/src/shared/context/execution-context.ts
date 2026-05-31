export interface ExecutionContext {
  tenantId: string;
  userId: string;
  correlationId: string;
  requestId: string;
  timestamp: Date;
  metadata?: Record<string, any>;
}

export class ExecutionContextImpl implements ExecutionContext {
  tenantId: string;
  userId: string;
  correlationId: string;
  requestId: string;
  timestamp: Date;
  metadata?: Record<string, any>;

  constructor(
    tenantId: string = 'default',
    userId: string = 'system',
    correlationId?: string,
    requestId?: string,
  ) {
    this.tenantId = tenantId;
    this.userId = userId;
    this.correlationId = correlationId || this.generateId();
    this.requestId = requestId || this.generateId();
    this.timestamp = new Date();
  }

  private generateId(): string {
    return `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
  }

  withTenant(tenantId: string): ExecutionContext {
    this.tenantId = tenantId;
    return this;
  }

  withUser(userId: string): ExecutionContext {
    this.userId = userId;
    return this;
  }

  withMetadata(metadata: Record<string, any>): ExecutionContext {
    this.metadata = { ...this.metadata, ...metadata };
    return this;
  }

  toJSON(): Record<string, any> {
    return {
      tenantId: this.tenantId,
      userId: this.userId,
      correlationId: this.correlationId,
      requestId: this.requestId,
      timestamp: this.timestamp,
      metadata: this.metadata,
    };
  }

  static fromJSON(json: Record<string, any>): ExecutionContext {
    const ctx = new ExecutionContextImpl(
      json.tenantId,
      json.userId,
      json.correlationId,
      json.requestId,
    );
    ctx.timestamp = new Date(json.timestamp);
    ctx.metadata = json.metadata;
    return ctx;
  }
}
