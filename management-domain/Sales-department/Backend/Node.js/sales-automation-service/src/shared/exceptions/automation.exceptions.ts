import { DomainException, NotFoundException, ValidationException, ConflictException } from './index';

export class AutomationRuleNotFoundException extends NotFoundException {
  constructor(ruleId: string) {
    super(`Automation rule with ID ${ruleId} not found`, { ruleId });
  }
}

export class WorkflowNotFoundException extends NotFoundException {
  constructor(workflowId: string) {
    super(`Workflow with ID ${workflowId} not found`, { workflowId });
  }
}

export class TriggerNotFoundException extends NotFoundException {
  constructor(triggerId: string) {
    super(`Trigger with ID ${triggerId} not found`, { triggerId });
  }
}

export class InvalidAutomationRuleException extends ValidationException {
  constructor(message: string, details?: any) {
    super(message, details);
  }
}

export class WorkflowExecutionException extends DomainException {
  constructor(workflowId: string, reason: string) {
    super(`Workflow execution failed: ${reason}`, { workflowId, reason });
  }
}

export class TriggerAlreadyExistsException extends ConflictException {
  constructor(triggerType: string, entityId: string) {
    super(`Trigger already exists for ${triggerType} on entity ${entityId}`, { triggerType, entityId });
  }
}

export class ActionExecutionException extends DomainException {
  constructor(actionId: string, reason: string) {
    super(`Action execution failed: ${reason}`, { actionId, reason });
  }
}

export class AutomationRuleAlreadyActiveException extends ConflictException {
  constructor(ruleId: string) {
    super(`Automation rule ${ruleId} is already active`, { ruleId });
  }
}

export class AutomationRuleAlreadyInactiveException extends ConflictException {
  constructor(ruleId: string) {
    super(`Automation rule ${ruleId} is already inactive`, { ruleId });
  }
}
