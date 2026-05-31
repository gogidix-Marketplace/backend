import { IsString, IsOptional, IsArray, IsNumber, IsEnum, IsBoolean } from 'class-validator';
import { AgentStatus, RoutingStrategy } from '@domain/enums';

export class CreateAgentDto {
  @IsString() name!: string;
  @IsString() email!: string;
  @IsOptional() @IsEnum(AgentStatus) status?: AgentStatus;
  @IsOptional() @IsArray() skills?: Array<{ name: string; level: number }>;
  @IsOptional() @IsNumber() maxConcurrentTickets?: number;
  @IsArray() @IsString({ each: true }) departments: string[];
}

export class UpdateAgentStatusDto {
  @IsEnum(AgentStatus) status: AgentStatus;
}

export class RouteTicketDto {
  @IsString() ticketId!: string;
  @IsOptional() @IsEnum(RoutingStrategy) strategy?: RoutingStrategy;
}

export class ReassignTicketDto {
  @IsString() ticketId!: string;
  @IsString() currentAgentId!: string;
  @IsString() reason!: string;
  @IsOptional() @IsBoolean() force?: boolean;
}

export class CreateTicketDto {
  @IsOptional() @IsString() id?: string;
  @IsString() customerId!: string;
  @IsString() subject!: string;
  @IsString() description!: string;
  @IsOptional() @IsString() priority?: string;
  @IsString() department!: string;
  @IsOptional() @IsArray() @IsString({ each: true }) requiredSkills?: string[];
}
