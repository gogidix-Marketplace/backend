import { IsString, IsOptional, IsEnum, IsArray, IsObject } from 'class-validator';
import { TicketPriority } from '@domain/enums';

export class RouteTicketDto {
  @IsString()
  ticketId: string;

  @IsEnum(TicketPriority)
  priority: TicketPriority;

  @IsArray()
  @IsString({ each: true })
  requiredSkills: string[] = [];

  @IsOptional()
  @IsString()
  customerId?: string;

  @IsOptional()
  @IsString()
  language?: string;

  @IsOptional()
  @IsString()
  teamId?: string;

  @IsOptional()
  @IsString()
  queueId?: string;

  @IsOptional()
  @IsObject()
  metadata?: Record<string, unknown>;
}
