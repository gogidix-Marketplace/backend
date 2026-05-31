import { createParamDecorator, ExecutionContext } from '@nestjs/common';

export interface RequestContextData {
  tenantId: string;
  userId: string;
  correlationId: string;
  userAgent?: string;
  ipAddress?: string;
  roles: string[];
}

export const RequestContext = createParamDecorator(
  (data: unknown, ctx: ExecutionContext): RequestContextData => {
    const request = ctx.switchToHttp().getRequest();

    return {
      tenantId: request.headers['x-tenant-id'] || 'default',
      userId: request.user?.id || 'system',
      correlationId: request.headers['x-correlation-id'] || generateCorrelationId(),
      userAgent: request.headers['user-agent'],
      ipAddress: request.ip || request.connection.remoteAddress,
      roles: request.user?.roles || [],
    };
  },
);

function generateCorrelationId(): string {
  return `corr_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
}
