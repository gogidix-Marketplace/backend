import { HttpStatus } from '@nestjs/common';
import { BaseException } from './base.exception';

/**
 * Validation exception
 * Thrown when input validation fails
 */
export class ValidationException extends BaseException {
  readonly errorCode = 'VALIDATION_ERROR';

  constructor(message: string, public errors?: Array<{ field: string; message: string }>) {
    super(message, HttpStatus.BAD_REQUEST, { errors });
  }
}
