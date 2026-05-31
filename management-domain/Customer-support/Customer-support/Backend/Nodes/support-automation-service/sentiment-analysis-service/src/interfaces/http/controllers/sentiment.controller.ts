import { Controller, Post, Get, Delete, Body, UseInterceptors } from '@nestjs/common';
import { SentimentAnalysisInputPort } from '@domain/ports/input';
import { AnalyzeTextDto, BatchAnalyzeDto } from '@application/dto';
import { TransformInterceptor } from '@shared/interceptors';

@Controller('api/sentiment')
@UseInterceptors(TransformInterceptor)
export class SentimentController {
  constructor(private readonly sentimentService: SentimentAnalysisInputPort) {}

  @Post('analyze')
  async analyze(@Body() dto: AnalyzeTextDto) { return this.sentimentService.analyze(dto.text, dto.includeEmotions); }

  @Post('batch')
  async batch(@Body() dto: BatchAnalyzeDto) { return this.sentimentService.batchAnalyze(dto.texts, dto.includeEmotions); }

  @Post('sentiment-only')
  async sentimentOnly(@Body() body: { text: string }) { return this.sentimentService.analyzeSentiment(body.text); }

  @Post('emotions-only')
  async emotionsOnly(@Body() body: { text: string }) { return this.sentimentService.analyzeEmotions(body.text); }

  @Get('stats')
  async stats() { return this.sentimentService.getStats(); }

  @Delete('cache')
  async clearCache() { const count = await this.sentimentService.clearCache(); return { success: true, clearedCount: count }; }
}

@Controller('health')
export class HealthController {
  @Get() check() { return { success: true, service: 'sentiment-analysis-service', status: 'healthy', timestamp: new Date().toISOString() }; }
}
