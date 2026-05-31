import {
  ExceptionFilter,
  Catch,
  ArgumentsHost,
  HttpException,
  HttpStatus,
  Logger,
} from '@nestjs/common';
import { Request, Response } from 'express';
import { RequestContextHolder } from '../../shared/context/request-context';
import { BaseException } from '../../shared/exceptions/base.exception';

/**
 * Global exception filter to standardize error responses
 */
@Catch()
export class AllExceptionsFilter implements ExceptionFilter {
  private readonly logger = new Logger(AllExceptionsFilter.name);

  catch(exception: unknown, host: ArgumentsHost): void {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();
    const request = ctx.getRequest<Request>();
    const correlationId = RequestContextHolder.getCorrelationId();

    let status = HttpStatus.INTERNAL_SERVER_ERROR;
    let message = 'Internal server error';
    let errorCode = 'INTERNAL_SERVER_ERROR';
    let details: Record<string, unknown> | undefined;

    if (exception instanceof HttpException) {
      status = exception.getStatus();
      const exceptionResponse = exception.getResponse();

      if (typeof exceptionResponse === 'string') {
        message = exceptionResponse;
      } else if (typeof exceptionResponse === 'object') {
        const responseObj = exceptionResponse as Record<string, unknown>;
        message = (responseObj.message as string) || message;
        if (Array.isArray(responseObj.message)) {
          message = responseObj.message.join(', ');
        }
      }
    }

    if (exception instanceof BaseException) {
      errorCode = exception.errorCode;
      details = exception.context;
      message = exception.message;
    }

    const errorResponse = {
      success: false,
      error: {
        code: errorCode,
        message,
        details,
      },
      correlationId,
      timestamp: new Date().toISOString(),
      path: request.url,
    };

    this.logger.error(
      `[${correlationId}] ${request.method} ${request.url} - Status: ${status} - Error: ${message}`,
      exception instanceof Error ? exception.stack : undefined,
    );

    response.status(status).json(errorResponse);
  }
}
