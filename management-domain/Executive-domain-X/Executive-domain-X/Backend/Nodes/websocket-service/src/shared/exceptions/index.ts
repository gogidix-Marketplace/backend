import { HttpException, HttpStatus } from '@nestjs/common';
export class DashboardNotFoundException extends HttpException { constructor(id: string) { super(`Dashboard ${id} not found`, HttpStatus.NOT_FOUND); } }
export class RoomFullException extends HttpException { constructor(id: string) { super(`Room ${id} is full`, HttpStatus.FORBIDDEN); } }
