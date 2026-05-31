import { HttpStatus } from '@nestjs/common';
import { BaseException } from './base.exception';

/**
 * Business logic exception
 * Thrown when a business rule is violated
 */
export class BusinessException extends BaseException {
  readonly errorCode = 'BUSINESS_ERROR';

  constructor(message: string, context?: Record<string, unknown>) {
    super(message, HttpStatus.UNPROCESSABLE_ENTITY, context);
  }
}
