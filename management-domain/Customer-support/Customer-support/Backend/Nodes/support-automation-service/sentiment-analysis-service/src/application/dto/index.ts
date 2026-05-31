import { IsString, IsOptional, IsBoolean, IsArray } from 'class-validator';

export class AnalyzeTextDto {
  @IsString() text!: string;
  @IsOptional() @IsBoolean() includeEmotions?: boolean;
  @IsOptional() @IsString() language?: string;
}

export class BatchAnalyzeDto {
  @IsArray() @IsString({ each: true }) texts: string[] = [];
  @IsOptional() @IsBoolean() includeEmotions?: boolean;
  @IsOptional() @IsString() language?: string;
}
