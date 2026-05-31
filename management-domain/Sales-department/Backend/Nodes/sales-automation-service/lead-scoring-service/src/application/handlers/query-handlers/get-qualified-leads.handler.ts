import { Injectable } from '@nestjs/common';
import { IQueryHandler } from './interfaces/query-handler.interface';
import { GetQualifiedLeadsQuery, GetScoreStatisticsQuery } from '../../queries/get-qualified-leads.query';
import { LeadScore } from '../../../domain/entities/lead-score.entity';
import { LeadScoreRepositoryPort } from '../../../domain/ports/in/lead-score.repository.port';

export interface ScoreStatistics {
  totalLeads: number;
  averageScore: number;
  scoreDistribution: {
    range: string;
    count: number;
    percentage: number;
  }[];
  gradeDistribution: {
    grade: string;
    count: number;
    percentage: number;
  }[];
  topPerformers: {
    leadId: string;
    score: number;
    grade: string;
  }[];
}

@Injectable()
export class GetQualifiedLeadsHandler implements IQueryHandler<GetQualifiedLeadsQuery, LeadScore[]> {
  constructor(
    private readonly leadScoreRepository: LeadScoreRepositoryPort,
  ) {}

  async execute(query: GetQualifiedLeadsQuery): Promise<LeadScore[]> {
    return await this.leadScoreRepository.findQualifiedLeads(
      query.tenantId,
      query.minScore,
      query.options
    );
  }
}

@Injectable()
export class GetScoreStatisticsHandler implements IQueryHandler<GetScoreStatisticsQuery, ScoreStatistics> {
  constructor(
    private readonly leadScoreRepository: LeadScoreRepositoryPort,
  ) {}

  async execute(query: GetScoreStatisticsQuery): Promise<ScoreStatistics> {
    const statistics = await this.leadScoreRepository.getScoreStatistics(query.tenantId);

    const total = statistics.totalLeads || 1;
    const scoreDistribution = Object.entries(statistics.scoreDistribution).map(([range, count]) => ({
      range,
      count,
      percentage: (count / total) * 100,
    }));
    const gradeDistribution = Object.entries(statistics.gradeDistribution).map(([grade, count]) => ({
      grade,
      count,
      percentage: (count / total) * 100,
    }));

    return {
      totalLeads: statistics.totalLeads,
      averageScore: statistics.averageScore,
      scoreDistribution,
      gradeDistribution,
      topPerformers: statistics.topPerformers.map(p => ({
        ...p,
        grade: this.getGradeForScore(p.score),
      })),
    };
  }

  private getGradeForScore(score: number): string {
    if (score >= 80) return 'A';
    if (score >= 60) return 'B';
    if (score >= 40) return 'C';
    return 'D';
  }
}
