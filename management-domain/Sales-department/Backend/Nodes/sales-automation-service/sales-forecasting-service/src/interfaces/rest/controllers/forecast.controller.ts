import {
  Controller,
  Get,
  Post,
  Put,
  Body,
  Param,
  Query,
  UseGuards,
  UseInterceptors,
} from '@nestjs/common';
import {
  ApiTags,
  ApiOperation,
  ApiResponse,
  ApiBearerAuth,
  ApiParam,
  ApiQuery,
} from '@nestjs/swagger';
import { RequestContext, RequestContextData } from '../../../shared/request-context';
import { GenerateForecastUseCase } from '../../../domain/ports/in/generate-forecast.use-case';
import { AdjustForecastUseCase } from '../../../domain/ports/in/adjust-forecast.use-case';
import { GetForecastUseCase } from '../../../domain/ports/in/get-forecast.use-case';
import { ListForecastsUseCase } from '../../../domain/ports/in/list-forecasts.use-case';
import { CalculateAccuracyUseCase } from '../../../domain/ports/in/calculate-accuracy.use-case';
import { TriggerRollingForecastUseCase } from '../../../domain/ports/in/trigger-rolling-forecast.use-case';
import {
  GenerateForecastRequestDto,
  AdjustForecastRequestDto,
  CalculateAccuracyRequestDto,
  ListForecastsQueryDto,
} from '../../../application/dto/request.dto';
import {
  ForecastResponseDto,
  GenerateForecastResponseDto,
  AdjustForecastResponseDto,
  ListForecastsResponseDto,
  CalculateAccuracyResponseDto,
} from '../../../application/dto/response.dto';
import { ForecastMapper } from '../mappers/forecast.mapper';

@ApiTags('Forecasts')
@ApiBearerAuth()
@Controller('forecasts')
export class ForecastController {
  constructor(
    private readonly generateForecastUseCase: GenerateForecastUseCase,
    private readonly adjustForecastUseCase: AdjustForecastUseCase,
    private readonly getForecastUseCase: GetForecastUseCase,
    private readonly listForecastsUseCase: ListForecastsUseCase,
    private readonly calculateAccuracyUseCase: CalculateAccuracyUseCase,
    private readonly triggerRollingForecastUseCase: TriggerRollingForecastUseCase,
    private readonly forecastMapper: ForecastMapper,
  ) {}

  @Post()
  @ApiOperation({ summary: 'Generate a new sales forecast' })
  @ApiResponse({ status: 201, type: GenerateForecastResponseDto })
  async generateForecast(
    @RequestContext() context: RequestContextData,
    @Body() request: GenerateForecastRequestDto,
  ): Promise<GenerateForecastResponseDto> {
    const result = await this.generateForecastUseCase.execute(context, {
      name: request.name,
      description: request.description,
      model: request.model,
      period: request.period,
      granularity: request.granularity,
      granularityId: request.granularityId,
      currency: request.currency,
      startDate: request.startDate,
      endDate: request.endDate,
      horizonMonths: request.horizonMonths,
      confidenceLevel: request.confidenceLevel,
    });

    return this.forecastMapper.toGenerateResponse(result);
  }

  @Get(':forecastId')
  @ApiOperation({ summary: 'Get a forecast by ID' })
  @ApiParam({ name: 'forecastId', description: 'Forecast ID' })
  @ApiResponse({ status: 200, type: ForecastResponseDto })
  async getForecast(
    @RequestContext() context: RequestContextData,
    @Param('forecastId') forecastId: string,
    @Query('includeAccuracy') includeAccuracy?: string,
    @Query('includePeriods') includePeriods?: string,
  ): Promise<ForecastResponseDto> {
    const forecast = await this.getForecastUseCase.execute(context, {
      forecastId,
      includeAccuracy: includeAccuracy === 'true',
      includePeriods: includePeriods === 'true',
    });

    return this.forecastMapper.toResponse(forecast);
  }

  @Get()
  @ApiOperation({ summary: 'List forecasts with filters' })
  @ApiQuery({ type: ListForecastsQueryDto })
  @ApiResponse({ status: 200, type: ListForecastsResponseDto })
  async listForecasts(
    @RequestContext() context: RequestContextData,
    @Query() query: ListForecastsQueryDto,
  ): Promise<ListForecastsResponseDto> {
    const result = await this.listForecastsUseCase.execute(context, {
      model: query.model,
      period: query.period,
      granularity: query.granularity,
      granularityId: query.granularityId,
      startDate: query.startDate,
      endDate: query.endDate,
      page: query.page,
      limit: query.limit,
      sortBy: query.sortBy,
      sortOrder: query.sortOrder,
    });

    return this.forecastMapper.toListResponse(result);
  }

  @Put(':forecastId/adjust')
  @ApiOperation({ summary: 'Adjust a forecast' })
  @ApiParam({ name: 'forecastId', description: 'Forecast ID' })
  @ApiResponse({ status: 200, type: AdjustForecastResponseDto })
  async adjustForecast(
    @RequestContext() context: RequestContextData,
    @Param('forecastId') forecastId: string,
    @Body() request: AdjustForecastRequestDto,
  ): Promise<AdjustForecastResponseDto> {
    const result = await this.adjustForecastUseCase.execute(context, {
      forecastId,
      adjustmentFactor: request.adjustmentFactor,
      reason: request.reason,
      adjustmentType: request.adjustmentType,
    });

    return this.forecastMapper.toAdjustResponse(result);
  }

  @Post(':forecastId/accuracy')
  @ApiOperation({ summary: 'Calculate forecast accuracy' })
  @ApiParam({ name: 'forecastId', description: 'Forecast ID' })
  @ApiResponse({ status: 201, type: CalculateAccuracyResponseDto })
  async calculateAccuracy(
    @RequestContext() context: RequestContextData,
    @Param('forecastId') forecastId: string,
    @Body() request: CalculateAccuracyRequestDto,
  ): Promise<CalculateAccuracyResponseDto> {
    const result = await this.calculateAccuracyUseCase.execute(context, {
      forecastId,
      periodId: request.periodId,
      actualAmount: request.actualAmount,
      comparisonStartDate: request.comparisonStartDate,
      comparisonEndDate: request.comparisonEndDate,
    });

    return this.forecastMapper.toAccuracyResponse(result);
  }

  @Post('rolling/trigger')
  @ApiOperation({ summary: 'Trigger a rolling forecast' })
  @ApiQuery({ name: 'granularity', required: false })
  @ApiQuery({ name: 'granularityId', required: false })
  @ApiResponse({ status: 201, type: ForecastResponseDto })
  async triggerRollingForecast(
    @RequestContext() context: RequestContextData,
    @Query('granularity') granularity?: string,
    @Query('granularityId') granularityId?: string,
    @Query('triggeredBy') triggeredBy?: 'SCHEDULER' | 'MANUAL' | 'EVENT',
    @Query('reason') reason?: string,
  ): Promise<ForecastResponseDto> {
    const result = await this.triggerRollingForecastUseCase.execute(context, {
      granularity,
      granularityId,
      triggeredBy,
      reason,
    });

    return this.forecastMapper.toResponse(result.forecast);
  }
}
