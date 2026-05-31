import { CommissionCalculatedEvent } from '../../events/commission-calculated.event';
import { PayoutApprovedEvent } from '../../events/payout-approved.event';
import { PayoutPaidEvent } from '../../events/payout-paid.event';
import { CommissionClawbackEvent } from '../../events/commission-clawback.event';
import { CommissionPeriodClosedEvent } from '../../events/commission-period-closed.event';

/**
 * Output port for publishing domain events
 */
export interface EventPublisherPort {
  /**
   * Publish commission calculated event
   */
  publishCommissionCalculated(event: CommissionCalculatedEvent): Promise<void>;

  /**
   * Publish payout approved event
   */
  publishPayoutApproved(event: PayoutApprovedEvent): Promise<void>;

  /**
   * Publish payout paid event
   */
  publishPayoutPaid(event: PayoutPaidEvent): Promise<void>;

  /**
   * Publish commission clawback event
   */
  publishCommissionClawback(event: CommissionClawbackEvent): Promise<void>;

  /**
   * Publish commission period closed event
   */
  publishCommissionPeriodClosed(event: CommissionPeriodClosedEvent): Promise<void>;

  /**
   * Publish multiple events
   */
  publishBatch(events: DomainEvent[]): Promise<void>;
}

/**
 * Base domain event type
 */
export type DomainEvent =
  | CommissionCalculatedEvent
  | PayoutApprovedEvent
  | PayoutPaidEvent
  | CommissionClawbackEvent
  | CommissionPeriodClosedEvent;
