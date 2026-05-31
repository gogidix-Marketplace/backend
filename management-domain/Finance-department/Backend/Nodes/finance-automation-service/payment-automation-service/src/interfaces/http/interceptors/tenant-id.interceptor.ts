import {
  Injectable,
  NestInterceptor,
  ExecutionContext,
  CallHandler,
  BadRequestException,
} from '@nestjs/common';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable()
export class TenantIdInterceptor implements NestInterceptor {
  intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    const request = context.switchToHttp().getRequest();
    const tenantId =
      request.headers['x-tenant-id'] ||
      request.query.tenantId ||
      request.body?.tenantId ||
      'default';

    request.tenantId = tenantId;

    return next.handle();
  }
}
