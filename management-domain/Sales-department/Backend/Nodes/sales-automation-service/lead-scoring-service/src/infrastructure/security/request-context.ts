import { createParamDecorator, ExecutionContext } from '@nestjs/common';

export interface RequestContextData {
  tenantId: string;
  userId: string;
  userRole?: string;
  correlationId?: string;
  requestId?: string;
}

export const RequestContext = createParamDecorator(
  (data: unknown, ctx: ExecutionContext): RequestContextData => {
    const request = ctx.switchToHttp().getRequest();

    return {
      tenantId: request.headers['x-tenant-id'] || request.tenant?.id || 'default',
      userId: request.headers['x-user-id'] || request.user?.id || 'system',
      userRole: request.headers['x-user-role'] || request.user?.role,
      correlationId: request.headers['x-correlation-id'],
      requestId: request.id,
    };
  }
);

export class RequestContextHelper {
  static setTenant(request: any, tenantId: string): void {
    request.tenant = { id: tenantId };
  }

  static getTenantId(request: any): string {
    return request.headers['x-tenant-id'] || request.tenant?.id || 'default';
  }

  static getUserId(request: any): string {
    return request.headers['x-user-id'] || request.user?.id || 'system';
  }
}
