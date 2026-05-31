import { HttpException, HttpStatus } from '@nestjs/common';

export class ReconciliationNotFoundException extends HttpException {
  constructor(reconciliationId: string) {
    super(`Reconciliation with ID ${reconciliationId} not found`, HttpStatus.NOT_FOUND);
  }
}

export class ReconciliationAlreadyRunningException extends HttpException {
  constructor(reconciliationId: string) {
    super(`Reconciliation ${reconciliationId} is already running`, HttpStatus.CONFLICT);
  }
}

export class RuleNotFoundException extends HttpException {
  constructor(ruleId: string) {
    super(`Reconciliation rule with ID ${ruleId} not found`, HttpStatus.NOT_FOUND);
  }
}

export class InvalidRuleException extends HttpException {
  constructor(message: string) {
    super(`Invalid reconciliation rule: ${message}`, HttpStatus.BAD_REQUEST);
  }
}

export class MatchNotFoundException extends HttpException {
  constructor(matchId: string) {
    super(`Match with ID ${matchId} not found`, HttpStatus.NOT_FOUND);
  }
}

export class DifferenceNotFoundException extends HttpException {
  constructor(differenceId: string) {
    super(`Difference with ID ${differenceId} not found`, HttpStatus.NOT_FOUND);
  }
}

export class DifferenceAlreadyResolvedException extends HttpException {
  constructor(differenceId: string) {
    super(`Difference ${differenceId} has already been resolved`, HttpStatus.CONFLICT);
  }
}

export class DataSourceException extends HttpException {
  constructor(message: string, details?: string) {
    super(
      {
        statusCode: HttpStatus.SERVICE_UNAVAILABLE,
        message: `Data source error: ${message}`,
        details,
      },
      HttpStatus.SERVICE_UNAVAILABLE,
    );
  }
}

export class AutoResolutionFailedException extends HttpException {
  constructor(differenceId: string, reason: string) {
    super(
      {
        statusCode: HttpStatus.UNPROCESSABLE_ENTITY,
        message: `Auto-resolution failed for difference ${differenceId}: ${reason}`,
      },
      HttpStatus.UNPROCESSABLE_ENTITY,
    );
  }
}
