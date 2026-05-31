import { AutomationRule } from '../../models/automation-rule.entity';
import { ExecutionContext } from '@shared/context';

export interface CreateAutomationRuleCommand {
  name: string;
  description?: string;
  trigger: {
    type: string;
    conditions?: Array<{
      field: string;
      operator: string;
      value?: any;
      values?: any[];
    }>;
    entityTypes?: string[];
    cronExpression?: string;
    debounceMs?: number;
  };
  actions: Array<{
    type: string;
    order: number;
    parameters: Record<string, any>;
    continueOnError?: boolean;
    delayMs?: number;
    name: string;
  }>;
  tags?: string[];
  priority?: number;
  category?: string;
  templateId?: string;
  schedule?: {
    frequency: 'once' | 'daily' | 'weekly' | 'monthly' | 'cron';
    cronExpression?: string;
    timezone?: string;
    startDate?: Date;
    endDate?: Date;
  };
  leadScoring?: {
    enabled: boolean;
    conditions: Array<{
      field: string;
      operator: string;
      value?: any;
      values?: any[];
    }>;
    score: number;
    maxScore?: number;
    category?: 'demographic' | 'behavioral' | 'engagement';
  };
  dealStageRule?: {
    enabled: boolean;
    currentStage?: string;
    targetStage?: string;
    conditions?: Array<{
      field: string;
      operator: string;
      value?: any;
    }>;
    autoTransition?: boolean;
    notifyAssignee?: boolean;
  };
}

export interface UpdateAutomationRuleCommand {
  ruleId: string;
  name?: string;
  description?: string;
  trigger?: Partial<CreateAutomationRuleCommand['trigger']>;
  actions?: Array<{
    type: string;
    order: number;
    parameters: Record<string, any>;
    continueOnError?: boolean;
    delayMs?: number;
    name: string;
  }>;
  tags?: string[];
  priority?: number;
  leadScoring?: any;
  dealStageRule?: any;
}

export interface DeleteAutomationRuleCommand {
  ruleId: string;
  reason?: string;
}

export interface ActivateAutomationRuleCommand {
  ruleId: string;
}

export interface PauseAutomationRuleCommand {
  ruleId: string;
}

export interface ArchiveAutomationRuleCommand {
  ruleId: string;
}

export interface AutomationRuleCommand {
  create(command: CreateAutomationRuleCommand, context: ExecutionContext): Promise<AutomationRule>;
  update(command: UpdateAutomationRuleCommand, context: ExecutionContext): Promise<AutomationRule>;
  delete(command: DeleteAutomationRuleCommand, context: ExecutionContext): Promise<void>;
  activate(command: ActivateAutomationRuleCommand, context: ExecutionContext): Promise<AutomationRule>;
  pause(command: PauseAutomationRuleCommand, context: ExecutionContext): Promise<AutomationRule>;
  archive(command: ArchiveAutomationRuleCommand, context: ExecutionContext): Promise<AutomationRule>;
}
