import { Injectable, NestInterceptor, ExecutionContext, CallHandler, Logger } from '@nestjs/common';
import { Observable } from 'rxjs'; import { tap, map } from 'rxjs/operators';
@Injectable()
export class LoggingInterceptor implements NestInterceptor {
  private readonly logger = new Logger('HTTP');
  intercept(ctx: ExecutionContext, next: CallHandler): Observable<any> {
    const req = ctx.switchToHttp().getRequest(); const now = Date.now();
    return next.handle().pipe(tap(() => this.logger.log(`${req.method} ${req.url} - ${Date.now() - now}ms`)));
  }
}
export interface ApiResponse<T> { success: boolean; data: T; timestamp: string; }
@Injectable()
export class TransformInterceptor<T> implements NestInterceptor<T, ApiResponse<T>> {
  intercept(ctx: ExecutionContext, next: CallHandler): Observable<ApiResponse<T>> {
    return next.handle().pipe(map(data => ({ success: true, data, timestamp: new Date().toISOString() })));
  }
}
