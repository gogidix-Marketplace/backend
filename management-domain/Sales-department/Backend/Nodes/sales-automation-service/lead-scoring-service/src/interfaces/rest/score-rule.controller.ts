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
import { ScoreRuleService } from '../../application/services/score-rule.service';
import {
  CreateScoreRuleRequestDto,
  UpdateScoreRuleRequestDto,
  UpdateRuleScoreRequestDto,
} from '../../application/dto/request/score-rule.request.dto';
import {
  ScoreRuleResponseDto,
  ScoreRuleListItemDto,
} from '../../application/dto/response/score-rule.response.dto';
import { RequestContext } from '../../infrastructure/security/request-context';
import { TenantGuard } from '../../infrastructure/security/guards/tenant.guard';

@ApiTags('Score Rules')
@ApiBearerAuth()
@Controller('score-rules')
@UseGuards(TenantGuard)
export class ScoreRuleController {
  constructor(private readonly scoreRuleService: ScoreRuleService) {}

  @Post()
  @ApiOperation({ summary: 'Create a new scoring rule' })
  @ApiResponse({ status: 201, type: ScoreRuleResponseDto })
  async createRule(
    @Body() request: CreateScoreRuleRequestDto,
    @RequestContext() context: any,
  ): Promise<ScoreRuleResponseDto> {
    request.tenantId = context.tenantId;
    return this.scoreRuleService.createRule(request);
  }

  @Put(':id')
  @ApiOperation({ summary: 'Update a scoring rule' })
  @ApiResponse({ status: 200, type: ScoreRuleResponseDto })
  async updateRule(
    @Param('id') id: string,
    @Body() request: UpdateScoreRuleRequestDto,
  ): Promise<ScoreRuleResponseDto> {
    return this.scoreRuleService.updateRule(id, request);
  }

  @Put(':id/score')
  @ApiOperation({ summary: 'Update rule scores' })
  @ApiResponse({ status: 200, type: ScoreRuleResponseDto })
  async updateRuleScore(
    @Param('id') id: string,
    @Body() request: UpdateRuleScoreRequestDto,
  ): Promise<ScoreRuleResponseDto> {
    return this.scoreRuleService.updateRuleScore(id, request);
  }

  @Post(':id/activate')
  @ApiOperation({ summary: 'Activate a scoring rule' })
  @ApiResponse({ status: 200, type: ScoreRuleResponseDto })
  async activateRule(
    @Param('id') id: string,
  ): Promise<ScoreRuleResponseDto> {
    return this.scoreRuleService.activateRule(id);
  }

  @Post(':id/deactivate')
  @ApiOperation({ summary: 'Deactivate a scoring rule' })
  @ApiResponse({ status: 200, type: ScoreRuleResponseDto })
  async deactivateRule(
    @Param('id') id: string,
  ): Promise<ScoreRuleResponseDto> {
    return this.scoreRuleService.deactivateRule(id);
  }

  @Post(':id/conditions')
  @ApiOperation({ summary: 'Add a condition to the rule' })
  @ApiResponse({ status: 200, type: ScoreRuleResponseDto })
  async addCondition(
    @Param('id') id: string,
    @Body() condition: any,
  ): Promise<ScoreRuleResponseDto> {
    return this.scoreRuleService.addCondition(id, condition);
  }

  @Delete(':id/conditions/:conditionIndex')
  @ApiOperation({ summary: 'Remove a condition from the rule' })
  @ApiResponse({ status: 200, type: ScoreRuleResponseDto })
  async removeCondition(
    @Param('id') id: string,
    @Param('conditionIndex', ParseIntPipe) conditionIndex: number,
  ): Promise<ScoreRuleResponseDto> {
    return this.scoreRuleService.removeCondition(id, conditionIndex);
  }

  @Post(':id/tags')
  @ApiOperation({ summary: 'Add a tag to the rule' })
  @ApiResponse({ status: 200, type: ScoreRuleResponseDto })
  async addTag(
    @Param('id') id: string,
    @Body('tag') tag: string,
  ): Promise<ScoreRuleResponseDto> {
    return this.scoreRuleService.addTag(id, tag);
  }

  @Delete(':id/tags/:tag')
  @ApiOperation({ summary: 'Remove a tag from the rule' })
  @ApiResponse({ status: 200, type: ScoreRuleResponseDto })
  async removeTag(
    @Param('id') id: string,
    @Param('tag') tag: string,
  ): Promise<ScoreRuleResponseDto> {
    return this.scoreRuleService.removeTag(id, tag);
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get a specific scoring rule' })
  @ApiResponse({ status: 200, type: ScoreRuleResponseDto })
  async getRule(
    @Param('id') id: string,
  ): Promise<ScoreRuleResponseDto> {
    return this.scoreRuleService.getRule(id);
  }

  @Get()
  @ApiOperation({ summary: 'Get all scoring rules for tenant' })
  @ApiResponse({ status: 200, type: [ScoreRuleListItemDto] })
  async getRulesByTenant(
    @RequestContext() context: any,
    @Query('page', new DefaultValuePipe(1), ParseIntPipe) page: number,
    @Query('limit', new DefaultValuePipe(20), ParseIntPipe) limit: number,
  ): Promise<ScoreRuleListItemDto[]> {
    return this.scoreRuleService.getRulesByTenant(context.tenantId);
  }

  @Get('model/:scoreModelId')
  @ApiOperation({ summary: 'Get all rules for a scoring model' })
  @ApiResponse({ status: 200, type: [ScoreRuleResponseDto] })
  async getRulesByModel(
    @Param('scoreModelId') scoreModelId: string,
  ): Promise<ScoreRuleResponseDto[]> {
    return this.scoreRuleService.getRulesByModel(scoreModelId);
  }

  @Get('model/:scoreModelId/active')
  @ApiOperation({ summary: 'Get active rules for a scoring model' })
  @ApiResponse({ status: 200, type: [ScoreRuleResponseDto] })
  async getActiveRulesByModel(
    @Param('scoreModelId') scoreModelId: string,
  ): Promise<ScoreRuleResponseDto[]> {
    return this.scoreRuleService.getActiveRulesByModel(scoreModelId);
  }

  @Delete(':id')
  @ApiOperation({ summary: 'Delete a scoring rule' })
  @ApiResponse({ status: 204 })
  async deleteRule(
    @Param('id') id: string,
  ): Promise<void> {
    return this.scoreRuleService.deleteRule(id);
  }
}

