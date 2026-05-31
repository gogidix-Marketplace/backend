import { HttpException, HttpStatus } from '@nestjs/common';

/**
 * Base exception class for all domain exceptions
 */
export abstract class BaseException extends HttpException {
  abstract readonly errorCode: string;
  readonly context?: Record<string, unknown>;

  constructor(
    message: string,
    status: HttpStatus,
    context?: Record<string, unknown>,
  ) {
    super(message, status);
    this.context = context;
  }

  getStatus(): HttpStatus {
    return this.getStatus();
  }
}
