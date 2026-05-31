import { Injectable, NestInterceptor, ExecutionContext, CallHandler } from '@nestjs/common';
import { Observable } from 'rxjs';
@Injectable()
export class UserInterceptor implements NestInterceptor { intercept(c: ExecutionContext, n: CallHandler): Observable<any> { c.switchToHttp().getRequest().userId = c.switchToHttp().getRequest().headers['x-user-id'] || 'system-user'; return n.handle(); } }
