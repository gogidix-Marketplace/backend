import { Inject, Injectable } from '@nestjs/common';
import { Forecast } from '../../domain/entities/forecast.entity';
import { RequestContextData } from '../../shared/request-context';
import {
  ListForecastsUseCase,
  ListForecastsInput,
  ListForecastsResult,
} from '../../domain/ports/in/list-forecasts.use-case';
import { ForecastRepositoryPort } from '../../domain/ports/out/forecast.repository.port';

@Injectable()
export class ListForecastsUseCaseImpl implements ListForecastsUseCase {
  constructor(
    @Inject('ForecastRepositoryPort')
    private readonly forecastRepository: ForecastRepositoryPort,
  ) {}

  async execute(
    context: RequestContextData,
    input: ListForecastsInput,
  ): Promise<ListForecastsResult> {
    const page = input.page || 1;
    const limit = input.limit || 20;

    const filters = {
      tenantId: context.tenantId,
      status: input.status,
      model: input.model,
      period: input.period,
      granularity: input.granularity,
      granularityId: input.granularityId,
      startDate: input.startDate,
      endDate: input.endDate,
    };

    const { forecasts, total } = await this.forecastRepository.findByTenantAndFilters(
      filters,
      page,
      limit,
      input.sortBy || 'createdAt',
      input.sortOrder || 'DESC',
    );

    return {
      forecasts,
      total,
      page,
      limit,
      totalPages: Math.ceil(total / limit),
    };
  }
}





