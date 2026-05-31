import { IsString, IsEnum, IsArray, IsBoolean, IsNumber, IsOptional, IsEmail } from 'class-validator';
import { AgentStatus, AgentRole } from '@domain/enums';

export class CreateAgentDto {
  @IsString()
  userId: string;

  @IsString()
  name: string;

  @IsEmail()
  email: string;

  @IsEnum(AgentRole)
  role: AgentRole;

  @IsArray()
  skills: Array<{ name: string; level: number; verified: boolean }> = [];

  @IsNumber()
  capacity: number = 5;

  @IsArray()
  @IsString({ each: true })
  teams: string[] = [];
}

export class UpdateAgentStatusDto {
  @IsEnum(AgentStatus)
  status: AgentStatus;
}

export class UpdateAgentDto {
  @IsOptional()
  @IsString()
  name?: string;

  @IsOptional()
  @IsEmail()
  email?: string;

  @IsOptional()
  @IsEnum(AgentStatus)
  status?: AgentStatus;

  @IsOptional()
  @IsArray()
  skills?: Array<{ name: string; level: number; verified: boolean }>;

  @IsOptional()
  @IsNumber()
  capacity?: number;

  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  teams?: string[];
}
