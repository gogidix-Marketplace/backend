export class ForecastError extends Error {
  constructor(message: string, public readonly code: string) {
    super(message);
    this.name = 'ForecastError';
  }
}

export class ForecastNotFoundError extends ForecastError {
  constructor(id: string) {
    super(`Forecast not found: ${id}`, 'FORECAST_NOT_FOUND');
    this.name = 'ForecastNotFoundError';
  }
}

export class InvalidForecastModelError extends ForecastError {
  constructor(model: string) {
    super(`Invalid forecast model: ${model}`, 'INVALID_FORECAST_MODEL');
    this.name = 'InvalidForecastModelError';
  }
}

export class InsufficientHistoricalDataError extends ForecastError {
  constructor(required: number, available: number) {
    super(
      `Insufficient historical data. Required: ${required} months, Available: ${available} months`,
      'INSUFFICIENT_HISTORICAL_DATA',
    );
    this.name = 'InsufficientHistoricalDataError';
  }
}

export class ForecastGenerationError extends ForecastError {
  constructor(message: string) {
    super(`Failed to generate forecast: ${message}`, 'FORECAST_GENERATION_ERROR');
    this.name = 'ForecastGenerationError';
  }
}
