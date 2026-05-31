export class RequestContext {
  private static _current: RequestContext | null = null;
  constructor(public readonly requestId: string, public readonly userId?: string, public readonly correlationId?: string) {}
  static set(context: RequestContext): void { RequestContext._current = context; }
  static get(): RequestContext | null { return RequestContext._current; }
  static clear(): void { RequestContext._current = null; }
}
