import { HttpException, HttpStatus } from '@nestjs/common';

export class PayrollNotFoundException extends HttpException {
  constructor(id: string) { super(`Payroll with ID ${id} not found`, HttpStatus.NOT_FOUND); }
}
export class PayrollJobNotFoundException extends HttpException {
  constructor(id: string) { super(`Payroll job with ID ${id} not found`, HttpStatus.NOT_FOUND); }
}
export class EmployeePayrollNotFoundException extends HttpException {
  constructor(payrollId: string, employeeId: string) { super(`Employee payroll not found for payroll ${payrollId} and employee ${employeeId}`, HttpStatus.NOT_FOUND); }
}
export class InvalidPayrollRequestException extends HttpException {
  constructor(message: string) { super(message, HttpStatus.BAD_REQUEST); }
}
