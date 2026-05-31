import { Schema } from 'mongoose';

const scalingMetricsSchema = new Schema({
  cpuUtilization: { type: Number, required: true },
  memoryUtilization: { type: Number, required: true },
  diskUtilization: { type: Number, required: true },
  networkInBytes: { type: Number, required: true },
  networkOutBytes: { type: Number, required: true },
  requestCount: { type: Number, required: true },
  timestamp: { type: Date, required: true },
}, { _id: false });

const aggregatedMetricsSchema = new Schema({
  avgCpuUtilization: { type: Number, required: true },
  avgMemoryUtilization: { type: Number, required: true },
  maxCpuUtilization: { type: Number, required: true },
  maxMemoryUtilization: { type: Number, required: true },
  minCpuUtilization: { type: Number, required: true },
  minMemoryUtilization: { type: Number, required: true },
}, { _id: false });

export const MetricsHistorySchema = new Schema({
  resourceId: { type: String, required: true, trim: true },
  cloudProvider: { type: String, enum: ['aws', 'azure', 'gcp'], required: true },
  metrics: { type: [scalingMetricsSchema], default: [] },
  aggregatedMetrics: { type: aggregatedMetricsSchema, required: true },
  collectedAt: { type: Date, required: true, default: Date.now },
}, { timestamps: true, collection: 'metrics_history' });

MetricsHistorySchema.index({ resourceId: 1, collectedAt: -1 });
