import { Injectable, Logger } from '@nestjs/common';
import Fuse from 'fuse.js';
import { ReconciliationMatch, ReconciliationMatchProps } from '../../domain/models/reconciliation-match.entity';
import { TransactionDifference, TransactionDifferenceProps, DifferenceType } from '../../domain/models/transaction-difference.entity';
import { ReconciliationRule } from '../../domain/models/reconciliation-rule.entity';
import { MatchType } from '../../domain/enums/match-type.enum';
import { TransactionData } from '../../domain/ports/output/data-source.interface';

interface MatchResult {
  matches: ReconciliationMatch[];
  differences: TransactionDifference[];
}

interface MatchCandidate {
  bankTransaction: TransactionData;
  internalTransaction: TransactionData;
  score: number;
  matchType: MatchType;
  matchingRules: string[];
}

@Injectable()
export class AutoMatchService {
  private readonly logger = new Logger(AutoMatchService.name);

  async findMatches(
    bankTransactions: TransactionData[],
    internalTransactions: TransactionData[],
    rules: ReconciliationRule[],
    reconciliationId: string,
    tenantId: string,
  ): Promise<MatchResult> {
    this.logger.log(`Finding matches between ${bankTransactions.length} bank and ${internalTransactions.length} internal transactions`);

    const matches: ReconciliationMatch[] = [];
    const differences: TransactionDifference[] = [];
    const processedBankIds = new Set<string>();
    const processedInternalIds = new Set<string>();

    const sortedRules = [...rules].sort((a, b) => b.priority - a.priority);

    for (const bankTx of bankTransactions) {
      const candidate = this.findBestMatch(bankTx, internalTransactions.filter((tx) => !processedInternalIds.has(tx.id)), sortedRules);

      if (candidate && candidate.score >= 0.7) {
        const match = this.createMatch(candidate, reconciliationId, tenantId);
        matches.push(match);
        processedBankIds.add(bankTx.id);
        processedInternalIds.add(candidate.internalTransaction.id);
      } else if (candidate && candidate.score >= 0.5) {
        const difference = this.createDifference(bankTx, candidate.internalTransaction, candidate, reconciliationId, tenantId);
        differences.push(difference);
        processedBankIds.add(bankTx.id);
        processedInternalIds.add(candidate.internalTransaction.id);
      } else {
        const difference = this.createMissingDifference(bankTx, 'MISSING_INTERNAL_TRANSACTION', reconciliationId, tenantId);
        differences.push(difference);
        processedBankIds.add(bankTx.id);
      }
    }

    for (const internalTx of internalTransactions) {
      if (!processedInternalIds.has(internalTx.id)) {
        const difference = this.createMissingDifference(internalTx, 'MISSING_BANK_TRANSACTION', reconciliationId, tenantId);
        differences.push(difference);
      }
    }

    this.logger.log(`Found ${matches.length} matches and ${differences.length} differences`);

    return { matches, differences };
  }

  private findBestMatch(
    bankTx: TransactionData,
    internalTxs: TransactionData[],
    rules: ReconciliationRule[],
  ): MatchCandidate | null {
    let bestCandidate: MatchCandidate | null = null;

    for (const internalTx of internalTxs) {
      const score = this.calculateMatchScore(bankTx, internalTx, rules);
      if (score.score >= 0.5 && (!bestCandidate || score.score > bestCandidate.score)) {
        bestCandidate = {
          bankTransaction: bankTx,
          internalTransaction: internalTx,
          score: score.score,
          matchType: score.matchType,
          matchingRules: score.matchingRules,
        };
      }
    }

    return bestCandidate;
  }

  private calculateMatchScore(
    bankTx: TransactionData,
    internalTx: TransactionData,
    rules: ReconciliationRule[],
  ): { score: number; matchType: MatchType; matchingRules: string[] } {
    let totalScore = 0;
    let weightSum = 0;
    const matchingRules: string[] = [];

    const exactMatch = this.checkExactMatch(bankTx, internalTx);
    if (exactMatch) {
      return { score: 1.0, matchType: MatchType.EXACT, matchingRules: ['exact_match'] };
    }

    const amountScore = this.compareAmounts(bankTx.amount, internalTx.amount);
    const dateScore = this.compareDates(bankTx.date, internalTx.date);
    const referenceScore = this.compareReferences(bankTx.reference, internalTx.reference);
    const descriptionScore = this.compareDescriptions(bankTx.description, internalTx.description);
    const counterpartyScore = this.compareCounterparties(bankTx.counterparty, internalTx.counterparty);

    const weights = {
      amount: 0.4,
      date: 0.2,
      reference: 0.2,
      description: 0.1,
      counterparty: 0.1,
    };

    totalScore =
      amountScore * weights.amount +
      dateScore * weights.date +
      referenceScore * weights.reference +
      descriptionScore * weights.description +
      counterpartyScore * weights.counterparty;

    weightSum = Object.values(weights).reduce((sum, w) => sum + w, 0);

    const finalScore = totalScore / weightSum;

    for (const rule of rules) {
      const evaluation = rule.evaluate({
        bank: bankTx,
        internal: internalTx,
        amountDiff: Math.abs(bankTx.amount - internalTx.amount),
        dateDiff: Math.abs(bankTx.date.getTime() - internalTx.date.getTime()),
      });

      if (evaluation.matched) {
        totalScore += evaluation.confidence * 0.1;
        matchingRules.push(rule.props.name);
      }
    }

    let matchType = MatchType.FUZZY;
    if (finalScore >= 0.95) {
      matchType = MatchType.EXACT;
    } else if (finalScore >= 0.85 && matchingRules.length > 0) {
      matchType = MatchType.AI_BASED;
    }

    return { score: finalScore, matchType, matchingRules };
  }

  private checkExactMatch(bankTx: TransactionData, internalTx: TransactionData): boolean {
    return (
      bankTx.amount === internalTx.amount &&
      bankTx.currency === internalTx.currency &&
      bankTx.date.toISOString().split('T')[0] === internalTx.date.toISOString().split('T')[0] &&
      bankTx.reference === internalTx.reference &&
      bankTx.type === internalTx.type
    );
  }

  private compareAmounts(bankAmount: number, internalAmount: number): number {
    if (bankAmount === internalAmount) return 1;
    const diff = Math.abs(bankAmount - internalAmount);
    const maxAmount = Math.max(Math.abs(bankAmount), Math.abs(internalAmount));
    if (maxAmount === 0) return 1;
    const ratio = diff / maxAmount;
    return Math.max(0, 1 - ratio * 10);
  }

  private compareDates(bankDate: Date, internalDate: Date): number {
    const diffInMs = Math.abs(bankDate.getTime() - internalDate.getTime());
    const diffInDays = diffInMs / (1000 * 60 * 60 * 24);
    if (diffInDays === 0) return 1;
    if (diffInDays <= 1) return 0.9;
    if (diffInDays <= 3) return 0.7;
    if (diffInDays <= 7) return 0.5;
    return 0.2;
  }

  private compareReferences(bankRef: string | undefined, internalRef: string | undefined): number {
    if (!bankRef || !internalRef) return 0;
    if (bankRef === internalRef) return 1;
    const fuse = new Fuse([internalRef], {
      includeScore: true,
      threshold: 0.3,
    });
    const result = fuse.search(bankRef);
    return result.length > 0 ? 1 - (result[0].score || 0) : 0;
  }

  private compareDescriptions(bankDesc: string | undefined, internalDesc: string | undefined): number {
    if (!bankDesc || !internalDesc) return 0;
    if (bankDesc === internalDesc) return 1;
    const fuse = new Fuse([internalDesc], {
      includeScore: true,
      threshold: 0.4,
    });
    const result = fuse.search(bankDesc);
    return result.length > 0 ? 1 - (result[0].score || 0) : 0;
  }

  private compareCounterparties(bankCounterparty: string | undefined, internalCounterparty: string | undefined): number {
    if (!bankCounterparty || !internalCounterparty) return 0;
    if (bankCounterparty === internalCounterparty) return 1;
    const fuse = new Fuse([internalCounterparty], {
      includeScore: true,
      threshold: 0.3,
    });
    const result = fuse.search(bankCounterparty);
    return result.length > 0 ? 1 - (result[0].score || 0) : 0;
  }

  private createMatch(candidate: MatchCandidate, reconciliationId: string, tenantId: string): ReconciliationMatch {
    const props: ReconciliationMatchProps = {
      reconciliationId: reconciliationId as any,
      tenantId,
      bankTransactionId: candidate.bankTransaction.id,
      internalTransactionId: candidate.internalTransaction.id,
      matchType: candidate.matchType,
      confidence: candidate.score,
      matchDate: new Date(),
      matchedBy: 'SYSTEM',
      bankTransaction: {
        id: candidate.bankTransaction.id,
        amount: candidate.bankTransaction.amount,
        currency: candidate.bankTransaction.currency,
        date: candidate.bankTransaction.date,
        reference: candidate.bankTransaction.reference,
        description: candidate.bankTransaction.description,
        counterparty: candidate.bankTransaction.counterparty,
      },
      internalTransaction: {
        id: candidate.internalTransaction.id,
        amount: candidate.internalTransaction.amount,
        currency: candidate.internalTransaction.currency,
        date: candidate.internalTransaction.date,
        reference: candidate.internalTransaction.reference,
        description: candidate.internalTransaction.description,
        counterparty: candidate.internalTransaction.counterparty,
      },
      createdAt: new Date(),
    };

    return new ReconciliationMatch(props);
  }

  private createDifference(
    bankTx: TransactionData,
    internalTx: TransactionData,
    candidate: MatchCandidate,
    reconciliationId: string,
    tenantId: string,
  ): TransactionDifference {
    const amountDiff = bankTx.amount - internalTx.amount;
    let differenceType: DifferenceType = 'AMOUNT_MISMATCH';

    if (bankTx.currency !== internalTx.currency) {
      differenceType = 'CURRENCY_MISMATCH';
    } else if (Math.abs(amountDiff) > 0.01) {
      differenceType = 'AMOUNT_MISMATCH';
    } else if (bankTx.counterparty !== internalTx.counterparty) {
      differenceType = 'COUNTERPARTY_MISMATCH';
    } else if (bankTx.reference !== internalTx.reference) {
      differenceType = 'REFERENCE_MISMATCH';
    }

    const props: TransactionDifferenceProps = {
      reconciliationId: reconciliationId as any,
      tenantId,
      bankTransactionId: bankTx.id,
      internalTransactionId: internalTx.id,
      differenceType,
      status: 'PENDING_REVIEW' as any,
      bankAmount: bankTx.amount,
      internalAmount: internalTx.amount,
      amountDifference: amountDiff,
      currency: bankTx.currency,
      bankDate: bankTx.date,
      internalDate: internalTx.date,
      description: `Low confidence match (${(candidate.score * 100).toFixed(1)}%) requires review`,
      detectedAt: new Date(),
      severity: 'MEDIUM' as any,
      bankTransaction: {
        id: bankTx.id,
        amount: bankTx.amount,
        currency: bankTx.currency,
        date: bankTx.date,
        reference: bankTx.reference,
        description: bankTx.description,
        counterparty: bankTx.counterparty,
      },
      internalTransaction: {
        id: internalTx.id,
        amount: internalTx.amount,
        currency: internalTx.currency,
        date: internalTx.date,
        reference: internalTx.reference,
        description: internalTx.description,
        counterparty: internalTx.counterparty,
      },
      createdAt: new Date(),
    };

    return new TransactionDifference(props);
  }

  private createMissingDifference(
    tx: TransactionData,
    differenceType: 'MISSING_BANK_TRANSACTION' | 'MISSING_INTERNAL_TRANSACTION',
    reconciliationId: string,
    tenantId: string,
  ): TransactionDifference {
    const isBank = differenceType === 'MISSING_BANK_TRANSACTION';

    const props: TransactionDifferenceProps = {
      reconciliationId: reconciliationId as any,
      tenantId,
      bankTransactionId: isBank ? undefined : tx.id,
      internalTransactionId: isBank ? tx.id : undefined,
      differenceType,
      status: 'PENDING_REVIEW' as any,
      bankAmount: isBank ? undefined : tx.amount,
      internalAmount: isBank ? tx.amount : undefined,
      amountDifference: isBank ? undefined : tx.amount,
      currency: tx.currency,
      bankDate: isBank ? undefined : tx.date,
      internalDate: isBank ? tx.date : undefined,
      description: `${isBank ? 'Internal' : 'Bank'} transaction has no matching ${isBank ? 'bank' : 'internal'} record`,
      detectedAt: new Date(),
      severity: 'HIGH' as any,
      ...(isBank
        ? {
            internalTransaction: {
              id: tx.id,
              amount: tx.amount,
              currency: tx.currency,
              date: tx.date,
              reference: tx.reference,
              description: tx.description,
              counterparty: tx.counterparty,
            },
          }
        : {
            bankTransaction: {
              id: tx.id,
              amount: tx.amount,
              currency: tx.currency,
              date: tx.date,
              reference: tx.reference,
              description: tx.description,
              counterparty: tx.counterparty,
            },
          }),
      createdAt: new Date(),
    };

    return new TransactionDifference(props);
  }
}
