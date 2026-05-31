import { HttpStatus } from '@nestjs/common';
import { BaseException } from './base.exception';

/**
 * Exception thrown when validation fails
 */
export class ValidationException extends BaseException {
  constructor(message: string, violations: Array<{ field: string; message: string }>) {
    super(
      message,
      HttpStatus.BAD_REQUEST,
      'VALIDATION_ERROR',
      { violations },
    );
  }
}
