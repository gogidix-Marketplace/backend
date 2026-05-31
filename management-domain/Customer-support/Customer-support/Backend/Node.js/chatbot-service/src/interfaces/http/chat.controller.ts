import { Controller, Post, Get, Body, Param, Query, UseGuards, HttpCode, HttpStatus } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiBearerAuth, ApiParam } from '@nestjs/swagger';
import { ChatApplicationService } from '@application/services/chat.service';
import { HandoffApplicationService } from '@application/services/handoff.service';
import { AnalyticsApplicationService } from '@application/services/analytics.service';
import { TranslationApplicationService } from '@application/services/translation.service';
import { CreateSessionRequestDto } from '@application/dto/requests/create-session.request.dto';
import { SendMessageRequestDto } from '@application/dto/requests/send-message.request.dto';
import { HandoffRequestDto } from '@application/dto/requests/handoff.request.dto';
import { FeedbackRequestDto } from '@application/dto/requests/feedback.request.dto';

@ApiTags('Chat')
@Controller('api/v1/chat')
export class ChatController {
  constructor(
    private readonly chatService: ChatApplicationService,
    private readonly handoffService: HandoffApplicationService,
    private readonly analyticsService: AnalyticsApplicationService,
    private readonly translationService: TranslationApplicationService,
  ) {}

  @Post('sessions')
  @ApiOperation({ summary: 'Create a new chat session' })
  async createSession(@Body() dto: CreateSessionRequestDto) {
    const session = await this.chatService.createSession(dto.customerId, dto.language, dto.metadata);
    return {
      success: true,
      data: {
        sessionId: session.sessionId,
        status: session.status,
        language: session.language,
        startedAt: session.startedAt,
      },
    };
  }

  @Post('send')
  @ApiOperation({ summary: 'Send a message' })
  async sendMessage(@Body() dto: SendMessageRequestDto) {
    const response = await this.chatService.sendMessage(
      dto.sessionId,
      dto.message,
      dto.customerId,
      dto.language,
      dto.metadata,
    );
    return { success: true, data: response };
  }

  @Get('sessions/:sessionId')
  @ApiParam({ name: 'sessionId' })
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get session details' })
  async getSession(@Param('sessionId') sessionId: string) {
    const session = await this.chatService.getSession(sessionId);
    if (!session) return { success: false, error: 'Session not found' };
    return {
      success: true,
      data: {
        sessionId: session.sessionId,
        status: session.status,
        language: session.language,
        startedAt: session.startedAt,
        lastActivityAt: session.lastActivityAt,
        messageCount: session.messages.length,
        turnCount: session.context.turnCount,
        tags: session.tags,
        sentiment: session.sentiment,
        assignedAgentId: session.assignedAgentId,
      },
    };
  }

  @Get('sessions/:sessionId/history')
  @ApiParam({ name: 'sessionId' })
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get session history' })
  async getSessionHistory(@Param('sessionId') sessionId: string, @Query('limit') limit?: number) {
    const history = await this.chatService.getSessionHistory(sessionId);
    const limited = limit ? history.slice(-(limit)) : history.slice(-50);
    return { success: true, data: limited };
  }

  @Post('sessions/:sessionId/close')
  @ApiParam({ name: 'sessionId' })
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Close a session' })
  async closeSession(@Param('sessionId') sessionId: string) {
    await this.chatService.closeSession(sessionId);
    await this.analyticsService.createSessionAnalytics(sessionId);
    return { success: true, message: 'Session closed successfully' };
  }

  @Post('sessions/:sessionId/handoff')
  @ApiParam({ name: 'sessionId' })
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Request handoff to human agent' })
  async requestHandoff(@Param('sessionId') sessionId: string, @Body() dto: HandoffRequestDto) {
    await this.handoffService.requestHandoff(sessionId, {
      reason: dto.reason,
      priority: dto.priority,
      requiredSkills: dto.requiredSkills,
    });
    return { success: true, message: 'Handoff requested successfully' };
  }

  @Get('sessions/:sessionId/handoff/status')
  @ApiParam({ name: 'sessionId' })
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get handoff status' })
  async getHandoffStatus(@Param('sessionId') sessionId: string) {
    const status = await this.handoffService.getHandoffStatus(sessionId);
    return { success: true, data: status };
  }

  @Post('sessions/:sessionId/handoff/cancel')
  @ApiParam({ name: 'sessionId' })
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Cancel handoff' })
  async cancelHandoff(@Param('sessionId') sessionId: string) {
    await this.handoffService.cancelHandoff(sessionId);
    return { success: true, message: 'Handoff cancelled successfully' };
  }

  @Post('feedback')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Submit feedback' })
  async submitFeedback(@Body() dto: FeedbackRequestDto) {
    await this.chatService.submitFeedback(dto.sessionId, dto.rating, dto.comment, dto.resolved);
    return { success: true, message: 'Feedback submitted successfully' };
  }

  @Get('customers/:customerId/sessions')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get customer sessions' })
  async getCustomerSessions(
    @Param('customerId') customerId: string,
    @Query('status') status?: string,
    @Query('limit') limit?: number,
  ) {
    const sessions = await this.chatService.getCustomerSessions(customerId, status, limit ? parseInt(String(limit)) : 20);
    return {
      success: true,
      data: sessions.map(s => ({
        sessionId: s.sessionId,
        status: s.status,
        language: s.language,
        startedAt: s.startedAt,
        lastActivityAt: s.lastActivityAt,
        messageCount: s.messages.length,
        tags: s.tags,
      })),
    };
  }

  @Post('translate')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Translate text' })
  async translateText(@Body() body: { text: string; targetLanguage: string; sourceLanguage?: string }) {
    const result = await this.translationService.translate({
      text: body.text,
      targetLanguage: body.targetLanguage,
      sourceLanguage: body.sourceLanguage,
    });
    return { success: true, data: result };
  }

  @Get('languages')
  @ApiOperation({ summary: 'Get supported languages' })
  async getSupportedLanguages() {
    return { success: true, data: this.translationService.getSupportedLanguages() };
  }
}
