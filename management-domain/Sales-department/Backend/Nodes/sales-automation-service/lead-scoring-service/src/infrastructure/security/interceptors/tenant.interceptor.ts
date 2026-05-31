import {
  Injectable,
  NestInterceptor,
  ExecutionContext,
  CallHandler,
  BadRequestException,
} from '@nestjs/common';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { RequestContext } from '../request-context.service';

@Injectable()
export class TenantInterceptor implements NestInterceptor {
  constructor(private readonly requestContext: RequestContext) {}

  intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    const request = context.switchToHttp().getRequest();

    // Extract tenant ID from various sources
    let tenantId: string;

    // 1. Check header first
    tenantId = request.headers['x-tenant-id'];

    // 2. Check query parameter
    if (!tenantId) {
      tenantId = request.query?.tenantId;
    }

    // 3. Check body
    if (!tenantId && request.body) {
      tenantId = request.body.tenantId;
    }

    // 4. Check user context (if auth is implemented)
    if (!tenantId && request.user?.tenantId) {
      tenantId = request.user.tenantId;
    }

    if (!tenantId) {
      throw new BadRequestException('Tenant ID is required');
    }

    // Set tenant in request context
    this.requestContext.setTenant(tenantId);

    // Also set user ID if available
    if (request.user?.id) {
      this.requestContext.setUserId(request.user.id);
    }

    return next.handle().pipe(
      tap(() => {
        // Clear context after request is done
        this.requestContext.clear();
      })
    );
  }
}
