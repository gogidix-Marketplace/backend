import { ReconciliationStartedEvent } from '../../events/reconciliation-started.event';
import { ReconciliationCompletedEvent } from '../../events/reconciliation-completed.event';
import { ReconciliationFailedEvent } from '../../events/reconciliation-failed.event';
import { MatchFoundEvent } from '../../events/match-found.event';
import { DifferenceDetectedEvent } from '../../events/difference-detected.event';
import { DifferenceResolvedEvent } from '../../events/difference-resolved.event';

// Re-export events for convenience
export {
  ReconciliationStartedEvent,
  ReconciliationCompletedEvent,
  ReconciliationFailedEvent,
  MatchFoundEvent,
  DifferenceDetectedEvent,
  DifferenceResolvedEvent,
};

export interface IEventPublisher {
  publishReconciliationStarted(event: ReconciliationStartedEvent): Promise<void>;
  publishReconciliationCompleted(event: ReconciliationCompletedEvent): Promise<void>;
  publishReconciliationFailed(event: ReconciliationFailedEvent): Promise<void>;
  publishMatchFound(event: MatchFoundEvent): Promise<void>;
  publishDifferenceDetected(event: DifferenceDetectedEvent): Promise<void>;
  publishDifferenceResolved(event: DifferenceResolvedEvent): Promise<void>;
  publishBulkEvents(events: unknown[]): Promise<void>;
}
