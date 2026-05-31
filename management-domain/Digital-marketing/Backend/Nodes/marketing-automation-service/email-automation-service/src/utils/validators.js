/**
 * Input Validators
 * Joi validation schemas for API endpoints
 */

const Joi = require('joi');

/**
 * Email sending validation schema
 */
const sendEmailSchema = Joi.object({
  to: Joi.alternatives().try(
    Joi.string().email(),
    Joi.array().items(Joi.string().email()).min(1).max(100)
  ).required(),
  cc: Joi.alternatives().try(
    Joi.string().email(),
    Joi.array().items(Joi.string().email()).max(50)
  ),
  bcc: Joi.alternatives().try(
    Joi.string().email(),
    Joi.array().items(Joi.string().email()).max(50)
  ),
  subject: Joi.string().required().min(1).max(500),
  templateId: Joi.string().when('html', {
    is: Joi.exist(),
    then: Joi.forbidden(),
    otherwise: Joi.optional()
  }),
  templateData: Joi.object().when('templateId', {
    is: Joi.exist(),
    then: Joi.required(),
    otherwise: Joi.optional()
  }),
  html: Joi.string().when('templateId', {
    is: Joi.exist(),
    then: Joi.forbidden(),
    otherwise: Joi.optional()
  }),
  text: Joi.string(),
  from: Joi.object({
    email: Joi.string().email().required(),
    name: Joi.string().max(100)
  }),
  replyTo: Joi.string().email(),
  attachments: Joi.array().items(
    Joi.object({
      filename: Joi.string().required(),
      content: Joi.string().required(),
      encoding: Joi.string().valid('base64', 'utf8').default('base64'),
      contentType: Joi.string(),
      disposition: Joi.string().valid('attachment', 'inline').default('attachment')
    })
  ).max(10),
  headers: Joi.object(),
  tags: Joi.array().items(Joi.string().max(50)).max(10),
  metadata: Joi.object(),
  priority: Joi.string().valid('high', 'normal', 'low').default('normal'),
  scheduledFor: Joi.date().min('now'),
  campaignId: Joi.string(),
  tenantId: Joi.string().required()
});

/**
 * Template creation validation schema
 */
const createTemplateSchema = Joi.object({
  name: Joi.string().required().min(1).max(100),
  description: Joi.string().max(500),
  subject: Joi.string().required().min(1).max(500),
  htmlBody: Joi.string().required(),
  textBody: Joi.string(),
  variables: Joi.array().items(
    Joi.object({
      name: Joi.string().required(),
      description: Joi.string(),
      defaultValue: Joi.any(),
      required: Joi.boolean().default(false)
    })
  ).default([]),
  category: Joi.string().max(50),
  tenantId: Joi.string().required(),
  tags: Joi.array().items(Joi.string().max(50)).max(10)
});

/**
 * Template update validation schema
 */
const updateTemplateSchema = Joi.object({
  name: Joi.string().min(1).max(100),
  description: Joi.string().max(500),
  subject: Joi.string().min(1).max(500),
  htmlBody: Joi.string(),
  textBody: Joi.string(),
  variables: Joi.array().items(
    Joi.object({
      name: Joi.string().required(),
      description: Joi.string(),
      defaultValue: Joi.any(),
      required: Joi.boolean().default(false)
    })
  ),
  category: Joi.string().max(50),
  tags: Joi.array().items(Joi.string().max(50)).max(10),
  version: Joi.string()
});

/**
 * Batch email validation schema
 */
const batchEmailSchema = Joi.object({
  emails: Joi.array().items(
    Joi.object({
      to: Joi.alternatives().try(
        Joi.string().email(),
        Joi.array().items(Joi.string().email()).min(1).max(100)
      ).required(),
      subject: Joi.string().optional(),
      templateId: Joi.string(),
      templateData: Joi.object(),
      html: Joi.string(),
      text: Joi.string(),
      cc: Joi.alternatives().try(
        Joi.string().email(),
        Joi.array().items(Joi.string().email())
      ),
      bcc: Joi.alternatives().try(
        Joi.string().email(),
        Joi.array().items(Joi.string().email())
      ),
      replyTo: Joi.string().email(),
      attachments: Joi.array().items(
        Joi.object({
          filename: Joi.string().required(),
          content: Joi.string().required(),
          encoding: Joi.string().valid('base64', 'utf8'),
          contentType: Joi.string()
        })
      ).max(10),
      metadata: Joi.object(),
      priority: Joi.string().valid('high', 'normal', 'low')
    })
  ).min(1).max(1000).required(),
  campaignId: Joi.string(),
  tenantId: Joi.string().required(),
  scheduleAt: Joi.date().min('now')
});

/**
 * Query validation schema
 */
const querySchema = Joi.object({
  page: Joi.number().integer().min(1).default(1),
  limit: Joi.number().integer().min(1).max(100).default(20),
  status: Joi.string().valid('pending', 'processing', 'sent', 'failed', 'bounced', 'deferred'),
  startDate: Joi.date(),
  endDate: Joi.date(),
  tenantId: Joi.string(),
  campaignId: Joi.string()
});

module.exports = {
  sendEmailSchema,
  createTemplateSchema,
  updateTemplateSchema,
  batchEmailSchema,
  querySchema
};
