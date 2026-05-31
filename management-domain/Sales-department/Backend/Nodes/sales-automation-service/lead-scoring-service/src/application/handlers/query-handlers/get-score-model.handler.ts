import { Injectable, NotFoundException } from '@nestjs/common';
import { IQueryHandler } from './interfaces/query-handler.interface';
import { GetScoreModelQuery, GetScoreModelsByTenantQuery } from '../../queries/get-score-model.query';
import { ScoreModel } from '../../../domain/entities/score-model.entity';
import { ScoreModelRepositoryPort } from '../../../domain/ports/in/score-model.repository.port';

@Injectable()
export class GetScoreModelHandler implements IQueryHandler<GetScoreModelQuery, ScoreModel> {
  constructor(
    private readonly scoreModelRepository: ScoreModelRepositoryPort,
  ) {}

  async execute(query: GetScoreModelQuery): Promise<ScoreModel> {
    const scoreModel = await this.scoreModelRepository.findById(query.modelId);

    if (!scoreModel || scoreModel.tenantId !== query.tenantId) {
      throw new NotFoundException('Score model not found');
    }

    return scoreModel;
  }
}

@Injectable()
export class GetScoreModelsByTenantHandler implements IQueryHandler<GetScoreModelsByTenantQuery, ScoreModel[]> {
  constructor(
    private readonly scoreModelRepository: ScoreModelRepositoryPort,
  ) {}

  async execute(query: GetScoreModelsByTenantQuery): Promise<ScoreModel[]> {
    return await this.scoreModelRepository.findByTenantId(
      query.tenantId,
      query.options
    );
  }
}
