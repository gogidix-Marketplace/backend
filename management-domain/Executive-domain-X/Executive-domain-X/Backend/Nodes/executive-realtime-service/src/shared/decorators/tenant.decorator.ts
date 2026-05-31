import { createParamDecorator, ExecutionContext } from '@nestjs/common';
export const CurrentTenant = createParamDecorator((d: unknown, ctx: ExecutionContext) => { const r = ctx.switchToHttp().getRequest(); return r.tenantId || 'default-tenant'; });
export const CurrentUser = createParamDecorator((d: unknown, ctx: ExecutionContext) => { const r = ctx.switchToHttp().getRequest(); return r.user || { id: r.userId || 'system-user' }; });
