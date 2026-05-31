export class AutomationStatisticsResponseDto {
  total: number;
  active: number;
  paused: number;
  draft: number;
  archived: number;
  byCategory: Record<string, number>;
  averageExecutionsPerDay: number;
  successRate: number;
  mostTriggeredRules: Array<{
    ruleId: string;
    ruleName: string;
    executionCount: number;
  }>;
  recentExecutions: Array<{
    ruleId: string;
    ruleName: string;
    executedAt: Date;
    status: string;
  }>;
}

export class WorkflowStatisticsResponseDto {
  total: number;
  active: number;
  totalExecutions: number;
  successfulExecutions: number;
  failedExecutions: number;
  averageExecutionDuration?: number;
  mostExecutedWorkflows: Array<{
    workflowId: string;
    workflowName: string;
    executionCount: number;
  }>;
  recentExecutions: Array<{
    workflowId: string;
    workflowName: string;
    executedAt: Date;
    status: string;
    duration?: number;
  }>;
}
