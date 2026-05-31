import { HttpException, HttpStatus } from '@nestjs/common';
export class LogSourceNotFoundException extends HttpException {
  constructor(id: string) { super(`Log source with ID ${id} not found`, HttpStatus.NOT_FOUND); }
}
export class LogNotFoundException extends HttpException {
  constructor(id: string) { super(`Log with ID ${id} not found`, HttpStatus.NOT_FOUND); }
}
export class ElasticsearchException extends HttpException {
  constructor(message: string) { super(`Elasticsearch error: ${message}`, HttpStatus.SERVICE_UNAVAILABLE); }
}
