import { Injectable } from '@nestjs/common';
import { ICommandHandler } from './interfaces/command-handler.interface';
import { ActivateScoreModelCommand, DeactivateScoreModelCommand } from '../../commands/activate-score-model.command';
import { ScoreModel } from '../../../domain/entities/score-model.entity';
import { ScoreModelRepositoryPort } from '../../../domain/ports/in/score-model.repository.port';
import { EventPublisherPort } from '../../../domain/ports/out/event-publisher.port';

@Injectable()
export class ActivateScoreModelHandler implements ICommandHandler<ActivateScoreModelCommand> {
  constructor(
    private readonly scoreModelRepository: ScoreModelRepositoryPort,
    private readonly eventPublisher: EventPublisherPort,
  ) {}

  async execute(command: ActivateScoreModelCommand): Promise<ScoreModel> {
    const scoreModel = await this.scoreModelRepository.findById(command.modelId);

    if (!scoreModel) {
      throw new Error('Score model not found');
    }

    if (scoreModel.tenantId !== command.tenantId) {
      throw new Error('Access denied');
    }

    scoreModel.activate(command.userId);

    const savedModel = await this.scoreModelRepository.save(scoreModel);

    // Publish domain events
    for (const event of savedModel.domainEvents) {
      await this.eventPublisher.publish(event);
    }
    savedModel.clearDomainEvents();

    return savedModel;
  }
}

@Injectable()
export class DeactivateScoreModelHandler implements ICommandHandler<DeactivateScoreModelCommand> {
  constructor(
    private readonly scoreModelRepository: ScoreModelRepositoryPort,
    private readonly eventPublisher: EventPublisherPort,
  ) {}

  async execute(command: DeactivateScoreModelCommand): Promise<ScoreModel> {
    const scoreModel = await this.scoreModelRepository.findById(command.modelId);

    if (!scoreModel) {
      throw new Error('Score model not found');
    }

    if (scoreModel.tenantId !== command.tenantId) {
      throw new Error('Access denied');
    }

    scoreModel.deactivate(command.userId);

    const savedModel = await this.scoreModelRepository.save(scoreModel);

    // Publish domain events
    for (const event of savedModel.domainEvents) {
      await this.eventPublisher.publish(event);
    }
    savedModel.clearDomainEvents();

    return savedModel;
  }
}
