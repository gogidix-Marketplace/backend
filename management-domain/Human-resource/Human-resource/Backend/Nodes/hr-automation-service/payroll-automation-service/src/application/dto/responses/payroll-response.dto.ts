import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class PayrollResponseDto {
  @ApiProperty() id!: string;
  @ApiProperty() tenantId!: string;
  @ApiProperty() periodStart!: Date;
  @ApiProperty() periodEnd!: Date;
  @ApiProperty() status!: string;
  @ApiProperty() totalEmployees!: number;
  @ApiProperty() processedEmployees!: number;
  @ApiPropertyOptional() totalGrossPay!: number;
  @ApiPropertyOptional() totalNetPay!: number;
  @ApiProperty() currency!: string;
  @ApiProperty() createdAt!: Date;
}
