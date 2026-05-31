export class CreateScoringRuleDto {
  name!: string;
  description?: string;
  category!: string;
  conditions!: any[];
  conditionLogic?: 'and' | 'or';
  points!: number;
  maxPoints?: number;
  priority?: number;
  validFrom?: Date;
  validTo?: Date;
}
export class UpdateScoringRuleDto {
  name?: string;
  description?: string;
  conditions?: any[];
  conditionLogic?: 'and' | 'or';
  points?: number;
  maxPoints?: number;
  priority?: number;
  isActive?: boolean;
  validFrom?: Date;
  validTo?: Date;
}
