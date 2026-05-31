import { Schema } from 'mongoose';

const ParameterSchema = new Schema(
  {
    name: { type: String, required: true },
    type: { type: String, required: true },
    required: { type: Boolean, default: false },
    entityType: { type: String, required: true },
    prompts: [String],
  },
  { _id: false },
);

export const IntentSchema = new Schema(
  {
    name: { type: String, required: true, unique: true, trim: true, index: true },
    category: {
      type: String,
      enum: ['greeting', 'faq', 'order_status', 'product_info', 'support', 'complaint', 'refund', 'billing', 'technical', 'shipping', 'returns', 'account', 'payment', 'general', 'escalation'],
      required: true,
      index: true,
    },
    description: { type: String, required: true, trim: true },
    trainingPhrases: {
      type: [String],
      required: true,
      validate: { validator: (phrases: string[]) => phrases.length > 0, message: 'At least one training phrase is required' },
    },
    responses: {
      type: [String],
      required: true,
      validate: { validator: (responses: string[]) => responses.length > 0, message: 'At least one response is required' },
    },
    parameters: { type: [ParameterSchema], default: [] },
    requiredSkills: { type: [String], default: [] },
    requiresHandoff: { type: Boolean, default: false },
    priority: { type: Number, default: 0, min: 0, max: 100 },
    language: { type: String, default: 'en', index: true },
    isActive: { type: Boolean, default: true, index: true },
  },
  { timestamps: true, collection: 'intents' },
);

IntentSchema.index({ name: 1, language: 1 }, { unique: true });
IntentSchema.index({ category: 1, isActive: 1 });
IntentSchema.index({ priority: -1 });
