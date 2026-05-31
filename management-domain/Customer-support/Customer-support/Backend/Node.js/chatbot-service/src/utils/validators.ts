/**
 * Input Validation Utilities
 */

import { body, param, query, validationResult, ValidationChain } from 'express-validator';
import { Response } from 'express';

export const validate = (validations: ValidationChain[]) => {
  return async (req: any, res: Response, next: any) => {
    await Promise.all(validations.map(validation => validation.run(req)));

    const errors = validationResult(req);
    if (errors.isEmpty()) {
      return next();
    }

    return res.status(400).json({
      success: false,
      errors: errors.array().map(error => ({
        field: error.type === 'field' ? error.path : 'unknown',
        message: error.msg,
      })),
    });
  };
};

export const sendMessageValidation = [
  body('message')
    .trim()
    .notEmpty()
    .withMessage('Message is required')
    .isLength({ min: 1, max: 5000 })
    .withMessage('Message must be between 1 and 5000 characters'),
  body('language')
    .optional()
    .isIn(['en', 'es', 'fr', 'de', 'pt', 'zh', 'ja', 'ar'])
    .withMessage('Invalid language code'),
  body('sessionId')
    .optional()
    .isString()
    .isLength({ min: 1, max: 100 })
    .withMessage('Session ID must be a string'),
  body('customerId')
    .optional()
    .isString()
    .isLength({ min: 1, max: 100 })
    .withMessage('Customer ID must be a string'),
];

export const createSessionValidation = [
  body('language')
    .optional()
    .isIn(['en', 'es', 'fr', 'de', 'pt', 'zh', 'ja', 'ar'])
    .withMessage('Invalid language code'),
  body('customerId')
    .optional()
    .isString()
    .isLength({ min: 1, max: 100 })
    .withMessage('Customer ID must be a string'),
];

export const handoffValidation = [
  body('sessionId')
    .trim()
    .notEmpty()
    .withMessage('Session ID is required')
    .isString()
    .isLength({ min: 1, max: 100 })
    .withMessage('Session ID must be a string'),
  body('reason')
    .trim()
    .notEmpty()
    .withMessage('Reason is required')
    .isLength({ min: 5, max: 500 })
    .withMessage('Reason must be between 5 and 500 characters'),
  body('priority')
    .optional()
    .isIn(['low', 'medium', 'high', 'urgent'])
    .withMessage('Priority must be one of: low, medium, high, urgent'),
];

export const feedbackValidation = [
  body('sessionId')
    .trim()
    .notEmpty()
    .withMessage('Session ID is required')
    .isString()
    .isLength({ min: 1, max: 100 })
    .withMessage('Session ID must be a string'),
  body('rating')
    .isInt({ min: 1, max: 5 })
    .withMessage('Rating must be an integer between 1 and 5'),
  body('comment')
    .optional()
    .trim()
    .isLength({ max: 1000 })
    .withMessage('Comment must not exceed 1000 characters'),
  body('resolved')
    .isBoolean()
    .withMessage('Resolved must be a boolean'),
];

export const sessionParamValidation = [
  param('sessionId')
    .trim()
    .notEmpty()
    .withMessage('Session ID is required')
    .isString()
    .isLength({ min: 1, max: 100 })
    .withMessage('Session ID must be a string'),
];

export const analyticsQueryValidation = [
  query('startDate')
    .optional()
    .isISO8601()
    .withMessage('Start date must be a valid ISO 8601 date'),
  query('endDate')
    .optional()
    .isISO8601()
    .withMessage('End date must be a valid ISO 8601 date'),
  query('page')
    .optional()
    .isInt({ min: 1 })
    .withMessage('Page must be a positive integer'),
  query('limit')
    .optional()
    .isInt({ min: 1, max: 100 })
    .withMessage('Limit must be between 1 and 100'),
];

export const intentValidation = [
  body('name')
    .trim()
    .notEmpty()
    .withMessage('Intent name is required')
    .isLength({ min: 2, max: 100 })
    .withMessage('Intent name must be between 2 and 100 characters')
    .matches(/^[a-z_][a-z0-9_]*$/)
    .withMessage('Intent name must start with a letter or underscore and contain only lowercase letters, numbers, and underscores'),
  body('category')
    .trim()
    .notEmpty()
    .withMessage('Category is required')
    .isIn([
      'greeting',
      'faq',
      'order_status',
      'product_info',
      'support',
      'complaint',
      'refund',
      'billing',
      'technical',
      'shipping',
      'returns',
      'account',
      'payment',
      'general',
      'escalation',
    ])
    .withMessage('Invalid category'),
  body('description')
    .trim()
    .notEmpty()
    .withMessage('Description is required')
    .isLength({ min: 10, max: 500 })
    .withMessage('Description must be between 10 and 500 characters'),
  body('trainingPhrases')
    .isArray({ min: 1 })
    .withMessage('At least one training phrase is required'),
  body('trainingPhrases.*')
    .trim()
    .notEmpty()
    .withMessage('Training phrases cannot be empty')
    .isLength({ min: 2, max: 200 })
    .withMessage('Each training phrase must be between 2 and 200 characters'),
  body('responses')
    .isArray({ min: 1 })
    .withMessage('At least one response is required'),
  body('responses.*')
    .trim()
    .notEmpty()
    .withMessage('Responses cannot be empty')
    .isLength({ min: 2, max: 1000 })
    .withMessage('Each response must be between 2 and 1000 characters'),
  body('language')
    .optional()
    .isIn(['en', 'es', 'fr', 'de', 'pt', 'zh', 'ja', 'ar'])
    .withMessage('Invalid language code'),
  body('priority')
    .optional()
    .isInt({ min: 0, max: 100 })
    .withMessage('Priority must be between 0 and 100'),
  body('requiresHandoff')
    .optional()
    .isBoolean()
    .withMessage('requiresHandoff must be a boolean'),
];

export const intentUpdateValidation = [
  param('intentId')
    .trim()
    .notEmpty()
    .withMessage('Intent ID is required')
    .isMongoId()
    .withMessage('Invalid Intent ID'),
  ...intentValidation.filter(v => v.builder.keyword !== 'body'),
];

export const translateValidation = [
  body('text')
    .trim()
    .notEmpty()
    .withMessage('Text is required')
    .isLength({ min: 1, max: 5000 })
    .withMessage('Text must be between 1 and 5000 characters'),
  body('targetLanguage')
    .trim()
    .notEmpty()
    .withMessage('Target language is required')
    .isLength({ min: 2, max: 5 })
    .withMessage('Invalid target language code'),
  body('sourceLanguage')
    .optional()
    .trim()
    .isLength({ min: 2, max: 5 })
    .withMessage('Invalid source language code'),
];
