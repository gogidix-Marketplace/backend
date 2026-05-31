import { UUID } from 'crypto';
import { ReconciliationStatus } from '../../enums/reconciliation-status.enum';
import { MatchType } from '../../enums/match-type.enum';
import { DifferenceStatus } from '../../enums/difference-status.enum';
import { DifferenceType } from '../../models/transaction-difference.entity';

export interface IReconciliationCommandHandler {
  startReconciliation(command: StartReconciliationCommand): Promise<ReconciliationResult>;
  cancelReconciliation(command: CancelReconciliationCommand): Promise<void>;
  scheduleReconciliation(command: ScheduleReconciliationCommand): Promise<void>;
  resolveDifference(command: ResolveDifferenceCommand): Promise<void>;
  bulkResolveDifferences(command: BulkResolveDifferencesCommand): Promise<void>;
  verifyMatch(command: VerifyMatchCommand): Promise<void>;
  unverifyMatch(command: UnverifyMatchCommand): Promise<void>;
  createRule(command: CreateReconciliationRuleCommand): Promise<RuleResult>;
  updateRule(command: UpdateReconciliationRuleCommand): Promise<void>;
  deleteRule(command: DeleteReconciliationRuleCommand): Promise<void>;
  enableRule(command: EnableRuleCommand): Promise<void>;
  disableRule(command: DisableRuleCommand): Promise<void>;
}

export class StartReconciliationCommand {
  constructor(
    public readonly tenantId: string,
    public readonly name: string,
    public readonly dataSourceType: 'BANK' | 'INTERNAL' | 'BOTH',
    public readonly createdBy: string,
    public readonly ruleIds: string[] = [],
    public readonly bankAccountId?: string,
    public readonly internalAccountId?: string,
    public readonly description?: string,
    public readonly startDate?: Date,
    public readonly endDate?: Date,
    public readonly metadata?: Record<string, unknown>,
    public readonly correlationId?: string,
  ) {}
}

export class CancelReconciliationCommand {
  constructor(
    public readonly reconciliationId: UUID,
    public readonly tenantId: string,
    public readonly userId: string,
    public readonly reason?: string,
  ) {}
}

export class ScheduleReconciliationCommand {
  constructor(
    public readonly tenantId: string,
    public readonly name: string,
    public readonly dataSourceType: 'BANK' | 'INTERNAL' | 'BOTH',
    public readonly scheduleExpression: string,
    public readonly createdBy: string,
    public readonly ruleIds: string[] = [],
    public readonly bankAccountId?: string,
    public readonly internalAccountId?: string,
    public readonly description?: string,
    public readonly timezone?: string,
    public readonly metadata?: Record<string, unknown>,
  ) {}
}

export class ResolveDifferenceCommand {
  constructor(
    public readonly differenceId: UUID,
    public readonly reconciliationId: UUID,
    public readonly tenantId: string,
    public readonly action: 'AUTO_RESOLVE' | 'MANUALLY_RESOLVE' | 'IGNORE' | 'ESCALATE',
    public readonly userId: string,
    public readonly notes?: string,
    public readonly resolutionData?: Record<string, unknown>,
  ) {}
}

export class BulkResolveDifferencesCommand {
  constructor(
    public readonly differenceIds: UUID[],
    public readonly reconciliationId: UUID,
    public readonly tenantId: string,
    public readonly action: 'AUTO_RESOLVE' | 'MANUALLY_RESOLVE' | 'IGNORE',
    public readonly userId: string,
    public readonly notes?: string,
  ) {}
}

export class VerifyMatchCommand {
  constructor(
    public readonly matchId: UUID,
    public readonly reconciliationId: UUID,
    public readonly tenantId: string,
    public readonly userId: string,
    public readonly notes?: string,
  ) {}
}

export class UnverifyMatchCommand {
  constructor(
    public readonly matchId: UUID,
    public readonly reconciliationId: UUID,
    public readonly tenantId: string,
    public readonly userId: string,
  ) {}
}

export class CreateReconciliationRuleCommand {
  constructor(
    public readonly tenantId: string,
    public readonly name: string,
    public readonly matchType: MatchType,
    public readonly priority: number,
    public readonly createdBy: string,
    public readonly conditions: RuleCondition[],
    public readonly actions: RuleAction[],
    public readonly description?: string,
    public readonly confidenceThreshold?: number,
  ) {}
}

export interface RuleCondition {
  field: string;
  operator: 'equals' | 'contains' | 'startsWith' | 'endsWith' | 'greaterThan' | 'lessThan' | 'between';
  value: unknown;
  weight?: number;
}

export interface RuleAction {
  type: 'AUTO_MATCH' | 'FLAG_FOR_REVIEW' | 'AUTO_RESOLVE' | 'SEND_NOTIFICATION';
  params?: Record<string, unknown>;
}

export class UpdateReconciliationRuleCommand {
  constructor(
    public readonly ruleId: UUID,
    public readonly tenantId: string,
    public readonly name?: string,
    public readonly description?: string,
    public readonly matchType?: MatchType,
    public readonly priority?: number,
    public readonly conditions?: RuleCondition[],
    public readonly actions?: RuleAction[],
    public readonly confidenceThreshold?: number,
  ) {}
}

export class DeleteReconciliationRuleCommand {
  constructor(
    public readonly ruleId: UUID,
    public readonly tenantId: string,
  ) {}
}

export class EnableRuleCommand {
  constructor(
    public readonly ruleId: UUID,
    public readonly tenantId: string,
  ) {}
}

export class DisableRuleCommand {
  constructor(
    public readonly ruleId: UUID,
    public readonly tenantId: string,
  ) {}
}

export interface ReconciliationResult {
  reconciliationId: UUID;
  status: ReconciliationStatus;
  startedAt: Date;
}

export interface RuleResult {
  ruleId: UUID;
  name: string;
  enabled: boolean;
}
