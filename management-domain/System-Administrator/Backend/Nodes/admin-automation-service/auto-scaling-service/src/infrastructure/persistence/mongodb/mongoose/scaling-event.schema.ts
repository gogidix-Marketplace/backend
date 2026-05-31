import { Schema } from 'mongoose';

const scalingMetricsSchema = new Schema({
  cpuUtilization: { type: Number, required: true, min: 0, max: 100 },
  memoryUtilization: { type: Number, required: true, min: 0, max: 100 },
  diskUtilization: { type: Number, required: true, min: 0, max: 100 },
  networkInBytes: { type: Number, required: true, min: 0 },
  networkOutBytes: { type: Number, required: true, min: 0 },
  requestCount: { type: Number, required: true, min: 0 },
  timestamp: { type: Date, required: true },
}, { _id: false });

export const ScalingEventSchema = new Schema({
  policyId: { type: String, required: true, trim: true },
  policyName: { type: String, required: true, trim: true },
  eventType: { type: String, enum: ['scale_out', 'scale_in', 'evaluation_passed', 'evaluation_failed'], required: true },
  previousCapacity: { type: Number, required: true, min: 0 },
  newCapacity: { type: Number, required: true, min: 0 },
  triggeredBy: { type: String, required: true, trim: true },
  metrics: { type: scalingMetricsSchema, required: true },
  status: { type: String, enum: ['pending', 'in_progress', 'completed', 'failed'], default: 'pending' },
  error: { type: String, trim: true },
  startedAt: { type: Date, required: true, default: Date.now },
  completedAt: Date,
  metadata: { type: Map, of: Schema.Types.Mixed, default: new Map() },
}, { timestamps: true, collection: 'scaling_events' });

ScalingEventSchema.index({ policyId: 1, startedAt: -1 });
ScalingEventSchema.index({ status: 1 });
