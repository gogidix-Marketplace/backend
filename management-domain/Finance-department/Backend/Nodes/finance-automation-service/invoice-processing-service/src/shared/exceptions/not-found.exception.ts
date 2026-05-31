import { HttpStatus } from '@nestjs/common';
import { BaseException } from './base.exception';

/**
 * Exception thrown when a requested resource is not found
 */
export class NotFoundException extends BaseException {
  constructor(resource: string, identifier: string, context?: Record<string, unknown>) {
    super(
      `${resource} with identifier '${identifier}' not found`,
      HttpStatus.NOT_FOUND,
      'RESOURCE_NOT_FOUND',
      { resource, identifier, ...context },
    );
  }
}
