import { Injectable, NestInterceptor, ExecutionContext, CallHandler } from '@nestjs/common';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { RequestContext } from '../context/request-context';

@Injectable()
export class UserInterceptor implements NestInterceptor {
  intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    const request = context.switchToHttp().getRequest();

    if (request.user) {
      RequestContext.current = new RequestContext(
        request.id || request.headers['x-request-id'],
        request.user.userId,
        request.user.tenantId,
        request.user.role,
      );
    }

    return next.handle().pipe(
      tap(() => {
        RequestContext.current = null;
      }),
    );
  }
}
