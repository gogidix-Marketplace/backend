import { Inject, Injectable } from '@nestjs/common';
import { Forecast } from '../../domain/entities/forecast.entity';
import { RequestContextData } from '../../shared/request-context';
import { ForecastNotFoundError } from '../../shared/errors';
import {
  GetForecastUseCase,
  GetForecastInput,
} from '../../domain/ports/in/get-forecast.use-case';
import { ForecastRepositoryPort } from '../../domain/ports/out/forecast.repository.port';

@Injectable()
export class GetForecastUseCaseImpl implements GetForecastUseCase {
  constructor(
    @Inject('ForecastRepositoryPort')
    private readonly forecastRepository: ForecastRepositoryPort,
  ) {}

  async execute(
    context: RequestContextData,
    input: GetForecastInput,
  ): Promise<Forecast> {
    const forecast = await this.forecastRepository.findById(
      input.forecastId,
      context.tenantId,
    );

    if (!forecast) {
      throw new ForecastNotFoundError(input.forecastId);
    }

    return forecast;
  }
}





