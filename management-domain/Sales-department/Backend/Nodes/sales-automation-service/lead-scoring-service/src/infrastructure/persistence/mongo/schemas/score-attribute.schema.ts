import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export type ScoreAttributeDocument = ScoreAttributeSchema & Document;

@Schema({ collection: 'score_attributes', timestamps: true })
export class ScoreAttributeSchema {
  @Prop({ required: true, unique: true, index: true })
  _id: string;

  @Prop({ required: true, index: true })
  tenantId: string;

  @Prop({ required: true })
  name: string;

  @Prop({ required: false })
  description: string;

  @Prop({ required: true, enum: ['demographic', 'behavioral', 'firmographic', 'engagement', 'custom', 'combined'] })
  type: string;

  @Prop({ required: true })
  dataType: string;

  @Prop({ required: true })
  weight: number;

  @Prop()
  defaultValue: any;

  @Prop({ required: true, default: false })
  isRequired: boolean;

  @Prop({ type: [String], default: [] })
  options: string[];

  @Prop({ type: String })
  validationRule: string;

  @Prop({ required: true })
  sourceField: string;

  @Prop({ required: true, default: true, index: true })
  isActive: boolean;

  @Prop({ required: true, default: 0 })
  displayOrder: number;

  @Prop({ type: Object, default: {} })
  metadata: Record<string, any>;

  @Prop({ required: true, type: Date })
  createdAt: Date;

  @Prop({ required: true, type: Date })
  updatedAt: Date;
}

export const ScoreAttributeSchemaModel = SchemaFactory.createForClass(ScoreAttributeSchema);

// Indexes
ScoreAttributeSchemaModel.index({ tenantId: 1, type: 1 });
ScoreAttributeSchemaModel.index({ tenantId: 1, isActive: 1 });
