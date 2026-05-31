import { AggregateRoot } from '@shared/base/base.entity';

export class LogAlertRule extends AggregateRoot {
  constructor(
    public name: string,
    public description: string,
    public enabled: boolean,
    public conditions: Array<{
      field: string;
      operator: 'equals' | 'contains' | 'regex' | 'gt' | 'lt' | 'exists';
      value?: string | number;
      threshold?: number;
      timeWindow?: number;
    }>,
    public actions: Array<{
      type: 'webhook' | 'email' | 'slack' | 'pagerduty';
      config: Record<string, unknown>;
      enabled: boolean;
    }>,
    public cooldown: number,
    public lastTriggeredAt?: Date,
    public triggerCount: number = 0,
    props?: { id?: string; createdAt?: Date; updatedAt?: Date },
  ) {
    super(props);
  }
}
