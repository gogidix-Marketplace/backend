import { Injectable } from '@nestjs/common';
import { ICommandHandler } from './interfaces/command-handler.interface';
import { CreateScoreModelCommand } from '../../commands/create-score-model.command';
import { ScoreModel } from '../../../domain/entities/score-model.entity';
import { ScoreModelRepositoryPort } from '../../../domain/ports/in/score-model.repository.port';
import { EventPublisherPort } from '../../../domain/ports/out/event-publisher.port';

@Injectable()
export class CreateScoreModelHandler implements ICommandHandler<CreateScoreModelCommand> {
  constructor(
    private readonly scoreModelRepository: ScoreModelRepositoryPort,
    private readonly eventPublisher: EventPublisherPort,
  ) {}

  async execute(command: CreateScoreModelCommand): Promise<ScoreModel> {
    const scoreModel = new ScoreModel({
      tenantId: command.tenantId,
      name: command.name,
      description: command.description,
      modelType: command.modelType,
      scoringConfig: command.scoringConfig as any,
      ruleIds: command.ruleIds,
      attributeIds: command.attributeIds,
      isDefault: command.isDefault,
      createdBy: command.createdBy,
    });

    const savedModel = await this.scoreModelRepository.save(scoreModel);

    // Publish domain events
    for (const event of savedModel.domainEvents) {
      await this.eventPublisher.publish(event);
    }
    savedModel.clearDomainEvents();

    return savedModel;
  }
}
