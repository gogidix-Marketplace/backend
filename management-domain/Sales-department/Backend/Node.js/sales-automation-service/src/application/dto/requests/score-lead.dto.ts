import { IsString, IsNumber, IsOptional } from 'class-validator';

export class ScoreLeadDto {
  @IsString()
  leadId: string;

  @IsOptional()
  @IsString()
  leadEmail?: string;

  @IsNumber()
  score: number;

  @IsString()
  reason: string;

  @IsOptional()
  @IsString()
  ruleId?: string;

  @IsOptional()
  @IsString()
  category?: 'demographic' | 'behavioral' | 'engagement' | 'manual';
}
