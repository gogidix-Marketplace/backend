import { HttpStatus } from '@nestjs/common';
import { BaseException } from './base.exception';

/**
 * Conflict exception
 * Thrown when a conflict with existing data occurs
 */
export class ConflictException extends BaseException {
  readonly errorCode = 'CONFLICT';

  constructor(message: string, context?: Record<string, unknown>) {
    super(message, HttpStatus.CONFLICT, context);
  }
}
