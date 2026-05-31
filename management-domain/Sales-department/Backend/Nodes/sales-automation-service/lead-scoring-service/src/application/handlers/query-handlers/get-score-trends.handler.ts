import { Injectable } from '@nestjs/common';
import { IQueryHandler } from './interfaces/query-handler.interface';
import { GetScoreTrendsQuery, GetScoreHistoryQuery } from '../../queries/get-score-trends.query';
import { LeadScoreRepositoryPort } from '../../../domain/ports/in/lead-score.repository.port';

export interface TrendDataPoint {
  date: Date;
  averageScore: number;
  count: number;
}

export interface ScoreHistoryEntry {
  timestamp: Date;
  score: number;
  grade: string;
  changeReason: string;
}

@Injectable()
export class GetScoreTrendsHandler implements IQueryHandler<GetScoreTrendsQuery, TrendDataPoint[]> {
  constructor(
    private readonly leadScoreRepository: LeadScoreRepositoryPort,
  ) {}

  async execute(query: GetScoreTrendsQuery): Promise<TrendDataPoint[]> {
    return await this.leadScoreRepository.getScoreTrends(
      query.tenantId,
      query.leadId,
      query.days,
      query.granularity
    );
  }
}

@Injectable()
export class GetScoreHistoryHandler implements IQueryHandler<GetScoreHistoryQuery, ScoreHistoryEntry[]> {
  constructor(
    private readonly leadScoreRepository: LeadScoreRepositoryPort,
  ) {}

  async execute(query: GetScoreHistoryQuery): Promise<ScoreHistoryEntry[]> {
    return await this.leadScoreRepository.getScoreHistory(
      query.leadId,
      query.tenantId,
      query.limit
    );
  }
}
