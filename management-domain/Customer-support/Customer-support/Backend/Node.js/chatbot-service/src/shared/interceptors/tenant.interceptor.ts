import { Injectable, NestInterceptor, ExecutionContext, CallHandler, Logger } from '@nestjs/common';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { RequestContext } from '../context/request-context';

@Injectable()
export class TenantInterceptor implements NestInterceptor {
  private readonly logger = new Logger(TenantInterceptor.name);

  intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    const request = context.switchToHttp().getRequest();
    const tenantId = request.headers['x-tenant-id'] || request.user?.tenantId;

    if (tenantId) {
      RequestContext.current = new RequestContext(
        request.id || request.headers['x-request-id'],
        request.user?.userId,
        tenantId,
        request.user?.role,
      );
    }

    return next.handle().pipe(
      tap(() => {
        RequestContext.current = null;
      }),
    );
  }
}
