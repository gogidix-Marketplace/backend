export class CalculateScoreDto {
  leadId!: string;
  eventData!: Record<string, any>;
}
export class ScoreResponseDto {
  leadId!: string;
  score!: number;
  previousScore!: number;
  categoryScores!: Record<string, number>;
  flags!: any[];
}
