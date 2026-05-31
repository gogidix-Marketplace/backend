import { ScoreModelStatus, ScoreModelType } from '../../../domain/entities/enums/score-type.enum';

export interface ModelVariantDto {
  id: string;
  name: string;
  description: string;
  percentage: number;
  isActive: boolean;
  createdAt: Date;
}

export interface GradeConfigDto {
  grade: string;
  minScore: number;
  maxScore: number;
  label: string;
  description: string;
  recommendedAction: string;
}

export interface ScoringConfigurationDto {
  enableScoreDecay: boolean;
  decayRate: number;
  decayPeriodDays: number;
  minimumScoreThreshold: number;
  autoReScoreEnabled: boolean;
  reScoreIntervalDays: number;
  gradeConfigs: GradeConfigDto[];
}

export class ScoreModelResponseDto {
  id!: string;
  tenantId!: string;
  name!: string;
  description!: string;
  modelType!: ScoreModelType;
  status!: ScoreModelStatus;
  version!: number;
  scoringConfig!: ScoringConfigurationDto;
  ruleIds!: string[];
  attributeIds!: string[];
  variants!: ModelVariantDto[];
  isDefault!: boolean;
  isABTestEnabled!: boolean;
  testStartDate!: Date | null;
  testEndDate!: Date | null;
  metadata!: Record<string, any>;
  createdBy!: string;
  updatedBy!: string;
  createdAt!: Date;
  updatedAt!: Date;
}

export class ScoreModelListItemDto {
  id!: string;
  name!: string;
  description!: string;
  modelType!: ScoreModelType;
  status!: ScoreModelStatus;
  version!: number;
  isDefault!: boolean;
  isABTestEnabled!: boolean;
  createdAt!: Date;
  updatedAt!: Date;
}
