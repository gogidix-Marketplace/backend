import { Injectable, NotFoundException } from '@nestjs/common';
import { IQueryHandler } from './interfaces/query-handler.interface';
import { GetLeadScoreQuery, GetLeadScoresByTenantQuery } from '../../queries/get-lead-score.query';
import { LeadScore } from '../../../domain/entities/lead-score.entity';
import { LeadScoreRepositoryPort } from '../../../domain/ports/in/lead-score.repository.port';

@Injectable()
export class GetLeadScoreHandler implements IQueryHandler<GetLeadScoreQuery, LeadScore> {
  constructor(
    private readonly leadScoreRepository: LeadScoreRepositoryPort,
  ) {}

  async execute(query: GetLeadScoreQuery): Promise<LeadScore> {
    const leadScore = await this.leadScoreRepository.findByLeadIdAndTenantId(
      query.leadId,
      query.tenantId
    );

    if (!leadScore) {
      throw new NotFoundException('Lead score not found');
    }

    return leadScore;
  }
}

@Injectable()
export class GetLeadScoresByTenantHandler implements IQueryHandler<GetLeadScoresByTenantQuery, LeadScore[]> {
  constructor(
    private readonly leadScoreRepository: LeadScoreRepositoryPort,
  ) {}

  async execute(query: GetLeadScoresByTenantQuery): Promise<LeadScore[]> {
    return await this.leadScoreRepository.findByTenantId(query.tenantId, query.options);
  }
}
