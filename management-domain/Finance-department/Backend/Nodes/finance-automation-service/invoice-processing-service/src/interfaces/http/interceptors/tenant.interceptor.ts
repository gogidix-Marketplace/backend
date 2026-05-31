import {
  Injectable,
  NestInterceptor,
  ExecutionContext,
  CallHandler,
  createParamDecorator,
  ExecutionContext as EC,
} from '@nestjs/common';
import { Observable } from 'rxjs';
import { Request } from 'express';
import { RequestContextHolder, RequestContextData } from '../../../shared/context/request-context';

/**
 * Request context decorator
 * Extracts request context from the request
 */
export const RequestContext = createParamDecorator(
  (data: unknown, ctx: EC): RequestContextData => {
    const request = ctx.switchToHttp().getRequest<Request>();
    const rolesHeader = request.headers['x-roles'];
    const rolesValue = typeof rolesHeader === 'string'
      ? rolesHeader.split(',')
      : Array.isArray(rolesHeader)
        ? rolesHeader
        : (typeof request.roles === 'string' ? [request.roles] : request.roles || ['user']);

    return {
      tenantId: request.headers['x-tenant-id'] as string || request.tenantId || 'default',
      userId: request.headers['x-user-id'] as string || request.userId || 'system',
      organizationId: request.headers['x-organization-id'] as string || request.organizationId || 'default',
      correlationId: request.headers['x-correlation-id'] as string || request.correlationId || generateCorrelationId(),
      roles: rolesValue,
    };
  },
);

// Re-export the type for convenience
export type RequestContextType = RequestContextData;

/**
 * Tenant ID decorator
 */
export const TenantId = createParamDecorator(
  (data: unknown, ctx: EC): string => {
    const request = ctx.switchToHttp().getRequest<Request>();
    return request.headers['x-tenant-id'] as string || request.tenantId || 'default';
  },
);

/**
 * Interceptor to extract and set request context
 * Handles multi-tenancy by extracting tenant info from headers
 */
@Injectable()
export class TenantIdInterceptor implements NestInterceptor {
  intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    const request = context.switchToHttp().getRequest<Request>();

    // Extract tenant ID from headers or default
    const tenantId = request.headers['x-tenant-id'] as string || 'default';
    const userId = request.headers['x-user-id'] as string || 'system';
    const organizationId = request.headers['x-organization-id'] as string || 'default';
    const correlationId = request.headers['x-correlation-id'] as string || generateCorrelationId();
    const roles = ((request.headers['x-roles'] as string) || 'user').split(',');

    // Set context on request for later use
    request.tenantId = tenantId;
    request.userId = userId;
    request.organizationId = organizationId;
    request.correlationId = correlationId;
    request.roles = roles;

    // Store in async local storage for access in services
    RequestContextHolder.setContext({
      tenantId,
      userId,
      organizationId,
      correlationId,
      roles,
    });

    return next.handle();
  }
}

function generateCorrelationId(): string {
  return `corr-${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
}

/**
 * User ID decorator
 */
export const UserId = createParamDecorator(
  (data: unknown, ctx: EC): string => {
    const request = ctx.switchToHttp().getRequest<Request>();
    return request.headers['x-user-id'] as string || request.userId || 'system';
  },
);

/**
 * Organization ID decorator
 */
export const OrganizationId = createParamDecorator(
  (data: unknown, ctx: EC): string => {
    const request = ctx.switchToHttp().getRequest<Request>();
    return request.headers['x-organization-id'] as string || request.organizationId || 'default';
  },
);
