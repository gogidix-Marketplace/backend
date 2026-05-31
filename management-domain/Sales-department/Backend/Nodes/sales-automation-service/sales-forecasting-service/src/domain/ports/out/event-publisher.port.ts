import { ForecastGeneratedEvent } from '../../events/forecast-generated.event';
import { ForecastAdjustedEvent } from '../../events/forecast-adjusted.event';
import { ForecastAccuracyCalculatedEvent } from '../../events/forecast-accuracy-calculated.event';
import { RollingForecastTriggeredEvent } from '../../events/rolling-forecast-triggered.event';

export interface EventPublisherPort {
  publishForecastGenerated(event: ForecastGeneratedEvent): Promise<void>;
  publishForecastAdjusted(event: ForecastAdjustedEvent): Promise<void>;
  publishForecastAccuracyCalculated(event: ForecastAccuracyCalculatedEvent): Promise<void>;
  publishRollingForecastTriggered(event: RollingForecastTriggeredEvent): Promise<void>;
}
