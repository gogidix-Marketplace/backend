import { Controller, Get, Post, Param, Query } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiBearerAuth } from '@nestjs/swagger';
import { AnalyticsApplicationService } from '@application/services/analytics.service';
import { AnalyticsQueryRequestDto } from '@application/dto/requests/analytics-query.request.dto';

@ApiTags('Analytics')
@Controller('api/v1/analytics')
export class AnalyticsController {
  constructor(private readonly analyticsService: AnalyticsApplicationService) {}

  @Get('dashboard')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get dashboard summary' })
  async getDashboardSummary() {
    const data = await this.analyticsService.getDashboardSummary();
    return { success: true, data };
  }

  @Get('realtime')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get real-time statistics' })
  async getRealtimeStats() {
    const data = await this.analyticsService.getRealtimeStats();
    return { success: true, data };
  }

  @Get('sessions/:sessionId')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get session analytics' })
  async getSessionAnalytics(@Param('sessionId') sessionId: string) {
    const analytics = await this.analyticsService.getSessionAnalytics(sessionId);
    if (!analytics) return { success: false, error: 'Session analytics not found' };
    return { success: true, data: analytics };
  }

  @Get('customers/:customerId')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get customer analytics' })
  async getCustomerAnalytics(
    @Param('customerId') customerId: string,
    @Query() query: AnalyticsQueryRequestDto,
  ) {
    const data = await this.analyticsService.getCustomerAnalytics(customerId, query.page, query.limit);
    return { success: true, data };
  }

  @Get('aggregated')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get aggregated analytics' })
  async getAggregatedAnalytics(@Query() query: AnalyticsQueryRequestDto) {
    if (!query.startDate || !query.endDate) return { success: false, error: 'Start date and end date are required' };
    const data = await this.analyticsService.getAggregatedAnalytics(new Date(query.startDate), new Date(query.endDate));
    return { success: true, data };
  }

  @Post('daily')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Generate daily analytics' })
  async generateDailyAnalytics(@Query('date') date?: string) {
    const targetDate = date ? new Date(date) : new Date();
    const data = await this.analyticsService.generateDailyAnalytics(targetDate);
    return { success: true, data };
  }

  @Get('daily')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get analytics by date range' })
  async getAnalyticsByDateRange(@Query() query: AnalyticsQueryRequestDto) {
    if (!query.startDate || !query.endDate) return { success: false, error: 'Start date and end date are required' };
    const data = await this.analyticsService.getAnalyticsByDateRange(new Date(query.startDate), new Date(query.endDate));
    return { success: true, data };
  }

  @Get('intents')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get intent statistics' })
  async getIntentStats(@Query() query: AnalyticsQueryRequestDto) {
    if (!query.startDate || !query.endDate) return { success: false, error: 'Start date and end date are required' };
    const data = await this.analyticsService.getIntentStats(new Date(query.startDate), new Date(query.endDate), query.limit);
    return { success: true, data };
  }

  @Get('satisfaction')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get satisfaction metrics' })
  async getSatisfactionMetrics(@Query() query: AnalyticsQueryRequestDto) {
    if (!query.startDate || !query.endDate) return { success: false, error: 'Start date and end date are required' };
    const data = await this.analyticsService.getSatisfactionMetrics(new Date(query.startDate), new Date(query.endDate));
    return { success: true, data };
  }

  @Get('handoffs')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Get handoff statistics' })
  async getHandoffStats(@Query() query: AnalyticsQueryRequestDto) {
    if (!query.startDate || !query.endDate) return { success: false, error: 'Start date and end date are required' };
    const data = await this.analyticsService.getHandoffStats(new Date(query.startDate), new Date(query.endDate));
    return { success: true, data };
  }

  @Get('export')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Export analytics' })
  async exportAnalytics(
    @Query('startDate') startDate: string,
    @Query('endDate') endDate: string,
    @Query('format') format: 'json' | 'csv' = 'json',
  ) {
    const data = await this.analyticsService.exportAnalytics(new Date(startDate), new Date(endDate), format);
    return data;
  }
}
