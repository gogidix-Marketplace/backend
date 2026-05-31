import {
  Injectable,
  NestInterceptor,
  ExecutionContext,
  CallHandler,
} from '@nestjs/common';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { RequestContextHolder } from '../../shared/context/request-context';

/**
 * Response wrapper interface
 */
export interface Response<T> {
  success: boolean;
  data: T;
  correlationId: string;
  timestamp: string;
}

/**
 * Interceptor to wrap all responses in a standard format
 */
@Injectable()
export class TransformInterceptor<T> implements NestInterceptor<T, Response<T>> {
  intercept(context: ExecutionContext, next: CallHandler): Observable<Response<T>> {
    const correlationId = RequestContextHolder.getCorrelationId();

    return next.handle().pipe(
      map(data => ({
        success: true,
        data,
        correlationId,
        timestamp: new Date().toISOString(),
      })),
    );
  }
}
