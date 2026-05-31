import { Controller, Get, Post, Delete, Param, Body, Query } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { LogSearchQueryService } from '@application/services/log-search-query.service';
import { LogIngestionService } from '@application/services/log-ingestion.service';
import { SearchLogsDto } from '@application/dto/requests/search-logs.dto';

@ApiTags('logs')
@Controller('logs')
export class LogController {
  constructor(
    private readonly searchService: LogSearchQueryService,
    private readonly ingestionService: LogIngestionService,
  ) {}

  @Get('search')
  @ApiOperation({ summary: 'Search logs' })
  async searchLogs(@Query() dto: SearchLogsDto) {
    return this.searchService.searchLogs(dto);
  }

  @Get('stats')
  @ApiOperation({ summary: 'Get log statistics' })
  async getLogStats(@Query('hours') hours = 24) {
    return this.searchService.getLogStats(Number(hours));
  }

  @Get('levels')
  @ApiOperation({ summary: 'Get available log levels' })
  async getLogLevels() {
    return this.searchService.getLogLevels();
  }

  @Get('timeseries')
  @ApiOperation({ summary: 'Get log time series' })
  async getTimeSeries(@Query('hours') hours = 24, @Query('interval') interval = '1h') {
    return this.searchService.getTimeSeries(Number(hours), interval);
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get log by ID' })
  async getLogById(@Param('id') id: string, @Query('index') index?: string) {
    return this.searchService.getLogById(id, index);
  }

  @Post('ingest')
  @ApiOperation({ summary: 'Ingest logs via HTTP' })
  async ingestLogs(@Body() body: any | any[]) {
    const logs = Array.isArray(body) ? body : [body];
    const result = await this.ingestionService.ingestBulkLogs(logs, 'http-ingest');
    return { success: true, message: 'Logs ingested', data: result };
  }
}
