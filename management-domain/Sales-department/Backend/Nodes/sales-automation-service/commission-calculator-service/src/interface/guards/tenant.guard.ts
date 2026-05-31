import { CanActivate, ExecutionContext, Injectable, UnauthorizedException } from '@nestjs/common';
import { Reflector } from '@nestjs/core';
import { RequestContextHolder } from '../../shared/context/request-context';

/**
 * Guard to ensure tenant context is present
 */
@Injectable()
export class TenantGuard implements CanActivate {
  constructor(private readonly reflector: Reflector) {}

  canActivate(context: ExecutionContext): boolean {
    const request = context.switchToHttp().getRequest();
    const tenantId = request.headers['x-tenant-id'] || request.tenantId;

    if (!tenantId) {
      throw new UnauthorizedException('Tenant ID is required');
    }

    // Set the context in async local storage
    RequestContextHolder.setContext({
      tenantId,
      userId: request.headers['x-user-id'] || request.userId || 'system',
      correlationId: request.headers['x-correlation-id'] || request.correlationId || this.generateCorrelationId(),
      roles: request.headers['x-roles']?.split(',') || request.roles || [],
      organizationId: request.headers['x-organization-id'] || request.organizationId,
    });

    return true;
  }

  private generateCorrelationId(): string {
    return `corr-${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
  }
}
