import { IsString, IsOptional } from 'class-validator';

export class ReassignTicketDto {
  @IsString()
  currentAgentId: string;

  @IsString()
  reason: string;

  @IsOptional()
  @IsString()
  force?: boolean;
}
