import { Injectable, NestInterceptor, ExecutionContext, CallHandler } from '@nestjs/common';
import { Observable } from 'rxjs';
@Injectable()
export class UserInterceptor implements NestInterceptor { intercept(c: ExecutionContext, n: CallHandler): Observable<any> { const r = c.switchToHttp().getRequest(); r.userId = r.headers['x-user-id'] || r.user?.id || 'system-user'; return n.handle(); } }
