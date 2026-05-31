import { LogAlertRule } from '../models/log-alert-rule.entity';

export interface ILogAlertRuleRepository {
  save(rule: LogAlertRule): Promise<LogAlertRule>;
  findById(id: string): Promise<LogAlertRule | null>;
  findEnabled(): Promise<LogAlertRule[]>;
  findByIdAndUpdate(id: string, update: any): Promise<LogAlertRule | null>;
  findByIdAndDelete(id: string): Promise<LogAlertRule | null>;
}
