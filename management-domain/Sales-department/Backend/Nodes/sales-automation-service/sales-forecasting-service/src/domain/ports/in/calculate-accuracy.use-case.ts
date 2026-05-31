import { ForecastAccuracy } from '../../entities/forecast-accuracy.entity';
import { RequestContextData } from '../../../shared/request-context';

export interface CalculateAccuracyInput {
  forecastId: string;
  periodId?: string;
  actualAmount: number;
  comparisonStartDate: Date;
  comparisonEndDate: Date;
}

export interface CalculateAccuracyResult {
  accuracy: ForecastAccuracy;
  alertThreshold?: {
    threshold: number;
    currentAccuracy: number;
    requiresAttention: boolean;
  };
}

export interface CalculateAccuracyUseCase {
  execute(
    context: RequestContextData,
    input: CalculateAccuracyInput,
  ): Promise<CalculateAccuracyResult>;
}

