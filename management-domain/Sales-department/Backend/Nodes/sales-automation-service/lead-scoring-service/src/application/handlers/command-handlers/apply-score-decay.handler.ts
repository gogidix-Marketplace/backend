import { Injectable } from '@nestjs/common';
import { ICommandHandler } from './interfaces/command-handler.interface';
import { ApplyScoreDecayCommand, RescoreLeadCommand } from '../../commands/apply-score-decay.command';
import { ScoreLeadCommand } from '../../commands/score-lead.command';
import { LeadScoreRepositoryPort } from '../../../domain/ports/in/lead-score.repository.port';
import { ScoreModelRepositoryPort } from '../../../domain/ports/in/score-model.repository.port';
import { EventPublisherPort } from '../../../domain/ports/out/event-publisher.port';
import { ScoreLeadHandler } from './score-lead.handler';

@Injectable()
export class ApplyScoreDecayHandler implements ICommandHandler<ApplyScoreDecayCommand> {
  constructor(
    private readonly leadScoreRepository: LeadScoreRepositoryPort,
    private readonly eventPublisher: EventPublisherPort,
  ) {}

  async execute(command: ApplyScoreDecayCommand): Promise<void> {
    const leadScores = await this.leadScoreRepository.findByTenantId(command.tenantId);

    for (const leadScore of leadScores) {
      const daysSinceActivity = leadScore.daysSinceLastScored();

      if (daysSinceActivity > (command.decayPeriodDays ?? 30)) {
        leadScore.applyDecay(daysSinceActivity);
        await this.leadScoreRepository.save(leadScore);

        // Publish events
        for (const event of leadScore.domainEvents) {
          await this.eventPublisher.publish(event);
        }
        leadScore.clearDomainEvents();
      }
    }
  }
}

@Injectable()
export class RescoreLeadHandler implements ICommandHandler<RescoreLeadCommand> {
  constructor(
    private readonly scoreLeadHandler: ScoreLeadHandler,
    private readonly leadScoreRepository: LeadScoreRepositoryPort,
  ) {}

  async execute(command: RescoreLeadCommand): Promise<any> {
    const existingScore = await this.leadScoreRepository.findByLeadIdAndTenantId(
      command.leadId,
      command.tenantId
    );

    if (!command.force && existingScore) {
      const model = await this.leadScoreRepository.findById(existingScore.id);
      if (!existingScore.needsRescoring(7)) {
        return existingScore;
      }
    }

    const scoreCommand = new ScoreLeadCommand(
      command.leadId,
      command.tenantId,
      existingScore?.scoreModelId,
      undefined,
      undefined,
      command.requestedBy
    );

    return await this.scoreLeadHandler.execute(scoreCommand);
  }
}
