export class CreateSegmentDto {
  name!: string;
  description?: string;
  criteria!: any[];
  criteriaLogic?: 'and' | 'or';
  minScore?: number;
  maxScore?: number;
  autoAssign?: boolean;
}
export class UpdateSegmentDto {
  name?: string;
  description?: string;
  criteria?: any[];
  criteriaLogic?: 'and' | 'or';
  minScore?: number;
  maxScore?: number;
  isActive?: boolean;
  autoAssign?: boolean;
}
