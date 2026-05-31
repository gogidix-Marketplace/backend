import { IsString, IsOptional, IsArray, IsObject, IsNumber, IsBoolean, ValidateNested } from 'class-validator';
import { Type } from 'class-transformer';

export class TriggerConfigurationDto {
  @IsString()
  type: string;

  @IsOptional()
  @IsArray()
  conditions?: Array<{
    field: string;
    operator: string;
    value: any;
  }>;

  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  entityTypes?: string[];

  @IsOptional()
  @IsString()
  cronExpression?: string;

  @IsOptional()
  @IsObject()
  filters?: Record<string, any>;
}

export class WorkflowTriggerDto {
  @IsString()
  name: string;

  @IsObject()
  @ValidateNested()
  @Type(() => TriggerConfigurationDto)
  configuration: TriggerConfigurationDto;
}

export class WorkflowActionDto {
  @IsString()
  name: string;

  @IsString()
  type: string;

  @IsObject()
  parameters: Record<string, any>;

  @IsNumber()
  order: number;

  @IsOptional()
  @IsBoolean()
  continueOnError?: boolean;

  @IsOptional()
  @IsNumber()
  delayMs?: number;
}

export class CreateWorkflowDto {
  @IsString()
  name: string;

  @IsOptional()
  @IsString()
  description?: string;

  @IsObject()
  @ValidateNested()
  @Type(() => WorkflowTriggerDto)
  trigger: WorkflowTriggerDto;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => WorkflowActionDto)
  actions: WorkflowActionDto[];

  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  tags?: string[];

  @IsOptional()
  @IsNumber()
  priority?: number;

  @IsOptional()
  @IsNumber()
  maxConcurrentExecutions?: number;

  @IsOptional()
  @IsNumber()
  timeoutMs?: number;
}
