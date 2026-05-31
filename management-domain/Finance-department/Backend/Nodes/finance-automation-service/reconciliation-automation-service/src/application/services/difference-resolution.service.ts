import { Injectable, Logger } from '@nestjs/common';
import { TransactionDifference } from '../../domain/models/transaction-difference.entity';
import { ReconciliationRule } from '../../domain/models/reconciliation-rule.entity';
import { DifferenceStatus } from '../../domain/enums/difference-status.enum';

interface ResolutionResult {
  resolved: boolean;
  action?: string;
  notes?: string;
}

@Injectable()
export class DifferenceResolutionService {
  private readonly logger = new Logger(DifferenceResolutionService.name);

  async tryAutoResolve(
    difference: TransactionDifference,
    rules: ReconciliationRule[],
  ): Promise<ResolutionResult> {
    this.logger.debug(`Attempting auto-resolution for difference: ${difference.id}`);

    if (difference.status !== DifferenceStatus.PENDING_REVIEW) {
      return { resolved: false };
    }

    const autoResolveRules = rules.filter(
      (rule) => rule.enabled && rule.props.actions.some((a) => a.type === 'AUTO_RESOLVE'),
    );

    for (const rule of autoResolveRules) {
      const evaluation = rule.evaluate({
        differenceType: difference.differenceType,
        amountDifference: difference.props.amountDifference,
        bankAmount: difference.props.bankAmount,
        internalAmount: difference.props.internalAmount,
        severity: difference.props.severity,
      });

      if (evaluation.matched && evaluation.confidence >= (rule.confidenceThreshold || 0.9)) {
        const autoResolveAction = rule.props.actions.find((a) => a.type === 'AUTO_RESOLVE');
        const notes = autoResolveAction?.params?.notes as string || `Auto-resolved by rule: ${rule.props.name}`;

        difference.autoResolve(rule.props.name, 'AUTO_RESOLVE', notes);

        this.logger.log(`Auto-resolved difference ${difference.id} using rule: ${rule.props.name}`);

        return {
          resolved: true,
          action: 'AUTO_RESOLVE',
          notes,
        };
      }
    }

    const timingDifferences = this.checkTimingDifference(difference);
    if (timingDifferences.shouldResolve) {
      difference.autoResolve('TIMING_DIFFERENCE_RULE', 'AUTO_RESOLVE', timingDifferences.notes || 'Auto-resolved timing difference');
      return {
        resolved: true,
        action: 'AUTO_RESOLVE',
        notes: timingDifferences.notes,
      };
    }

    const roundingDifferences = this.checkRoundingDifference(difference);
    if (roundingDifferences.shouldResolve) {
      difference.autoResolve('ROUNDING_DIFFERENCE_RULE', 'AUTO_RESOLVE', roundingDifferences.notes || 'Auto-resolved rounding difference');
      return {
        resolved: true,
        action: 'AUTO_RESOLVE',
        notes: roundingDifferences.notes,
      };
    }

    return { resolved: false };
  }

  private checkTimingDifference(difference: TransactionDifference): { shouldResolve: boolean; notes?: string } {
    if (difference.differenceType !== 'TIMING_DIFFERENCE') {
      return { shouldResolve: false };
    }

    if (!difference.props.bankDate || !difference.props.internalDate) {
      return { shouldResolve: false };
    }

    const daysDiff = Math.abs(
      difference.props.bankDate.getTime() - difference.props.internalDate.getTime(),
    ) / (1000 * 60 * 60 * 24);

    if (daysDiff <= 3 && !difference.props.amountDifference) {
      return {
        shouldResolve: true,
        notes: `Auto-resolved: Transactions are within ${daysDiff} days with no amount difference`,
      };
    }

    return { shouldResolve: false };
  }

  private checkRoundingDifference(difference: TransactionDifference): { shouldResolve: boolean; notes?: string } {
    if (difference.differenceType !== 'AMOUNT_MISMATCH') {
      return { shouldResolve: false };
    }

    const amountDiff = difference.props.amountDifference || 0;

    if (Math.abs(amountDiff) <= 0.05) {
      return {
        shouldResolve: true,
        notes: `Auto-resolved: Amount difference of ${amountDiff} is within rounding tolerance`,
      };
    }

    return { shouldResolve: false };
  }

  suggestResolution(difference: TransactionDifference): string[] {
    const suggestions: string[] = [];

    switch (difference.differenceType) {
      case 'AMOUNT_MISMATCH':
        suggestions.push('Verify exchange rates if currencies differ');
        suggestions.push('Check for fees or charges not recorded in one system');
        suggestions.push('Review payment adjustments or partial payments');
        if (Math.abs(difference.props.amountDifference || 0) <= 0.05) {
          suggestions.push('Consider auto-resolving as rounding difference');
        }
        break;

      case 'TIMING_DIFFERENCE':
        suggestions.push('Verify posting dates in both systems');
        suggestions.push('Check cut-off times for transaction processing');
        suggestions.push('Review weekends and holidays affecting settlement');
        break;

      case 'MISSING_BANK_TRANSACTION':
        suggestions.push('Verify bank statement has been fully imported');
        suggestions.push('Check for pending transactions not yet cleared');
        suggestions.push('Review transaction date filters');
        break;

      case 'MISSING_INTERNAL_TRANSACTION':
        suggestions.push('Verify transaction was recorded in internal system');
        suggestions.push('Check for journal entries not posted');
        suggestions.push('Review import logs from accounting system');
        break;

      case 'DUPLICATE_TRANSACTION':
        suggestions.push('Review for duplicate imports');
        suggestions.push('Check transaction reference numbers');
        suggestions.push('Verify manual entry errors');
        break;

      case 'CURRENCY_MISMATCH':
        suggestions.push('Verify currency code configuration');
        suggestions.push('Check exchange rate applied at transaction time');
        suggestions.push('Review multi-currency account settings');
        break;

      case 'COUNTERPARTY_MISMATCH':
        suggestions.push('Verify counterparty name variations');
        suggestions.push('Check for parent/subsidiary relationships');
        suggestions.push('Review alias configurations');
        break;

      case 'REFERENCE_MISMATCH':
        suggestions.push('Check for formatting differences');
        suggestions.push('Verify invoice/payment number formats');
        suggestions.push('Review reference mapping rules');
        break;
    }

    if (difference.props.severity === 'CRITICAL') {
      suggestions.push('URGENT: Escalate to finance team immediately');
    } else if (difference.props.severity === 'HIGH') {
      suggestions.push('High priority: Assign to senior accountant');
    }

    return suggestions;
  }

  calculatePriorityScore(difference: TransactionDifference): number {
    let score = 0;

    const severityWeight = {
      LOW: 10,
      MEDIUM: 30,
      HIGH: 60,
      CRITICAL: 100,
    };

    score += severityWeight[difference.props.severity as keyof typeof severityWeight] || 0;

    if (difference.isOverdue()) {
      score += 50;
    }

    if (difference.props.amountDifference) {
      const absAmount = Math.abs(difference.props.amountDifference);
      if (absAmount > 10000) {
        score += 40;
      } else if (absAmount > 1000) {
        score += 20;
      } else if (absAmount > 100) {
        score += 10;
      }
    }

    const ageInDays = (Date.now() - difference.props.detectedAt.getTime()) / (1000 * 60 * 60 * 24);
    if (ageInDays > 30) {
      score += 30;
    } else if (ageInDays > 14) {
      score += 15;
    } else if (ageInDays > 7) {
      score += 5;
    }

    return Math.min(score, 200);
  }
}
