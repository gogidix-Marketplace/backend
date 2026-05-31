import { BaseException } from './base.exception';
import { HttpStatus } from '@nestjs/common';

export class DomainException extends BaseException {
  readonly statusCode = HttpStatus.BAD_REQUEST;
  readonly details: any;

  constructor(message: string, details?: any) {
    super(message);
    this.details = details;
    Object.setPrototypeOf(this, DomainException.prototype);
  }
}
