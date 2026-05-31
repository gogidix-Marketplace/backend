import { Schema } from 'mongoose';

const alertConditionSchema = new Schema({
  field: { type: String, required: true },
  operator: { type: String, enum: ['equals', 'contains', 'regex', 'gt', 'lt', 'exists'], required: true },
  value: Schema.Types.Mixed,
  threshold: Number,
  timeWindow: { type: Number, default: 300 },
}, { _id: false });

const alertActionSchema = new Schema({
  type: { type: String, enum: ['webhook', 'email', 'slack', 'pagerduty'], required: true },
  config: { type: Map, of: Schema.Types.Mixed, required: true },
  enabled: { type: Boolean, default: true },
}, { _id: false });

export const LogAlertRuleSchema = new Schema({
  name: { type: String, required: true, trim: true, unique: true },
  description: { type: String, required: true, trim: true },
  enabled: { type: Boolean, default: true },
  conditions: { type: [alertConditionSchema], required: true },
  actions: { type: [alertActionSchema], required: true },
  cooldown: { type: Number, default: 300, min: 0 },
  lastTriggeredAt: Date,
  triggerCount: { type: Number, default: 0 },
}, { timestamps: true, collection: 'log_alert_rules' });

LogAlertRuleSchema.index({ enabled: 1 });
