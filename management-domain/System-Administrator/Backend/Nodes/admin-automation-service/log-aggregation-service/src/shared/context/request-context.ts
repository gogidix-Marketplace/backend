export class RequestContextData {
  constructor(public tenantId: string, public userId: string, public organizationId: string, public correlationId?: string) {}
}
export class RequestContextHolder {
  private static context: RequestContextData | null = null;
  static setContext(c: RequestContextData): void { RequestContextHolder.context = c; }
  static getContext(): RequestContextData | null { return RequestContextHolder.context; }
  static clearContext(): void { RequestContextHolder.context = null; }
  static getTenantId(): string { return RequestContextHolder.context?.tenantId || 'default'; }
  static getUserId(): string { return RequestContextHolder.context?.userId || 'system'; }
}
