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
import { ScoreModelService } from '../../application/services/score-model.service';
import {
  CreateScoreModelRequestDto,
  UpdateScoreModelRequestDto,
  ActivateScoreModelRequestDto,
  AddVariantRequestDto,
  EnableABTestingRequestDto,
} from '../../application/dto/request/score-model.request.dto';
import {
  ScoreModelResponseDto,
  ScoreModelListItemDto,
} from '../../application/dto/response/score-model.response.dto';
import { RequestContext } from '../../infrastructure/security/request-context';
import { TenantGuard } from '../../infrastructure/security/guards/tenant.guard';

@ApiTags('Score Models')
@ApiBearerAuth()
@Controller('score-models')
@UseGuards(TenantGuard)
export class ScoreModelController {
  constructor(private readonly scoreModelService: ScoreModelService) {}

  @Post()
  @ApiOperation({ summary: 'Create a new scoring model' })
  @ApiResponse({ status: 201, type: ScoreModelResponseDto })
  async createModel(
    @Body() request: CreateScoreModelRequestDto,
    @RequestContext() context: any,
  ): Promise<ScoreModelResponseDto> {
    request.tenantId = context.tenantId;
    request.createdBy = context.userId;
    return this.scoreModelService.createModel(request);
  }

  @Put(':id')
  @ApiOperation({ summary: 'Update a scoring model' })
  @ApiResponse({ status: 200, type: ScoreModelResponseDto })
  async updateModel(
    @Param('id') id: string,
    @Body() request: UpdateScoreModelRequestDto,
    @RequestContext() context: any,
  ): Promise<ScoreModelResponseDto> {
    return this.scoreModelService.updateModel(id, request, context.userId);
  }

  @Post(':id/activate')
  @ApiOperation({ summary: 'Activate a scoring model' })
  @ApiResponse({ status: 200, type: ScoreModelResponseDto })
  async activateModel(
    @Param('id') id: string,
    @Body() request: ActivateScoreModelRequestDto,
    @RequestContext() context: any,
  ): Promise<ScoreModelResponseDto> {
    request.userId = context.userId;
    return this.scoreModelService.activateModel(id, request);
  }

  @Post(':id/deactivate')
  @ApiOperation({ summary: 'Deactivate a scoring model' })
  @ApiResponse({ status: 200, type: ScoreModelResponseDto })
  async deactivateModel(
    @Param('id') id: string,
    @RequestContext() context: any,
  ): Promise<ScoreModelResponseDto> {
    return this.scoreModelService.deactivateModel(id, context.userId);
  }

  @Post(':id/archive')
  @ApiOperation({ summary: 'Archive a scoring model' })
  @ApiResponse({ status: 200, type: ScoreModelResponseDto })
  async archiveModel(
    @Param('id') id: string,
    @RequestContext() context: any,
  ): Promise<ScoreModelResponseDto> {
    return this.scoreModelService.archiveModel(id, context.userId);
  }

  @Post(':id/variants')
  @ApiOperation({ summary: 'Add a variant to the model' })
  @ApiResponse({ status: 200, type: ScoreModelResponseDto })
  async addVariant(
    @Param('id') id: string,
    @Body() request: AddVariantRequestDto,
  ): Promise<ScoreModelResponseDto> {
    return this.scoreModelService.addVariant(id, request);
  }

  @Delete(':id/variants/:variantId')
  @ApiOperation({ summary: 'Remove a variant from the model' })
  @ApiResponse({ status: 200, type: ScoreModelResponseDto })
  async removeVariant(
    @Param('id') id: string,
    @Param('variantId') variantId: string,
  ): Promise<ScoreModelResponseDto> {
    return this.scoreModelService.removeVariant(id, variantId);
  }

  @Post(':id/ab-testing/enable')
  @ApiOperation({ summary: 'Enable A/B testing for the model' })
  @ApiResponse({ status: 200, type: ScoreModelResponseDto })
  async enableABTesting(
    @Param('id') id: string,
    @Body() request: EnableABTestingRequestDto,
  ): Promise<ScoreModelResponseDto> {
    return this.scoreModelService.enableABTesting(id, request);
  }

  @Post(':id/ab-testing/disable')
  @ApiOperation({ summary: 'Disable A/B testing for the model' })
  @ApiResponse({ status: 200, type: ScoreModelResponseDto })
  async disableABTesting(
    @Param('id') id: string,
  ): Promise<ScoreModelResponseDto> {
    return this.scoreModelService.disableABTesting(id);
  }

  @Post(':id/set-default')
  @ApiOperation({ summary: 'Set model as default' })
  @ApiResponse({ status: 200, type: ScoreModelResponseDto })
  async setAsDefault(
    @Param('id') id: string,
  ): Promise<ScoreModelResponseDto> {
    return this.scoreModelService.setAsDefault(id);
  }

  @Post(':id/new-version')
  @ApiOperation({ summary: 'Create a new version of the model' })
  @ApiResponse({ status: 201, type: ScoreModelResponseDto })
  async createNewVersion(
    @Param('id') id: string,
    @RequestContext() context: any,
  ): Promise<ScoreModelResponseDto> {
    return this.scoreModelService.createNewVersion(id, context.userId);
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get a specific scoring model' })
  @ApiResponse({ status: 200, type: ScoreModelResponseDto })
  async getModel(
    @Param('id') id: string,
  ): Promise<ScoreModelResponseDto> {
    return this.scoreModelService.getModel(id);
  }

  @Get()
  @ApiOperation({ summary: 'Get all scoring models for tenant' })
  @ApiResponse({ status: 200, type: [ScoreModelListItemDto] })
  async getModelsByTenant(
    @RequestContext() context: any,
    @Query('status') status?: string,
  ): Promise<ScoreModelListItemDto[]> {
    if (status === 'active') {
      return this.scoreModelService.getActiveModelsByTenant(context.tenantId);
    }
    return this.scoreModelService.getModelsByTenant(context.tenantId);
  }

  @Get('default/current')
  @ApiOperation({ summary: 'Get the default scoring model' })
  @ApiResponse({ status: 200, type: ScoreModelResponseDto })
  async getDefaultModel(
    @RequestContext() context: any,
  ): Promise<ScoreModelResponseDto> {
    return this.scoreModelService.getDefaultModel(context.tenantId);
  }

  @Delete(':id')
  @ApiOperation({ summary: 'Delete a scoring model' })
  @ApiResponse({ status: 204 })
  async deleteModel(
    @Param('id') id: string,
  ): Promise<void> {
    return this.scoreModelService.deleteModel(id);
  }
}

