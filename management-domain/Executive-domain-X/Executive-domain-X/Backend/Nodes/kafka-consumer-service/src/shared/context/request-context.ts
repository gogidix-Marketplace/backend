export class RequestContextData { constructor(public tenantId: string, public userId: string, public organizationId: string) {} }
export class RequestContextHolder { private static ctx: RequestContextData | null = null; static set(c: RequestContextData) { RequestContextHolder.ctx = c; } static get() { return RequestContextHolder.ctx; } static clear() { RequestContextHolder.ctx = null; } }
