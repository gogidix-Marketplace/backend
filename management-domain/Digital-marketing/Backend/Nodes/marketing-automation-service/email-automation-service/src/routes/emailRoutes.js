/**
 * Email Routes
 * API routes for email operations
 */

const express = require('express');
const router = express.Router();
const { asyncHandler } = require('../middleware/errorHandler');
const { emailSendLimiter } = require('../middleware/rateLimiter');
const { addEmailJob, addBatchEmailJobs, getJob, getQueueStats, retryJob, removeJob } = require('../services/emailQueue');
const {
  createTemplate,
  updateTemplate,
  deleteTemplate,
  listTemplates,
  renderTemplate
} = require('../services/templateService');
const EmailJob = require('../models/EmailJob');
const { sendEmailSchema, createTemplateSchema, updateTemplateSchema, batchEmailSchema, querySchema } = require('../utils/validators');
const { v4: uuidv4 } = require('uuid');

/**
 * POST /api/v1/emails/send
 * Send a single email
 */
router.post('/send', emailSendLimiter, asyncHandler(async (req, res) => {
  // Validate input
  const { error, value } = sendEmailSchema.validate(req.body);
  if (error) {
    return res.status(400).json({
      error: 'ValidationError',
      message: 'Invalid request data',
      errors: error.details.map(d => ({
        field: d.path.join('.'),
        message: d.message
      }))
    });
  }

  const emailData = {
    ...value,
    tenantId: req.tenantId || value.tenantId,
    jobId: uuidv4()
  };

  // Add to queue
  const job = await addEmailJob(emailData);

  res.status(202).json({
    success: true,
    message: 'Email queued for sending',
    data: {
      jobId: job.id,
      status: 'queued',
      scheduledFor: emailData.scheduledFor
    }
  });
}));

/**
 * POST /api/v1/emails/batch
 * Send batch emails
 */
router.post('/batch', emailSendLimiter, asyncHandler(async (req, res) => {
  const { error, value } = batchEmailSchema.validate(req.body);
  if (error) {
    return res.status(400).json({
      error: 'ValidationError',
      message: 'Invalid request data',
      errors: error.details.map(d => ({
        field: d.path.join('.'),
        message: d.message
      }))
    });
  }

  const emails = value.emails.map(email => ({
    ...email,
    tenantId: req.tenantId || value.tenantId,
    jobId: uuidv4(),
    campaignId: value.campaignId,
    priority: email.priority || 'normal'
  }));

  const jobs = await addBatchEmailJobs(emails, {
    tenantId: req.tenantId || value.tenantId,
    scheduleAt: value.scheduleAt
  });

  res.status(202).json({
    success: true,
    message: `${jobs.length} emails queued for sending`,
    data: {
      count: jobs.length,
      jobs: jobs.map(j => ({ jobId: j.id }))
    }
  });
}));

/**
 * GET /api/v1/emails/:jobId
 * Get email job status
 */
router.get('/:jobId', asyncHandler(async (req, res) => {
  const { jobId } = req.params;

  // Check queue first
  let job = await getJob(jobId);

  if (!job) {
    // Check database
    const emailJob = await EmailJob.findOne({ jobId });
    if (!emailJob) {
      return res.status(404).json({
        error: 'NotFound',
        message: 'Email job not found'
      });
    }

    return res.json({
      success: true,
      data: {
        jobId: emailJob.jobId,
        status: emailJob.status,
        to: emailJob.to,
        subject: emailJob.subject,
        sentAt: emailJob.sentAt,
        deliveryAttempts: emailJob.deliveryAttempts,
        error: emailJob.error,
        opened: emailJob.opened,
        clicked: emailJob.clicked
      }
    });
  }

  res.json({
    success: true,
    data: job
  });
}));

/**
 * GET /api/v1/emails
 * List email jobs with filters
 */
router.get('/', asyncHandler(async (req, res) => {
  const { error, value } = querySchema.validate(req.query);
  if (error) {
    return res.status(400).json({
      error: 'ValidationError',
      message: 'Invalid query parameters',
      errors: error.details.map(d => d.message)
    });
  }

  const tenantId = req.tenantId || value.tenantId || req.headers['x-tenant-id'];

  const options = {
    status: value.status,
    campaignId: value.campaignId,
    startDate: value.startDate,
    endDate: value.endDate,
    limit: value.limit,
    skip: (value.page - 1) * value.limit
  };

  const jobs = await EmailJob.getByTenant(tenantId, options);
  const total = await EmailJob.countDocuments({ tenantId, ...options });

  res.json({
    success: true,
    data: {
      jobs,
      pagination: {
        page: value.page,
        limit: value.limit,
        total
      }
    }
  });
}));

/**
 * POST /api/v1/emails/:jobId/retry
 * Retry failed email
 */
router.post('/:jobId/retry', asyncHandler(async (req, res) => {
  const { jobId } = req.params;

  const result = await retryJob(jobId);

  res.json({
    success: true,
    message: 'Email queued for retry',
    data: result
  });
}));

/**
 * DELETE /api/v1/emails/:jobId
 * Cancel/delete email job
 */
router.delete('/:jobId', asyncHandler(async (req, res) => {
  const { jobId } = req.params;

  await removeJob(jobId);

  // Also remove from database
  await EmailJob.deleteOne({ jobId });

  res.json({
    success: true,
    message: 'Email job deleted'
  });
}));

/**
 * GET /api/v1/queue/stats
 * Get queue statistics
 */
router.get('/queue/stats', asyncHandler(async (req, res) => {
  const stats = await getQueueStats();

  res.json({
    success: true,
    data: stats
  });
}));

// ==================== Template Routes ====================

/**
 * POST /api/v1/templates
 * Create email template
 */
router.post('/templates', asyncHandler(async (req, res) => {
  const { error, value } = createTemplateSchema.validate(req.body);
  if (error) {
    return res.status(400).json({
      error: 'ValidationError',
      message: 'Invalid request data',
      errors: error.details.map(d => d.message)
    });
  }

  const template = await createTemplate({
    ...value,
    tenantId: req.tenantId || value.tenantId
  });

  res.status(201).json({
    success: true,
    message: 'Template created',
    data: template
  });
}));

/**
 * GET /api/v1/templates
 * List templates
 */
router.get('/templates', asyncHandler(async (req, res) => {
  const tenantId = req.tenantId || req.headers['x-tenant-id'];
  const { category, search, page = 1, limit = 20 } = req.query;

  const result = await listTemplates(tenantId, { category, search, page, limit });

  res.json({
    success: true,
    data: result
  });
}));

/**
 * GET /api/v1/templates/:templateId
 * Get template by ID
 */
router.get('/templates/:templateId', asyncHandler(async (req, res) => {
  const { templateId } = req.params;
  const tenantId = req.tenantId || req.headers['x-tenant-id'];

  const template = await EmailTemplate.findOne({ templateId, tenantId, isActive: true });

  if (!template) {
    return res.status(404).json({
      error: 'NotFound',
      message: 'Template not found'
    });
  }

  res.json({
    success: true,
    data: template
  });
}));

/**
 * PUT /api/v1/templates/:templateId
 * Update template
 */
router.put('/templates/:templateId', asyncHandler(async (req, res) => {
  const { templateId } = req.params;
  const tenantId = req.tenantId || req.headers['x-tenant-id'];

  const { error, value } = updateTemplateSchema.validate(req.body);
  if (error) {
    return res.status(400).json({
      error: 'ValidationError',
      message: 'Invalid request data',
      errors: error.details.map(d => d.message)
    });
  }

  const template = await updateTemplate(templateId, tenantId, value);

  res.json({
    success: true,
    message: 'Template updated',
    data: template
  });
}));

/**
 * DELETE /api/v1/templates/:templateId
 * Delete template
 */
router.delete('/templates/:templateId', asyncHandler(async (req, res) => {
  const { templateId } = req.params;
  const tenantId = req.tenantId || req.headers['x-tenant-id'];

  await deleteTemplate(templateId, tenantId);

  res.json({
    success: true,
    message: 'Template deleted'
  });
}));

/**
 * POST /api/v1/templates/:templateId/preview
 * Preview template with data
 */
router.post('/templates/:templateId/preview', asyncHandler(async (req, res) => {
  const { templateId } = req.params;
  const tenantId = req.tenantId || req.headers['x-tenant-id'];
  const { templateData } = req.body;

  const rendered = await renderTemplate(templateId, templateData || {}, tenantId);

  res.json({
    success: true,
    data: rendered
  });
}));

module.exports = router;
