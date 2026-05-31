import { createParamDecorator, ExecutionContext } from '@nestjs/common';
export const CurrentTenant = createParamDecorator((d: unknown, ctx: ExecutionContext) => ctx.switchToHttp().getRequest().tenantId || 'default-tenant');
export const CurrentUser = createParamDecorator((d: unknown, ctx: ExecutionContext) => ctx.switchToHttp().getRequest().user || { id: 'system-user' });
