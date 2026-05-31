import { Reconciliation, ReconciliationProps } from '../../../domain/models/reconciliation.entity';
import { ReconciliationStatus } from '../../../domain/enums/reconciliation-status.enum';
import { ReconciliationMatch } from '../../../domain/models/reconciliation-match.entity';
import { TransactionDifference } from '../../../domain/models/transaction-difference.entity';
import { MatchType } from '../../../domain/enums/match-type.enum';

describe('Reconciliation Entity', () => {
  let props: ReconciliationProps;
  let reconciliation: Reconciliation;

  beforeEach(() => {
    props = {
      tenantId: 'tenant-001',
      name: 'Daily Bank Reconciliation',
      description: 'Match bank transactions with internal records',
      status: ReconciliationStatus.PENDING,
      startDate: new Date('2024-01-15'),
      dataSourceType: 'BOTH',
      bankAccountId: 'bank-acc-001',
      internalAccountId: 'internal-acc-001',
      ruleIds: ['rule-001', 'rule-002'],
      matches: [],
      differences: [],
      totalTransactionsProcessed: 0,
      totalMatches: 0,
      totalDifferences: 0,
      autoResolvedCount: 0,
      manualReviewCount: 0,
      createdBy: 'user-001',
      createdAt: new Date('2024-01-15'),
      updatedAt: new Date('2024-01-15')
    };

    reconciliation = new Reconciliation(props);
  });

  describe('Creation', () => {
    it('should create reconciliation with valid props', () => {
      expect(reconciliation.id).toBeDefined();
      expect(reconciliation.tenantId).toBe('tenant-001');
      expect(reconciliation.name).toBe('Daily Bank Reconciliation');
      expect(reconciliation.status).toBe(ReconciliationStatus.PENDING);
    });

    it('should generate UUID if not provided', () => {
      const reconWithoutId = new Reconciliation({ ...props, id: undefined as any });

      expect(reconWithoutId.id).toBeDefined();
      expect(reconWithoutId.id.length).toBeGreaterThan(0);
    });

    it('should initialize with zero counts', () => {
      expect(reconciliation.props.totalTransactionsProcessed).toBe(0);
      expect(reconciliation.props.totalMatches).toBe(0);
      expect(reconciliation.props.totalDifferences).toBe(0);
      expect(reconciliation.props.autoResolvedCount).toBe(0);
      expect(reconciliation.props.manualReviewCount).toBe(0);
    });

    it('should initialize with empty arrays', () => {
      expect(reconciliation.props.matches).toEqual([]);
      expect(reconciliation.props.differences).toEqual([]);
    });

    it('should set default dates if not provided', () => {
      const reconWithoutDates = new Reconciliation({
        ...props,
        createdAt: undefined as any,
        updatedAt: undefined as any
      });

      expect(reconWithoutDates.props.createdAt).toBeInstanceOf(Date);
      expect(reconWithoutDates.props.updatedAt).toBeInstanceOf(Date);
    });
  });

  describe('Starting Reconciliation', () => {
    it('should start reconciliation from PENDING status', () => {
      reconciliation.start();

      expect(reconciliation.status).toBe(ReconciliationStatus.RUNNING);
      expect(reconciliation.props.startDate).toBeInstanceOf(Date);
    });

    it('should throw error when starting from non-PENDING status', () => {
      reconciliation.props.status = ReconciliationStatus.RUNNING;

      expect(() => {
        reconciliation.start();
      }).toThrow('Reconciliation can only be started from PENDING status');
    });

    it('should update timestamp on start', () => {
      const oldUpdatedAt = reconciliation.props.updatedAt;
      reconciliation.start();

      expect(reconciliation.props.updatedAt.getTime()).toBeGreaterThanOrEqual(oldUpdatedAt.getTime());
    });
  });

  describe('Completing Reconciliation', () => {
    beforeEach(() => {
      reconciliation.props.status = ReconciliationStatus.RUNNING;
    });

    it('should complete to COMPLETED status when no differences', () => {
      reconciliation.complete();

      expect(reconciliation.status).toBe(ReconciliationStatus.COMPLETED);
      expect(reconciliation.props.endDate).toBeInstanceOf(Date);
      expect(reconciliation.props.completedAt).toBeInstanceOf(Date);
    });

    it('should complete to PARTIAL status when there are differences', () => {
      reconciliation.props.totalDifferences = 5;

      reconciliation.complete();

      expect(reconciliation.status).toBe(ReconciliationStatus.PARTIAL);
    });

    it('should throw error when completing from non-RUNNING status', () => {
      reconciliation.props.status = ReconciliationStatus.PENDING;

      expect(() => {
        reconciliation.complete();
      }).toThrow('Reconciliation can only be completed from RUNNING status');
    });

    it('should set end date and completed at date', () => {
      reconciliation.complete();

      expect(reconciliation.props.endDate).toBeInstanceOf(Date);
      expect(reconciliation.props.completedAt).toBeInstanceOf(Date);
    });
  });

  describe('Failing Reconciliation', () => {
    it('should fail reconciliation with error message', () => {
      const errorMessage = 'Database connection failed';

      reconciliation.fail(errorMessage);

      expect(reconciliation.status).toBe(ReconciliationStatus.FAILED);
      expect(reconciliation.props.errorDetails).toBe(errorMessage);
      expect(reconciliation.props.endDate).toBeInstanceOf(Date);
    });

    it('should update timestamp on failure', () => {
      const oldUpdatedAt = reconciliation.props.updatedAt;
      reconciliation.fail('Error');

      expect(reconciliation.props.updatedAt.getTime()).toBeGreaterThanOrEqual(oldUpdatedAt.getTime());
    });
  });

  describe('Adding Matches', () => {
    it('should add match to reconciliation', () => {
      const match = new ReconciliationMatch({
        reconciliationId: reconciliation.id,
        tenantId: 'tenant-001',
        matchType: MatchType.EXACT,
        confidence: 0.95,
        matchDate: new Date(),
        matchedBy: 'system',
        createdAt: new Date()
      });

      reconciliation.addMatch(match);

      expect(reconciliation.props.matches).toHaveLength(1);
      expect(reconciliation.props.totalMatches).toBe(1);
      expect(reconciliation.props.matches[0]).toEqual(match);
    });

    it('should increment total matches count', () => {
      const match = new ReconciliationMatch({
        reconciliationId: reconciliation.id,
        tenantId: 'tenant-001',
        matchType: MatchType.EXACT,
        confidence: 0.9,
        matchDate: new Date(),
        matchedBy: 'system',
        createdAt: new Date()
      });

      reconciliation.addMatch(match);
      reconciliation.addMatch(match);

      expect(reconciliation.props.totalMatches).toBe(2);
    });

    it('should update timestamp when adding match', () => {
      const match = new ReconciliationMatch({
        reconciliationId: reconciliation.id,
        tenantId: 'tenant-001',
        matchType: MatchType.EXACT,
        confidence: 0.9,
        matchDate: new Date(),
        matchedBy: 'system',
        createdAt: new Date()
      });
      const oldUpdatedAt = reconciliation.props.updatedAt;

      reconciliation.addMatch(match);

      expect(reconciliation.props.updatedAt.getTime()).toBeGreaterThanOrEqual(oldUpdatedAt.getTime());
    });
  });

  describe('Adding Differences', () => {
    it('should add difference to reconciliation', () => {
      const difference = new TransactionDifference({
        reconciliationId: reconciliation.id,
        tenantId: 'tenant-001',
        bankTransactionId: 'bank-txn-001',
        internalTransactionId: 'internal-txn-001',
        differenceType: 'AMOUNT_MISMATCH',
        status: 'OPEN' as any,
        bankAmount: 100,
        internalAmount: 95,
        amountDifference: 5,
        severity: 'MEDIUM',
        detectedAt: new Date(),
        createdAt: new Date()
      });

      reconciliation.addDifference(difference);

      expect(reconciliation.props.differences).toHaveLength(1);
      expect(reconciliation.props.totalDifferences).toBe(1);
      expect(reconciliation.props.differences[0]).toEqual(difference);
    });

    it('should increment total differences count', () => {
      const difference = new TransactionDifference({
        reconciliationId: reconciliation.id,
        tenantId: 'tenant-001',
        bankTransactionId: 'bank-txn-001',
        internalTransactionId: 'internal-txn-001',
        differenceType: 'AMOUNT_MISMATCH',
        status: 'OPEN' as any,
        bankAmount: 100,
        internalAmount: 95,
        amountDifference: 5,
        severity: 'MEDIUM',
        detectedAt: new Date(),
        createdAt: new Date()
      });

      reconciliation.addDifference(difference);
      reconciliation.addDifference(difference);

      expect(reconciliation.props.totalDifferences).toBe(2);
    });

    it('should update timestamp when adding difference', () => {
      const difference = new TransactionDifference({
        reconciliationId: reconciliation.id,
        tenantId: 'tenant-001',
        bankTransactionId: 'bank-txn-001',
        internalTransactionId: 'internal-txn-001',
        differenceType: 'AMOUNT_MISMATCH',
        status: 'OPEN' as any,
        bankAmount: 100,
        internalAmount: 95,
        amountDifference: 5,
        severity: 'MEDIUM',
        detectedAt: new Date(),
        createdAt: new Date()
      });
      const oldUpdatedAt = reconciliation.props.updatedAt;

      reconciliation.addDifference(difference);

      expect(reconciliation.props.updatedAt.getTime()).toBeGreaterThanOrEqual(oldUpdatedAt.getTime());
    });
  });

  describe('Processing Count', () => {
    it('should increment processed count by default', () => {
      reconciliation.incrementProcessedCount();

      expect(reconciliation.props.totalTransactionsProcessed).toBe(1);
    });

    it('should increment processed count by specified amount', () => {
      reconciliation.incrementProcessedCount(10);

      expect(reconciliation.props.totalTransactionsProcessed).toBe(10);
    });

    it('should accumulate processed count', () => {
      reconciliation.incrementProcessedCount(5);
      reconciliation.incrementProcessedCount(3);

      expect(reconciliation.props.totalTransactionsProcessed).toBe(8);
    });

    it('should update timestamp when incrementing count', () => {
      const oldUpdatedAt = reconciliation.props.updatedAt;

      reconciliation.incrementProcessedCount();

      expect(reconciliation.props.updatedAt.getTime()).toBeGreaterThanOrEqual(oldUpdatedAt.getTime());
    });
  });

  describe('Schedule Management', () => {
    it('should update schedule with expression and next run date', () => {
      const expression = '0 0 * * *';
      const nextRun = new Date('2024-12-31T00:00:00Z');

      reconciliation.updateSchedule(expression, nextRun);

      expect(reconciliation.props.scheduleExpression).toBe(expression);
      expect(reconciliation.props.nextRunAt).toEqual(nextRun);
      expect(reconciliation.props.scheduled).toBe(true);
    });

    it('should update timestamp when updating schedule', () => {
      const oldUpdatedAt = reconciliation.props.updatedAt;

      reconciliation.updateSchedule('0 0 * * *', new Date());

      expect(reconciliation.props.updatedAt.getTime()).toBeGreaterThanOrEqual(oldUpdatedAt.getTime());
    });
  });

  describe('Data Source Types', () => {
    it('should support BANK data source type', () => {
      const bankRecon = new Reconciliation({
        ...props,
        dataSourceType: 'BANK',
        internalAccountId: undefined
      });

      expect(bankRecon.props.dataSourceType).toBe('BANK');
      expect(bankRecon.props.bankAccountId).toBeDefined();
    });

    it('should support INTERNAL data source type', () => {
      const internalRecon = new Reconciliation({
        ...props,
        dataSourceType: 'INTERNAL',
        bankAccountId: undefined
      });

      expect(internalRecon.props.dataSourceType).toBe('INTERNAL');
      expect(internalRecon.props.internalAccountId).toBeDefined();
    });

    it('should support BOTH data source type', () => {
      expect(reconciliation.props.dataSourceType).toBe('BOTH');
      expect(reconciliation.props.bankAccountId).toBeDefined();
      expect(reconciliation.props.internalAccountId).toBeDefined();
    });
  });

  describe('Serialization', () => {
    it('should convert to JSON', () => {
      const json = reconciliation.toJSON();

      expect(json.id).toBeDefined();
      expect(json.tenantId).toBe('tenant-001');
      expect(json.name).toBe('Daily Bank Reconciliation');
      expect(json.status).toBe(ReconciliationStatus.PENDING);
      expect(json.matches).toEqual([]);
      expect(json.differences).toEqual([]);
    });

    it('should include all properties in JSON', () => {
      reconciliation.props.status = ReconciliationStatus.RUNNING;
      const match = new ReconciliationMatch({
        reconciliationId: reconciliation.id,
        tenantId: 'tenant-001',
        matchType: MatchType.EXACT,
        confidence: 0.9,
        matchDate: new Date(),
        matchedBy: 'system',
        createdAt: new Date()
      });
      const difference = new TransactionDifference({
        reconciliationId: reconciliation.id,
        tenantId: 'tenant-001',
        bankTransactionId: 'bank-txn-001',
        internalTransactionId: 'internal-txn-001',
        differenceType: 'AMOUNT_MISMATCH',
        status: 'OPEN' as any,
        bankAmount: 100,
        internalAmount: 95,
        amountDifference: 5,
        severity: 'MEDIUM',
        detectedAt: new Date(),
        createdAt: new Date()
      });

      reconciliation.addMatch(match);
      reconciliation.addDifference(difference);

      const json = reconciliation.toJSON();

      expect(json).toHaveProperty('id');
      expect(json).toHaveProperty('tenantId');
      expect(json).toHaveProperty('name');
      expect(json).toHaveProperty('status');
      expect(json).toHaveProperty('matches');
      expect(json).toHaveProperty('differences');
      expect(json).toHaveProperty('totalMatches');
      expect(json).toHaveProperty('totalDifferences');
    });
  });

  describe('Getters', () => {
    it('should get id', () => {
      expect(reconciliation.id).toBeDefined();
    });

    it('should get tenantId', () => {
      expect(reconciliation.tenantId).toBe('tenant-001');
    });

    it('should get status', () => {
      expect(reconciliation.status).toBe(ReconciliationStatus.PENDING);
    });

    it('should get name', () => {
      expect(reconciliation.name).toBe('Daily Bank Reconciliation');
    });
  });

  describe('Status Transitions', () => {
    it('should support PENDING -> RUNNING transition', () => {
      reconciliation.start();

      expect(reconciliation.status).toBe(ReconciliationStatus.RUNNING);
    });

    it('should support RUNNING -> COMPLETED transition', () => {
      reconciliation.props.status = ReconciliationStatus.RUNNING;
      reconciliation.complete();

      expect(reconciliation.status).toBe(ReconciliationStatus.COMPLETED);
    });

    it('should support RUNNING -> PARTIAL transition', () => {
      reconciliation.props.status = ReconciliationStatus.RUNNING;
      reconciliation.props.totalDifferences = 1;
      reconciliation.complete();

      expect(reconciliation.status).toBe(ReconciliationStatus.PARTIAL);
    });

    it('should support any -> FAILED transition', () => {
      reconciliation.fail('Critical error');

      expect(reconciliation.status).toBe(ReconciliationStatus.FAILED);
    });
  });

  describe('Edge Cases', () => {
    it('should handle adding multiple matches', () => {
      const match1 = new ReconciliationMatch({
        reconciliationId: reconciliation.id,
        tenantId: 'tenant-001',
        matchType: MatchType.EXACT,
        confidence: 0.9,
        matchDate: new Date(),
        matchedBy: 'system',
        createdAt: new Date()
      });
      const match2 = new ReconciliationMatch({
        reconciliationId: reconciliation.id,
        tenantId: 'tenant-001',
        matchType: MatchType.EXACT,
        confidence: 0.9,
        matchDate: new Date(),
        matchedBy: 'system',
        createdAt: new Date()
      });
      const match3 = new ReconciliationMatch({
        reconciliationId: reconciliation.id,
        tenantId: 'tenant-001',
        matchType: MatchType.EXACT,
        confidence: 0.9,
        matchDate: new Date(),
        matchedBy: 'system',
        createdAt: new Date()
      });

      reconciliation.addMatch(match1);
      reconciliation.addMatch(match2);
      reconciliation.addMatch(match3);

      expect(reconciliation.props.matches).toHaveLength(3);
      expect(reconciliation.props.totalMatches).toBe(3);
    });

    it('should handle adding multiple differences', () => {
      const diff1 = new TransactionDifference({
        reconciliationId: reconciliation.id,
        tenantId: 'tenant-001',
        bankTransactionId: 'bank-txn-001',
        internalTransactionId: 'internal-txn-001',
        differenceType: 'AMOUNT_MISMATCH',
        status: 'OPEN' as any,
        bankAmount: 100,
        internalAmount: 95,
        amountDifference: 5,
        severity: 'MEDIUM',
        detectedAt: new Date(),
        createdAt: new Date()
      });
      const diff2 = new TransactionDifference({
        reconciliationId: reconciliation.id,
        tenantId: 'tenant-001',
        bankTransactionId: 'bank-txn-002',
        internalTransactionId: 'internal-txn-002',
        differenceType: 'AMOUNT_MISMATCH',
        status: 'OPEN' as any,
        bankAmount: 200,
        internalAmount: 190,
        amountDifference: 10,
        severity: 'MEDIUM',
        detectedAt: new Date(),
        createdAt: new Date()
      });

      reconciliation.addDifference(diff1);
      reconciliation.addDifference(diff2);

      expect(reconciliation.props.differences).toHaveLength(2);
      expect(reconciliation.props.totalDifferences).toBe(2);
    });

    it('should handle processing multiple transactions', () => {
      reconciliation.incrementProcessedCount(100);

      expect(reconciliation.props.totalTransactionsProcessed).toBe(100);
    });
  });

  describe('Metadata', () => {
    it('should include metadata in props', () => {
      const reconWithMetadata = new Reconciliation({
        ...props,
        metadata: { key1: 'value1', key2: 'value2' }
      });

      expect(reconWithMetadata.props.metadata).toEqual({
        key1: 'value1',
        key2: 'value2'
      });
    });

    it('should handle undefined metadata', () => {
      expect(reconciliation.props.metadata).toBeUndefined();
    });
  });

  describe('Description', () => {
    it('should include description', () => {
      expect(reconciliation.props.description).toBe('Match bank transactions with internal records');
    });

    it('should handle undefined description', () => {
      const reconWithoutDesc = new Reconciliation({
        ...props,
        description: undefined
      });

      expect(reconWithoutDesc.props.description).toBeUndefined();
    });
  });

  describe('Rule Management', () => {
    it('should store rule IDs', () => {
      expect(reconciliation.props.ruleIds).toEqual(['rule-001', 'rule-002']);
    });

    it('should handle empty rule IDs array', () => {
      const reconWithoutRules = new Reconciliation({
        ...props,
        ruleIds: []
      });

      expect(reconWithoutRules.props.ruleIds).toEqual([]);
    });
  });

  describe('User Tracking', () => {
    it('should track created by user', () => {
      expect(reconciliation.props.createdBy).toBe('user-001');
    });

    it('should track completed by user', () => {
      reconciliation.props.status = ReconciliationStatus.RUNNING;
      reconciliation.props.completedBy = 'user-002';

      reconciliation.complete();

      expect(reconciliation.props.completedBy).toBe('user-002');
    });
  });

  describe('Scheduling', () => {
    it('should handle scheduled reconciliation', () => {
      reconciliation.updateSchedule('0 0 * * *', new Date('2024-12-31'));

      expect(reconciliation.props.scheduled).toBe(true);
      expect(reconciliation.props.scheduleExpression).toBe('0 0 * * *');
      expect(reconciliation.props.nextRunAt).toEqual(new Date('2024-12-31'));
    });

    it('should handle non-scheduled reconciliation', () => {
      expect(reconciliation.props.scheduled).toBeUndefined();
      expect(reconciliation.props.scheduleExpression).toBeUndefined();
      expect(reconciliation.props.nextRunAt).toBeUndefined();
    });
  });

  describe('Account Management', () => {
    it('should handle bank account ID', () => {
      expect(reconciliation.props.bankAccountId).toBe('bank-acc-001');
    });

    it('should handle internal account ID', () => {
      expect(reconciliation.props.internalAccountId).toBe('internal-acc-001');
    });

    it('should work with only bank account', () => {
      const bankOnlyRecon = new Reconciliation({
        ...props,
        dataSourceType: 'BANK',
        internalAccountId: undefined
      });

      expect(bankOnlyRecon.props.bankAccountId).toBeDefined();
      expect(bankOnlyRecon.props.internalAccountId).toBeUndefined();
    });
  });
});
