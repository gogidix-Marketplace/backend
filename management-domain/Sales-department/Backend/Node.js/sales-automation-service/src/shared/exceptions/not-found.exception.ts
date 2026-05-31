import { BaseException } from './base.exception';
import { HttpStatus } from '@nestjs/common';

export class NotFoundException extends BaseException {
  readonly statusCode = HttpStatus.NOT_FOUND;
  readonly details: any;

  constructor(message: string, details?: any) {
    super(message);
    this.details = details;
    Object.setPrototypeOf(this, NotFoundException.prototype);
  }
}
