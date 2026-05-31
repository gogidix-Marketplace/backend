import {
  Controller,
  Get,
  Post,
  Put,
  Delete,
  Body,
  Param,
  Query,
  UseGuards,
  ParseIntPipe,
  DefaultValuePipe,
} from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse, ApiBearerAuth } from '@nestjs/swagger';
import { LeadScoringService } from '../../application/services/lead-scoring.service';
import {
  ScoreLeadRequestDto,
  BatchScoreLeadRequestDto,
} from '../../application/dto/request/score-lead.request.dto';
import {
  LeadScoreResponseDto,
  BatchScoreResponseDto,
  ScoreStatisticsDto,
} from '../../application/dto/response/lead-score.response.dto';
import { RequestContext } from '../../infrastructure/security/request-context';
import { TenantGuard } from '../../infrastructure/security/guards/tenant.guard';

@ApiTags('Lead Scoring')
@ApiBearerAuth()
@Controller('lead-scores')
@UseGuards(TenantGuard)
export class LeadScoreController {
  constructor(private readonly leadScoringService: LeadScoringService) {}

  @Post('score')
  @ApiOperation({ summary: 'Score a single lead' })
  @ApiResponse({ status: 200, type: LeadScoreResponseDto })
  async scoreLead(
    @Body() request: ScoreLeadRequestDto,
    @RequestContext() context: any,
  ): Promise<LeadScoreResponseDto> {
    // Ensure tenant ID from context is used
    request.tenantId = context.tenantId;
    return this.leadScoringService.scoreLead(request);
  }

  @Post('batch-score')
  @ApiOperation({ summary: 'Score multiple leads in batch' })
  @ApiResponse({ status: 200, type: BatchScoreResponseDto })
  async batchScoreLeads(
    @Body() request: BatchScoreLeadRequestDto,
    @RequestContext() context: any,
  ): Promise<BatchScoreResponseDto> {
    request.tenantId = context.tenantId;
    return this.leadScoringService.batchScoreLeads(request);
  }

  @Get('lead/:leadId')
  @ApiOperation({ summary: 'Get score for a specific lead' })
  @ApiResponse({ status: 200, type: LeadScoreResponseDto })
  async getLeadScore(
    @Param('leadId') leadId: string,
    @RequestContext() context: any,
  ): Promise<LeadScoreResponseDto> {
    return this.leadScoringService.getLeadScore(leadId, context.tenantId);
  }

  @Get('tenant')
  @ApiOperation({ summary: 'Get all scores for a tenant' })
  @ApiResponse({ status: 200, type: [LeadScoreResponseDto] })
  async getLeadScoresByTenant(
    @RequestContext() context: any,
    @Query('page', new DefaultValuePipe(1), ParseIntPipe) page: number,
    @Query('limit', new DefaultValuePipe(20), ParseIntPipe) limit: number,
    @Query('sortBy') sortBy?: string,
    @Query('sortOrder') sortOrder?: 'asc' | 'desc',
  ): Promise<LeadScoreResponseDto[]> {
    return this.leadScoringService.getLeadScoresByTenant(context.tenantId, {
      page,
      limit,
      sortBy,
      sortOrder,
    });
  }

  @Get('qualified')
  @ApiOperation({ summary: 'Get qualified leads' })
  @ApiResponse({ status: 200, type: [LeadScoreResponseDto] })
  async getQualifiedLeads(
    @RequestContext() context: any,
    @Query('minScore', new DefaultValuePipe(50), ParseIntPipe) minScore: number,
    @Query('page', new DefaultValuePipe(1), ParseIntPipe) page: number,
    @Query('limit', new DefaultValuePipe(20), ParseIntPipe) limit: number,
  ): Promise<LeadScoreResponseDto[]> {
    return this.leadScoringService.getQualifiedLeads(context.tenantId, minScore, {
      page,
      limit,
    });
  }

  @Get('statistics')
  @ApiOperation({ summary: 'Get score statistics for a tenant' })
  @ApiResponse({ status: 200, type: ScoreStatisticsDto })
  async getScoreStatistics(
    @RequestContext() context: any,
  ): Promise<ScoreStatisticsDto> {
    return this.leadScoringService.getScoreStatistics(context.tenantId);
  }

  @Post('apply-decay')
  @ApiOperation({ summary: 'Apply score decay for all leads' })
  @ApiResponse({ status: 200 })
  async applyScoreDecay(
    @RequestContext() context: any,
  ): Promise<void> {
    return this.leadScoringService.applyScoreDecay(context.tenantId);
  }
}

