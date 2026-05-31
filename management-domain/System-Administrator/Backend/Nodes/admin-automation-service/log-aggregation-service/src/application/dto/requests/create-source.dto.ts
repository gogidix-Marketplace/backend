import { IsString, IsBoolean, IsOptional, IsObject, ValidateNested } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';
import { Type } from 'class-transformer';

export class ParsingConfigDto {
  @ApiProperty() @IsBoolean() enabled: boolean = true;
  @ApiProperty() @IsString() format: string = 'json';
  @ApiPropertyOptional() @IsOptional() @IsString() timestampFormat?: string;
}

export class RetentionConfigDto {
  @ApiProperty() @IsBoolean() enabled: boolean = true;
  @ApiProperty() days: number = 30;
  @ApiProperty() @IsBoolean() archive: boolean = false;
}

export class CreateSourceDto {
  @ApiProperty() @IsString() name!: string;
  @ApiProperty() @IsString() description!: string;
  @ApiPropertyOptional() @IsOptional() @IsBoolean() enabled?: boolean = true;
  @ApiProperty() @IsString() type!: string;
  @ApiProperty() @IsString() sourceType!: string;
  @ApiProperty() @IsObject() config: Record<string, unknown>;
  @ApiPropertyOptional() @Type(() => ParsingConfigDto) @ValidateNested() parsing?: ParsingConfigDto;
  @ApiPropertyOptional() @Type(() => RetentionConfigDto) @ValidateNested() retention?: RetentionConfigDto;
}
