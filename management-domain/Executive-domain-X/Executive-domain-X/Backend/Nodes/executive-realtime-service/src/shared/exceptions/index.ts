import { HttpException, HttpStatus } from '@nestjs/common';
export class ConnectionNotFoundException extends HttpException { constructor(id: string) { super(`Connection ${id} not found`, HttpStatus.NOT_FOUND); } }
