import { Controller, Post, Get, Put, Delete, Patch, Body, Param, Query } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiBearerAuth, ApiParam } from '@nestjs/swagger';
import { IntentApplicationService } from '@application/services/intent.service';
import { CreateIntentRequestDto } from '@application/dto/requests/create-intent.request.dto';
import { UpdateIntentRequestDto } from '@application/dto/requests/update-intent.request.dto';
import { DetectIntentRequestDto } from '@application/dto/requests/detect-intent.request.dto';

@ApiTags('Intents')
@Controller('api/v1/intents')
export class IntentController {
  constructor(private readonly intentService: IntentApplicationService) {}

  @Post('detect')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Detect intent from text' })
  async detectIntent(@Body() dto: DetectIntentRequestDto) {
    const detection = await this.intentService.detectIntent(dto.message, dto.language);
    return { success: true, data: detection };
  }

  @Get()
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get all intents' })
  async getIntents(
    @Query('language') language?: string,
    @Query('category') category?: string,
    @Query('isActive') isActive?: string,
  ) {
    const intents = await this.intentService.getIntents({
      language,
      category,
      isActive: isActive !== undefined ? isActive === 'true' : undefined,
    });
    return { success: true, data: intents, total: intents.length };
  }

  @Get('categories')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get intent categories' })
  async getCategories() {
    return { success: true, data: this.intentService.getIntentCategories() };
  }

  @Post()
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Create a new intent' })
  async createIntent(@Body() dto: CreateIntentRequestDto) {
    const intent = await this.intentService.createIntent(dto);
    return { success: true, data: intent };
  }

  @Post('batch-detect')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Batch detect intents' })
  async batchDetectIntents(@Body() body: { messages: string[]; language?: string }) {
    const results = await this.intentService.batchDetect(body.messages, body.language);
    return { success: true, data: results };
  }

  @Get(':intentId')
  @ApiParam({ name: 'intentId' })
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get intent by ID' })
  async getIntentById(@Param('intentId') intentId: string) {
    const intent = await this.intentService.getIntent(intentId);
    if (!intent) return { success: false, error: 'Intent not found' };
    return { success: true, data: intent };
  }

  @Put(':intentId')
  @ApiParam({ name: 'intentId' })
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Update an intent' })
  async updateIntent(@Param('intentId') intentId: string, @Body() dto: UpdateIntentRequestDto) {
    const intent = await this.intentService.updateIntent(intentId, dto);
    if (!intent) return { success: false, error: 'Intent not found' };
    return { success: true, data: intent };
  }

  @Delete(':intentId')
  @ApiParam({ name: 'intentId' })
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Delete an intent' })
  async deleteIntent(@Param('intentId') intentId: string) {
    await this.intentService.deleteIntent(intentId);
    return { success: true, message: 'Intent deleted successfully' };
  }

  @Post(':intentId/training-phrases')
  @ApiParam({ name: 'intentId' })
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Add training phrase to intent' })
  async addTrainingPhrase(@Param('intentId') intentId: string, @Body() body: { phrase: string }) {
    const intent = await this.intentService.addTrainingPhrase(intentId, body.phrase);
    if (!intent) return { success: false, error: 'Intent not found' };
    return { success: true, data: intent };
  }

  @Post(':intentId/responses')
  @ApiParam({ name: 'intentId' })
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Add response to intent' })
  async addResponse(@Param('intentId') intentId: string, @Body() body: { response: string }) {
    const intent = await this.intentService.addResponse(intentId, body.response);
    if (!intent) return { success: false, error: 'Intent not found' };
    return { success: true, data: intent };
  }

  @Patch(':intentId/toggle')
  @ApiParam({ name: 'intentId' })
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Toggle intent active status' })
  async toggleIntent(@Param('intentId') intentId: string) {
    const intent = await this.intentService.toggleIntent(intentId);
    if (!intent) return { success: false, error: 'Intent not found' };
    return { success: true, data: intent };
  }
}
