import { AutomationRule, RuleAction, RuleTrigger, RuleCondition } from '../../../src/domain/models/automation-rule.entity';
import { TriggerType } from '../../../src/domain/enums/trigger-type.enum';
import { ActionType } from '../../../src/domain/enums/action-type.enum';
import { AutomationRuleStatus } from '../../../src/domain/enums/automation-rule-status.enum';

describe('AutomationRule Entity', () => {
  let rule: AutomationRule;
  let trigger: RuleTrigger;
  let actions: RuleAction[];

  beforeEach(() => {
    trigger = {
      type: TriggerType.LEAD_CREATED,
      conditions: [
        { field: 'lead.leadSource', operator: 'eq', value: 'WEB' }
      ],
      entityTypes: ['lead']
    };

    actions = [
      {
        type: ActionType.SEND_EMAIL,
        order: 1,
        name: 'Send welcome email',
        parameters: { templateId: 'welcome-template' }
      },
      {
        type: ActionType.ASSIGN_OWNER,
        order: 2,
        name: 'Assign to sales rep',
        parameters: { ownerId: 'rep-123' }
      }
    ];
  });

  describe('Creation', () => {
    it('should create a valid automation rule', () => {
      rule = AutomationRule.create(
        'Welcome new leads',
        trigger,
        actions,
        'tenant-1',
        'Automatically welcome new web leads'
      );

      expect(rule.name).toBe('Welcome new leads');
      expect(rule.status).toBe(AutomationRuleStatus.DRAFT);
      expect(rule.trigger.type).toBe(TriggerType.LEAD_CREATED);
      expect(rule.actions).toHaveLength(2);
      expect(rule.isEnabled).toBe(false);
    });

    it('should create a lead scoring rule', () => {
      const scoringRule = {
        enabled: true,
        conditions: [
          { field: 'lead.annualRevenue', operator: 'gte', value: 1000000 },
          { field: 'lead.employeeCount', operator: 'gte', value: 100 }
        ],
        score: 25,
        maxScore: 50,
        category: 'demographic' as const
      };

      rule = AutomationRule.createLeadScoringRule(
        'Enterprise lead scoring',
        scoringRule,
        'tenant-1'
      );

      expect(rule.category).toBe('lead-scoring');
      expect(rule.leadScoring?.enabled).toBe(true);
      expect(rule.leadScoring?.score).toBe(25);
    });

    it('should create a deal stage rule', () => {
      const dealStageRule = {
        enabled: true,
        currentStage: 'PROPOSAL',
        targetStage: 'NEGOTIATION',
        conditions: [
          { field: 'deal.amount', operator: 'gte', value: 50000 }
        ],
        autoTransition: false,
        notifyAssignee: true
      };

      rule = AutomationRule.createDealStageRule(
        'Proposal to Negotiation',
        dealStageRule,
        actions,
        'tenant-1'
      );

      expect(rule.category).toBe('deal-stage');
      expect(rule.dealStageRule?.currentStage).toBe('PROPOSAL');
    });

    it('should sort actions by order', () => {
      actions = [
        { type: ActionType.SEND_EMAIL, order: 3, name: 'Third', parameters: {} },
        { type: ActionType.SEND_EMAIL, order: 1, name: 'First', parameters: {} },
        { type: ActionType.SEND_EMAIL, order: 2, name: 'Second', parameters: {} }
      ] as RuleAction[];

      rule = AutomationRule.create(
        'Test rule',
        trigger,
        actions,
        'tenant-1'
      );

      expect(rule.actions[0].name).toBe('First');
      expect(rule.actions[1].name).toBe('Second');
      expect(rule.actions[2].name).toBe('Third');
    });
  });

  describe('Activation Lifecycle', () => {
    beforeEach(() => {
      rule = AutomationRule.create(
        'Test rule',
        trigger,
        actions,
        'tenant-1'
      );
    });

    it('should activate a draft rule', () => {
      rule.activate();

      expect(rule.status).toBe(AutomationRuleStatus.ACTIVE);
      expect(rule.isEnabled).toBe(true);
    });

    it('should not activate an already active rule', () => {
      rule.activate();

      expect(() => rule.activate()).toThrow('Automation rule is already active');
    });

    it('should pause an active rule', () => {
      rule.activate();
      rule.pause();

      expect(rule.status).toBe(AutomationRuleStatus.PAUSED);
      expect(rule.isEnabled).toBe(false);
    });

    it('should only pause active rules', () => {
      expect(() => rule.pause()).toThrow('Only active rules can be paused');
    });

    it('should resume a paused rule', () => {
      rule.activate();
      rule.pause();
      rule.resume();

      expect(rule.status).toBe(AutomationRuleStatus.ACTIVE);
      expect(rule.isEnabled).toBe(true);
    });

    it('should only resume paused rules', () => {
      expect(() => rule.resume()).toThrow('Only paused rules can be resumed');
    });

    it('should archive a paused rule', () => {
      rule.activate();
      rule.pause();
      rule.archive();

      expect(rule.status).toBe(AutomationRuleStatus.ARCHIVED);
      expect(rule.isEnabled).toBe(false);
    });

    it('should not archive active rules', () => {
      expect(() => rule.archive()).toThrow('Active rules must be paused before archiving');
    });

    it('should enable a draft rule which makes it active', () => {
      rule.enable();

      expect(rule.status).toBe(AutomationRuleStatus.ACTIVE);
      expect(rule.isEnabled).toBe(true);
    });

    it('should disable an active rule', () => {
      rule.activate();
      rule.disable();

      expect(rule.status).toBe(AutomationRuleStatus.PAUSED);
      expect(rule.isEnabled).toBe(false);
    });
  });

  describe('Action Management', () => {
    beforeEach(() => {
      rule = AutomationRule.create(
        'Test rule',
        trigger,
        actions,
        'tenant-1'
      );
    });

    it('should add an action to draft rule', () => {
      const newAction: RuleAction = {
        type: ActionType.ADD_TAG,
        order: 3,
        name: 'Add hot lead tag',
        parameters: { tag: 'hot-lead' }
      };

      rule.addAction(newAction);

      expect(rule.actions).toHaveLength(3);
    });

    it('should not add actions to active rule', () => {
      rule.activate();

      const newAction: RuleAction = {
        type: ActionType.ADD_TAG,
        order: 3,
        name: 'Add tag',
        parameters: {}
      };

      expect(() => rule.addAction(newAction)).toThrow('Cannot add actions to an active rule');
    });

    it('should remove an action from draft rule', () => {
      rule.removeAction(2);

      expect(rule.actions).toHaveLength(1);
      expect(rule.actions[0].order).toBe(1);
    });

    it('should not remove actions from active rule', () => {
      rule.activate();

      expect(() => rule.removeAction(1)).toThrow('Cannot remove actions from an active rule');
    });

    it('should update an action in draft rule', () => {
      rule.updateAction(1, { parameters: { templateId: 'new-template' } });

      expect(rule.actions[0].parameters.templateId).toBe('new-template');
    });

    it('should not update actions in active rule', () => {
      rule.activate();

      expect(() => rule.updateAction(1, { parameters: {} })).toThrow('Cannot update actions in an active rule');
    });
  });

  describe('Trigger Management', () => {
    beforeEach(() => {
      rule = AutomationRule.create(
        'Test rule',
        trigger,
        actions,
        'tenant-1'
      );
    });

    it('should update trigger in draft rule', () => {
      rule.updateTrigger({ type: TriggerType.LEAD_QUALIFIED });

      expect(rule.trigger.type).toBe(TriggerType.LEAD_QUALIFIED);
    });

    it('should not update trigger in active rule', () => {
      rule.activate();

      expect(() => rule.updateTrigger({ type: TriggerType.DEAL_CREATED }))
        .toThrow('Cannot update trigger of an active rule');
    });
  });

  describe('Trigger Evaluation', () => {
    beforeEach(() => {
      rule = AutomationRule.create(
        'Test rule',
        trigger,
        actions,
        'tenant-1'
      );
    });

    it('should evaluate equal condition correctly', () => {
      const result = rule.shouldTrigger({ lead: { leadSource: 'WEB' } });
      expect(result).toBe(true);
    });

    it('should fail equal condition when values differ', () => {
      const result = rule.shouldTrigger({ lead: { leadSource: 'EMAIL' } });
      expect(result).toBe(false);
    });

    it('should evaluate greater than condition', () => {
      rule.updateTrigger({
        conditions: [{ field: 'deal.amount', operator: 'gt', value: 50000 }]
      });

      const result = rule.shouldTrigger({ deal: { amount: 75000 } });
      expect(result).toBe(true);
    });

    it('should evaluate contains condition for strings', () => {
      rule.updateTrigger({
        conditions: [{ field: 'lead.notes', operator: 'contains', value: 'urgent' }]
      });

      const result = rule.shouldTrigger({ lead: { notes: 'This is urgent lead' } });
      expect(result).toBe(true);
    });

    it('should evaluate empty condition', () => {
      rule.updateTrigger({
        conditions: [{ field: 'lead.description', operator: 'empty', value: null }]
      });

      const result = rule.shouldTrigger({ lead: { description: '' } });
      expect(result).toBe(true);
    });

    it('should evaluate in condition for arrays', () => {
      rule.updateTrigger({
        conditions: [{ field: 'lead.tags', operator: 'in', values: ['hot', 'cold'] }]
      });

      const result = rule.shouldTrigger({ lead: { tags: ['hot', 'enterprise'] } });
      expect(result).toBe(true);
    });

    it('should return true when no conditions defined', () => {
      rule.updateTrigger({ conditions: [] });

      const result = rule.shouldTrigger({ lead: { anyData: 'value' } });
      expect(result).toBe(true);
    });

    it('should evaluate nested properties', () => {
      rule.updateTrigger({
        conditions: [{ field: 'account.attributes.industry', operator: 'eq', value: 'Technology' }]
      });

      const result = rule.shouldTrigger({ account: { attributes: { industry: 'Technology' } } });
      expect(result).toBe(true);
    });
  });

  describe('Priority Management', () => {
    beforeEach(() => {
      rule = AutomationRule.create(
        'Test rule',
        trigger,
        actions,
        'tenant-1'
      );
    });

    it('should set valid priority', () => {
      rule.setPriority(75);

      expect(rule.priority).toBe(75);
    });

    it('should not allow priority below 0', () => {
      expect(() => rule.setPriority(-1)).toThrow('Priority must be between 0 and 100');
    });

    it('should not allow priority above 100', () => {
      expect(() => rule.setPriority(101)).toThrow('Priority must be between 0 and 100');
    });
  });

  describe('Tag Management', () => {
    beforeEach(() => {
      rule = AutomationRule.create(
        'Test rule',
        trigger,
        actions,
        'tenant-1'
      );
    });

    it('should add a tag', () => {
      rule.addTag('marketing');

      expect(rule.tags).toContain('marketing');
    });

    it('should not add duplicate tags', () => {
      rule.addTag('marketing');
      rule.addTag('marketing');

      expect(rule.tags.filter(t => t === 'marketing')).toHaveLength(1);
    });

    it('should remove a tag', () => {
      rule.addTag('marketing');
      rule.addTag('sales');
      rule.removeTag('marketing');

      expect(rule.tags).not.toContain('marketing');
      expect(rule.tags).toContain('sales');
    });
  });

  describe('Execution Tracking', () => {
    beforeEach(() => {
      rule = AutomationRule.create(
        'Test rule',
        trigger,
        actions,
        'tenant-1'
      );
    });

    it('should record successful execution', () => {
      rule.recordExecution('SUCCESS');

      expect(rule.executionCount).toBe(1);
      expect(rule.lastExecutionStatus).toBe('SUCCESS');
      expect(rule.lastExecutedAt).toBeDefined();
    });

    it('should record partial execution', () => {
      rule.recordExecution('PARTIAL');

      expect(rule.lastExecutionStatus).toBe('PARTIAL');
    });

    it('should increment execution count', () => {
      rule.recordExecution('SUCCESS');
      rule.recordExecution('SUCCESS');
      rule.recordExecution('FAILURE');

      expect(rule.executionCount).toBe(3);
    });
  });

  describe('Lead Scoring', () => {
    it('should calculate lead score based on matched conditions', () => {
      const scoringRule = {
        enabled: true,
        conditions: [
          { field: 'lead.annualRevenue', operator: 'gte', value: 1000000 },
          { field: 'lead.employeeCount', operator: 'gte', value: 100 }
        ],
        score: 25,
        maxScore: 50,
        category: 'demographic' as const
      };

      rule = AutomationRule.createLeadScoringRule(
        'Enterprise scoring',
        scoringRule,
        'tenant-1'
      );

      const leadData = {
        lead: {
          annualRevenue: 1500000,
          employeeCount: 250
        }
      };

      const score = rule.calculateLeadScore(leadData);

      expect(score).toBe(50); // 2 conditions * 25 points = 50 (max)
    });

    it('should return 0 when scoring rule is disabled', () => {
      const scoringRule = {
        enabled: false,
        conditions: [{ field: 'lead.industry', operator: 'eq', value: 'Technology' }],
        score: 25,
        category: 'demographic' as const
      };

      rule = AutomationRule.createLeadScoringRule(
        'Industry scoring',
        scoringRule,
        'tenant-1'
      );

      const score = rule.calculateLeadScore({ lead: { industry: 'Technology' } });

      expect(score).toBe(0);
    });

    it('should respect max score limit', () => {
      const scoringRule = {
        enabled: true,
        conditions: [
          { field: 'lead.revenue', operator: 'gt', value: 0 },
          { field: 'lead.employees', operator: 'gt', value: 0 },
          { field: 'lead.hasWebsite', operator: 'eq', value: true }
        ],
        score: 40,
        maxScore: 75,
        category: 'demographic' as const
      };

      rule = AutomationRule.createLeadScoringRule(
        'Multi-factor scoring',
        scoringRule,
        'tenant-1'
      );

      const score = rule.calculateLeadScore({
        lead: { revenue: 1000, employees: 10, hasWebsite: true }
      });

      expect(score).toBe(75); // Max score
    });
  });

  describe('Name Updates', () => {
    beforeEach(() => {
      rule = AutomationRule.create(
        'Test rule',
        trigger,
        actions,
        'tenant-1'
      );
    });

    it('should update name with valid value', () => {
      rule.updateName('Updated rule name');

      expect(rule.name).toBe('Updated rule name');
    });

    it('should not allow empty name', () => {
      expect(() => rule.updateName('')).toThrow('Rule name cannot be empty');
    });

    it('should trim whitespace from name', () => {
      rule.updateName('  Trimmed Name  ');

      expect(rule.name).toBe('Trimmed Name');
    });
  });

  describe('Validation', () => {
    it('should throw error for empty name', () => {
      expect(() => {
        AutomationRule.create('', trigger, actions, 'tenant-1');
      }).toThrow();
    });

    it('should throw error for missing trigger', () => {
      expect(() => {
        AutomationRule.create('Test rule', null as any, actions, 'tenant-1');
      }).toThrow();
    });

    it('should throw error for empty actions', () => {
      expect(() => {
        AutomationRule.create('Test rule', trigger, [], 'tenant-1');
      }).toThrow();
    });

    it('should throw error for duplicate action orders', () => {
      actions = [
        { type: ActionType.SEND_EMAIL, order: 1, name: 'First', parameters: {} },
        { type: ActionType.SEND_EMAIL, order: 1, name: 'Duplicate', parameters: {} }
      ] as RuleAction[];

      expect(() => {
        AutomationRule.create('Test rule', trigger, actions, 'tenant-1');
      }).toThrow('Rule actions must have unique order values');
    });
  });

  describe('Serialization', () => {
    beforeEach(() => {
      rule = AutomationRule.create(
        'Test rule',
        trigger,
        actions,
        'tenant-1',
        'Test description'
      );
      rule.addTag('test-tag');
      rule.setPriority(50);
    });

    it('should serialize to JSON', () => {
      const json = rule.toJSON();

      expect(json.name).toBe('Test rule');
      expect(json.description).toBe('Test description');
      expect(json.tags).toContain('test-tag');
      expect(json.priority).toBe(50);
      expect(json.status).toBe(AutomationRuleStatus.DRAFT);
      expect(json.tenantId).toBe('tenant-1');
      expect(json.id).toBeDefined();
      expect(json.createdAt).toBeDefined();
    });

    it('should deserialize from JSON', () => {
      const json = rule.toJSON();
      const deserialized = AutomationRule.fromJSON(json);

      expect(deserialized.name).toBe(rule.name);
      expect(deserialized.description).toBe(rule.description);
      expect(deserialized.priority).toBe(rule.priority);
      expect(deserialized.tags).toContain('test-tag');
      expect(deserialized.status).toBe(AutomationRuleStatus.DRAFT);
    });
  });

  describe('Can Execute Check', () => {
    it('should return true for active and enabled rule', () => {
      rule = AutomationRule.create('Test rule', trigger, actions, 'tenant-1');
      rule.activate();

      expect(rule.canExecute()).toBe(true);
    });

    it('should return false for draft rule', () => {
      rule = AutomationRule.create('Test rule', trigger, actions, 'tenant-1');

      expect(rule.canExecute()).toBe(false);
    });

    it('should return false for disabled rule', () => {
      rule = AutomationRule.create('Test rule', trigger, actions, 'tenant-1');
      rule.activate();
      rule.disable();

      expect(rule.canExecute()).toBe(false);
    });
  });

  describe('Schedule Management', () => {
    beforeEach(() => {
      rule = AutomationRule.create(
        'Test rule',
        trigger,
        actions,
        'tenant-1'
      );
    });

    it('should set schedule', () => {
      const schedule = {
        frequency: 'daily' as const,
        timezone: 'UTC'
      };

      rule.setSchedule(schedule);

      expect(rule.schedule?.frequency).toBe('daily');
      expect(rule.schedule?.timezone).toBe('UTC');
    });

    it('should set cron expression in schedule', () => {
      const schedule = {
        frequency: 'cron' as const,
        cronExpression: '0 0 * * *'
      };

      rule.setSchedule(schedule);

      expect(rule.schedule?.cronExpression).toBe('0 0 * * *');
    });
  });
});
