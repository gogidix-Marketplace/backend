import {
  ExceptionFilter,
  Catch,
  ArgumentsHost,
  HttpException,
  HttpStatus,
  Logger,
} from '@nestjs/common';
import { Request, Response } from 'express';
import { BaseException, NotFoundException, ValidationException, ConflictException } from '../../../shared/exceptions';

/**
 * Global exception filter
 * Catches all exceptions and formats them consistently
 */
@Catch()
export class GlobalExceptionFilter implements ExceptionFilter {
  private readonly logger = new Logger(GlobalExceptionFilter.name);

  catch(exception: unknown, host: ArgumentsHost) {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();
    const request = ctx.getRequest<Request>();

    let status = HttpStatus.INTERNAL_SERVER_ERROR;
    let message = 'Internal server error';
    let errorCode = 'INTERNAL_SERVER_ERROR';
    let details: any = undefined;

    // Handle BaseException and its subclasses
    if (exception instanceof BaseException) {
      status = exception.getStatus();
      message = exception.message;
      errorCode = exception.errorCode;
      details = exception.context;
    }
    // Handle HttpException
    else if (exception instanceof HttpException) {
      status = exception.getStatus();
      const exceptionResponse = exception.getResponse();

      if (typeof exceptionResponse === 'string') {
        message = exceptionResponse;
      } else if (typeof exceptionResponse === 'object') {
        const responseObj = exceptionResponse as any;
        message = responseObj.message || message;
        details = responseObj.details;
        errorCode = responseObj.errorCode || errorCode;
      }
    }
    // Handle Error
    else if (exception instanceof Error) {
      message = exception.message;
    }

    // Build error response
    const errorResponse = {
      statusCode: status,
      message,
      errorCode,
      timestamp: new Date().toISOString(),
      path: request.url,
      method: request.method,
      ...(details && { details }),
      ...(process.env.NODE_ENV === 'development' && exception instanceof Error
        ? { stack: exception.stack }
        : {}),
    };

    // Log the error
    this.logError(request, exception, status);

    response.status(status).json(errorResponse);
  }

  private logError(request: Request, exception: unknown, status: number): void {
    const message = `${request.method} ${request.url} - ${status}`;

    if (status >= 500) {
      this.logger.error(message, exception instanceof Error ? exception.stack : String(exception));
    } else if (status >= 400) {
      this.logger.warn(message + ' - ' + (exception instanceof Error ? exception.message : String(exception)));
    }
  }
}

/**
 * HTTP status codes for exception types
 */
const ExceptionStatusMap: Record<string, HttpStatus> = {
  NotFoundException: HttpStatus.NOT_FOUND,
  ValidationException: HttpStatus.BAD_REQUEST,
  ConflictException: HttpStatus.CONFLICT,
  BusinessException: HttpStatus.UNPROCESSABLE_ENTITY,
  InvoiceException: HttpStatus.BAD_REQUEST,
  InvoiceNotFoundException: HttpStatus.NOT_FOUND,
  DuplicateInvoiceException: HttpStatus.CONFLICT,
  InvoiceValidationException: HttpStatus.BAD_REQUEST,
  InvoiceProcessingException: HttpStatus.UNPROCESSABLE_ENTITY,
  OcrProcessingException: HttpStatus.UNPROCESSABLE_ENTITY,
  InvalidInvoiceStatusException: HttpStatus.BAD_REQUEST,
};
