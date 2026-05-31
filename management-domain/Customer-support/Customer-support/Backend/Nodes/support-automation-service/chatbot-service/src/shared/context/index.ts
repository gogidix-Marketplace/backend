export class RequestContext {
  private static _current: RequestContext | null = null;
  constructor(public readonly requestId: string, public readonly userId?: string) {}
  static set(c: RequestContext): void { RequestContext._current = c; }
  static get(): RequestContext | null { return RequestContext._current; }
  static clear(): void { RequestContext._current = null; }
}
