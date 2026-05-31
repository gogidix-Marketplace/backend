import { IsString, IsOptional } from 'class-validator';

export class StartConversationDto {
  @IsOptional() @IsString() customerId?: string;
  @IsOptional() @IsString() language?: string;
}

export class SendMessageDto {
  @IsString() message!: string;
  @IsOptional() @IsString() customerId?: string;
  @IsOptional() @IsString() language?: string;
}

export class SearchKnowledgeDto {
  @IsString() query!: string;
  @IsOptional() @IsString() language?: string;
}
