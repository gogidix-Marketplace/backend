import { HttpException, HttpStatus } from '@nestjs/common';

export class KpiNotFoundException extends HttpException {
  constructor(kpiId: string) {
    super(`KPI with ID ${kpiId} not found`, HttpStatus.NOT_FOUND);
  }
}

export class KpiAlreadyExistsException extends HttpException {
  constructor(name: string, period: string) {
    super(`KPI "${name}" already exists for period "${period}"`, HttpStatus.CONFLICT);
  }
}

export class DuplicateKpiException extends HttpException {
  constructor(name: string) {
    super(`Duplicate KPI: ${name}`, HttpStatus.CONFLICT);
  }
}
