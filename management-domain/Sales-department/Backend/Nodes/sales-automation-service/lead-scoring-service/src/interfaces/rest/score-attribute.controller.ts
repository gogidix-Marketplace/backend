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
import { ScoreAttributeService } from '../../application/services/score-attribute.service';
import {
  CreateScoreAttributeRequestDto,
  UpdateScoreAttributeRequestDto,
  UpdateAttributeWeightRequestDto,
} from '../../application/dto/request/score-attribute.request.dto';
import {
  ScoreAttributeResponseDto,
  ScoreAttributeListItemDto,
  AttributeValidationDto,
  AttributeContributionDto,
} from '../../application/dto/response/score-attribute.response.dto';
import { RequestContext } from '../../infrastructure/security/request-context';
import { TenantGuard } from '../../infrastructure/security/guards/tenant.guard';

@ApiTags('Score Attributes')
@ApiBearerAuth()
@Controller('score-attributes')
@UseGuards(TenantGuard)
export class ScoreAttributeController {
  constructor(private readonly scoreAttributeService: ScoreAttributeService) {}

  @Post()
  @ApiOperation({ summary: 'Create a new score attribute' })
  @ApiResponse({ status: 201, type: ScoreAttributeResponseDto })
  async createAttribute(
    @Body() request: CreateScoreAttributeRequestDto,
    @RequestContext() context: any,
  ): Promise<ScoreAttributeResponseDto> {
    request.tenantId = context.tenantId;
    return this.scoreAttributeService.createAttribute(request);
  }

  @Put(':id')
  @ApiOperation({ summary: 'Update a score attribute' })
  @ApiResponse({ status: 200, type: ScoreAttributeResponseDto })
  async updateAttribute(
    @Param('id') id: string,
    @Body() request: UpdateScoreAttributeRequestDto,
  ): Promise<ScoreAttributeResponseDto> {
    return this.scoreAttributeService.updateAttribute(id, request);
  }

  @Put(':id/weight')
  @ApiOperation({ summary: 'Update attribute weight' })
  @ApiResponse({ status: 200, type: ScoreAttributeResponseDto })
  async updateWeight(
    @Param('id') id: string,
    @Body() request: UpdateAttributeWeightRequestDto,
  ): Promise<ScoreAttributeResponseDto> {
    return this.scoreAttributeService.updateWeight(id, request);
  }

  @Post(':id/activate')
  @ApiOperation({ summary: 'Activate a score attribute' })
  @ApiResponse({ status: 200, type: ScoreAttributeResponseDto })
  async activateAttribute(
    @Param('id') id: string,
  ): Promise<ScoreAttributeResponseDto> {
    return this.scoreAttributeService.activateAttribute(id);
  }

  @Post(':id/deactivate')
  @ApiOperation({ summary: 'Deactivate a score attribute' })
  @ApiResponse({ status: 200, type: ScoreAttributeResponseDto })
  async deactivateAttribute(
    @Param('id') id: string,
  ): Promise<ScoreAttributeResponseDto> {
    return this.scoreAttributeService.deactivateAttribute(id);
  }

  @Post(':id/options')
  @ApiOperation({ summary: 'Add an option to the attribute' })
  @ApiResponse({ status: 200, type: ScoreAttributeResponseDto })
  async addOption(
    @Param('id') id: string,
    @Body('option') option: string,
  ): Promise<ScoreAttributeResponseDto> {
    return this.scoreAttributeService.addOption(id, option);
  }

  @Delete(':id/options/:option')
  @ApiOperation({ summary: 'Remove an option from the attribute' })
  @ApiResponse({ status: 200, type: ScoreAttributeResponseDto })
  async removeOption(
    @Param('id') id: string,
    @Param('option') option: string,
  ): Promise<ScoreAttributeResponseDto> {
    return this.scoreAttributeService.removeOption(id, option);
  }

  @Post(':id/validate')
  @ApiOperation({ summary: 'Validate a value against the attribute' })
  @ApiResponse({ status: 200, type: AttributeValidationDto })
  async validateValue(
    @Param('id') id: string,
    @Body('value') value: any,
  ): Promise<AttributeValidationDto> {
    return this.scoreAttributeService.validateValue(id, value);
  }

  @Post(':id/contribution')
  @ApiOperation({ summary: 'Calculate contribution of a value' })
  @ApiResponse({ status: 200, type: AttributeContributionDto })
  async calculateContribution(
    @Param('id') id: string,
    @Body('value') value: any,
  ): Promise<AttributeContributionDto> {
    return this.scoreAttributeService.calculateContribution(id, value);
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get a specific score attribute' })
  @ApiResponse({ status: 200, type: ScoreAttributeResponseDto })
  async getAttribute(
    @Param('id') id: string,
  ): Promise<ScoreAttributeResponseDto> {
    return this.scoreAttributeService.getAttribute(id);
  }

  @Get()
  @ApiOperation({ summary: 'Get all score attributes for tenant' })
  @ApiResponse({ status: 200, type: [ScoreAttributeListItemDto] })
  async getAttributesByTenant(
    @RequestContext() context: any,
    @Query('page', new DefaultValuePipe(1), ParseIntPipe) page: number,
    @Query('limit', new DefaultValuePipe(20), ParseIntPipe) limit: number,
  ): Promise<ScoreAttributeListItemDto[]> {
    return this.scoreAttributeService.getAttributesByTenant(context.tenantId);
  }

  @Get('active')
  @ApiOperation({ summary: 'Get active attributes for tenant' })
  @ApiResponse({ status: 200, type: [ScoreAttributeListItemDto] })
  async getActiveAttributesByTenant(
    @RequestContext() context: any,
  ): Promise<ScoreAttributeListItemDto[]> {
    return this.scoreAttributeService.getActiveAttributesByTenant(context.tenantId);
  }

  @Get('type/:type')
  @ApiOperation({ summary: 'Get attributes by type' })
  @ApiResponse({ status: 200, type: [ScoreAttributeResponseDto] })
  async getAttributesByType(
    @Param('type') type: string,
    @RequestContext() context: any,
  ): Promise<ScoreAttributeResponseDto[]> {
    return this.scoreAttributeService.getAttributesByType(context.tenantId, type);
  }

  @Delete(':id')
  @ApiOperation({ summary: 'Delete a score attribute' })
  @ApiResponse({ status: 204 })
  async deleteAttribute(
    @Param('id') id: string,
  ): Promise<void> {
    return this.scoreAttributeService.deleteAttribute(id);
  }
}

