import {
  Injectable,
  NestInterceptor,
  ExecutionContext,
  CallHandler,
  Logger,
} from '@nestjs/common';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { RequestContextHolder } from '../../shared/context/request-context';

/**
 * Interceptor for logging requests and responses
 */
@Injectable()
export class LoggingInterceptor implements NestInterceptor {
  private readonly logger = new Logger(LoggingInterceptor.name);

  intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    const request = context.switchToHttp().getRequest();
    const { method, url } = request;
    const correlationId = RequestContextHolder.getCorrelationId();

    const now = Date.now();

    this.logger.log(
      `[${correlationId}] Incoming Request: ${method} ${url}`,
    );

    return next.handle().pipe(
      tap({
        next: () => {
          const response = context.switchToHttp().getResponse();
          const delay = Date.now() - now;
          this.logger.log(
            `[${correlationId}] Outgoing Response: ${method} ${url} - Status: ${response.statusCode} - ${delay}ms`,
          );
        },
        error: (error) => {
          const delay = Date.now() - now;
          this.logger.error(
            `[${correlationId}] Request Failed: ${method} ${url} - ${error.message} - ${delay}ms`,
            error.stack,
          );
        },
      }),
    );
  }
}
