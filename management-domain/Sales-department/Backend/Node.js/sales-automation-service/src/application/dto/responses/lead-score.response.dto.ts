export class LeadScoreResponseDto {
  leadId: string;
  leadEmail?: string;
  previousScore: number;
  newScore: number;
  scoreChange: number;
  reason: string;
  ruleId?: string;
  ruleName?: string;
  category?: 'demographic' | 'behavioral' | 'engagement' | 'manual';
  scoredAt: Date;

  static fromResult(data: any): LeadScoreResponseDto {
    return {
      leadId: data.leadId,
      leadEmail: data.leadEmail,
      previousScore: data.previousScore,
      newScore: data.newScore,
      scoreChange: data.scoreChange,
      reason: data.reason,
      ruleId: data.ruleId,
      ruleName: data.ruleName,
      category: data.category,
      scoredAt: data.scoredAt || new Date(),
    };
  }
}
