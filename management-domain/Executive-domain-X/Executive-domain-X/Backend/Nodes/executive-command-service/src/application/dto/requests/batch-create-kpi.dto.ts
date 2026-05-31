import { IsArray, ValidateNested } from 'class-validator';
import { Type } from 'class-transformer';
import { CreateKpiDto } from './create-kpi.dto';

export class BatchCreateKpiDto {
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => CreateKpiDto)
  kpis: CreateKpiDto[];
}
