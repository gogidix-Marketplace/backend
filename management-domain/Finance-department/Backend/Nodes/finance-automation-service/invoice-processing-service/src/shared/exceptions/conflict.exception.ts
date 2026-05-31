import { HttpStatus } from '@nestjs/common';
import { BaseException } from './base.exception';

/**
 * Exception thrown when a conflict occurs (e.g., duplicate resource)
 */
export class ConflictException extends BaseException {
  constructor(message: string, resource: string, context?: Record<string, unknown>) {
    super(
      message,
      HttpStatus.CONFLICT,
      'CONFLICT_ERROR',
      { resource, ...context },
    );
  }
}
