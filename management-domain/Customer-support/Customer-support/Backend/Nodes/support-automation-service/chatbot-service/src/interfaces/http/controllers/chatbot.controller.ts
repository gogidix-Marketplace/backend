import { Controller, Post, Get, Delete, Body, Param, UseInterceptors } from '@nestjs/common';
import { ChatbotInputPort } from '@domain/ports/input';
import { StartConversationDto, SendMessageDto } from '@application/dto';
import { TransformInterceptor } from '@shared/interceptors';

@Controller('api/chatbot')
@UseInterceptors(TransformInterceptor)
export class ChatbotController {
  constructor(private readonly chatbotService: ChatbotInputPort) {}

  @Post('conversations')
  async startConversation(@Body() dto: StartConversationDto) {
    return this.chatbotService.startConversation(dto.customerId, dto.language);
  }

  @Post('conversations/:sessionId/message')
  async sendMessage(@Param('sessionId') sessionId: string, @Body() dto: SendMessageDto) {
    return this.chatbotService.processMessage(sessionId, dto.message, dto.customerId, dto.language);
  }

  @Get('conversations/:sessionId/history')
  async getHistory(@Param('sessionId') sessionId: string) {
    return this.chatbotService.getConversationHistory(sessionId);
  }

  @Delete('conversations/:sessionId')
  async endConversation(@Param('sessionId') sessionId: string) {
    return this.chatbotService.endConversation(sessionId);
  }

  @Get('stats')
  async getStats() { return this.chatbotService.getStats(); }
}
