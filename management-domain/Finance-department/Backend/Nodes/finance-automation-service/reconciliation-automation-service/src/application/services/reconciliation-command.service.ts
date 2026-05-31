import {Injectable, Logger, NotFoundException, ForbiddenException, Inject} from '@nestjs/common';
import { v4 as uuidv4 } from 'uuid';
import { Reconciliation } from '../../domain/models/reconciliation.entity';
import { ReconciliationRule } from '../../domain/models/reconciliation-rule.entity';
import { ReconciliationMatch } from '../../domain/models/reconciliation-match.entity';
import { TransactionDifference } from '../../domain/models/transaction-difference.entity';
import { ReconciliationStartedEvent } from '../../domain/events/reconciliation-started.event';
import { ReconciliationCompletedEvent } from '../../domain/events/reconciliation-completed.event';
import { ReconciliationFailedEvent } from '../../domain/events/reconciliation-failed.event';
import { MatchFoundEvent } from '../../domain/events/match-found.event';
import { DifferenceDetectedEvent } from '../../domain/events/difference-detected.event';
import { DifferenceResolvedEvent } from '../../domain/events/difference-resolved.event';
import { IReconciliationCommandHandler, StartReconciliationCommand, ScheduleReconciliationCommand, ResolveDifferenceCommand, BulkResolveDifferencesCommand, VerifyMatchCommand, UnverifyMatchCommand, CreateReconciliationRuleCommand, UpdateReconciliationRuleCommand, DeleteReconciliationRuleCommand, EnableRuleCommand, DisableRuleCommand, CancelReconciliationCommand } from '../../domain/ports/input/reconciliation.command';
import { IReconciliationRepository, IReconciliationRuleRepository, IReconciliationMatchRepository, ITransactionDifferenceRepository, IAuditLogRepository } from '../../domain/repositories/reconciliation-repository.interface';
import { IEventPublisher } from '../../domain/ports/output/event-publisher.interface';
import { IDataSource } from '../../domain/ports/output/data-source.interface';
import { AutoMatchService } from './auto-match.service';
import { DifferenceResolutionService } from './difference-resolution.service';
import { CronJob } from 'cron';

@Injectable()
export class ReconciliationCommandService implements IReconciliationCommandHandler {
  private readonly logger = new Logger(ReconciliationCommandService.name);

  constructor(
    @Inject('IReconciliationRepository')
    private readonly reconciliationRepository: IReconciliationRepository,
    @Inject('IReconciliationRuleRepository')
    private readonly ruleRepository: IReconciliationRuleRepository,
    @Inject('IReconciliationMatchRepository')
    private readonly matchRepository: IReconciliationMatchRepository,
    @Inject('ITransactionDifferenceRepository')
    private readonly differenceRepository: ITransactionDifferenceRepository,
    @Inject('IAuditLogRepository')
    private readonly auditLogRepository: IAuditLogRepository,
    @Inject('IEventPublisher')
    private readonly eventPublisher: IEventPublisher,
    @Inject('IDataSource')
    private readonly dataSource: IDataSource,
    private readonly autoMatchService: AutoMatchService,
    private readonly differenceResolutionService: DifferenceResolutionService,
  ) {}

  async startReconciliation(command: StartReconciliationCommand): Promise<any> {
    this.logger.log(`Starting reconciliation: ${command.name} for tenant: ${command.tenantId}`);

    const reconciliation = new Reconciliation({
      tenantId: command.tenantId,
      name: command.name,
      description: command.description,
      status: 'PENDING' as any,
      startDate: new Date(),
      dataSourceType: command.dataSourceType,
      bankAccountId: command.bankAccountId,
      internalAccountId: command.internalAccountId,
      ruleIds: command.ruleIds,
      matches: [],
      differences: [],
      totalTransactionsProcessed: 0,
      totalMatches: 0,
      totalDifferences: 0,
      autoResolvedCount: 0,
      manualReviewCount: 0,
      createdBy: command.createdBy,
      metadata: command.metadata,
      createdAt: new Date(),
      updatedAt: new Date(),
    });

    try {
      reconciliation.start();

      const savedReconciliation = await this.reconciliationRepository.save(reconciliation);

      await this.auditLogRepository.save({
        reconciliationId: savedReconciliation.id as any,
        action: 'RECONCILIATION_STARTED',
        userId: command.createdBy,
        timestamp: new Date(),
        details: { name: command.name, dataSourceType: command.dataSourceType },
      });

      await this.eventPublisher.publishReconciliationStarted(
        new ReconciliationStartedEvent(
          savedReconciliation.id as any,
          savedReconciliation.tenantId,
          savedReconciliation.name,
          savedReconciliation.props.dataSourceType,
          savedReconciliation.props.bankAccountId || '',
          savedReconciliation.props.internalAccountId || '',
          command.correlationId,
        ),
      );

      this.processReconciliationAsync(savedReconciliation.id).catch((error) => {
        this.logger.error(`Async reconciliation processing failed: ${error.message}`, error.stack);
      });

      return {
        reconciliationId: savedReconciliation.id as any,
        status: savedReconciliation.status,
        startedAt: savedReconciliation.props.startDate,
      };
    } catch (error) {
      this.logger.error(`Failed to start reconciliation: ${error.message}`, error.stack);
      throw error;
    }
  }

  private async processReconciliationAsync(reconciliationId: string): Promise<void> {
    const startTime = Date.now();
    let reconciliation: Reconciliation | undefined;

    try {
      const foundReconciliation = await this.reconciliationRepository.findById(reconciliationId as any, '');
      if (!foundReconciliation) {
        throw new NotFoundException(`Reconciliation ${reconciliationId} not found`);
      }
      reconciliation = foundReconciliation;

      const bankTransactions = reconciliation.props.dataSourceType === 'BANK' || reconciliation.props.dataSourceType === 'BOTH'
        ? await this.dataSource.getBankTransactions({
            tenantId: reconciliation.props.tenantId,
            accountId: reconciliation.props.bankAccountId,
          })
        : [];

      const internalTransactions = reconciliation.props.dataSourceType === 'INTERNAL' || reconciliation.props.dataSourceType === 'BOTH'
        ? await this.dataSource.getInternalTransactions({
            tenantId: reconciliation.props.tenantId,
            accountId: reconciliation.props.internalAccountId,
          })
        : [];

      reconciliation.incrementProcessedCount(bankTransactions.length + internalTransactions.length);

      const rules = reconciliation.props.ruleIds.length > 0
        ? await Promise.all(
            reconciliation.props.ruleIds.map((ruleId) => this.ruleRepository.findById(ruleId as any, reconciliation.props.tenantId)),
          ).then((rules) => rules.filter((r) => r !== null && r.enabled) as ReconciliationRule[])
        : await this.ruleRepository.findActive(reconciliation.props.tenantId);

      const { matches, differences } = await this.autoMatchService.findMatches(
        bankTransactions,
        internalTransactions,
        rules,
        reconciliation.id,
        reconciliation.props.tenantId,
      );

      for (const match of matches) {
        await this.matchRepository.save(match);
        reconciliation.addMatch(match);

        await this.eventPublisher.publishMatchFound(
          new MatchFoundEvent(
            reconciliation.id,
            reconciliation.props.tenantId,
            match.id,
            match.props.bankTransactionId || '',
            match.props.internalTransactionId || '',
            match.props.matchType,
            match.props.confidence,
            match.props.bankTransaction || { id: '', amount: 0, currency: '', date: new Date() },
            match.props.internalTransaction || { id: '', amount: 0, currency: '', date: new Date() },
          ),
        );
      }

      for (const difference of differences) {
        difference.calculateSeverity();
        await this.differenceRepository.save(difference);
        reconciliation.addDifference(difference);

        await this.eventPublisher.publishDifferenceDetected(
          new DifferenceDetectedEvent(
            reconciliation.id as any,
            reconciliation.props.tenantId,
            difference.id as any,
            difference.differenceType,
            difference.props.severity as any,
            difference.props.description || '',
            difference.props.bankTransactionId,
            difference.props.internalTransactionId,
            difference.props.bankAmount,
            difference.props.internalAmount,
            difference.props.amountDifference,
            difference.props.currency,
          ),
        );

        const autoResolutionResult = await this.differenceResolutionService.tryAutoResolve(difference, rules);
        if (autoResolutionResult.resolved) {
          reconciliation.props.autoResolvedCount++;
        } else {
          reconciliation.props.manualReviewCount++;
        }
      }

      reconciliation.complete();
      await this.reconciliationRepository.update(reconciliation);

      const duration = Date.now() - startTime;

      await this.auditLogRepository.save({
        reconciliationId: reconciliation.id as any,
        action: 'RECONCILIATION_COMPLETED',
        userId: reconciliation.props.createdBy,
        timestamp: new Date(),
        details: {
          duration,
          totalMatches: reconciliation.props.totalMatches,
          totalDifferences: reconciliation.props.totalDifferences,
        },
      });

      await this.eventPublisher.publishReconciliationCompleted(
        new ReconciliationCompletedEvent(
          reconciliation.id,
          reconciliation.props.tenantId,
          reconciliation.props.name,
          reconciliation.props.status as any,
          reconciliation.props.totalTransactionsProcessed,
          reconciliation.props.totalMatches,
          reconciliation.props.totalDifferences,
          reconciliation.props.autoResolvedCount,
          reconciliation.props.manualReviewCount,
          duration,
        ),
      );

      this.logger.log(`Reconciliation ${reconciliationId} completed successfully`);
    } catch (error) {
      this.logger.error(`Reconciliation ${reconciliationId} failed: ${error.message}`, error.stack);

      if (reconciliation) {
        reconciliation.fail(error.message);
        await this.reconciliationRepository.update(reconciliation);

        await this.eventPublisher.publishReconciliationFailed(
          new ReconciliationFailedEvent(
            reconciliation.id as any,
            reconciliation.tenantId,
            reconciliation.name,
            error.message,
            reconciliation.props.totalTransactionsProcessed,
            error.stack,
          ),
        );
      }
    }
  }

  async cancelReconciliation(command: CancelReconciliationCommand): Promise<void> {
    this.logger.log(`Cancelling reconciliation: ${command.reconciliationId}`);

    const reconciliation = await this.reconciliationRepository.findById(command.reconciliationId, command.tenantId);
    if (!reconciliation) {
      throw new NotFoundException('Reconciliation not found');
    }

    if (reconciliation.status !== 'RUNNING' && reconciliation.status !== 'PENDING') {
      throw new ForbiddenException('Can only cancel running or pending reconciliations');
    }

    reconciliation.props.status = 'FAILED' as any;
    reconciliation.props.errorDetails = command.reason || 'Cancelled by user';
    reconciliation.props.endDate = new Date();

    await this.reconciliationRepository.update(reconciliation);

    await this.auditLogRepository.save({
      reconciliationId: reconciliation.id,
      action: 'RECONCILIATION_CANCELLED',
      userId: command.userId,
      timestamp: new Date(),
      details: { reason: command.reason },
    });
  }

  async scheduleReconciliation(command: ScheduleReconciliationCommand): Promise<void> {
    this.logger.log(`Scheduling reconciliation: ${command.name}`);

    const reconciliation = new Reconciliation({
      tenantId: command.tenantId,
      name: command.name,
      description: command.description,
      status: 'PENDING' as any,
      startDate: new Date(),
      dataSourceType: command.dataSourceType,
      bankAccountId: command.bankAccountId,
      internalAccountId: command.internalAccountId,
      ruleIds: command.ruleIds,
      matches: [],
      differences: [],
      totalTransactionsProcessed: 0,
      totalMatches: 0,
      totalDifferences: 0,
      autoResolvedCount: 0,
      manualReviewCount: 0,
      createdBy: command.createdBy,
      scheduled: true,
      scheduleExpression: command.scheduleExpression,
      metadata: command.metadata,
      createdAt: new Date(),
      updatedAt: new Date(),
    });

    const savedReconciliation = await this.reconciliationRepository.save(reconciliation);

    const job = new CronJob(
      command.scheduleExpression,
      async () => {
        await this.startReconciliation(
          new StartReconciliationCommand(
            command.tenantId,
            command.name,
            command.dataSourceType,
            command.createdBy,
            command.ruleIds,
            command.bankAccountId,
            command.internalAccountId,
            command.description,
            new Date(),
            undefined,
            command.metadata,
          ),
        );
      },
      null,
      true,
      command.timezone || 'UTC',
    );

    const nextRunAt = job.nextDate().toJSDate();
    await this.reconciliationRepository.update(savedReconciliation);
  }

  async resolveDifference(command: ResolveDifferenceCommand): Promise<void> {
    this.logger.log(`Resolving difference: ${command.differenceId} with action: ${command.action}`);

    const difference = await this.differenceRepository.findById(command.differenceId, command.tenantId);
    if (!difference) {
      throw new NotFoundException('Difference not found');
    }

    const previousStatus = difference.status;

    switch (command.action) {
      case 'AUTO_RESOLVE':
        if (command.resolutionData?.rule) {
          difference.autoResolve(
            command.resolutionData.rule as string,
            command.resolutionData.action as string,
            command.notes || '',
          );
        }
        break;
      case 'MANUALLY_RESOLVE':
        difference.manuallyResolve(command.userId, command.notes || '');
        break;
      case 'IGNORE':
        difference.ignore();
        break;
      case 'ESCALATE':
        difference.escalate();
        break;
    }

    await this.differenceRepository.update(difference);

    await this.auditLogRepository.save({
      reconciliationId: difference.reconciliationId as any,
      action: 'DIFFERENCE_RESOLVED',
      userId: command.userId,
      timestamp: new Date(),
      details: {
        differenceId: command.differenceId,
        action: command.action,
        notes: command.notes,
      },
    });

    await this.eventPublisher.publishDifferenceResolved(
      new DifferenceResolvedEvent(
        difference.reconciliationId as any,
        command.tenantId,
        difference.id as any,
        difference.differenceType,
        previousStatus as any,
        difference.status as any,
        command.userId,
        command.notes,
        command.action === 'AUTO_RESOLVE',
      ),
    );
  }

  async bulkResolveDifferences(command: BulkResolveDifferencesCommand): Promise<void> {
    this.logger.log(`Bulk resolving ${command.differenceIds.length} differences`);

    for (const differenceId of command.differenceIds) {
      try {
        await this.resolveDifference(
          new ResolveDifferenceCommand(
            differenceId,
            command.reconciliationId,
            command.tenantId,
            command.action as any,
            command.userId,
            command.notes,
          ),
        );
      } catch (error) {
        this.logger.error(`Failed to resolve difference ${differenceId}: ${error.message}`);
      }
    }
  }

  async verifyMatch(command: VerifyMatchCommand): Promise<void> {
    this.logger.log(`Verifying match: ${command.matchId}`);

    const match = await this.matchRepository.findById(command.matchId, command.tenantId);
    if (!match) {
      throw new NotFoundException('Match not found');
    }

    match.verify(command.userId);
    if (command.notes) {
      match.addNotes(command.notes);
    }

    await this.matchRepository.update(match);

    await this.auditLogRepository.save({
      reconciliationId: match.reconciliationId as any,
      action: 'MATCH_VERIFIED',
      userId: command.userId,
      timestamp: new Date(),
      details: { matchId: command.matchId },
    });
  }

  async unverifyMatch(command: UnverifyMatchCommand): Promise<void> {
    this.logger.log(`Unverifying match: ${command.matchId}`);

    const match = await this.matchRepository.findById(command.matchId, command.tenantId);
    if (!match) {
      throw new NotFoundException('Match not found');
    }

    match.unverify();
    await this.matchRepository.update(match);

    await this.auditLogRepository.save({
      reconciliationId: match.reconciliationId as any,
      action: 'MATCH_UNVERIFIED',
      userId: command.userId,
      timestamp: new Date(),
      details: { matchId: command.matchId },
    });
  }

  async createRule(command: CreateReconciliationRuleCommand): Promise<any> {
    this.logger.log(`Creating reconciliation rule: ${command.name}`);

    const rule = new ReconciliationRule({
      tenantId: command.tenantId,
      name: command.name,
      description: command.description,
      matchType: command.matchType,
      priority: command.priority,
      enabled: true,
      conditions: command.conditions,
      actions: command.actions,
      confidenceThreshold: command.confidenceThreshold,
      createdBy: command.createdBy,
      createdAt: new Date(),
      updatedAt: new Date(),
    });

    const savedRule = await this.ruleRepository.save(rule);

    return {
      ruleId: savedRule.id as any,
      name: savedRule.props.name,
      enabled: savedRule.props.enabled,
    };
  }

  async updateRule(command: UpdateReconciliationRuleCommand): Promise<void> {
    this.logger.log(`Updating reconciliation rule: ${command.ruleId}`);

    const rule = await this.ruleRepository.findById(command.ruleId, command.tenantId);
    if (!rule) {
      throw new NotFoundException('Rule not found');
    }

    if (command.name !== undefined) {
      (rule.props as any).name = command.name;
    }
    if (command.description !== undefined) {
      (rule.props as any).description = command.description;
    }
    if (command.matchType !== undefined) {
      (rule.props as any).matchType = command.matchType;
    }
    if (command.priority !== undefined) {
      rule.updatePriority(command.priority);
    }
    if (command.conditions !== undefined) {
      (rule.props as any).conditions = command.conditions;
    }
    if (command.actions !== undefined) {
      (rule.props as any).actions = command.actions;
    }
    if (command.confidenceThreshold !== undefined) {
      (rule.props as any).confidenceThreshold = command.confidenceThreshold;
    }

    await this.ruleRepository.update(rule);
  }

  async deleteRule(command: DeleteReconciliationRuleCommand): Promise<void> {
    this.logger.log(`Deleting reconciliation rule: ${command.ruleId}`);

    const rule = await this.ruleRepository.findById(command.ruleId, command.tenantId);
    if (!rule) {
      throw new NotFoundException('Rule not found');
    }

    await this.ruleRepository.delete(command.ruleId, command.tenantId);
  }

  async enableRule(command: EnableRuleCommand): Promise<void> {
    this.logger.log(`Enabling reconciliation rule: ${command.ruleId}`);

    const rule = await this.ruleRepository.findById(command.ruleId, command.tenantId);
    if (!rule) {
      throw new NotFoundException('Rule not found');
    }

    rule.enable();
    await this.ruleRepository.update(rule);
  }

  async disableRule(command: DisableRuleCommand): Promise<void> {
    this.logger.log(`Disabling reconciliation rule: ${command.ruleId}`);

    const rule = await this.ruleRepository.findById(command.ruleId, command.tenantId);
    if (!rule) {
      throw new NotFoundException('Rule not found');
    }

    rule.disable();
    await this.ruleRepository.update(rule);
  }
}
