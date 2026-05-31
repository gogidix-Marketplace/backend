import { ApiProperty } from '@nestjs/swagger';

export class ScalingPolicyResponseDto {
  @ApiProperty() id!: string;
  @ApiProperty() name!: string;
  @ApiProperty() description!: string;
  @ApiProperty() resourceId!: string;
  @ApiProperty() cloudProvider!: string;
  @ApiProperty() enabled!: boolean;
  @ApiProperty() minInstances!: number;
  @ApiProperty() maxInstances!: number;
  @ApiProperty() currentInstances!: number;
  @ApiProperty() targetInstances!: number;
  @ApiProperty() createdAt!: Date;
}
