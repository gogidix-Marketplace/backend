import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

/**
 * MongoDB schema for CommissionRule
 */
@Schema({ timestamps: true, versionKey: false })
export class CommissionRuleDocument extends Document {
  @Prop({ required: true })
  name: string;

  @Prop({ type: String })
  description?: string;

  @Prop({
    required: true,
    enum: ['PERCENTAGE', 'FIXED_AMOUNT', 'TIERED', 'GRADUATED'],
  })
  calculationType: string;

  @Prop({ required: true, type: Number })
  baseRate: number;

  @Prop({ type: Array, default: [] })
  tiers: Array<{
    id: string;
    name: string;
    tierType: string;
    minThreshold: number;
    maxThreshold?: number;
    commissionRate: number;
    fixedAmount?: number;
    multiplier?: number;
  }>;

  @Prop({
    required: true,
    enum: ['ALL_SALES', 'SPECIFIC_PRODUCTS', 'SPECIFIC_CUSTOMERS', 'SPECIFIC_REGIONS'],
    default: 'ALL_SALES',
  })
  applicationScope: string;

  @Prop({ type: Map, of: [String] })
  scopeFilters: Map<string, string[]>;

  @Prop({
    required: true,
    enum: ['NONE', 'LINEAR', 'STEP_UP', 'RETROACTIVE'],
    default: 'NONE',
  })
  acceleratorType: string;

  @Prop({ type: Number })
  acceleratorThreshold?: number;

  @Prop({ type: Number })
  acceleratorMultiplier?: number;

  @Prop({
    required: true,
    enum: ['NONE', 'AMOUNT', 'PERCENTAGE'],
    default: 'NONE',
  })
  capType: string;

  @Prop({ type: Number })
  capValue?: number;

  @Prop({ required: true, default: true })
  isActive: boolean;

  @Prop({ required: true, type: Date })
  effectiveDate: Date;

  @Prop({ type: Date })
  expirationDate?: Date;

  @Prop({ required: true })
  tenantId: string;

  @Prop({ required: true })
  organizationId: string;

  @Prop({ type: Map, of: Number })
  productRates: Map<string, number>;

  @Prop({ type: Map, of: Number })
  customerRates: Map<string, number>;

  @Prop({ required: true, default: 0 })
  version: number;
}

export const CommissionRuleSchema = SchemaFactory.createForClass(CommissionRuleDocument);

// Indexes
CommissionRuleSchema.index({ tenantId: 1, organizationId: 1 });
CommissionRuleSchema.index({ isActive: 1, tenantId: 1 });
CommissionRuleSchema.index({ name: 1, tenantId: 1 });
