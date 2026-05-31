import { HttpStatus } from '@nestjs/common';
import { BaseException } from './base.exception';

/**
 * Not found exception
 * Thrown when a requested resource is not found
 */
export class NotFoundException extends BaseException {
  readonly errorCode = 'NOT_FOUND';

  constructor(message: string, context?: Record<string, unknown>) {
    super(message, HttpStatus.NOT_FOUND, context);
  }
}
