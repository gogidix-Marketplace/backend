export class RequestContext {
  private static _current: RequestContext | null = null;

  constructor(
    public readonly requestId: string,
    public readonly userId?: string,
    public readonly tenantId?: string,
    public readonly role?: string,
  ) {}

  static set current(ctx: RequestContext | null) {
    RequestContext._current = ctx;
  }

  static get current(): RequestContext | null {
    return RequestContext._current;
  }
}
