/**
 * Intent MongoDB Model
 */

import mongoose, { Schema } from 'mongoose';
import { IIntent, IntentCategory } from '../types';

const ParameterSchema = new Schema(
  {
    name: {
      type: String,
      required: true,
    },
    type: {
      type: String,
      required: true,
    },
    required: {
      type: Boolean,
      default: false,
    },
    entityType: {
      type: String,
      required: true,
    },
    prompts: [String],
  },
  { _id: false }
);

const IntentSchema = new Schema<IIntent>(
  {
    name: {
      type: String,
      required: true,
      unique: true,
      trim: true,
      index: true,
    },
    category: {
      type: String,
      enum: Object.values(IntentCategory),
      required: true,
      index: true,
    },
    description: {
      type: String,
      required: true,
      trim: true,
    },
    trainingPhrases: {
      type: [String],
      required: true,
      validate: {
        validator: (phrases: string[]) => phrases.length > 0,
        message: 'At least one training phrase is required',
      },
    },
    responses: {
      type: [String],
      required: true,
      validate: {
        validator: (responses: string[]) => responses.length > 0,
        message: 'At least one response is required',
      },
    },
    parameters: {
      type: [ParameterSchema],
      default: [],
    },
    requiredSkills: {
      type: [String],
      default: [],
    },
    requiresHandoff: {
      type: Boolean,
      default: false,
    },
    priority: {
      type: Number,
      default: 0,
      min: 0,
      max: 100,
    },
    language: {
      type: String,
      default: 'en',
      index: true,
    },
    isActive: {
      type: Boolean,
      default: true,
      index: true,
    },
  },
  {
    timestamps: true,
    collection: 'intents',
  }
);

// Indexes
IntentSchema.index({ name: 1, language: 1 }, { unique: true });
IntentSchema.index({ category: 1, isActive: 1 });
IntentSchema.index({ priority: -1 });

// Static methods
IntentSchema.statics.findActiveByLanguage = function (language: string) {
  return this.find({ language, isActive: true }).sort({ priority: -1, name: 1 });
};

IntentSchema.statics.findByCategory = function (category: IntentCategory, language: string = 'en') {
  return this.find({ category, language, isActive: true });
};

IntentSchema.statics.searchIntents = function (query: string, language: string = 'en') {
  return this.find({
    language,
    isActive: true,
    $or: [
      { name: { $regex: query, $options: 'i' } },
      { description: { $regex: query, $options: 'i' } },
      { trainingPhrases: { $in: [new RegExp(query, 'i')] } },
    ],
  });
};

// Instance method to add training phrase
IntentSchema.methods.addTrainingPhrase = function (phrase: string): void {
  if (!this.trainingPhrases.includes(phrase)) {
    this.trainingPhrases.push(phrase);
  }
};

// Instance method to add response
IntentSchema.methods.addResponse = function (response: string): void {
  if (!this.responses.includes(response)) {
    this.responses.push(response);
  }
};

export const IntentModel = mongoose.model<IIntent>('Intent', IntentSchema);
