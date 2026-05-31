import {Injectable, Logger, NotFoundException, Inject} from '@nestjs/common';
import {
  IReconciliationQueryHandler,
  GetReconciliationQuery,
  GetReconciliationsByTenantQuery,
  GetReconciliationsByStatusQuery,
  SearchReconciliationsQuery,
  GetReconciliationStatsQuery,
  GetMatchesByReconciliationQuery,
  GetMatchByIdQuery,
  GetDifferencesByReconciliationQuery,
  GetDifferenceByIdQuery,
  GetUnresolvedDifferencesQuery,
  GetRuleByIdQuery,
  GetRulesByTenantQuery,
  GetActiveRulesQuery,
  GetReconciliationAuditTrailQuery,
  ReconciliationDto,
  MatchDto,
  DifferenceDto,
  RuleDto,
  ReconciliationStats,
  AuditLogDto,
  PaginatedResult,
} from '../../domain/ports/input/reconciliation.query';
import { IReconciliationRepository, IReconciliationRuleRepository, IReconciliationMatchRepository, ITransactionDifferenceRepository, IAuditLogRepository } from '../../domain/repositories/reconciliation-repository.interface';
import { DifferenceStatus } from '../../domain/enums/difference-status.enum';

@Injectable()
export class ReconciliationQueryService implements IReconciliationQueryHandler {
  private readonly logger = new Logger(ReconciliationQueryService.name);

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
  ) {}

  async getReconciliationById(query: GetReconciliationQuery): Promise<ReconciliationDto | null> {
    this.logger.debug(`Getting reconciliation by ID: ${query.reconciliationId}`);

    const reconciliation = await this.reconciliationRepository.findById(
      query.reconciliationId,
      query.tenantId,
    );

    if (!reconciliation) {
      return null;
    }

    return this.toReconciliationDto(reconciliation);
  }

  async getReconciliationsByTenant(query: GetReconciliationsByTenantQuery): Promise<ReconciliationDto[]> {
    this.logger.debug(`Getting reconciliations for tenant: ${query.tenantId}`);

    const reconciliations = await this.reconciliationRepository.findByTenant(query.tenantId, {
      page: query.page,
      limit: query.limit,
      sortBy: query.sortBy,
      sortOrder: query.sortOrder,
    });

    return reconciliations.map((r) => this.toReconciliationDto(r));
  }

  async getReconciliationsByStatus(query: GetReconciliationsByStatusQuery): Promise<ReconciliationDto[]> {
    this.logger.debug(`Getting reconciliations by status: ${query.status}`);

    const reconciliations = await this.reconciliationRepository.findByStatus(
      query.status,
      query.tenantId,
    );

    return reconciliations.map((r) => this.toReconciliationDto(r));
  }

  async searchReconciliations(query: SearchReconciliationsQuery): Promise<PaginatedResult<ReconciliationDto>> {
    this.logger.debug(`Searching reconciliations for tenant: ${query.tenantId}`);

    const filters = {
      searchTerm: query.searchTerm,
      status: query.status,
      dataSourceType: query.dataSourceType,
      startDate: query.startDate,
      endDate: query.endDate,
    };

    const reconciliations = await this.reconciliationRepository.search(query.tenantId, filters);
    const total = reconciliations.length;

    const start = (query.page - 1) * query.limit;
    const paginated = reconciliations.slice(start, start + query.limit);

    return {
      data: paginated.map((r) => this.toReconciliationDto(r)),
      total,
      page: query.page,
      limit: query.limit,
      totalPages: Math.ceil(total / query.limit),
    };
  }

  async getReconciliationStats(query: GetReconciliationStatsQuery): Promise<ReconciliationStats> {
    this.logger.debug(`Getting reconciliation stats for tenant: ${query.tenantId}`);

    const stats = await this.reconciliationRepository.getStats(query.tenantId, {
      type: query.period || 'all',
      startDate: query.startDate,
      endDate: query.endDate,
    });

    return stats as any;
  }

  async getMatchesByReconciliation(query: GetMatchesByReconciliationQuery): Promise<MatchDto[]> {
    this.logger.debug(`Getting matches for reconciliation: ${query.reconciliationId}`);

    const matches = await this.matchRepository.findByReconciliation(
      query.reconciliationId,
      query.tenantId,
      {
        matchType: query.matchType,
        minConfidence: query.minConfidence,
        page: query.page,
        limit: query.limit,
      },
    );

    return matches.map((m) => this.toMatchDto(m));
  }

  async getMatchById(query: GetMatchByIdQuery): Promise<MatchDto | null> {
    this.logger.debug(`Getting match by ID: ${query.matchId}`);

    const match = await this.matchRepository.findById(query.matchId, query.tenantId);

    if (!match) {
      return null;
    }

    return this.toMatchDto(match);
  }

  async getDifferencesByReconciliation(query: GetDifferencesByReconciliationQuery): Promise<DifferenceDto[]> {
    this.logger.debug(`Getting differences for reconciliation: ${query.reconciliationId}`);

    const differences = await this.differenceRepository.findByReconciliation(
      query.reconciliationId,
      query.tenantId,
      {
        status: query.status,
        differenceType: query.differenceType,
        severity: query.severity,
        page: query.page,
        limit: query.limit,
      },
    );

    return differences.map((d) => this.toDifferenceDto(d));
  }

  async getDifferenceById(query: GetDifferenceByIdQuery): Promise<DifferenceDto | null> {
    this.logger.debug(`Getting difference by ID: ${query.differenceId}`);

    const difference = await this.differenceRepository.findById(query.differenceId, query.tenantId);

    if (!difference) {
      return null;
    }

    return this.toDifferenceDto(difference);
  }

  async getUnresolvedDifferences(query: GetUnresolvedDifferencesQuery): Promise<DifferenceDto[]> {
    this.logger.debug(`Getting unresolved differences for tenant: ${query.tenantId}`);

    const differences = await this.differenceRepository.findUnresolved(query.tenantId, {
      severity: query.severity,
      overdueOnly: query.overdueOnly,
      page: query.page,
      limit: query.limit,
    });

    return differences.map((d) => this.toDifferenceDto(d));
  }

  async getRuleById(query: GetRuleByIdQuery): Promise<RuleDto | null> {
    this.logger.debug(`Getting rule by ID: ${query.ruleId}`);

    const rule = await this.ruleRepository.findById(query.ruleId, query.tenantId);

    if (!rule) {
      return null;
    }

    return this.toRuleDto(rule);
  }

  async getRulesByTenant(query: GetRulesByTenantQuery): Promise<RuleDto[]> {
    this.logger.debug(`Getting rules for tenant: ${query.tenantId}`);

    const rules = await this.ruleRepository.findByTenant(query.tenantId, {
      enabledOnly: query.enabledOnly,
      matchType: query.matchType,
    });

    return rules.map((r) => this.toRuleDto(r));
  }

  async getActiveRules(query: GetActiveRulesQuery): Promise<RuleDto[]> {
    this.logger.debug(`Getting active rules for tenant: ${query.tenantId}`);

    const rules = await this.ruleRepository.findActive(query.tenantId);

    return rules.map((r) => this.toRuleDto(r));
  }

  async getReconciliationAuditTrail(query: GetReconciliationAuditTrailQuery): Promise<AuditLogDto[]> {
    this.logger.debug(`Getting audit trail for reconciliation: ${query.reconciliationId}`);

    const logs = await this.auditLogRepository.findByReconciliation(query.reconciliationId, {
      page: query.page,
      limit: query.limit,
    });

    return logs.map((log) => ({
      id: log.id,
      reconciliationId: log.reconciliationId,
      action: log.action,
      userId: log.userId,
      timestamp: log.timestamp,
      details: log.details,
      ipAddress: log.ipAddress,
    }));
  }

  private toReconciliationDto(reconciliation: any): ReconciliationDto {
    return {
      id: reconciliation.id,
      tenantId: reconciliation.tenantId,
      name: reconciliation.name,
      description: reconciliation.props.description,
      status: reconciliation.status,
      startDate: reconciliation.props.startDate,
      endDate: reconciliation.props.endDate,
      dataSourceType: reconciliation.props.dataSourceType,
      bankAccountId: reconciliation.props.bankAccountId,
      internalAccountId: reconciliation.props.internalAccountId,
      ruleIds: reconciliation.props.ruleIds,
      totalTransactionsProcessed: reconciliation.props.totalTransactionsProcessed,
      totalMatches: reconciliation.props.totalMatches,
      totalDifferences: reconciliation.props.totalDifferences,
      autoResolvedCount: reconciliation.props.autoResolvedCount,
      manualReviewCount: reconciliation.props.manualReviewCount,
      createdBy: reconciliation.props.createdBy,
      completedBy: reconciliation.props.completedBy,
      completedAt: reconciliation.props.completedAt,
      errorDetails: reconciliation.props.errorDetails,
      scheduled: reconciliation.props.scheduled,
      scheduleExpression: reconciliation.props.scheduleExpression,
      lastRunAt: reconciliation.props.lastRunAt,
      nextRunAt: reconciliation.props.nextRunAt,
      createdAt: reconciliation.props.createdAt,
      updatedAt: reconciliation.props.updatedAt,
    };
  }

  private toMatchDto(match: any): MatchDto {
    return {
      id: match.id,
      reconciliationId: match.reconciliationId,
      tenantId: match.props.tenantId,
      bankTransactionId: match.props.bankTransactionId,
      internalTransactionId: match.props.internalTransactionId,
      matchType: match.props.matchType,
      confidence: match.props.confidence,
      matchDate: match.props.matchDate,
      matchedBy: match.props.matchedBy,
      verified: match.props.verified,
      verifiedBy: match.props.verifiedBy,
      verifiedAt: match.props.verifiedAt,
      notes: match.props.notes,
      bankTransaction: match.props.bankTransaction,
      internalTransaction: match.props.internalTransaction,
    };
  }

  private toDifferenceDto(difference: any): DifferenceDto {
    return {
      id: difference.id,
      reconciliationId: difference.reconciliationId,
      tenantId: difference.props.tenantId,
      bankTransactionId: difference.props.bankTransactionId,
      internalTransactionId: difference.props.internalTransactionId,
      differenceType: difference.differenceType,
      status: difference.status as DifferenceStatus,
      bankAmount: difference.props.bankAmount,
      internalAmount: difference.props.internalAmount,
      amountDifference: difference.props.amountDifference,
      currency: difference.props.currency,
      bankDate: difference.props.bankDate,
      internalDate: difference.props.internalDate,
      description: difference.props.description,
      detectedAt: difference.props.detectedAt,
      resolvedAt: difference.props.resolvedAt,
      resolvedBy: difference.props.resolvedBy,
      resolutionNotes: difference.props.resolutionNotes,
      autoResolution: difference.props.autoResolution,
      severity: difference.props.severity,
      assignee: difference.props.assignee,
      dueDate: difference.props.dueDate,
      isOverdue: difference.isOverdue(),
    };
  }

  private toRuleDto(rule: any): RuleDto {
    return {
      id: rule.id,
      tenantId: rule.tenantId,
      name: rule.props.name,
      description: rule.props.description,
      matchType: rule.props.matchType,
      priority: rule.props.priority,
      enabled: rule.props.enabled,
      conditions: rule.props.conditions,
      actions: rule.props.actions,
      confidenceThreshold: rule.props.confidenceThreshold,
      createdBy: rule.props.createdBy,
      createdAt: rule.props.createdAt,
      updatedAt: rule.props.updatedAt,
    };
  }
}
