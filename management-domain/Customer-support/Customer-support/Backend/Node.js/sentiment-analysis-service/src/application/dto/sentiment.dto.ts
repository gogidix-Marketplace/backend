import { IsString, IsOptional, IsBoolean } from 'class-validator';

export class AnalyzeTextDto {
  @IsString()
  text: string;

  @IsOptional()
  @IsString()
  ticketId?: string;

  @IsOptional()
  @IsString()
  language?: string;

  @IsOptional()
  @IsBoolean()
  includeEmotions?: boolean;
}

export class BatchAnalyzeDto {
  @IsString({ each: true })
  texts: string[];

  @IsOptional()
  @IsString()
  language?: string;
}
