import { MetricsHistory } from '../models/metrics-history.entity';

export interface IMetricsHistoryRepository {
  save(history: MetricsHistory): Promise<MetricsHistory>;
  findByResourceId(resourceId: string, hours: number): Promise<MetricsHistory[]>;
}
