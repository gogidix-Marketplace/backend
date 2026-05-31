import { Controller, Get, Param, Query } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { ScalingPolicyQueryService } from '@application/services/scaling-policy-query.service';

@ApiTags('events')
@Controller('events')
export class ScalingEventController {
  constructor(private readonly queryService: ScalingPolicyQueryService) {}

  @Get()
  @ApiOperation({ summary: 'List scaling events' })
  async getEvents(@Query('policyId') policyId?: string, @Query('limit') limit = 50) {
    return this.queryService.getEvents(policyId, Number(limit));
  }
}
