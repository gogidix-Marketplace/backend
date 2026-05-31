import { IsString, IsOptional, IsObject } from 'class-validator';

export class ExecuteWorkflowDto {
  @IsObject()
  triggerData: Record<string, any>;

  @IsOptional()
  @IsString()
  entityId?: string;

  @IsOptional()
  @IsString()
  entityType?: string;

  @IsOptional()
  @IsString()
  userId?: string;

  @IsOptional()
  @IsString()
  correlationId?: string;
}
