import { Controller, Get, Param, Query } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { ScalingPolicyQueryService } from '@application/services/scaling-policy-query.service';

@ApiTags('metrics')
@Controller('metrics')
export class MetricsController {
  constructor(private readonly queryService: ScalingPolicyQueryService) {}

  @Get('history/:resourceId')
  @ApiOperation({ summary: 'Get metrics history for a resource' })
  async getMetricsHistory(@Param('resourceId') resourceId: string, @Query('hours') hours = 24) {
    return this.queryService.getMetricsHistory(resourceId, Number(hours));
  }

  @Get('current/:resourceId')
  @ApiOperation({ summary: 'Get current metrics for a resource' })
  async getCurrentMetrics(@Param('resourceId') resourceId: string) {
    return this.queryService.getCurrentMetrics(resourceId);
  }
}
