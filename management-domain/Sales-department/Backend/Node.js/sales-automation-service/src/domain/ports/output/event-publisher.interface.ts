import {
  AutomationTriggeredEvent,
  WorkflowExecutedEvent,
  WorkflowFailedEvent,
  RuleCreatedEvent,
  RuleUpdatedEvent,
  RuleDeletedEvent,
  LeadScoredEvent,
  DealStageChangedEvent,
} from '../../events';

export interface EventPublisher {
  publish(event: AutomationTriggeredEvent | WorkflowExecutedEvent | WorkflowFailedEvent | RuleCreatedEvent | RuleUpdatedEvent | RuleDeletedEvent | LeadScoredEvent | DealStageChangedEvent): Promise<void>;
  publishBatch(events: Array<AutomationTriggeredEvent | WorkflowExecutedEvent | WorkflowFailedEvent | RuleCreatedEvent | RuleUpdatedEvent | RuleDeletedEvent | LeadScoredEvent | DealStageChangedEvent>): Promise<void>;
}

export interface MessageBroker {
  connect(): Promise<void>;
  disconnect(): Promise<void>;
  publish(topic: string, message: any): Promise<void>;
  subscribe(topic: string, handler: (message: any) => void): Promise<void>;
  isConnected(): boolean;
}
