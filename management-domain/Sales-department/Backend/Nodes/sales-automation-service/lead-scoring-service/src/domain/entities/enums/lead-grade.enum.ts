export enum LeadGrade {
  A = 'A',
  B = 'B',
  C = 'C',
  D = 'D',
}

export interface GradeConfig {
  grade: LeadGrade;
  minScore: number;
  maxScore: number;
  label: string;
  description: string;
  recommendedAction: string;
}

export const DEFAULT_GRADE_CONFIGS: GradeConfig[] = [
  {
    grade: LeadGrade.A,
    minScore: 80,
    maxScore: 100,
    label: 'Hot Lead',
    description: 'Highly qualified lead, ready for immediate sales engagement',
    recommendedAction: 'Contact within 24 hours, assign to senior sales rep',
  },
  {
    grade: LeadGrade.B,
    minScore: 60,
    maxScore: 79,
    label: 'Warm Lead',
    description: 'Qualified lead with good potential',
    recommendedAction: 'Contact within 48-72 hours, nurture with targeted content',
  },
  {
    grade: LeadGrade.C,
    minScore: 40,
    maxScore: 59,
    label: 'Cool Lead',
    description: 'Moderately qualified lead, requires nurturing',
    recommendedAction: 'Add to nurture campaign, monitor engagement',
  },
  {
    grade: LeadGrade.D,
    minScore: 0,
    maxScore: 39,
    label: 'Cold Lead',
    description: 'Low quality or unqualified lead',
    recommendedAction: 'Review for disqualification or long-term nurture',
  },
];
