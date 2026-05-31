import { HttpException, HttpStatus } from '@nestjs/common';
export class KpiNotFoundException extends HttpException { constructor(kpiId: string) { super(`KPI with ID ${kpiId} not found`, HttpStatus.NOT_FOUND); } }
