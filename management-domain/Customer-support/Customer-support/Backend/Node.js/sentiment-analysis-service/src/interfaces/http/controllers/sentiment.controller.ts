import { Controller, Post, Get, Delete, Body, UseInterceptors } from '@nestjs/common';
import { SentimentAnalysisInputPort } from '@domain/ports/input';
import { AnalyzeTextDto, BatchAnalyzeDto } from '@application/dto';
import { TransformInterceptor } from '@shared/interceptors';

@Controller('api/v1/sentiment')
@UseInterceptors(TransformInterceptor)
export class SentimentController {
  constructor(private readonly sentimentService: SentimentAnalysisInputPort) {}

  @Post('analyze')
  async analyze(@Body() dto: AnalyzeTextDto) {
    return this.sentimentService.analyzeText({
      text: dto.text,
      ticketId: dto.ticketId,
      language: dto.language,
    });
  }

  @Post('batch')
  async batchAnalyze(@Body() dto: BatchAnalyzeDto) {
    return this.sentimentService.batchAnalyze(dto.texts, dto.language);
  }

  @Get('alerts')
  async getAlerts() {
    return this.sentimentService.getAlerts();
  }

  @Get('alerts/stats')
  async getAlertStats() {
    return this.sentimentService.getAlertStats();
  }
}

@Controller('api/v1/health')
export class HealthController {
  @Get()
  check() {
    return { status: 'ok', service: 'sentiment-analysis-service', timestamp: new Date().toISOString() };
  }
}
