import { Injectable, Logger } from '@nestjs/common';
import { AutomationRuleRepository } from '../../domain/ports/output';
import { EventPublisher } from '../../domain/ports/output/event-publisher.interface';
import { LeadScoredEvent } from '../../domain/events';
import { ExecutionContext } from '@shared/context';

export interface ScoreLeadCommand {
  leadId: string;
  leadEmail?: string;
  data: Record<string, any>;
  tenantId: string;
}

@Injectable()
export class LeadScoringService {
  private readonly logger = new Logger(LeadScoringService.name);

  constructor(
    private readonly ruleRepository: AutomationRuleRepository,
    private readonly eventPublisher: EventPublisher,
  ) {}

  async calculateLeadScore(
    command: ScoreLeadCommand,
    context: ExecutionContext,
  ): Promise<{ leadId: string; previousScore: number; newScore: number; scoreChanges: Array<{ ruleId: string; ruleName: string; score: number }> }> {
    this.logger.log(`Calculating lead score for lead: ${command.leadId}`);

    const scoringRules = await this.ruleRepository.findByTenantId(command.tenantId, {
      category: 'lead-scoring',
    });

    let totalScore = 0;
    const scoreChanges: Array<{ ruleId: string; ruleName: string; score: number }> = [];

    for (const rule of scoringRules) {
      if (!rule.leadScoring || !rule.leadScoring.enabled) {
        continue;
      }

      if (rule.shouldTrigger(command.data)) {
        const score = rule.calculateLeadScore(command.data);
        if (score > 0) {
          totalScore += score;
          scoreChanges.push({
            ruleId: rule.id,
            ruleName: rule.name,
            score,
          });

          const event = LeadScoredEvent.fromLead(
            command.leadId,
            0, // We don't track previous score in this simplified version
            score,
            `Rule "${rule.name}" matched`,
            command.tenantId,
            context.userId,
            command.leadEmail,
            rule.id,
            rule.name,
            rule.leadScoring.category,
            context.correlationId,
          );

          await this.eventPublisher.publish(event);
        }
      }
    }

    this.logger.log(`Lead ${command.leadId} scored ${totalScore} points from ${scoreChanges.length} rules`);

    return {
      leadId: command.leadId,
      previousScore: 0,
      newScore: totalScore,
      scoreChanges,
    };
  }

  async getScoreForLead(leadId: string, tenantId: string): Promise<number> {
    this.logger.debug(`Getting score for lead: ${leadId}`);

    // This would typically fetch from a lead service or cache
    // For now, we'll return 0
    return 0;
  }
}
