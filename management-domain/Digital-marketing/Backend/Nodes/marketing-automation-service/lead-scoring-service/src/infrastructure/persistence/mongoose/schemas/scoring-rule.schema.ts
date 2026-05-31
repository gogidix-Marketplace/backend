import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ collection: 'scoring_rules', timestamps: true })
export class ScoringRuleDocument extends Document {
  @Prop({ required: true }) tenantId: string;
  @Prop({ required: true }) name: string;
  @Prop() description: string;
  @Prop({ required: true }) category: string;
  @Prop([Object]) conditions: any[];
  @Prop({ default: 'and' }) conditionLogic: string;
  @Prop({ required: true }) points: number;
  @Prop() maxPoints: number;
  @Prop({ default: 0 }) priority: number;
  @Prop({ default: true }) isActive: boolean;
  @Prop() validFrom: Date;
  @Prop() validTo: Date;
  @Prop({ default: 0 }) applicationCount: number;
  @Prop() lastAppliedAt: Date;
}

export const ScoringRuleSchema = SchemaFactory.createForClass(ScoringRuleDocument);
