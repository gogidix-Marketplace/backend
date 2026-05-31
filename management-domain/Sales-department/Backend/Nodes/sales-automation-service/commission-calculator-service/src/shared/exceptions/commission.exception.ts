import { HttpStatus } from '@nestjs/common';
import { BaseException } from './base.exception';

/**
 * Commission-related exceptions
 */

export class CommissionNotFoundException extends BaseException {
  readonly errorCode = 'COMMISSION_NOT_FOUND';

  constructor(commissionId: string) {
    super(`Commission with ID ${commissionId} not found`, HttpStatus.NOT_FOUND, { commissionId });
  }
}

export class CommissionRuleNotFoundException extends BaseException {
  readonly errorCode = 'COMMISSION_RULE_NOT_FOUND';

  constructor(ruleId: string) {
    super(`Commission rule with ID ${ruleId} not found`, HttpStatus.NOT_FOUND, { ruleId });
  }
}

export class InvalidCommissionStatusException extends BaseException {
  readonly errorCode = 'INVALID_COMMISSION_STATUS';

  constructor(currentStatus: string, expectedStatus: string[]) {
    super(
      `Invalid commission status. Current: ${currentStatus}, Expected: ${expectedStatus.join(', ')}`,
      HttpStatus.BAD_REQUEST,
      { currentStatus, expectedStatus },
    );
  }
}

export class CommissionCalculationException extends BaseException {
  readonly errorCode = 'COMMISSION_CALCULATION_ERROR';

  constructor(message: string, context?: Record<string, unknown>) {
    super(`Commission calculation error: ${message}`, HttpStatus.UNPROCESSABLE_ENTITY, context);
  }
}

export class DuplicateCommissionException extends BaseException {
  readonly errorCode = 'DUPLICATE_COMMISSION';

  constructor(salesRepId: string, period: string) {
    super(
      `Commission already exists for sales rep ${salesRepId} in period ${period}`,
      HttpStatus.CONFLICT,
      { salesRepId, period },
    );
  }
}

export class CommissionAlreadyPaidException extends BaseException {
  readonly errorCode = 'COMMISSION_ALREADY_PAID';

  constructor(commissionId: string) {
    super(`Commission ${commissionId} has already been paid`, HttpStatus.BAD_REQUEST, { commissionId });
  }
}

export class CommissionPeriodClosedException extends BaseException {
  readonly errorCode = 'COMMISSION_PERIOD_CLOSED';

  constructor(period: string) {
    super(`Commission period ${period} is closed and cannot be modified`, HttpStatus.BAD_REQUEST, { period });
  }
}

export class InvalidCommissionTierException extends BaseException {
  readonly errorCode = 'INVALID_COMMISSION_TIER';

  constructor(message: string) {
    super(`Invalid commission tier: ${message}`, HttpStatus.BAD_REQUEST);
  }
}

export class CommissionClawbackException extends BaseException {
  readonly errorCode = 'COMMISSION_CLAWBACK_ERROR';

  constructor(message: string, context?: Record<string, unknown>) {
    super(`Commission clawback error: ${message}`, HttpStatus.UNPROCESSABLE_ENTITY, context);
  }
}
