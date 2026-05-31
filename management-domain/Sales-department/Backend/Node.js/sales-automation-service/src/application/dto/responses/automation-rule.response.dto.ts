import { AutomationRule } from '../../../domain/models/automation-rule.entity';
import { AutomationRuleStatus } from '../../../domain/enums/automation-rule-status.enum';

export class AutomationRuleResponseDto {
  id: string;
  name: string;
  description: string;
  status: AutomationRuleStatus;
  trigger: any;
  actions: any[];
  leadScoring?: any;
  dealStageRule?: any;
  tags: string[];
  priority: number;
  executionCount: number;
  lastExecutedAt?: Date;
  lastExecutionStatus?: 'SUCCESS' | 'FAILURE' | 'PARTIAL';
  category?: string;
  templateId?: string;
  isEnabled: boolean;
  schedule?: any;
  createdAt: Date;
  updatedAt: Date;
  tenantId: string;

  static fromEntity(rule: AutomationRule): AutomationRuleResponseDto {
    return {
      id: rule.id,
      name: rule.name,
      description: rule.description,
      status: rule.status,
      trigger: rule.trigger,
      actions: rule.actions,
      leadScoring: rule.leadScoring,
      dealStageRule: rule.dealStageRule,
      tags: rule.tags,
      priority: rule.priority,
      executionCount: rule.executionCount,
      lastExecutedAt: rule.lastExecutedAt,
      lastExecutionStatus: rule.lastExecutionStatus,
      category: rule.category,
      templateId: rule.templateId,
      isEnabled: rule.isEnabled,
      schedule: rule.schedule,
      createdAt: rule.createdAt,
      updatedAt: rule.updatedAt,
      tenantId: rule.tenantId,
    };
  }

  static fromEntities(rules: AutomationRule[]): AutomationRuleResponseDto[] {
    return rules.map(rule => AutomationRuleResponseDto.fromEntity(rule));
  }
}
