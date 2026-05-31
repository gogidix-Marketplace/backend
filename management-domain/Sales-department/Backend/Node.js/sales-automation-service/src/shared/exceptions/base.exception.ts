export abstract class BaseException extends Error {
  abstract readonly statusCode: number;
  abstract readonly details: any;

  constructor(message: string) {
    super(message);
    Object.setPrototypeOf(this, BaseException.prototype);
    Error.captureStackTrace(this, this.constructor);
  }
}
