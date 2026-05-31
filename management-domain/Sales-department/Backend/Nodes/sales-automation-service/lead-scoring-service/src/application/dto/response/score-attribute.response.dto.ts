import { ScoreType } from '../../../domain/entities/enums/score-type.enum';

export class ScoreAttributeResponseDto {
  id!: string;
  tenantId!: string;
  name!: string;
  description!: string;
  type!: ScoreType;
  dataType!: string;
  weight!: number;
  defaultValue!: any;
  isRequired!: boolean;
  options!: string[];
  validationRule!: string | null;
  sourceField!: string;
  isActive!: boolean;
  displayOrder!: number;
  metadata!: Record<string, any>;
  createdAt!: Date;
  updatedAt!: Date;
}

export class ScoreAttributeListItemDto {
  id!: string;
  name!: string;
  description!: string;
  type!: ScoreType;
  weight!: number;
  isActive!: boolean;
  displayOrder!: number;
}

export class AttributeValidationDto {
  valid!: boolean;
  error?: string;
}

export class AttributeContributionDto {
  attributeName!: string;
  value!: any;
  contribution!: number;
  weight!: number;
}
