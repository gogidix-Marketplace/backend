import { LeadGrade } from '../../../domain/entities/enums/lead-grade.enum';
import { ScoreType } from '../../../domain/entities/enums/score-type.enum';

export interface ScoreBreakdownDto {
  demographicScore: number;
  behavioralScore: number;
  engagementScore: number;
  firmographicScore: number;
  customScores: Record<string, number>;
}

export interface ScoreAttributeDto {
  name: string;
  value: any;
  weight: number;
  contribution: number;
}

export class LeadScoreResponseDto {
  id!: string;
  leadId!: string;
  tenantId!: string;
  scoreModelId!: string;
  totalScore!: number;
  grade!: LeadGrade;
  scoreType!: ScoreType;
  breakdown!: ScoreBreakdownDto;
  attributes!: ScoreAttributeDto[];
  lastScoredAt!: Date;
  scoreDecayRate!: number;
  decayApplied!: number;
  lastDecayAppliedAt!: Date | null;
  variantId!: string | null;
  isControlGroup!: boolean;
  metadata!: Record<string, any>;
  createdAt!: Date;
  updatedAt!: Date;
}

export class BatchScoreResponseDto {
  successful: Array<{ leadId: string; scoreId: string; score: number }>;
  failed: Array<{ leadId: string; error: string }>;
  totalProcessed: number;
}

export class ScoreStatisticsDto {
  totalLeads!: number;
  averageScore!: number;
  scoreDistribution!: Record<string, number>;
  gradeDistribution!: Record<string, number>;
  topPerformers!: Array<{ leadId: string; score: number; grade: string }>;
}

export class ScoreTrendDto {
  date!: Date;
  averageScore!: number;
  totalLeads!: number;
  gradeDistribution!: Record<string, number>;
}
