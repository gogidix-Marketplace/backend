import { ApiProperty } from '@nestjs/swagger';

export class PayrollJobResponseDto {
  @ApiProperty() jobId!: string;
  @ApiProperty() payrollId!: string;
  @ApiProperty() status!: string;
  @ApiProperty() message!: string;
}
