import { HttpException, HttpStatus } from '@nestjs/common';

/**
 * Base exception class for all custom exceptions
 */
export abstract class BaseException extends HttpException {
  readonly errorCode: string;
  readonly context?: Record<string, unknown>;

  constructor(
    message: string,
    statusCode: HttpStatus,
    errorCode: string,
    context?: Record<string, unknown>,
  ) {
    super(
      {
        statusCode,
        message,
        errorCode,
        context,
        timestamp: new Date().toISOString(),
      },
      statusCode,
    );
    this.errorCode = errorCode;
    this.context = context;
    this.name = this.constructor.name;
  }
}
