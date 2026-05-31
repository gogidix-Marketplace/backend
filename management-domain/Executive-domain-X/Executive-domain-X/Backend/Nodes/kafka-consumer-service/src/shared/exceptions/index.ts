import { HttpException, HttpStatus } from '@nestjs/common';
export class EventNotFoundException extends HttpException { constructor(id: string) { super(`Event ${id} not found`, HttpStatus.NOT_FOUND); } }
