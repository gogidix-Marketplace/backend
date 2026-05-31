import { HttpException, HttpStatus } from '@nestjs/common';
export class PolicyNotFoundException extends HttpException {
  constructor(id: string) { super(`Scaling policy with ID ${id} not found`, HttpStatus.NOT_FOUND); }
}
export class InvalidPolicyException extends HttpException {
  constructor(message: string) { super(message, HttpStatus.BAD_REQUEST); }
}
export class CloudProviderException extends HttpException {
  constructor(message: string) { super(`Cloud provider error: ${message}`, HttpStatus.SERVICE_UNAVAILABLE); }
}
