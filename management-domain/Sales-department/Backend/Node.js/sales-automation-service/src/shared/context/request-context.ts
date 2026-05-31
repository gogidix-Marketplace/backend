import { createParamDecorator, ExecutionContext } from '@nestjs/common';

/**
 * Request context for multi-tenancy support
 * Contains tenant, user, and correlation information
 */
export interface RequestContextData {
  tenantId: string;
  userId: string;
  correlationId: string;
  roles: string[];
  organizationId?: string;
}

/**
 * Custom decorator to extract request context from request
 */
export const RequestContext = createParamDecorator(
  (data: unknown, ctx: ExecutionContext): RequestContextData => {
    const request = ctx.switchToHttp().getRequest();
    return {
      tenantId: request.headers['x-tenant-id'] || request.tenantId || 'default',
      userId: request.headers['x-user-id'] || request.userId || 'system',
      correlationId: request.headers['x-correlation-id'] || request.correlationId || generateCorrelationId(),
      roles: request.headers['x-roles']?.split(',') || request.roles || [],
      organizationId: request.headers['x-organization-id'] || request.organizationId,
    };
  },
);

function generateCorrelationId(): string {
  return `corr-${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
}

/**
 * Request context holder class for storing context in async local storage
 */
import { AsyncLocalStorage } from 'async_hooks';

export const RequestContextStorage = new AsyncLocalStorage<RequestContextData>();

export class RequestContextHolder {
  static setContext(context: RequestContextData): void {
    RequestContextStorage.enterWith(context);
  }

  static getContext(): RequestContextData | undefined {
    return RequestContextStorage.getStore();
  }

  static getTenantId(): string {
    return RequestContextStorage.getStore()?.tenantId || 'default';
  }

  static getUserId(): string {
    return RequestContextStorage.getStore()?.userId || 'system';
  }

  static getCorrelationId(): string {
    return RequestContextStorage.getStore()?.correlationId || generateCorrelationId();
  }

  static getOrganizationId(): string | undefined {
    return RequestContextStorage.getStore()?.organizationId;
  }

  static hasRole(role: string): boolean {
    const roles = RequestContextStorage.getStore()?.roles || [];
    return roles.includes(role);
  }
}

/**
 * Execution context interface for use cases and services
 */
export interface ExecutionContext {
  tenantId: string;
  userId: string;
  correlationId: string;
  roles?: string[];
  organizationId?: string;
}

export class ExecutionContextImpl implements ExecutionContext {
  constructor(
    public tenantId: string = 'default',
    public userId: string = 'system',
    public correlationId: string = generateCorrelationId(),
    public roles?: string[],
    public organizationId?: string,
  ) {}
}
