import { Injectable } from '@nestjs/common';
import { ICommandHandler } from './interfaces/command-handler.interface';
import { UpdateScoreModelCommand } from '../../commands/update-score-model.command';
import { ScoreModel } from '../../../domain/entities/score-model.entity';
import { ScoreModelRepositoryPort } from '../../../domain/ports/in/score-model.repository.port';
import { EventPublisherPort } from '../../../domain/ports/out/event-publisher.port';

@Injectable()
export class UpdateScoreModelHandler implements ICommandHandler<UpdateScoreModelCommand> {
  constructor(
    private readonly scoreModelRepository: ScoreModelRepositoryPort,
    private readonly eventPublisher: EventPublisherPort,
  ) {}

  async execute(command: UpdateScoreModelCommand): Promise<ScoreModel> {
    const scoreModel = await this.scoreModelRepository.findById(command.modelId);

    if (!scoreModel) {
      throw new Error('Score model not found');
    }

    if (scoreModel.tenantId !== command.tenantId) {
      throw new Error('Access denied');
    }

    scoreModel.update(
      {
        name: command.name,
        description: command.description,
        scoringConfig: command.scoringConfig as any,
      },
      command.updatedBy
    );

    const savedModel = await this.scoreModelRepository.save(scoreModel);

    // Publish domain events
    for (const event of savedModel.domainEvents) {
      await this.eventPublisher.publish(event);
    }
    savedModel.clearDomainEvents();

    return savedModel;
  }
}
