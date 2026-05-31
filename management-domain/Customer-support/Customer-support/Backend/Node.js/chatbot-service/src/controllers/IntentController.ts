/**
 * Intent Controller
 * Handles HTTP requests for intent management
 */

import { Response } from 'express';
import { AuthenticatedRequest } from '../types';
import { intentDetectionService } from '../services/IntentDetectionService';
import { IntentModel } from '../models/Intent';
import { logger } from '../utils/logger';
import { asyncHandler } from '../middleware/errorHandler';

export class IntentController {
  /**
   * Detect intent from text
   */
  detectIntent = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { message, language = 'en' } = req.body;

    logger.info(`Detecting intent for message: "${message}"`);

    const detection = await intentDetectionService.detectIntent(message, language);

    res.json({
      success: true,
      data: detection,
    });
  });

  /**
   * Get all intents
   */
  getIntents = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { language, category, isActive = true } = req.query;

    let query: any = {};

    if (language) {
      query.language = language;
    }

    if (category) {
      query.category = category;
    }

    if (isActive !== undefined) {
      query.isActive = isActive === 'true';
    }

    const intents = await IntentModel.find(query).sort({ priority: -1, name: 1 });

    res.json({
      success: true,
      data: intents,
      total: intents.length,
    });
  });

  /**
   * Get intent by ID
   */
  getIntentById = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { intentId } = req.params;

    const intent = await IntentModel.findById(intentId);

    if (!intent) {
      res.status(404).json({
        success: false,
        error: 'Intent not found',
      });
      return;
    }

    res.json({
      success: true,
      data: intent,
    });
  });

  /**
   * Create a new intent
   */
  createIntent = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const intentData = req.body;

    logger.info(`Creating intent: ${intentData.name}`);

    // Check if intent already exists
    const existing = await IntentModel.findOne({
      name: intentData.name,
      language: intentData.language || 'en',
    });

    if (existing) {
      res.status(409).json({
        success: false,
        error: 'Intent with this name already exists for this language',
      });
      return;
    }

    const intent = new IntentModel(intentData);
    await intent.save();

    // Reload intent detection service
    await intentDetectionService.reloadIntents();

    res.status(201).json({
      success: true,
      data: intent,
    });
  });

  /**
   * Update an intent
   */
  updateIntent = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { intentId } = req.params;
    const updateData = req.body;

    const intent = await IntentModel.findByIdAndUpdate(
      intentId,
      { $set: updateData },
      { new: true, runValidators: true }
    );

    if (!intent) {
      res.status(404).json({
        success: false,
        error: 'Intent not found',
      });
      return;
    }

    // Reload intent detection service
    await intentDetectionService.reloadIntents();

    logger.info(`Updated intent: ${intent.name}`);

    res.json({
      success: true,
      data: intent,
    });
  });

  /**
   * Delete an intent
   */
  deleteIntent = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { intentId } = req.params;

    const intent = await IntentModel.findByIdAndDelete(intentId);

    if (!intent) {
      res.status(404).json({
        success: false,
        error: 'Intent not found',
      });
      return;
    }

    // Reload intent detection service
    await intentDetectionService.reloadIntents();

    logger.info(`Deleted intent: ${intent.name}`);

    res.json({
      success: true,
      message: 'Intent deleted successfully',
    });
  });

  /**
   * Add training phrase to intent
   */
  addTrainingPhrase = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { intentId } = req.params;
    const { phrase } = req.body;

    const intent = await IntentModel.findById(intentId);

    if (!intent) {
      res.status(404).json({
        success: false,
        error: 'Intent not found',
      });
      return;
    }

    intent.addTrainingPhrase(phrase);
    await intent.save();

    // Reload intent detection service
    await intentDetectionService.reloadIntents();

    res.json({
      success: true,
      data: intent,
    });
  });

  /**
   * Add response to intent
   */
  addResponse = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { intentId } = req.params;
    const { response } = req.body;

    const intent = await IntentModel.findById(intentId);

    if (!intent) {
      res.status(404).json({
        success: false,
        error: 'Intent not found',
      });
      return;
    }

    intent.addResponse(response);
    await intent.save();

    res.json({
      success: true,
      data: intent,
    });
  });

  /**
   * Toggle intent active status
   */
  toggleIntent = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { intentId } = req.params;

    const intent = await IntentModel.findById(intentId);

    if (!intent) {
      res.status(404).json({
        success: false,
        error: 'Intent not found',
      });
      return;
    }

    intent.isActive = !intent.isActive;
    await intent.save();

    // Reload intent detection service
    await intentDetectionService.reloadIntents();

    res.json({
      success: true,
      data: intent,
    });
  });

  /**
   * Get intent categories
   */
  getCategories = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const categories = [
      { value: 'greeting', label: 'Greeting' },
      { value: 'faq', label: 'FAQ' },
      { value: 'order_status', label: 'Order Status' },
      { value: 'product_info', label: 'Product Information' },
      { value: 'support', label: 'Support' },
      { value: 'complaint', label: 'Complaint' },
      { value: 'refund', label: 'Refund' },
      { value: 'billing', label: 'Billing' },
      { value: 'technical', label: 'Technical' },
      { value: 'shipping', label: 'Shipping' },
      { value: 'returns', label: 'Returns' },
      { value: 'account', label: 'Account' },
      { value: 'payment', label: 'Payment' },
      { value: 'general', label: 'General' },
      { value: 'escalation', label: 'Escalation' },
    ];

    res.json({
      success: true,
      data: categories,
    });
  });

  /**
   * Batch detect intents
   */
  batchDetectIntents = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { messages, language = 'en' } = req.body;

    if (!Array.isArray(messages) || messages.length === 0) {
      res.status(400).json({
        success: false,
        error: 'Messages array is required',
      });
      return;
    }

    const results = await intentDetectionService.batchDetect(messages, language);

    res.json({
      success: true,
      data: results,
    });
  });
}

export const intentController = new IntentController();
