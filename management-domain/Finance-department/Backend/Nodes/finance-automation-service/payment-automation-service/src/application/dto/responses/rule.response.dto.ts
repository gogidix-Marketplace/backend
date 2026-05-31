import { PaymentRule } from '../../../domain/models/payment-rule.entity';

export class RuleResponseDto {
  id!: string;
  name!: string;
  description!: string;
  ruleType!: string;
  conditions!: any[];
  actions!: any[];
  priority!: number;
  isActive!: boolean;
  effectiveFrom?: Date;
  effectiveTo?: Date;
  metadata?: Record<string, any>;
  matchCount!: number;
  lastMatchedAt?: Date;
  createdBy!: string;
  createdAt!: Date;
  updatedAt!: Date;
  tenantId!: string;
  version!: number;

  static fromEntity(rule: PaymentRule): RuleResponseDto {
    const json = rule.toJSON();
    return {
      id: json.id,
      name: json.name,
      description: json.description,
      ruleType: json.ruleType,
      conditions: json.conditions,
      actions: json.actions,
      priority: json.priority,
      isActive: json.isActive,
      effectiveFrom: json.effectiveFrom ? new Date(json.effectiveFrom) : undefined,
      effectiveTo: json.effectiveTo ? new Date(json.effectiveTo) : undefined,
      metadata: json.metadata,
      matchCount: json.matchCount,
      lastMatchedAt: json.lastMatchedAt ? new Date(json.lastMatchedAt) : undefined,
      createdBy: json.createdBy,
      createdAt: new Date(json.createdAt),
      updatedAt: new Date(json.updatedAt),
      tenantId: json.tenantId,
      version: json.version,
    };
  }

  static fromEntities(rules: PaymentRule[]): RuleResponseDto[] {
    return rules.map((r) => RuleResponseDto.fromEntity(r));
  }
}
