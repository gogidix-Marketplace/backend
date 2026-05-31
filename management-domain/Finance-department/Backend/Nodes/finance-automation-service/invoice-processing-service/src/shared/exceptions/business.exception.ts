import { HttpStatus } from '@nestjs/common';
import { BaseException } from './base.exception';

/**
 * Exception thrown when a business rule is violated
 */
export class BusinessException extends BaseException {
  constructor(message: string, errorCode: string = 'BUSINESS_ERROR', context?: Record<string, unknown>) {
    super(
      message,
      HttpStatus.UNPROCESSABLE_ENTITY,
      errorCode,
      context,
    );
  }
}
