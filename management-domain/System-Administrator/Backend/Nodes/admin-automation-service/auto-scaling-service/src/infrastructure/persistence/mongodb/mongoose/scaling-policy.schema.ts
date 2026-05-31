import { Schema } from 'mongoose';

const scalingRuleSchema = new Schema({
  metric: { type: String, enum: ['cpu', 'memory', 'disk', 'network', 'requests'], required: true },
  operator: { type: String, enum: ['greater_than', 'less_than', 'equals'], required: true },
  threshold: { type: Number, required: true, min: 0, max: 100 },
  evaluationPeriods: { type: Number, required: true, default: 1, min: 1 },
  adjustment: { type: Number, required: true },
  adjustmentType: { type: String, enum: ['change_in_capacity', 'exact_capacity', 'percent_change_in_capacity'], required: true },
}, { _id: false });

export const ScalingPolicySchema = new Schema({
  name: { type: String, required: true, trim: true, unique: true },
  description: { type: String, required: true, trim: true },
  resourceId: { type: String, required: true, trim: true },
  cloudProvider: { type: String, enum: ['aws', 'azure', 'gcp'], required: true },
  enabled: { type: Boolean, default: true },
  scaleOutRules: { type: [scalingRuleSchema], default: [] },
  scaleInRules: { type: [scalingRuleSchema], default: [] },
  cooldownPeriod: { type: Number, required: true, default: 300000, min: 0 },
  minInstances: { type: Number, required: true, min: 0 },
  maxInstances: { type: Number, required: true, min: 1 },
  currentInstances: { type: Number, required: true, min: 0 },
  targetInstances: { type: Number, required: true, min: 0 },
  lastEvaluatedAt: Date,
}, { timestamps: true, collection: 'scaling_policies' });

ScalingPolicySchema.index({ resourceId: 1, cloudProvider: 1 });
ScalingPolicySchema.index({ enabled: 1 });
