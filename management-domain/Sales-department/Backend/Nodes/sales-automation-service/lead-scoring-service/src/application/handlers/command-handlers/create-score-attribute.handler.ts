import { Injectable } from '@nestjs/common';
import { ICommandHandler } from './interfaces/command-handler.interface';
import { CreateScoreAttributeCommand } from '../../commands/create-score-attribute.command';
import { ScoreAttribute } from '../../../domain/entities/score-attribute.entity';
import { ScoreAttributeRepositoryPort } from '../../../domain/ports/in/score-attribute.repository.port';

@Injectable()
export class CreateScoreAttributeHandler implements ICommandHandler<CreateScoreAttributeCommand> {
  constructor(
    private readonly scoreAttributeRepository: ScoreAttributeRepositoryPort,
  ) {}

  async execute(command: CreateScoreAttributeCommand): Promise<ScoreAttribute> {
    const scoreAttribute = new ScoreAttribute({
      tenantId: command.tenantId,
      name: command.name,
      description: command.description,
      type: command.type,
      dataType: command.dataType,
      weight: command.weight,
      defaultValue: command.defaultValue,
      isRequired: command.isRequired,
      options: command.options,
      validationRule: command.validationRule,
      sourceField: command.sourceField,
      displayOrder: command.displayOrder,
    });

    return await this.scoreAttributeRepository.save(scoreAttribute);
  }
}
