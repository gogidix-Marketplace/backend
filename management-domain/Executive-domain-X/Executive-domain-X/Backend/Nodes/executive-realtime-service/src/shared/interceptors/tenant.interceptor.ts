import { Injectable, NestInterceptor, ExecutionContext, CallHandler } from '@nestjs/common';
import { Observable } from 'rxjs';
@Injectable()
export class TenantInterceptor implements NestInterceptor { intercept(c: ExecutionContext, n: CallHandler): Observable<any> { const r = c.switchToHttp().getRequest(); r.tenantId = r.headers['x-tenant-id'] || 'default-tenant'; return n.handle(); } }
