export enum ForecastModel {
  WEIGHTED_PIPELINE = 'WEIGHTED_PIPELINE',
  HISTORICAL = 'HISTORICAL',
  AI_ML = 'AI_ML',
  HYBRID = 'HYBRID',
}

export enum ForecastPeriod {
  MONTHLY = 'MONTHLY',
  QUARTERLY = 'QUARTERLY',
  ANNUAL = 'ANNUAL',
}

export enum ForecastStatus {
  DRAFT = 'DRAFT',
  ACTIVE = 'ACTIVE',
  ARCHIVED = 'ARCHIVED',
}

export enum ForecastGranularity {
  GLOBAL = 'GLOBAL',
  TERRITORY = 'TERRITORY',
  TEAM = 'TEAM',
  REP = 'REP',
}

export enum ForecastAccuracyMetric {
  MAPE = 'MAPE', // Mean Absolute Percentage Error
  MAE = 'MAE',   // Mean Absolute Error
  RMSE = 'RMSE', // Root Mean Square Error
  BIAS = 'BIAS', // Forecast Bias
}

export const DEFAULT_FORECAST_HORIZON_MONTHS = 12;
export const MINIMUM_HISTORICAL_MONTHS = 3;
export const FORECAST_CONFIDENCE_LEVELS = [50, 75, 90, 95];
export const ROLLING_FORECAST_DEFAULT_FREQUENCY = '0 0 1 * *'; // First day of month
