import { Injectable, Scope } from '@nestjs/common';
import { createParamDecorator, ExecutionContext } from '@nestjs/common';
import { RequestContextData as RequestContextDataOrig } from './request-context';

export type RequestContextData = RequestContextDataOrig;

export const RequestContextDecorator = createParamDecorator(
  (data: unknown, ctx: ExecutionContext): RequestContextData => {
    const request = ctx.switchToHttp().getRequest();

    return {
      tenantId: request.headers['x-tenant-id'] || request.tenant?.id || request.tenantId || 'default',
      userId: request.headers['x-user-id'] || request.user?.id || request.userId || 'system',
      userRole: request.headers['x-user-role'] || request.user?.role,
      correlationId: request.headers['x-correlation-id'],
      requestId: request.id,
    };
  }
);

@Injectable({ scope: Scope.REQUEST })
export class RequestContext {
  private _tenantId?: string;
  private _userId?: string;
  private _userRole?: string;
  private _correlationId?: string;

  setTenant(tenantId: string): void {
    this._tenantId = tenantId;
  }

  getTenant(): string {
    return this._tenantId || 'default';
  }

  setUserId(userId: string): void {
    this._userId = userId;
  }

  getUserId(): string {
    return this._userId || 'system';
  }

  setUserRole(userRole: string): void {
    this._userRole = userRole;
  }

  getUserRole(): string | undefined {
    return this._userRole;
  }

  setCorrelationId(correlationId: string): void {
    this._correlationId = correlationId;
  }

  getCorrelationId(): string | undefined {
    return this._correlationId;
  }

  clear(): void {
    this._tenantId = undefined;
    this._userId = undefined;
    this._userRole = undefined;
    this._correlationId = undefined;
  }

  toJSON(): RequestContextData {
    return {
      tenantId: this.getTenant(),
      userId: this.getUserId(),
      userRole: this._userRole,
      correlationId: this._correlationId,
    };
  }
}
