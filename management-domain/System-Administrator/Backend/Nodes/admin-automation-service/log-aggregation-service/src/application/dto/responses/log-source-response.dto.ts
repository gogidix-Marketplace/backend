import { ApiProperty } from '@nestjs/swagger';

export class LogSourceResponseDto {
  @ApiProperty() id!: string;
  @ApiProperty() name!: string;
  @ApiProperty() description!: string;
  @ApiProperty() enabled!: boolean;
  @ApiProperty() type!: string;
  @ApiProperty() sourceType!: string;
  @ApiProperty() status!: string;
  @ApiProperty() createdAt!: Date;
}
