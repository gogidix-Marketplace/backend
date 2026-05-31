import { IsString } from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';

export class CancelPayrollJobDto {
  @ApiProperty() @IsString() jobId!: string;
}
