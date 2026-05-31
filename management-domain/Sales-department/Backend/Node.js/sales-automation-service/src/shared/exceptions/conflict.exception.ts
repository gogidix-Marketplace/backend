import { BaseException } from './base.exception';
import { HttpStatus } from '@nestjs/common';

export class ConflictException extends BaseException {
  readonly statusCode = HttpStatus.CONFLICT;
  readonly details: any;

  constructor(message: string, details?: any) {
    super(message);
    this.details = details;
    Object.setPrototypeOf(this, ConflictException.prototype);
  }
}
